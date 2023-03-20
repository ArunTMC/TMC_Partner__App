package com.meatchop.tmcpartner.tmc_testtable_details;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TMCTestTable_AsyncTask extends AsyncTask<String, String, List<Modal_TMCTestTable>> {

    List<Modal_TMCTestTable> testTableList = new ArrayList<>();
    TMCTestTableInterface tmcTestTableInterface = null;
    Context mContext;
    String url;



    public TMCTestTable_AsyncTask(Context mContext, TMCTestTableInterface tmcTestTableInterfacee , String urlToCall) {
        this.tmcTestTableInterface = tmcTestTableInterfacee;
        this.mContext = mContext;
        this.url = urlToCall;

    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }


    @Override
    protected List<Modal_TMCTestTable> doInBackground(String... strings) {
        try {
            testTableList = new ArrayList<>();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(@NonNull JSONObject response) {
                            if(tmcTestTableInterface != null) {
                            try{
                                String ordertype = "#";

                                testTableList.clear();

                                //converting jsonSTRING into array
                                JSONArray JArray = response.getJSONArray("content");
                                //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                                int i1 = 0;
                                int arrayLength = JArray.length();
                                //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);

                                if(arrayLength>0) {
                                    for (; i1 < (arrayLength); i1++) {
                                        JSONObject json = JArray.getJSONObject(i1);
                                        Modal_TMCTestTable modal_tmcTestTable = new Modal_TMCTestTable();


                                        try {
                                            if (json.has("vendorkey")) {
                                                modal_tmcTestTable.vendorkey = String.valueOf(json.get("vendorkey"));
                                            } else {
                                                modal_tmcTestTable.vendorkey = "";
                                            }
                                        } catch (Exception e) {
                                            modal_tmcTestTable.vendorkey = "";
                                            e.printStackTrace();
                                        }
                                        try {
                                            if (json.has("menuitemid")) {
                                                modal_tmcTestTable.menuitemid = String.valueOf(json.get("menuitemid"));
                                            } else {
                                                modal_tmcTestTable.menuitemid = "";
                                            }
                                        } catch (Exception e) {
                                            modal_tmcTestTable.menuitemid = "";
                                            e.printStackTrace();
                                        }

                                        try {
                                            if (json.has("uniquekey")) {
                                                modal_tmcTestTable.uniquekey = String.valueOf(json.get("uniquekey"));
                                            } else {
                                                modal_tmcTestTable.uniquekey = "";
                                            }
                                        } catch (Exception e) {
                                            modal_tmcTestTable.uniquekey = "";
                                            e.printStackTrace();
                                        }

                                        try {
                                            if (json.has("deliverymarkuppercentage")) {
                                                modal_tmcTestTable.deliverymarkuppercentage = String.valueOf(json.get("deliverymarkuppercentage"));
                                            } else {
                                                modal_tmcTestTable.deliverymarkuppercentage = "";
                                            }
                                        } catch (Exception e) {
                                            modal_tmcTestTable.deliverymarkuppercentage = "";
                                            e.printStackTrace();
                                        }

                                        try {
                                            if (json.has("discount")) {
                                                modal_tmcTestTable.discount = String.valueOf(json.get("discount"));
                                            } else {
                                                modal_tmcTestTable.discount = "";
                                            }
                                        } catch (Exception e) {
                                            modal_tmcTestTable.discount = "";
                                            e.printStackTrace();
                                        }

                                        try {
                                            if (json.has("Itemname")) {
                                                modal_tmcTestTable.Itemname = String.valueOf(json.get("Itemname"));
                                            } else {
                                                modal_tmcTestTable.Itemname = "";
                                            }
                                        } catch (Exception e) {
                                            modal_tmcTestTable.Itemname = "";
                                            e.printStackTrace();
                                        }

                                        try {
                                            if (json.has("orderid")) {
                                                modal_tmcTestTable.orderid = String.valueOf(json.get("orderid"));
                                            } else {
                                                modal_tmcTestTable.orderid = "";
                                            }
                                        } catch (Exception e) {
                                            modal_tmcTestTable.orderid = "";
                                            e.printStackTrace();
                                        }

                                        try {
                                            if (json.has("ordertype")) {
                                                modal_tmcTestTable.ordertype = String.valueOf(json.get("ordertype"));
                                            } else {
                                                modal_tmcTestTable.ordertype = "";
                                            }
                                        } catch (Exception e) {
                                            modal_tmcTestTable.ordertype = "";
                                            e.printStackTrace();
                                        }

                                        try {
                                            if (json.has("price")) {
                                                modal_tmcTestTable.price = String.valueOf(json.get("price"));
                                            } else {
                                                modal_tmcTestTable.price = "";
                                            }
                                        } catch (Exception e) {
                                            modal_tmcTestTable.price = "";
                                            e.printStackTrace();
                                        }


                                        try {
                                            if (json.has("pricetype")) {
                                                modal_tmcTestTable.pricetype = String.valueOf(json.get("pricetype"));
                                            } else {
                                                modal_tmcTestTable.pricetype = "";
                                            }
                                        } catch (Exception e) {
                                            modal_tmcTestTable.pricetype = "";
                                            e.printStackTrace();
                                        }
                                        try {
                                            if (json.has("quantity")) {
                                                modal_tmcTestTable.quantity = String.valueOf(json.get("quantity"));
                                            } else {
                                                modal_tmcTestTable.quantity = "";
                                            }
                                        } catch (Exception e) {
                                            modal_tmcTestTable.quantity = "";
                                            e.printStackTrace();
                                        }

                                        try {
                                            if (json.has("slotdate")) {
                                                modal_tmcTestTable.slotdate = String.valueOf(json.get("slotdate"));
                                            } else {
                                                modal_tmcTestTable.slotdate = "";
                                            }
                                        } catch (Exception e) {
                                            modal_tmcTestTable.slotdate = "";
                                            e.printStackTrace();
                                        }

                                        try {
                                            if (json.has("subctgykey")) {
                                                modal_tmcTestTable.subctgykey = String.valueOf(json.get("subctgykey"));
                                            } else {
                                                modal_tmcTestTable.subctgykey = "";
                                            }
                                        } catch (Exception e) {
                                            modal_tmcTestTable.subctgykey = "";
                                            e.printStackTrace();
                                        }

                                        try {
                                            if (json.has("weight")) {
                                                modal_tmcTestTable.weight = String.valueOf(json.get("weight"));
                                            } else {
                                                modal_tmcTestTable.weight = "";
                                            }
                                        } catch (Exception e) {
                                            modal_tmcTestTable.weight = "";
                                            e.printStackTrace();
                                        }

                                        try {
                                            if (json.has("price")) {
                                                modal_tmcTestTable.price = String.valueOf(json.get("price"));
                                            } else {
                                                modal_tmcTestTable.price = "";
                                            }
                                        } catch (Exception e) {
                                            modal_tmcTestTable.price = "";
                                            e.printStackTrace();
                                        }






                                        testTableList.add(modal_tmcTestTable);

                                        if((arrayLength-1) == i1){
                                            tmcTestTableInterface.notifySuccess("requestType", testTableList);
                                        }



                                    }
                                    }
                                    }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                            }


                        }

                    },new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {
                    Toast.makeText(mContext,"There is nocc Orders Yet ",Toast.LENGTH_LONG).show();
                    if(testTableList!=null)

                        tmcTestTableInterface.notifyError(String.valueOf(error));

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
            tmcTestTableInterface.notifyError(String.valueOf(e));

        }
        return null;
    }



    @Override
    protected void onPostExecute( List<Modal_TMCTestTable> ordersList ) {
        super.onPostExecute(ordersList);

    }

}
