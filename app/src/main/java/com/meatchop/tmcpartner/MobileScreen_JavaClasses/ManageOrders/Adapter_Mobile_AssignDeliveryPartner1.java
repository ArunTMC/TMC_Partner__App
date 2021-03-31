package com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.OtherClasses.MobileScreen_Dashboard;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.AssignDeliveryPartner_PojoClass;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Other_javaClasses.Pos_Dashboard_Screen;
import com.meatchop.tmcpartner.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter_Mobile_AssignDeliveryPartner1 extends ArrayAdapter<AssignDeliveryPartner_PojoClass> {
        Context mContext;
        List<AssignDeliveryPartner_PojoClass> deliveryPartnerList;
        String OrderKey;



public Adapter_Mobile_AssignDeliveryPartner1(Context mContext, List<AssignDeliveryPartner_PojoClass> deliveryPartnerList, String orderKey) {
        super(mContext, R.layout.mobile_screen_assigning_deliverypartner_listitem1, deliveryPartnerList);
        this.OrderKey=orderKey;
        this.mContext=mContext;
        this.deliveryPartnerList=deliveryPartnerList;
        }


public int getCount() {
        return super.getCount();
        }

@Nullable
@Override
public AssignDeliveryPartner_PojoClass getItem(int position) {
        return super.getItem(position);
        }

@Override
public int getPosition(@Nullable AssignDeliveryPartner_PojoClass item) {
        return super.getPosition(item);
        }

public View getView(final int pos, View view, ViewGroup v) {
@SuppressLint("ViewHolder") final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.mobile_screen_assigning_deliverypartner_listitem1, (ViewGroup) view, false);



final TextView deliveryPartner_name_widget = listViewItem.findViewById(R.id.deliveryPartner_name_widget);
final TextView deliveryPartner_mobileNo_widget = listViewItem.findViewById(R.id.deliveryPartner_mobileNo_widget);
final TextView deliveryPartner_status = listViewItem.findViewById(R.id.deliveryPartner_status);
final Button assignPartner_widget = listViewItem.findViewById(R.id.assignPartner_widget);

final AssignDeliveryPartner_PojoClass assignDeliveryPartner_pojoClass =deliveryPartnerList.get(pos);
        deliveryPartner_mobileNo_widget.setText(assignDeliveryPartner_pojoClass.getDeliveryPartnerMobileNo());
        deliveryPartner_name_widget.setText(assignDeliveryPartner_pojoClass.getDeliveryPartnerName());
        deliveryPartner_status.setText(assignDeliveryPartner_pojoClass.getDeliveryPartnerStatus());
        String deliveryPartnerKey = assignDeliveryPartner_pojoClass.getDeliveryPartnerKey();
        String deliveryPartnerMobileNo = assignDeliveryPartner_pojoClass.getDeliveryPartnerMobileNo();
        String deliveryPartnerName = assignDeliveryPartner_pojoClass.getDeliveryPartnerName();

        Log.i(Constants.TAG,"deliveryPartnerKey"+deliveryPartnerKey);


        assignPartner_widget.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {

        JSONObject jsonObject = new JSONObject();
        try {
        jsonObject.put("key", OrderKey);
        jsonObject.put("deliveryuserkey", deliveryPartnerKey);
        jsonObject.put("deliveryusermobileno", deliveryPartnerMobileNo);
        jsonObject.put("deliveryusername", deliveryPartnerName);

        Log.i("tag","listenertoken"+ "");

        } catch (JSONException e) {
        e.printStackTrace();
        Log.d(Constants.TAG, "JSONOBJECT: " + e);

        }
        Log.d(Constants.TAG, "Request Payload: " + jsonObject);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.api_updateTrackingOrderTable,
        jsonObject, new Response.Listener<JSONObject>() {
@Override
public void onResponse(@NonNull JSONObject response) {
        try {
        String msg = String.valueOf(response.get("message"));
        Log.d(Constants.TAG, "Response: " + msg);
        if(msg.equals("success")){

            /*Intent intent = new Intent(mContext, MobileScreen_Dashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intent);


             */
    /*    Intent intent =new Intent(mContext, MobileScreen_Dashboard.class);
        mContext.startActivity(intent);

     */
            for(int i=0;i<Mobile_ManageOrders1.sorted_OrdersList.size();i++){
                final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = Mobile_ManageOrders1.sorted_OrdersList.get(i);
                String TrackingTableKey  = modal_manageOrders_forOrderDetailList1.getKeyfromtrackingDetails().toString();
                if(TrackingTableKey.equals(OrderKey)){
                    modal_manageOrders_forOrderDetailList1.setDeliveryPartnerName(deliveryPartnerName);
                    modal_manageOrders_forOrderDetailList1.setDeliveryPartnerKey(deliveryPartnerKey);
                    modal_manageOrders_forOrderDetailList1.setDeliveryPartnerMobileNo(deliveryPartnerMobileNo);

                }
            }
            Intent i = new Intent(mContext, MobileScreen_Dashboard.class);
            i.putExtra("key",OrderKey);
            i.putExtra("deliveryuserkey",deliveryPartnerKey);
            i.putExtra("deliveryusermobileno",deliveryPartnerMobileNo);
            i.putExtra("deliveryusername",deliveryPartnerName);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            mContext.startActivity(i);
        }
        } catch (JSONException e) {
        e.printStackTrace();
        }
        Log.d(Constants.TAG, "Response: " + response);
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

        // Make the request

    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    Volley.newRequestQueue(mContext).add(jsonObjectRequest);

        }
        });




        return  listViewItem ;

        }






        }

