package com.meatchop.tmcpartner.mobilescreen_javaclasses.replacement_refund_classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.settings.add_replacement_refund_order.Modal_ReplacementOrderDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
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
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReplacementRefundListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReplacementRefundListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button fetchOrders_buttonWidget;
    EditText customermobileno_editwidget;
    TextView vendorName_textWidget,orderscount_textwidget;
    ListView orders_listview;
    String vendorName,ordertype,vendorkey,orderplaceddate ="";
    Context mContext;
    LinearLayout loadingpanelmask,loadingPanel;
    static List<Modal_ReplacementOrderDetails> markedOrdersList;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReplacementRefundListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReplacementRefundListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReplacementRefundListFragment newInstance(String param1, String param2) {
        ReplacementRefundListFragment fragment = new ReplacementRefundListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.replacement__refund_fragment, container, false);
        mContext = this.getActivity().getWindow().getContext();
        loadingPanel = rootView.findViewById(R.id.loadingPanel);
        loadingpanelmask = rootView.findViewById(R.id.loadingpanelmask);
        markedOrdersList = new ArrayList<Modal_ReplacementOrderDetails>();


        fetchOrders_buttonWidget = rootView.findViewById(R.id.fetchOrders_buttonWidget);
        customermobileno_editwidget = rootView.findViewById(R.id.customermobileno_editwidget);
        orders_listview = rootView.findViewById(R.id.orders_listview);
        vendorName_textWidget = rootView.findViewById(R.id.vendorName_textWidget);
        orderscount_textwidget= rootView.findViewById(R.id.orderscount_textwidget);
        try{
            SharedPreferences shared = requireContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);
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
        
        
        return rootView;

    }

    private void FetchOrdersFromDatabase(String vendorName, String mobileno) {
        final String[] Count = {"0"};

        markedOrdersList.clear();
        String deliveryUserMobileNumber ="";
        deliveryUserMobileNumber = mobileno;
        if (deliveryUserMobileNumber.length() == 13) {
            String deliveryUserMobileNumberEncoded  = deliveryUserMobileNumber;
            try {
                deliveryUserMobileNumberEncoded = URLEncoder.encode(deliveryUserMobileNumber, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            showProgressBar(true);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetReplacementOrderDetailsForMobilenoVendorkey +"?mobileno="+deliveryUserMobileNumberEncoded+"&vendorkey="+vendorkey, null,
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
                                    Modal_ReplacementOrderDetails modal_replacementOrderDetails = new Modal_ReplacementOrderDetails();

                                    try {
                                        JSONObject json = JArray.getJSONObject(i1);
                                        //Log.d(Constants.TAG, "convertingJsonStringintoArray orderStatus: " + String.valueOf(json.get("orderStatus")));



                                        try {
                                            if (json.has("amountusercanavl")) {
                                                modal_replacementOrderDetails.amountusercanavl = String.valueOf(json.get("amountusercanavl"));

                                            }
                                            else
                                            {
                                                modal_replacementOrderDetails.amountusercanavl = "";
                                            }
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }



                                    try {
                                         if (json.has("amountusercanavl")) {
                                            modal_replacementOrderDetails.amountusercanavl = String.valueOf(json.get("amountusercanavl"));

                                         }
                                         else
                                         {
                                            modal_replacementOrderDetails.amountusercanavl = "";
                                         }
                                    }
                                    catch (Exception e){
                                         e.printStackTrace();
                                    }
                                    try{
                                        if (json.has("markeddate")) {
                                             modal_replacementOrderDetails.markeddate = String.valueOf(json.get("markeddate"));
                                            try{
                                                modal_replacementOrderDetails.setMarkedDateLong(getLongValuefortheDate(String.valueOf(json.get("markeddate"))));
                                            }
                                            catch (Exception e){
                                                modal_replacementOrderDetails.setMarkedDateLong ("0");
                                                e.printStackTrace();
                                            }
                                        }
                                        else
                                        {
                                            modal_replacementOrderDetails.markeddate = "";
                                        }

                                    }
                                    catch (Exception e){
                                         e.printStackTrace();
                                    }
                                        try{
                                            if (json.has("ordertype")) {
                                                modal_replacementOrderDetails.ordertype = String.valueOf(json.get("ordertype"));
                                                try{
                                                    modal_replacementOrderDetails.setOrdertype((String.valueOf(json.get("ordertype"))));
                                                }
                                                catch (Exception e){
                                                    modal_replacementOrderDetails.setOrdertype ("");
                                                    e.printStackTrace();
                                                }
                                            }
                                            else
                                            {
                                                modal_replacementOrderDetails.ordertype = "";
                                            }

                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }


                                    try{
                                        if (json.has("markeditemdesp")) {
                                            modal_replacementOrderDetails.itemsmarked_Array = (JSONArray) json.get("markeditemdesp");

                                        }
                                        else
                                        {
                                            modal_replacementOrderDetails.itemsmarked_Array = new JSONArray();
                                        }
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    try{
                                        if (json.has("mobileno")) {
                                            modal_replacementOrderDetails.mobileno = String.valueOf(json.get("mobileno"));

                                        }
                                        else
                                        {
                                            modal_replacementOrderDetails.mobileno = "";
                                        }

                                    }
                                    catch (Exception e){
                                          e.printStackTrace();
                                    }

                                    try{
                                         if (json.has("orderid")) {
                                            modal_replacementOrderDetails.orderid = String.valueOf(json.get("orderid"));

                                         }
                                         else
                                         {
                                            modal_replacementOrderDetails.orderid = "";
                                         }
                                    }
                                    catch (Exception e){
                                            e.printStackTrace();
                                    }

                                    try{
                                        if (json.has("orderplaceddate")) {
                                            modal_replacementOrderDetails.orderplaceddate = String.valueOf(json.get("orderplaceddate"));

                                        }
                                        else
                                        {
                                        modal_replacementOrderDetails.orderplaceddate = "";
                                        }
                                    }
                                    catch (Exception e){
                                    e.printStackTrace();
                                    }
                                    try{
                                        if (json.has("status")) {
                                             modal_replacementOrderDetails.status = String.valueOf(json.get("status"));

                                        }
                                        else
                                        {
                                            modal_replacementOrderDetails.status = "";
                                        }
                                    }
                                    catch (Exception e){
                                            e.printStackTrace();
                                    }
                                    try{
                                        if (json.has("vendorkey")) {
                                            modal_replacementOrderDetails.vendorkey = String.valueOf(json.get("vendorkey"));

                                        }
                                        else
                                        {
                                            modal_replacementOrderDetails.vendorkey = "";
                                        }
                                    }
                                    catch (Exception e){
                                            e.printStackTrace();
                                    }


                                    try{
                                        if (json.has("refunddetails")) {
                                            modal_replacementOrderDetails.refunddetails_Array = (JSONArray) json.get("refunddetails");

                                        }
                                        else
                                        {
                                            modal_replacementOrderDetails.refunddetails_Array = new JSONArray();
                                        }
                                    }
                                    catch (Exception e){
                                        modal_replacementOrderDetails.refunddetails_Array = new JSONArray();

                                        e.printStackTrace();
                                    }

                                        try{
                                            if (json.has("refunddetails")) {
                                                modal_replacementOrderDetails.refunddetails_String = String.valueOf(json.get("refunddetails"));

                                            }
                                            else
                                            {
                                                modal_replacementOrderDetails.refunddetails_String = "";
                                            }
                                        }
                                        catch (Exception e){
                                            modal_replacementOrderDetails.refunddetails_String = "";

                                            e.printStackTrace();
                                        }


                                        try{
                                            if (json.has("replacementdetails")) {
                                                modal_replacementOrderDetails.replacementdetails_Array = (JSONArray) json.get("replacementdetails");

                                            }
                                            else
                                            {
                                                modal_replacementOrderDetails.replacementdetails_Array = new JSONArray();
                                            }
                                        }
                                        catch (Exception e){
                                            modal_replacementOrderDetails.replacementdetails_Array = new JSONArray();

                                            e.printStackTrace();
                                        }

                                        try{
                                            if (json.has("replacementdetails")) {
                                                modal_replacementOrderDetails.replacementdetails_String = String.valueOf(json.get("replacementdetails"));

                                            }
                                            else
                                            {
                                                modal_replacementOrderDetails.replacementdetails_String = "";
                                            }
                                        }
                                        catch (Exception e){
                                            modal_replacementOrderDetails.replacementdetails_String = "";

                                            e.printStackTrace();
                                        }


                                        try{
                                            if (json.has("totalrefundedamount")) {
                                                modal_replacementOrderDetails.totalrefundedamount = String.valueOf(json.get("totalrefundedamount"));

                                            }
                                            else
                                            {
                                                modal_replacementOrderDetails.totalrefundedamount = "0";
                                            }
                                        }
                                        catch (Exception e){
                                            modal_replacementOrderDetails.totalrefundedamount = "0";

                                            e.printStackTrace();
                                        }

                                        try{
                                            if (json.has("totalreplacementamount")) {
                                                modal_replacementOrderDetails.totalreplacementamount = String.valueOf(json.get("totalreplacementamount"));

                                            }
                                            else
                                            {
                                                modal_replacementOrderDetails.totalreplacementamount = "0";
                                            }
                                        }
                                        catch (Exception e){
                                            modal_replacementOrderDetails.totalreplacementamount = "0";

                                            e.printStackTrace();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();

                                    }
                                    markedOrdersList.add(modal_replacementOrderDetails);
                                }
                                CallAdapter();


                                try{
                                    orderscount_textwidget.setText(String.valueOf(markedOrdersList.size()));
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
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);


    } else {
        AlertDialogClass.showDialog(getActivity(), R.string.Enter_the_mobile_no_text);

    }
    }

    @Override
    public void onResume() {
        super.onResume();
        CallAdapter();
    }

    public void CallAdapter() {
        Collections.sort(markedOrdersList, new Comparator<Modal_ReplacementOrderDetails>() {
            public int compare(final Modal_ReplacementOrderDetails object1, final Modal_ReplacementOrderDetails object2) {
                return object2.getMarkedDateLong().compareTo(object1.getMarkedDateLong());
            }
        });


        Adapter_Replacement_Refund_List adapter_replacement_refund_list = new Adapter_Replacement_Refund_List(mContext, markedOrdersList, ReplacementRefundListFragment.this);
        orders_listview.setAdapter(adapter_replacement_refund_list);

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

        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                String time1 = orderplacedtime;
                //     Log.d(TAG, "time1long  "+orderplacedtime);

                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy",Locale.ENGLISH);
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



    private String getSlotTime(String slottime, String orderplacedtime) {
        String result = "", lastFourDigits = "";
        //   Log.d(TAG, "slottime  "+slottime);
        if (slottime.contains("mins")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss",Locale.ENGLISH);
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


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
                SimpleDateFormat sdff = new SimpleDateFormat("HH:mm",Locale.ENGLISH);
                sdff.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

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

        SimpleDateFormat previousday = new SimpleDateFormat("EEE",Locale.ENGLISH);
        previousday.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String PreviousdayDay = previousday.format(c1);

        SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy HH:mm:ss",Locale.ENGLISH);
        df1.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String  PreviousdayDate = df1.format(c1);
        PreviousdayDate = PreviousdayDay+", "+PreviousdayDate;



        return PreviousdayDate;
    }

}