package com.meatchop.tmcpartner.VendorOrder_TrackingDetails;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendorTrackingDetails_AsyncTask extends AsyncTask<String, String,  List<Modal_ManageOrders_Pojo_Class> > {
    List<Modal_ManageOrders_Pojo_Class> orderTrackingList = new ArrayList<>();
    VendorTrackingDetailsTableInterface vendorTrackingDetailsTableInterface = null;
    Context mContext;
    String orderTrackingDetailsURL ="";

    public VendorTrackingDetails_AsyncTask(Context mContext, VendorTrackingDetailsTableInterface trackingDetailsTableInterface,String orderTrackingDetailsURL) {
    this.vendorTrackingDetailsTableInterface = trackingDetailsTableInterface;
    this.mContext = mContext;
    this.orderTrackingDetailsURL = orderTrackingDetailsURL;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }
    @Override
    protected List<Modal_ManageOrders_Pojo_Class>  doInBackground(String... strings) {
        try {
            orderTrackingList = new ArrayList<>();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, orderTrackingDetailsURL,null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(@NonNull JSONObject response) {
                            if(vendorTrackingDetailsTableInterface != null) {
                                try {
                                    String ordertype = "#";

                                    orderTrackingList.clear();

                                    //converting jsonSTRING into array
                                    JSONArray JArray = response.getJSONArray("content");
                                    //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                                    int i1 = 0;
                                    int arrayLength = JArray.length();
                                    //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);

                                    if(arrayLength>0) {
                                        for (; i1 < (arrayLength); i1++) {
                                            JSONObject json = JArray.getJSONObject(i1);
                                            Modal_ManageOrders_Pojo_Class manageOrdersPojoClass = new Modal_ManageOrders_Pojo_Class();
                                           String orderStatus ="";
                                            try {
                                                if (json.has("orderstatus")) {
                                                    manageOrdersPojoClass.orderstatus = String.valueOf(json.get("orderstatus"));
                                                    orderStatus = String.valueOf(json.get("orderstatus"));
                                                } else {
                                                    orderStatus ="";
                                                    manageOrdersPojoClass.orderstatus = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.orderstatus = "";
                                                orderStatus ="";
                                                e.printStackTrace();
                                            }



                                            try {
                                                if (json.has("deliverydistanceinkm")) {
                                                    manageOrdersPojoClass.deliverydistance = String.valueOf(json.get("deliverydistanceinkm"));
                                                    if(manageOrdersPojoClass.getDeliverydistance().toString().equals("")){
                                                        manageOrdersPojoClass.deliverydistance = "0";
                                                    }
                                                } else {
                                                    manageOrdersPojoClass.deliverydistance = "0";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.deliverydistance = "0";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("deliveryuserkey")) {
                                                    manageOrdersPojoClass.deliveryPartnerKey = String.valueOf(json.get("deliveryuserkey"));

                                                } else {
                                                    manageOrdersPojoClass.deliveryPartnerKey = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.deliveryPartnerKey = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("paymenttranscationimageurl")) {
                                                    manageOrdersPojoClass.paymenttranscationimageurl = String.valueOf(json.get("paymenttranscationimageurl"));

                                                } else {
                                                    manageOrdersPojoClass.paymenttranscationimageurl = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.paymenttranscationimageurl = "";

                                                e.printStackTrace();
                                            }


                                            try {

                                                    manageOrdersPojoClass.isdataFetchedFromOrderTrackingDetails = "TRUE";

                                            } catch (Exception e) {
                                                manageOrdersPojoClass.isdataFetchedFromOrderTrackingDetails = "TRUE";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("deliveryusermobileno")) {
                                                    manageOrdersPojoClass.deliveryPartnerMobileNo = String.valueOf(json.get("deliveryusermobileno"));

                                                } else {
                                                    manageOrdersPojoClass.deliveryPartnerMobileNo = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.deliveryPartnerMobileNo = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("deliveryusername")) {
                                                    manageOrdersPojoClass.deliveryPartnerName = String.valueOf(json.get("deliveryusername"));

                                                } else {
                                                    manageOrdersPojoClass.deliveryPartnerName = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.deliveryPartnerName = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("key")) {
                                                    manageOrdersPojoClass.keyfromtrackingDetails = String.valueOf(json.get("key"));

                                                } else {
                                                    manageOrdersPojoClass.keyfromtrackingDetails = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.keyfromtrackingDetails = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("orderconfirmedtime")) {
                                                    manageOrdersPojoClass.orderconfirmedtime = String.valueOf(json.get("orderconfirmedtime"));

                                                } else {
                                                    manageOrdersPojoClass.orderconfirmedtime = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.orderconfirmedtime = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("orderdeliverytime")) {
                                                    manageOrdersPojoClass.orderdeliveredtime = String.valueOf(json.get("orderdeliverytime"));

                                                } else {
                                                    manageOrdersPojoClass.orderdeliveredtime = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.orderdeliveredtime = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("orderid")) {
                                                    manageOrdersPojoClass.orderid = String.valueOf(json.get("orderid"));

                                                } else {
                                                    manageOrdersPojoClass.orderid = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.orderid = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("orderpickeduptime")) {
                                                    manageOrdersPojoClass.orderpickeduptime = String.valueOf(json.get("orderpickeduptime"));

                                                } else {
                                                    manageOrdersPojoClass.orderpickeduptime = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.orderpickeduptime = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("orderplacedtime")) {
                                                    manageOrdersPojoClass.orderplacedtime = String.valueOf(json.get("orderplacedtime"));

                                                } else {
                                                    manageOrdersPojoClass.orderplacedtime = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.orderplacedtime = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("orderreadytime")) {
                                                    manageOrdersPojoClass.orderreadytime = String.valueOf(json.get("orderreadytime"));

                                                } else {
                                                    manageOrdersPojoClass.orderreadytime = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.orderreadytime = "";

                                                e.printStackTrace();
                                            }



                                            try {
                                                if (json.has("slotdate")) {
                                                    manageOrdersPojoClass.slotdate = String.valueOf(json.get("slotdate"));

                                                } else {
                                                    manageOrdersPojoClass.slotdate = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.slotdate = "";

                                                e.printStackTrace();
                                            }
                                            try {
                                                if (json.has("useraddresskey")) {
                                                    manageOrdersPojoClass.useraddresskey = String.valueOf(json.get("useraddresskey"));

                                                } else {
                                                    manageOrdersPojoClass.useraddresskey = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.useraddresskey = "";

                                                e.printStackTrace();
                                            }
                                            try {
                                                if (json.has("useraddresslat")) {
                                                    manageOrdersPojoClass.useraddresslat = String.valueOf(json.get("useraddresslat"));

                                                } else {
                                                    manageOrdersPojoClass.useraddresslat = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.useraddresslat = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("useraddresslong")) {
                                                    manageOrdersPojoClass.useraddresslon = String.valueOf(json.get("useraddresslong"));

                                                } else {
                                                    manageOrdersPojoClass.useraddresslon = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.useraddresslon = "";

                                                e.printStackTrace();
                                            }
                                            try {
                                                if (json.has("usermobileno")) {
                                                    manageOrdersPojoClass.usermobile = String.valueOf(json.get("usermobileno"));

                                                } else {
                                                    manageOrdersPojoClass.usermobile = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.usermobile = "";

                                                e.printStackTrace();
                                            }




                                            if ((!manageOrdersPojoClass.getUsermobile().equals("+919876543210"))) {

                                                if (!orderTrackingDetailsURL.contains("orderstatus=CANCELLED")) {
                                                    if (!orderStatus.toString().toUpperCase().equals(Constants.CANCELLED_ORDER_STATUS)) {
                                                        orderTrackingList.add(manageOrdersPojoClass);

                                                    }
                                                } else {
                                                    orderTrackingList.add(manageOrdersPojoClass);

                                                }
                                            }

                                           // orderTrackingList.add(manageOrdersPojoClass);


                                            if (arrayLength - i1 == 1) {
                                                if (orderTrackingList != null) {
                                                    vendorTrackingDetailsTableInterface.VendorTrackingDetailsResult(orderTrackingList);
                                                }
                                            }
                                        }
                                    }
                                    else{
                                        if (orderTrackingList != null) {
                                            vendorTrackingDetailsTableInterface.VendorTrackingDetailsResult(orderTrackingList);
                                        }
                                    }
                                } catch (JSONException e) {
                                    if (orderTrackingList != null) {
                                        vendorTrackingDetailsTableInterface.VendorTrackingDetailsResult(orderTrackingList);
                                    }
                                    e.printStackTrace();
                                }
                            }


                        }

                    },new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {
                    Toast.makeText(mContext,"There is nocc Orders Yet ",Toast.LENGTH_LONG).show();
                    if(orderTrackingList!=null)

                        vendorTrackingDetailsTableInterface.VendorTrackingDetailsError((error));

                    error.printStackTrace();
                }
            })
            {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    final Map<String, String> params = new HashMap<>();

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





        }catch(Exception e){
            vendorTrackingDetailsTableInterface.VendorTrackingDetailsError((VolleyError) e);

        }
        return null;
    }
    @Override
    protected void onPostExecute( List<Modal_ManageOrders_Pojo_Class> ordersList ) {
        super.onPostExecute(ordersList);

    }
}
