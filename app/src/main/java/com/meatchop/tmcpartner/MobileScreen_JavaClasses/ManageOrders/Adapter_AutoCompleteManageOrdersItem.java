package com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Pos_NewOrders.Modal_NewOrderItems;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Pos_NewOrders.NewOrders_MenuItem_Fragment;
import com.meatchop.tmcpartner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.meatchop.tmcpartner.Constants.TAG;

public class Adapter_AutoCompleteManageOrdersItem extends ArrayAdapter<Modal_ManageOrders_Pojo_Class> {
    String menulist;
    private  List<Modal_ManageOrders_Pojo_Class> ordersList=new ArrayList<>();
    private Context context;
    private Handler handler;
    int currentPosition;
    public Adapter_AutoCompleteManageOrdersItem(@NonNull Context context, @NonNull String menuList) {
        super(context, 0);
        this.menulist=menuList;
        convertingJsonStringintoArray(menulist);
        this.context=context;
    }
    @NonNull
    @Override
    public Filter getFilter() {
        return menuFilter;
    }

    public void setHandler(Handler handler) { this.handler = handler; }
    private void sendHandlerMessage(String bundlestr) {
        Log.i(Constants.TAG,"createBillDetails in AutoComplete");

        Message msg =  new Message();
        Bundle bundle = new Bundle();
        bundle.putString("dropdown", bundlestr);
        msg.setData(bundle);



        handler.sendMessage(msg);

    }


    private void convertingJsonStringintoArray( String jsonString) {
        try {
            ordersList.clear();

            //converting jsonSTRING into array
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray JArray  = jsonObject.getJSONArray("content");
            Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
            int i1=0;
            int arrayLength = JArray.length();
            Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


            for(;i1<(arrayLength);i1++) {

                try {
                    JSONObject json = JArray.getJSONObject(i1);
                    Modal_ManageOrders_Pojo_Class manageOrdersPojoClass = new Modal_ManageOrders_Pojo_Class();
                    Log.d(Constants.TAG, "convertingJsonStringintoArray orderStatus: " + String.valueOf(json.get("orderStatus")));

                    manageOrdersPojoClass.orderid = String.valueOf(json.get("orderid"));
                    manageOrdersPojoClass.payableamount = String.valueOf(json.get("payableamount"));
                    manageOrdersPojoClass.paymentmode = String.valueOf(json.get("paymentmode"));
                    manageOrdersPojoClass.tokenno = String.valueOf(json.get("tokenno"));
                    JSONArray itemdesp  = json.getJSONArray("itemdesp");
                    manageOrdersPojoClass.itemdesp = itemdesp;
                    manageOrdersPojoClass.orderstatus = String.valueOf(json.get("orderStatus"));
                    manageOrdersPojoClass.usermobile = String.valueOf(json.get("usermobile"));
                    manageOrdersPojoClass.vendorkey = String.valueOf(json.get("vendorkey"));
                    manageOrdersPojoClass.orderdetailskey = String.valueOf(json.get("orderdetailskey"));

                    manageOrdersPojoClass.coupondiscamount = String.valueOf(json.get("coupondiscount"));
                    manageOrdersPojoClass.deliverytype = String.valueOf(json.get("deliverytype"));
                    manageOrdersPojoClass.slotdate = String.valueOf(json.get("slotdate"));
                    manageOrdersPojoClass.slotname = String.valueOf(json.get("slotname"));
                    manageOrdersPojoClass.orderconfirmedtime = "";
                    manageOrdersPojoClass.orderreadytime = "";
                    manageOrdersPojoClass.orderpickeduptime = "";
                    manageOrdersPojoClass.orderdeliveredtime = "";





                    String orderstatus = String.valueOf(json.get("orderStatus")).toUpperCase();
                    if(!orderstatus.equals(Constants.NEW_ORDER_STATUS)){
                        if(json.has("deliveryuserkey")) {
                            manageOrdersPojoClass.deliveryPartnerKey = String.valueOf(json.get("deliveryuserkey"));
                        }
                        if(json.has("deliveryusermobileno")) {
                            manageOrdersPojoClass.deliveryPartnerMobileNo = String.valueOf(json.get("deliveryusermobileno"));
                        }if(json.has("deliveryusername")) {
                            manageOrdersPojoClass.deliveryPartnerName = String.valueOf(json.get("deliveryusername"));
                        }

                    }
                    manageOrdersPojoClass.orderType = String.valueOf(json.get("ordertype"));
                    manageOrdersPojoClass.orderplacedtime = String.valueOf(json.get("orderplacedtime"));
                    if(json.has("orderconfirmedtime")) {
                        manageOrdersPojoClass.orderconfirmedtime = String.valueOf(json.get("orderconfirmedtime"));
                    }
                    if(json.has("orderreadytime")) {
                        manageOrdersPojoClass.orderreadytime = String.valueOf(json.get("orderreadytime"));
                    }
                    if(json.has("orderpickeduptime")) {
                        manageOrdersPojoClass.orderpickeduptime = String.valueOf(json.get("orderpickeduptime"));
                    }
                    if(json.has("orderdeliveredtime")) {
                        manageOrdersPojoClass.orderdeliveredtime = String.valueOf(json.get("orderdeliveredtime"));
                    }


                    manageOrdersPojoClass.keyfromtrackingDetails = String.valueOf(json.get("keyfromtrackingDetails"));
                    ordersList.add(manageOrdersPojoClass);


                    Log.d(Constants.TAG, "convertingJsonStringintoArray ordersList: " + ordersList);



                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getLocalizedMessage());
                    Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getMessage());
                    Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.toString());

                }


            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.neworders_autocomplete_text_item, parent, false
            );
        }

        TextView menuItemName_widget = convertView.findViewById(R.id.menuItemName_widget);
        Modal_ManageOrders_Pojo_Class menuuItem = getItem(position);
        menuItemName_widget.setText(menuuItem.getUsermobile());
        CardView menuItem_CardLayout = convertView.findViewById(R.id.addItem_to_Cart_Layout);

        Log.d(TAG, "Auto 2  menu in  Menulist"+ordersList.size());




        menuItem_CardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        return convertView;
    }


    private Filter menuFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Modal_ManageOrders_Pojo_Class> suggestions = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(ordersList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Modal_ManageOrders_Pojo_Class item : ordersList) {
                    if (item.getUsermobile().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }
        @Override
        public CharSequence convertResultToString(Object resultValue) {

            return ((Modal_ManageOrders_Pojo_Class) resultValue).getUsermobile();
        }
    };
}

