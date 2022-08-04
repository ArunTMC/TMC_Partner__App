package com.meatchop.tmcpartner.Settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Pos_OrderDetailsScreen;
import com.meatchop.tmcpartner.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Adapter_RaisedTicketListForRating extends ArrayAdapter<Modal_RaisedTicketsRatingDetails> {
    Context mContext;
    String orderid="";
    RaisedTicketDetailsForRating raisedTicketDetailsForRating;
    ArrayList<Modal_RaisedTicketsRatingDetails> raisedTicketsRatingDetailsArray;
    public static BottomSheetDialog bottomSheetDialog;
    BottomNavigationView bottomNavigationView;

    public Adapter_RaisedTicketListForRating(Context context, ArrayList<Modal_RaisedTicketsRatingDetails> raisedTicketsRatingDetailsArrayy, RaisedTicketDetailsForRating raisedTicketDetailsForRatingg) {
        super(context, R.layout.raisedticketslist,raisedTicketsRatingDetailsArrayy);
        mContext = context;
        raisedTicketsRatingDetailsArray=raisedTicketsRatingDetailsArrayy;
        raisedTicketDetailsForRating = raisedTicketDetailsForRatingg;

    }

    @Nullable
    @Override
    public Modal_RaisedTicketsRatingDetails getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return super.getFilter();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup view) {

        @SuppressLint("ViewHolder")
        final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.raisedticketslist, (ViewGroup) view, false);
        final TextView orderid_text = listViewItem.findViewById(R.id.orderid_text);
        final TextView mobileno_text = listViewItem.findViewById(R.id.mobileno_text);
        final TextView ticketRaisedTime = listViewItem.findViewById(R.id.ticketRaisedTime);
        final TextView ticketDescription = listViewItem.findViewById(R.id.ticketDescription);
        final RatingBar qualityratingBar = listViewItem.findViewById(R.id.qualityratingBar);
        final RatingBar deliveryratingBar = listViewItem.findViewById(R.id.deliveryratingBar);

        final LinearLayout ticketDescLayout = listViewItem.findViewById(R.id.ticketDescLayout);
        final Button addDesp = listViewItem.findViewById(R.id.addDesp);
        final Button vieworderdetails = listViewItem.findViewById(R.id.vieworderdetails);

        final ImageView callCustomerNumber = listViewItem.findViewById(R.id.callCustomerNumber);

        String description ="";
        ticketDescLayout.setVisibility(View.GONE);
        final Modal_RaisedTicketsRatingDetails modal_raisedTicketsRatingDetails =raisedTicketsRatingDetailsArray.get(position);

        try{
            orderid_text.setText(raisedTicketsRatingDetailsArray.get(position).getOrderid().toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            mobileno_text.setText(raisedTicketsRatingDetailsArray.get(position).getUsermobileno().toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            ticketRaisedTime.setText(raisedTicketsRatingDetailsArray.get(position).getTicketraisedtime().toString());

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            String qualityrating = raisedTicketsRatingDetailsArray.get(position).getQualityrating().toString();
            qualityratingBar.setRating(Float.parseFloat(qualityrating));

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            String deliveryrating =raisedTicketsRatingDetailsArray.get(position).getDeliveryrating().toString();
            deliveryratingBar.setRating(Float.parseFloat(deliveryrating));

        }
        catch (Exception e){
            e.printStackTrace();
        }



        try{
            description = (raisedTicketsRatingDetailsArray.get(position).getDescription().toString());

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{

            if(!description.equals("")){
                ticketDescLayout.setVisibility(View.VISIBLE);
                ticketDescription.setText(modal_raisedTicketsRatingDetails.getDescription().toString());
            }
            else{
                ticketDescLayout.setVisibility(View.GONE);

            }
            ticketRaisedTime.setText(modal_raisedTicketsRatingDetails.getTicketraisedtime().toString());

        }
        catch (Exception e){
            e.printStackTrace();
        }
        callCustomerNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNumber = mobileno_text.getText().toString();

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobileNumber));// Initiates the Intent
                mContext.startActivity(intent);
            }
        });

        vieworderdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderid = modal_raisedTicketsRatingDetails.getOrderid().toString();
               boolean isOrderDetailsScreenOpened = modal_raisedTicketsRatingDetails.getisOrderDetailsScreenOpened();

                    Modal_OrderDetails_Tracking_RatingDetails modal_orderDetails_tracking_ratingDetails = modal_raisedTicketsRatingDetails.getModal_orderDetails_tracking_ratingDetails();


                    Intent intent = new Intent(mContext, OrderAndRatingDetailsScreen.class);
                   // intent.putExtra("orderid",orderid);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("data", modal_orderDetails_tracking_ratingDetails);
                    bundle.putString("orderid",orderid);
                    bundle.putBoolean("isOrderDetailsScreenOpened",isOrderDetailsScreenOpened);


                    intent.putExtras(bundle);





                    mContext.startActivity(intent);



            }
        });

        addDesp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = modal_raisedTicketsRatingDetails.getKey().toString();
                bottomSheetDialog = new BottomSheetDialog(mContext);
                bottomSheetDialog.setContentView(R.layout.add_enquiry_status);
                bottomSheetDialog.setCanceledOnTouchOutside(true);


                EditText editTextTextMultiLine_bottomsheet = bottomSheetDialog.findViewById(R.id.editTextTextMultiLine_bottomsheet);

                Button addDescriptionButton_bottomsheet = bottomSheetDialog.findViewById(R.id.addDescriptionButton_bottomsheet);
                Button closeTicketButton_bottomsheet = bottomSheetDialog.findViewById(R.id.closeTicketButton_bottomsheet);

                addDescriptionButton_bottomsheet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String descriptiontext =  editTextTextMultiLine_bottomsheet.getText().toString();
                        if(descriptiontext.equals("")){
                            Toast.makeText(mContext, "Can't leave Description Box Empty", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            bottomSheetDialog.cancel();
                            raisedTicketDetailsForRating.showProgressBar(true);

                            updateDatainRaisedTicketDetails(key,descriptiontext,false);

                        }
                    }
                });

                closeTicketButton_bottomsheet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String descriptiontext =  editTextTextMultiLine_bottomsheet.getText().toString();
                        bottomSheetDialog.cancel();
                            raisedTicketDetailsForRating.showProgressBar(true);

                        updateDatainRaisedTicketDetails(key,descriptiontext,true);

                    }
                });


                bottomSheetDialog.show();

            }
        });


        return listViewItem;
    }

    private void updateDatainRaisedTicketDetails(String key, String descriptiontext, boolean isCloseTicketClicked) {


        JSONObject jsonObject = new JSONObject();
      if(isCloseTicketClicked){
          String CurrentTime = getDate_and_time();
          if(descriptiontext.equals("")){
              try {
                  jsonObject.put("key", key);
                  jsonObject.put("ticketclosedtime",  CurrentTime);

                  jsonObject.put("status",  "CLOSED");


              } catch (JSONException e) {
                  e.printStackTrace();
              }
          }
          else{
              try {
                  jsonObject.put("key", key);
                  jsonObject.put("status",  "CLOSED");

                  jsonObject.put("description", descriptiontext);
                  jsonObject.put("ticketclosedtime",  CurrentTime);


              } catch (JSONException e) {
                  e.printStackTrace();
              }
          }

      }
      else{

          try {
              jsonObject.put("key", key);

              jsonObject.put("description", descriptiontext);


          } catch (JSONException e) {
              e.printStackTrace();
          }
      }




        Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_updateRaisedTicketsTablewithkey,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {
                        Log.d(Constants.TAG, "Rating ticket details uploaded Succesfully " + response);


                                changeinLocalArray(isCloseTicketClicked,descriptiontext,key);


                    }
                    else{
                        Log.d(Constants.TAG, "Failed during uploading Rating ticket   details" + response);

                    }

                } catch (JSONException e) {
                    raisedTicketDetailsForRating.showProgressBar(false);

                    Log.d(Constants.TAG, "Failed during uploading Rating ticket  details" + response);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                Log.d(Constants.TAG, "Failed during uploading Rating ticket  ff details" + error.toString());
                raisedTicketDetailsForRating.showProgressBar(false);

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

        Volley.newRequestQueue(mContext).add(jsonObjectRequest);





    }

    private void changeinLocalArray(boolean isCloseTicketClicked, String descriptiontext, String key) {



                try {
                    for(int i=0;i<raisedTicketDetailsForRating.raisedTicketsRatingDetailsArray.size();i++){
                        if(raisedTicketDetailsForRating.raisedTicketsRatingDetailsArray.get(i).key.equals(key)){
                            if(!descriptiontext.equals("")) {
                                raisedTicketDetailsForRating.raisedTicketsRatingDetailsArray.get(i).setDescription(descriptiontext);


                            }
                            if(isCloseTicketClicked) {
                                //raisedTicketDetailsForRating.raisedTicketsRatingDetailsArray.get(i).setStatus("CLOSED");
                                raisedTicketDetailsForRating.raisedTicketsRatingDetailsArray.remove(i);
                            }
                            }
                        if(raisedTicketDetailsForRating.raisedTicketsRatingDetailsArray.size()-i == 1){
                            raisedTicketDetailsForRating.showProgressBar(false);
                            notifyDataSetChanged();
                            raisedTicketDetailsForRating.count_textwidget.setText(String.valueOf( raisedTicketsRatingDetailsArray.size()));
                        }
                    }




                } catch (Exception e) {
                    e.printStackTrace();

            }







    }

    public String getDate_and_time()
    {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);

        SimpleDateFormat day = new SimpleDateFormat("EEE");
       String CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        String CurrentDatee = df.format(c);
        String  CurrentDate = CurrentDay+", "+CurrentDatee;


        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
        String  FormattedTime = dfTime.format(c);
        String formattedDate = CurrentDay+", "+CurrentDatee+" "+FormattedTime;
        return formattedDate;
    }


}
