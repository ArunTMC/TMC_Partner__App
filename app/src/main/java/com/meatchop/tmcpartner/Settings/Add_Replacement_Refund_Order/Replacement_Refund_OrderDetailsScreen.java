package com.meatchop.tmcpartner.Settings.Add_Replacement_Refund_Order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcpartner.AlertDialogClass;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Other_javaClasses.Pos_LoginScreen;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.Settings.Helper;
import com.meatchop.tmcpartner.Settings.ScreenSizeOfTheDevice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Replacement_Refund_OrderDetailsScreen extends AppCompatActivity {
    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =new Modal_ManageOrders_Pojo_Class();
    String ordertype="",orderid="",slotname ="",orderDeliveredDate, orderPlacedDate ="",deliverytype="",payableMode="",payableAmount="",vendorKey="",usermobileNo="",discountAmount="";
    TextView orderid_textview_widget,ordertype_text_widget,slotname_textview_widget,slotdate_textview_widget,reason_replacementtextview_Widget,
            delivertype_textview_widget,payablemode_textview_widget,payableAmount_textview_widget,discount_textview_widget,replacementDetails_textview;
    ListView orderidItemListview;
    List<Modal_ManageOrders_Pojo_Class> OrderdItems_desp;
    List<String> itemsSelectedForReplacementStringArray;
    HashMap<String,Modal_ManageOrders_Pojo_Class>itemsSelectedForReplacementhashmap = new HashMap<>();
    List<Modal_ManageOrders_Pojo_Class> itemsSelectedForReplacement;
    double screenInches;
    Button make_replacement_button;
    static LinearLayout loadingPanel;
    static LinearLayout loadingpanelmask;
    boolean isAlreadyMarkedForReplacement = false;
    Adapter_OrderDetails_OrderedItemList adapter_forOrderDetails_listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replacement__refund__order_details_screen);

        orderid_textview_widget = findViewById(R.id.orderid_textview_widget);
        ordertype_text_widget = findViewById(R.id.ordertype_text_widget);
        slotname_textview_widget = findViewById(R.id.slotname_textview_widget);
        slotdate_textview_widget  = findViewById(R.id.slotdate_textview_widget);
        delivertype_textview_widget  = findViewById(R.id.delivertype_textview_widget);
        payablemode_textview_widget = findViewById(R.id.payablemode_textview_widget);
        payableAmount_textview_widget = findViewById(R.id.payableAmount_textview_widget);
        discount_textview_widget = findViewById(R.id.discount_textview_widget);
        orderidItemListview = findViewById(R.id.orderidItemListview);
        make_replacement_button = findViewById(R.id.make_replacement_button);
        replacementDetails_textview  = findViewById(R.id.replacementDetails_textview);
        reason_replacementtextview_Widget  = findViewById(R.id.reason_replacementtextview_Widget);
        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        loadingPanel = findViewById(R.id.loadingPanel);


        OrderdItems_desp = new ArrayList<>();
        itemsSelectedForReplacementStringArray = new ArrayList<>();


        try {
            ScreenSizeOfTheDevice screenSizeOfTheDevice = new ScreenSizeOfTheDevice();
            screenInches = screenSizeOfTheDevice.getDisplaySize(Replacement_Refund_OrderDetailsScreen.this);
           // Toast.makeText(this, "ScreenSizeOfTheDevice : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
            try {
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
                double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
                screenInches = Math.sqrt(x + y);
                //Toast.makeText(this, "DisplayMetrics : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();

            }
            catch (Exception e1){
                e1.printStackTrace();
            }


        }

        SharedPreferences shared = getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        vendorKey = shared.getString("VendorKey","");

        Bundle bundle = getIntent().getExtras();
        modal_manageOrders_pojo_class = bundle.getParcelable("data");


        make_replacement_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reason_replacementtextview_Widget.getText().toString().length()>0) {
                    if ((itemsSelectedForReplacementStringArray.size() > 0) && (itemsSelectedForReplacementhashmap.size() > 0)) {
                        showProgressBar(true);

                        addEntryinReplacementOrderDetails();
                    } else {
                        AlertDialogClass.showDialog(Replacement_Refund_OrderDetailsScreen.this, Constants.AtleastSelectAnyOneMenuItem, 0);

                    }
                }
                else{
                    AlertDialogClass.showDialog(Replacement_Refund_OrderDetailsScreen.this, Constants.FillTheReasonForReplacement, 0);

                }
            }
        });

        try{
            usermobileNo = String.valueOf(modal_manageOrders_pojo_class.getUsermobile().toUpperCase());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            ordertype = String.valueOf(modal_manageOrders_pojo_class.getOrderType().toUpperCase());
        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            orderid = String.valueOf(modal_manageOrders_pojo_class.getOrderid());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            showProgressBar(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try{
            slotname = String.valueOf(modal_manageOrders_pojo_class.getSlotname());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            orderDeliveredDate = String.valueOf(modal_manageOrders_pojo_class.getOrderdeliveredtime());
            if(orderDeliveredDate.toUpperCase().equals("NULL")){
                orderDeliveredDate = "";

            }
        }
        catch (Exception e){
            orderDeliveredDate = "";
            e.printStackTrace();
        }


        try{
            if(orderPlacedDate.equals("") || orderPlacedDate.equals("null") || orderPlacedDate.equals(null)){
                orderPlacedDate =  String.valueOf(modal_manageOrders_pojo_class.getOrderplacedtime());
            }
        }
        catch (Exception e){
            orderPlacedDate ="";
            e.printStackTrace();
        }

        try{
            deliverytype = String.valueOf(modal_manageOrders_pojo_class.getDeliverytype());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            payableMode = String.valueOf(modal_manageOrders_pojo_class.getPaymentmode());
        }
        catch (Exception e){
            e.printStackTrace();
        }



        try{
            payableAmount = String.valueOf(modal_manageOrders_pojo_class.getPayableamount());
        }
        catch (Exception e){
            e.printStackTrace();
        }



        try{
            discountAmount = String.valueOf(modal_manageOrders_pojo_class.getCoupondiscamount());
        }
        catch (Exception e){
            e.printStackTrace();
        }






        orderid_textview_widget.setText(orderid);
        ordertype_text_widget.setText(ordertype);
        slotname_textview_widget.setText(slotname);
        slotdate_textview_widget.setText(orderDeliveredDate);
        delivertype_textview_widget.setText(deliverytype);
        payablemode_textview_widget.setText(payableMode);
        payableAmount_textview_widget.setText(payableAmount);
        discount_textview_widget.setText(discountAmount);




        String itemDespString = modal_manageOrders_pojo_class.getItemdesp_string();
        try {
            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(itemDespString);
            }
            catch (Exception e ){
                jsonArray = modal_manageOrders_pojo_class.getItemdesp();
                e.printStackTrace();
            }

            for(int i=0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String subCtgyKey ="";
                Modal_ManageOrders_Pojo_Class manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                manageOrders_pojo_class.itemdesp_string = String.valueOf(json);
                if (json.has("netweight")) {
                    manageOrders_pojo_class.ItemFinalWeight = String.valueOf(json.get("netweight"));

                }
                else{
                    manageOrders_pojo_class.ItemFinalWeight = "";

                }
                try {
                    if(json.has("tmcsubctgykey")) {
                        subCtgyKey = String.valueOf(json.get("tmcsubctgykey"));
                    }
                    else {
                        subCtgyKey = " ";
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    if(json.has("menuitemid")) {
                        manageOrders_pojo_class.menuItemKey = String.valueOf(json.get("menuitemid"));
                    }
                    else {
                        manageOrders_pojo_class.menuItemKey  = "";
                    }
                }
                catch (Exception e){
                    manageOrders_pojo_class.menuItemKey  = "";

                    e.printStackTrace();
                }


                if(subCtgyKey.equals("tmcsubctgy_16")){
                    //  itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName+(" ( Grill House ) "), quantity);
                    manageOrders_pojo_class.itemName = "Grill House "+String.valueOf(json.get("itemname"));

                }
                else  if(subCtgyKey.equals("tmcsubctgy_15")){
                    // itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName+(" ( Ready to Cook ) "), quantity);
                    manageOrders_pojo_class.itemName = "Ready to Cook "+String.valueOf(json.get("itemname"));

                }
                else{
                    manageOrders_pojo_class.itemName = String.valueOf(json.get("itemname"));

                }
                String cutname ="";
                try {
                    if(json.has("cutname")) {
                        cutname = String.valueOf(json.get("cutname"));
                    }
                    else {
                        cutname = " ";
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    manageOrders_pojo_class.cutname= String.valueOf(cutname);

                }
                catch (Exception E){
                    E.printStackTrace();
                }


                manageOrders_pojo_class.ItemFinalPrice= String.valueOf(json.get("tmcprice"));
                manageOrders_pojo_class.quantity = String.valueOf(json.get("quantity"));
                manageOrders_pojo_class.GstAmount = String.valueOf(json.get("gstamount"));



                if(json.has("marinadeitemdesp")) {
                    JSONObject marinadesObject = json.getJSONObject("marinadeitemdesp");
                    Modal_ManageOrders_Pojo_Class marinades_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                    try {
                        if(json.has("tmcsubctgykey")) {
                            subCtgyKey = String.valueOf(json.get("tmcsubctgykey"));
                        }
                        else {
                            subCtgyKey = " ";
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try {
                        if(json.has("menuitemkey")) {
                            marinades_manageOrders_pojo_class.menuItemKey = String.valueOf(json.get("menuitemkey"));
                        }
                        else {
                            marinades_manageOrders_pojo_class.menuItemKey  = "";
                        }
                    }
                    catch (Exception e){
                        marinades_manageOrders_pojo_class.menuItemKey  = "";

                        e.printStackTrace();
                    }
                    if(subCtgyKey.equals("tmcsubctgy_16")){
                        //  itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName+(" ( Grill House ) "), quantity);
                        marinades_manageOrders_pojo_class.itemName=" Grill House  "+marinadesObject.getString("itemname");

                    }
                    else  if(subCtgyKey.equals("tmcsubctgy_15")){
                        // itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName+(" ( Ready to Cook ) "), quantity);
                        marinades_manageOrders_pojo_class.itemName=" Ready to Cook "+marinadesObject.getString("itemname");

                    }
                    else{
                        marinades_manageOrders_pojo_class.itemName=marinadesObject.getString("itemname");

                    }
                    // marinades_manageOrders_pojo_class.itemName=marinadesObject.getString("itemname");
                    marinades_manageOrders_pojo_class.ItemFinalPrice= marinadesObject.getString("tmcprice");
                    marinades_manageOrders_pojo_class.quantity =String.valueOf(json.get("quantity"));//using same marinade quantity for the meat item also
                    marinades_manageOrders_pojo_class.GstAmount = marinadesObject.getString("gstamount");
                    if (json.has("netweight")) {
                        marinades_manageOrders_pojo_class.ItemFinalWeight = marinadesObject.getString("netweight");

                    }
                    else{
                        marinades_manageOrders_pojo_class.ItemFinalWeight = "";

                    }

                    OrderdItems_desp.add(marinades_manageOrders_pojo_class);

                }

                OrderdItems_desp.add(manageOrders_pojo_class);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        checkWeatherThisOrderIsMarkedOrNot(orderid,usermobileNo);

         adapter_forOrderDetails_listview= new Adapter_OrderDetails_OrderedItemList(Replacement_Refund_OrderDetailsScreen.this, OrderdItems_desp,Replacement_Refund_OrderDetailsScreen.this);
        orderidItemListview.setAdapter(adapter_forOrderDetails_listview);
          Helper.getListViewSize(orderidItemListview, screenInches);





    }

    private void checkWeatherThisOrderIsMarkedOrNot(String orderid, String usermobileNo) {

        String userMobileNumber ="";
        userMobileNumber = usermobileNo;
        if (userMobileNumber.length() == 13) {
            String userMobileNumberEncoded = userMobileNumber;
            try {
                userMobileNumberEncoded = URLEncoder.encode(userMobileNumber, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetReplacementOrderDetailsForMobilenoOrderid + "?mobileno=" + userMobileNumberEncoded + "&orderid=" + orderid, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(@NonNull JSONObject response) {
                            //Log.d(Constants.TAG, "Response: " + response);


                            Log.d(Constants.TAG, "Response: " + response);
                            try {

                                JSONArray resultArray = response.getJSONArray("content");
                                if (resultArray.length() == 0) {
                                    showProgressBar(false);

                                    replacementDetails_textview.setVisibility(View.GONE);
                                    make_replacement_button.setVisibility(View.VISIBLE);

                                } else {
                                for (int i = 0; i < resultArray.length(); i++) {
                                    JSONObject result = resultArray.getJSONObject(i);

                                    JSONArray markeditemdesp = new JSONArray();

                                    String status_json = "", orderid_json = "",reason_json="";

                                    try {
                                        if (result.has("status")) {
                                            status_json = result.getString("status");
                                        } else {
                                            status_json = "";
                                        }
                                    } catch (Exception e) {
                                        status_json = "";

                                        e.printStackTrace();
                                    }


                                    try {
                                        if (result.has("orderid")) {
                                            orderid_json = result.getString("orderid");
                                        } else {
                                            orderid_json = "";
                                        }
                                    } catch (Exception e) {
                                        orderid_json = "";

                                        e.printStackTrace();
                                    }


                                    try {
                                        if (result.has("reasonformarked")) {
                                            reason_json = result.getString("reasonformarked");
                                        } else {
                                            reason_json = "";
                                        }
                                    } catch (Exception e) {
                                        reason_json = "";

                                        e.printStackTrace();
                                    }
                                    try {
                                        reason_replacementtextview_Widget.setText(reason_json);
                                        reason_replacementtextview_Widget.setFocusable(false);
                                        reason_replacementtextview_Widget.setEnabled(false);
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    try {
                                        if (result.has("markeditemdesp")) {
                                            markeditemdesp = result.getJSONArray("markeditemdesp");
                                        } else {
                                            markeditemdesp = new JSONArray();
                                        }
                                    } catch (Exception e) {
                                        markeditemdesp = new JSONArray();

                                        e.printStackTrace();
                                    }

                                    if(OrderdItems_desp.size()>0){
                                        for (int j = 0; j<OrderdItems_desp.size();j++){
                                            Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class= OrderdItems_desp.get(j);
                                            String menuItemKeyFromOrderDetails = modal_manageOrders_pojo_class.getMenuItemKey().toString();
                                           String menuItemKeyFromReplacementDetails ="";
                                            for (int k =0 ;k < markeditemdesp.length();k++){

                                                JSONObject json = new JSONObject();
                                                json = markeditemdesp.getJSONObject(k);
                                                if(json.has("menuitemkey")){
                                                    menuItemKeyFromReplacementDetails = json.getString("menuitemkey");
                                                }
                                                else{
                                                    menuItemKeyFromReplacementDetails="";
                                                }

                                                if(menuItemKeyFromReplacementDetails.equals(menuItemKeyFromOrderDetails)){
                                                    modal_manageOrders_pojo_class.isItemMarkedForReplacement = true;
                                                    adapter_forOrderDetails_listview.notifyDataSetChanged();
                                                }

                                            }

                                        }
                                    }


                                    showProgressBar(false);
                                    make_replacement_button.setVisibility(View.GONE);
                                    replacementDetails_textview.setVisibility(View.VISIBLE);

                                    replacementDetails_textview.setText("This Order is Already Marked For Replacement/ Refund");
                                }
                            }
                            } catch (JSONException e) {
                                showProgressBar(false);
                                make_replacement_button.setVisibility(View.VISIBLE);
                                replacementDetails_textview.setVisibility(View.GONE);

                                e.printStackTrace();
                            }

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {
                    make_replacement_button.setVisibility(View.VISIBLE);
                    replacementDetails_textview.setVisibility(View.GONE);

                    //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                    //Log.d(Constants.TAG, "Error: " + error.getMessage());
                    //Log.d(Constants.TAG, "Error: " + error.toString());
                    showProgressBar(false);

                    error.printStackTrace();
                }
            }) {


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
            Volley.newRequestQueue(Replacement_Refund_OrderDetailsScreen.this).add(jsonObjectRequest);


        }


    }

    void showProgressBar(boolean show) {
        if(show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);

        }
        else {
            loadingpanelmask.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.GONE);


        }

    }
    private void addEntryinReplacementOrderDetails() {

        JSONArray markedItemsArray = new JSONArray();
        double total_amount_avl_for_user = 0;
        String Currenttime  = getDate_and_time() ;
        String Currenttime_transactiontable  = getDate_and_time_TransactionTable() ;

        try{
            for(int i = 0 ; i<itemsSelectedForReplacementStringArray.size();i++){
                JSONObject markedItemsJsonObject = new JSONObject();
                double gstamount_double=0;
                double quantity_double=0;
                double amount_double=0;

                String itemName = itemsSelectedForReplacementStringArray.get(i);
                try{
                    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class_fromHashmap = itemsSelectedForReplacementhashmap.get(itemName);

                    try {
                        String value = modal_manageOrders_pojo_class_fromHashmap.getTmcSubCtgyKey().toString();
                        if (!value.equals("null") && !value.equals(null)) {
                            markedItemsJsonObject.put("tmcsubctgykey", value);
                        } else {
                            markedItemsJsonObject.put("tmcsubctgykey", "");
                        }
                    } catch (Exception e) {
                        markedItemsJsonObject.put("tmcsubctgykey", "");
                        e.printStackTrace();
                    }
                    try {
                        String value = modal_manageOrders_pojo_class_fromHashmap.getGrossweight().toString();
                        if (!value.equals("null") && !value.equals(null)) {
                            markedItemsJsonObject.put("grossweight", value);
                        } else {
                            markedItemsJsonObject.put("grossweight", "");
                        }
                    } catch (Exception e) {
                        markedItemsJsonObject.put("grossweight", "");
                        e.printStackTrace();
                    }
                    try {
                        String value = modal_manageOrders_pojo_class_fromHashmap.getGrossweightingrams().toString();
                        if (!value.equals("null") && !value.equals(null)) {
                            try {
                                if (!value.equals("")) {
                                    value = value.replaceAll("[^\\d.]", "");

                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }



                            double grossweightingrams_double = 0;
                            try {
                                if (!value.equals("")) {
                                    grossweightingrams_double = Double.parseDouble(value);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            if(grossweightingrams_double>0){
                                markedItemsJsonObject.put("grossweightingrams", grossweightingrams_double);

                            }

                        } else {
                            try {
                                String value2 = modal_manageOrders_pojo_class_fromHashmap.getGrossweight().toString();
                                if (!value.equals("null") && !value.equals(null)) {
                                    try {
                                        if (!value2.equals("")) {
                                            value2 = value2.replaceAll("[^\\d.]", "");

                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }



                                    double grossweightingrams_double = 0;
                                    try {
                                        if (!value2.equals("")) {
                                            grossweightingrams_double = Double.parseDouble(value2);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                    if(grossweightingrams_double>0){
                                        markedItemsJsonObject.put("grossweightingrams", grossweightingrams_double);

                                    }
                                } else {


                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    try {
                        String value = modal_manageOrders_pojo_class_fromHashmap.getGstAmount().toString();
                        if (!value.equals("null") && !value.equals(null)) {
                            value = value.replaceAll("[^\\d.]", "");
                            if(value.equals("")){
                                value ="0";
                            }
                            try {
                                gstamount_double = Double.parseDouble(value);
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                            markedItemsJsonObject.put("gstamount", gstamount_double);
                        } else {
                            value ="0";
                            try {
                                gstamount_double = Double.parseDouble(value);
                            }
                            catch (Exception e){
                                gstamount_double =0;
                                e.printStackTrace();
                            }
                            markedItemsJsonObject.put("gstamount", gstamount_double);
                        }
                    } catch (Exception e) {
                        gstamount_double =0;
                        try {
                            gstamount_double = Double.parseDouble("0");
                        }
                        catch (Exception e1){
                            gstamount_double =0;
                            e1.printStackTrace();
                        }
                        markedItemsJsonObject.put("gstamount", gstamount_double);
                        e.printStackTrace();
                    }



                    try {
                        String value = modal_manageOrders_pojo_class_fromHashmap.getItemName().toString();
                        if (!value.equals("null") && !value.equals(null)) {
                            markedItemsJsonObject.put("itemname", value);
                        } else {
                            markedItemsJsonObject.put("itemname", "");
                        }
                    } catch (Exception e) {
                        markedItemsJsonObject.put("itemname", "");
                        e.printStackTrace();
                    }

                    try {
                        String value = modal_manageOrders_pojo_class_fromHashmap.getMenuItemKey().toString();
                        if (!value.equals("null") && !value.equals(null)) {
                            markedItemsJsonObject.put("menuitemkey", value);
                        } else {
                            markedItemsJsonObject.put("menuitemkey", "");
                        }
                    } catch (Exception e) {
                        markedItemsJsonObject.put("menuitemkey", "");
                        e.printStackTrace();
                    }


                    try {
                        String value = modal_manageOrders_pojo_class_fromHashmap.getNetweight().toString();
                        if (!value.equals("null") && !value.equals(null)) {
                            markedItemsJsonObject.put("netweight", value);
                        } else {
                            markedItemsJsonObject.put("netweight", "");
                        }
                    } catch (Exception e) {
                        markedItemsJsonObject.put("netweight", "");
                        e.printStackTrace();
                    }

                    try {
                        String value = modal_manageOrders_pojo_class_fromHashmap.getPortionsize().toString();
                        if (!value.equals("null") && !value.equals(null)) {
                            markedItemsJsonObject.put("portionsize", value);
                        } else {
                            markedItemsJsonObject.put("portionsize", "");
                        }
                    } catch (Exception e) {
                        markedItemsJsonObject.put("portionsize", "");
                        e.printStackTrace();
                    }



                    try {
                        String value = modal_manageOrders_pojo_class_fromHashmap.getQuantity().toString();
                        if (!value.equals("null") && !value.equals(null)) {
                            value = value.replaceAll("[^\\d.]", "");
                            if(value.equals("")){
                                value ="0";
                            }
                            try {
                                quantity_double = Double.parseDouble(value);
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                            markedItemsJsonObject.put("quantity", quantity_double);
                        } else {
                            value ="0";
                            try {
                                quantity_double = Double.parseDouble(value);
                            }
                            catch (Exception e){
                                quantity_double =0;
                                e.printStackTrace();
                            }
                            markedItemsJsonObject.put("quantity", quantity_double);
                        }
                    } catch (Exception e) {
                         quantity_double=0;

                        try {
                            quantity_double = Double.parseDouble("0");
                        }
                        catch (Exception e1){
                            quantity_double =0;
                            e1.printStackTrace();
                        }
                        markedItemsJsonObject.put("quantity", quantity_double);
                        e.printStackTrace();
                    }



                    try {
                        String value = modal_manageOrders_pojo_class_fromHashmap.getPrice().toString();
                        if (!value.equals("null") && !value.equals(null)) {
                            value = value.replaceAll("[^\\d.]", "");
                            if(value.equals("")){
                                value ="0";
                            }
                            try {
                                amount_double = Double.parseDouble(value);
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                            markedItemsJsonObject.put("tmcprice", amount_double);
                        } else {
                            value ="0";
                            try {
                                amount_double = Double.parseDouble(value);
                            }
                            catch (Exception e){
                                amount_double =0;
                                e.printStackTrace();
                            }
                            markedItemsJsonObject.put("tmcprice", amount_double);
                        }
                    } catch (Exception e) {
                         amount_double=0;

                        try {
                            amount_double = Double.parseDouble("0");
                        }
                        catch (Exception e1){
                            amount_double =0;
                            e1.printStackTrace();
                        }
                        markedItemsJsonObject.put("tmcprice", amount_double);
                        e.printStackTrace();
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }
                total_amount_avl_for_user = total_amount_avl_for_user+amount_double+gstamount_double;
                total_amount_avl_for_user = total_amount_avl_for_user*quantity_double;
                total_amount_avl_for_user = Math.round(total_amount_avl_for_user);
                markedItemsArray.put(markedItemsJsonObject);


            }

        }
        catch (Exception e){
            e.printStackTrace();
        }



        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobileno", usermobileNo);
            jsonObject.put("orderid", orderid);
            jsonObject.put("vendorkey", vendorKey);
            jsonObject.put("amountusercanavl", total_amount_avl_for_user);
            jsonObject.put("orderplaceddate", orderPlacedDate);

            jsonObject.put("orderdelivereddate", orderDeliveredDate);
            jsonObject.put("markeddate", Currenttime);
            jsonObject.put("markeditemdesp", markedItemsArray);
            jsonObject.put("status", "INITIATED");
            jsonObject.put("reasonformarked", reason_replacementtextview_Widget.getText().toString());



        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        double finalTotal_amount_avl_for_user = total_amount_avl_for_user;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addReplacementOrderDetailsTable,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                //Log.d(Constants.TAG, "Response: " + response);
                showProgressBar(false);
                String message = "";

                try {
                    message = response.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                addDataInReplacementTransactiondetails(Currenttime_transactiontable,usermobileNo,orderid,markedItemsArray,message,reason_replacementtextview_Widget.getText().toString(), finalTotal_amount_avl_for_user);
                make_replacement_button.setVisibility(View.GONE);
                replacementDetails_textview.setVisibility(View.VISIBLE);

                replacementDetails_textview.setText("This Order is Now Marked For Replacement/Refund");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Toast.makeText(Replacement_Refund_OrderDetailsScreen.this, "Replacement Order Details is not updated", Toast.LENGTH_LONG).show();
                showProgressBar(false);

                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
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
        Volley.newRequestQueue(this).add(jsonObjectRequest);





    }

    private void addDataInReplacementTransactiondetails(String currenttime, String usermobileNo, String orderid, JSONArray markedItemsArray, String message, String reasonForMarked, double finalTotal_amount_avl_for_user) {

        showProgressBar(true);
        String markedateOldFormat = getTimeinOLDFormat(currenttime);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ordermarkeddate", markedateOldFormat);
            jsonObject.put("transactiontime", currenttime);
            jsonObject.put("orderid", orderid);
            jsonObject.put("vendorkey", vendorKey);
            jsonObject.put("transactiontype", "MARKED");
            jsonObject.put("orderdelivereddate", orderDeliveredDate);
            jsonObject.put("mobileno", usermobileNo);
            jsonObject.put("markeditemdesp", markedItemsArray);
            jsonObject.put("transactionstatus", message);
            jsonObject.put("markeditemamount", finalTotal_amount_avl_for_user);

            jsonObject.put("reasonformarked", reasonForMarked);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addReplacementTransactionDetailsTable,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                //Log.d(Constants.TAG, "Response: " + response);
                showProgressBar(false);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Toast.makeText(Replacement_Refund_OrderDetailsScreen.this, "Replacement Transaction Details is not updated", Toast.LENGTH_LONG).show();
                showProgressBar(false);

                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
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
        Volley.newRequestQueue(this).add(jsonObjectRequest);





    }

    public String getDate_and_time()
    {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);

        SimpleDateFormat day = new SimpleDateFormat("EEE");
       String CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        String CurrentDatee = df.format(c);
        String CurrentDate = CurrentDay+", "+CurrentDatee;


        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
        String FormattedTime = dfTime.format(c);
        String formattedDate = CurrentDay+", "+CurrentDatee+" "+FormattedTime;
        return formattedDate;
    }

    public String getDate_and_time_TransactionTable()
    {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => 2022-03-01T10:03:14+0530 " + c);


        SimpleDateFormat dfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String FormattedTime = dfTime.format(c);

        return FormattedTime;
    }


    private String getTimeinOLDFormat(String transactiontime) {
        String CurrentDate1 = "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date = sdf.parse(transactiontime);

                SimpleDateFormat day = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                CurrentDate1 = day.format(date);


            } catch (ParseException e) {
                e.printStackTrace();
            }


        } catch (Exception e) {

            try {
                SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
                try {
                    Date date = sdff.parse(transactiontime);

                    SimpleDateFormat day = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                    CurrentDate1 = day.format(date);

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } catch (Exception e2) {

                try {
                    SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date = sdff.parse(transactiontime);

                        SimpleDateFormat day = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                        CurrentDate1 = day.format(date);

                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                } catch (Exception e4) {
                    CurrentDate1 = transactiontime;

                    e4.printStackTrace();
                }
                e2.printStackTrace();
            }
            e.printStackTrace();
        }

        return CurrentDate1;

    }


}