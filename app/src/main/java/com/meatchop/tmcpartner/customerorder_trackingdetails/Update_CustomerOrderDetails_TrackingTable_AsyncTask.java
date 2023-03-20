package com.meatchop.tmcpartner.customerorder_trackingdetails;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcpartner.Constants;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Update_CustomerOrderDetails_TrackingTable_AsyncTask extends AsyncTask<String, String, String> {
    Context mContext ;
    Update_CustomerOrderDetails_TrackingTableInterface update_customerOrderDetailsTrackingTableInterface =null;
    JSONObject jsonObject_to_update = new JSONObject();
    String api_toChangeOrderDetailsUsingOrderid ="";


    public Update_CustomerOrderDetails_TrackingTable_AsyncTask(Context mContext, Update_CustomerOrderDetails_TrackingTableInterface mResultCallback_updateCustomerOrderDetailsTableInterface, JSONObject jsonObject, String api_toChangeOrderDetailsUsingOrderid) {

        this.api_toChangeOrderDetailsUsingOrderid = api_toChangeOrderDetailsUsingOrderid;
        this.mContext = mContext;
        this.update_customerOrderDetailsTrackingTableInterface = mResultCallback_updateCustomerOrderDetailsTableInterface;
        this.jsonObject_to_update = jsonObject;




    }


    @Override
        protected void onPreExecute () {
        super.onPreExecute();

    }

        @Override
        protected String doInBackground (String...strings){
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,api_toChangeOrderDetailsUsingOrderid,
                    jsonObject_to_update, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(@NonNull JSONObject response) {
                    try {
                        Log.d(Constants.TAG, "Responsewwwww: " + response);
                        update_customerOrderDetailsTrackingTableInterface.notifySuccess("","Success");
                    }
                    catch (Exception e){
                        e.printStackTrace();


                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {
                    Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
                    Log.d(Constants.TAG, "Error: " + error.getMessage());
                    Log.d(Constants.TAG, "Error: " + error.toString());
                    update_customerOrderDetailsTrackingTableInterface.notifySuccess("","Error");

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

        return null;

    }


        @Override
        protected void onPostExecute (String k ){
        super.onPostExecute(k);

    }


    }