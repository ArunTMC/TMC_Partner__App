package com.meatchop.tmcpartner.mobilescreen_javaclasses.mobile_neworders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.meatchop.tmcpartner.posscreen_javaclasses.pos_new_orders.NewOrders_MenuItem_Fragment;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.settings.Modal_Address;
import com.meatchop.tmcpartner.settings.Modal_DeliverySlabDetails;

import java.util.ArrayList;
import java.util.List;

public class Adapter_AddressList extends ArrayAdapter<Modal_Address> {
    Context mContext;
    List<Modal_Address> addressList = new ArrayList<>();
    NewOrderScreenFragment_mobile newOrderScreenFragment_mobile ;
    boolean isFromPOSOdersScreen = false;
    NewOrders_MenuItem_Fragment newOrders_menuItem_fragment_POS_screen;
    String userKey = "";                double deliveryDistance_double =0;

    public Adapter_AddressList(Context mContext, List<Modal_Address> addressListt, NewOrderScreenFragment_mobile newOrderScreenFragment_mobile) {
        super(mContext, R.layout.address_list_item_listview, addressListt);
        this.newOrderScreenFragment_mobile = newOrderScreenFragment_mobile;
        this.mContext=mContext;
        this.addressList=addressListt;
        isFromPOSOdersScreen = false;


    }

    public Adapter_AddressList(Context mContext, List<Modal_Address> userAddressArrayList, NewOrders_MenuItem_Fragment newOrders_menuItem_fragment, boolean isfromPOSOdersScreen) {
        super(mContext, R.layout.address_list_item_listview, userAddressArrayList);
        this.newOrderScreenFragment_mobile = newOrderScreenFragment_mobile;
        this.mContext=mContext;
        this.addressList=userAddressArrayList;
        isFromPOSOdersScreen = true;
        this.newOrders_menuItem_fragment_POS_screen = newOrders_menuItem_fragment;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        @SuppressLint("ViewHolder") final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.address_list_item_listview, (ViewGroup) view, false);


        final TextView contactPersonName = listViewItem.findViewById(R.id.contactPersonName);
        final TextView contactPersonMobileNo = listViewItem.findViewById(R.id.contactPersonMobileNo);
        final TextView addresstext = listViewItem.findViewById(R.id.addresstext);
        final TextView address_type = listViewItem.findViewById(R.id.address_type);
        final TextView delivery_distance_in_km = listViewItem.findViewById(R.id.delivery_distance_in_km);

        final LinearLayout selectAddress_checkboxLayout = listViewItem.findViewById(R.id.selectAddress_checkboxLayout);
        final CheckBox selectAddress_checkbox = listViewItem.findViewById(R.id.selectAddress_checkbox);



        Modal_Address modal_address = addressList.get(position);

        try{
            contactPersonName.setText(String.valueOf(modal_address.getContactpersonname()));
        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            userKey = (String.valueOf(modal_address.getUserkey()));
        }
        catch (Exception e){
            userKey ="";
            e.printStackTrace();
        }

        try{
            contactPersonMobileNo.setText(String.valueOf(modal_address.getContactpersonmobileno()));

        }
        catch (Exception e){
            e.printStackTrace();
        }



        try{
            addresstext.setText(modal_address.getAddressline1() + " , " +  String.valueOf(modal_address.getAddressline2()) +" - "  + String.valueOf(modal_address.getPincode()));

        }
        catch (Exception e){
            e.printStackTrace();
        }



        try{
            address_type.setText(String.valueOf(modal_address.getAddresstype()));

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            selectAddress_checkbox.setChecked(modal_address.getisAddressSelected());

        }
        catch (Exception e){
            e.printStackTrace();
        }



        try{
            delivery_distance_in_km.setText(String.valueOf(modal_address.getDeliverydistance() +" KM"));

        }
        catch (Exception e){
            e.printStackTrace();
        }


        selectAddress_checkboxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key_address = String.valueOf(modal_address.getKey());

                if(selectAddress_checkbox.isChecked()){
                    selectAddress_checkbox.setChecked(false);
                    if(isFromPOSOdersScreen){
                        newOrders_menuItem_fragment_POS_screen. isAddressForPhoneOrderSelected  = false;

                    }
                    else{
                        newOrderScreenFragment_mobile. isAddressForPhoneOrderSelected  = false;

                    }

                    changeselectedAddressStatus(false,key_address);
                }
                else{
                    if(isFromPOSOdersScreen){
                        newOrders_menuItem_fragment_POS_screen.isAddressForPhoneOrderSelected  = true;
                    }
                    else{
                        newOrderScreenFragment_mobile. isAddressForPhoneOrderSelected  = true;

                    }



                    selectAddress_checkbox.setChecked(true);
                    changeselectedAddressStatus(true,key_address);

                }
            }
        });



        return  listViewItem;
    }

    private void changeselectedAddressStatus(boolean status , String key_address) {
        if(isFromPOSOdersScreen) {
            newOrders_menuItem_fragment_POS_screen.loadingpanelmask_Addressdialog.setVisibility(View.VISIBLE);
            newOrders_menuItem_fragment_POS_screen.loadingPanel_Addressdialog.setVisibility(View.VISIBLE);
        }
        else{

            newOrderScreenFragment_mobile.loadingpanelmask_addressbottomSheet.setVisibility(View.VISIBLE);
            newOrderScreenFragment_mobile.loadingPanel_addressbottomSheet.setVisibility(View.VISIBLE);
        }
        if(isFromPOSOdersScreen) {
            for (int i = 0; i < newOrders_menuItem_fragment_POS_screen.userAddressArrayList.size(); i++) {
                Modal_Address modal_address = new Modal_Address();
                modal_address = newOrders_menuItem_fragment_POS_screen.userAddressArrayList.get(i);
                String keyFromarray = "", addressLine1 = "", addressLine2 = "", pincode = "", userLatitude = "0", userLongitude = "0",deliveryDistance = "0";
                try {
                    keyFromarray = String.valueOf(modal_address.getKey());

                } catch (Exception e) {
                    keyFromarray = "";
                    e.printStackTrace();
                }

                try {
                    addressLine1 = String.valueOf(modal_address.getAddressline1());

                } catch (Exception e) {
                    addressLine1 = "";
                    e.printStackTrace();
                }

                try {
                    addressLine2 = String.valueOf(modal_address.getAddressline2());

                } catch (Exception e) {
                    addressLine2 = "";
                    e.printStackTrace();
                }

                try {
                    pincode = String.valueOf(modal_address.getPincode());

                } catch (Exception e) {
                    pincode = "";
                    e.printStackTrace();
                }

                try {
                    userLatitude = String.valueOf(modal_address.getLocationlat());

                } catch (Exception e) {
                    userLatitude = "0";
                    e.printStackTrace();
                }


                try {
                    userLongitude = String.valueOf(modal_address.getLocationlong());

                } catch (Exception e) {
                    userLongitude = "0";
                    e.printStackTrace();
                }
                try {
                    deliveryDistance = String.valueOf(modal_address.getDeliverydistance());

                } catch (Exception e) {
                    deliveryDistance = "0";
                    e.printStackTrace();
                }





                if (status) {
                    if (key_address.equals(keyFromarray)) {

                         newOrders_menuItem_fragment_POS_screen.userAddressArrayList.get(i).setAddressSelected(true);
                         newOrders_menuItem_fragment_POS_screen.selected_Address_modal.setAddressSelected(true);
                        // newOrders_menuItem_fragment_POS_screen.selectedAddress = String.valueOf(addressLine1 + " , " + addressLine2 + " - " + pincode);
                         newOrders_menuItem_fragment_POS_screen.selected_Address_modal.setAddressline1( String.valueOf(addressLine1 + " , " + addressLine2 + " - " + pincode));
                        // newOrders_menuItem_fragment_POS_screen.selectedAddressKey = String.valueOf(keyFromarray);
                         newOrders_menuItem_fragment_POS_screen.selected_Address_modal.setKey(String.valueOf(keyFromarray));
                         newOrders_menuItem_fragment_POS_screen.selected_Address_modal.setLocationlat(String.valueOf(userLatitude));
                         newOrders_menuItem_fragment_POS_screen.selected_Address_modal.setLocationlong(String.valueOf(userLongitude));
                       //  newOrders_menuItem_fragment_POS_screen.userLatitude = String.valueOf(userLatitude);
                       //  newOrders_menuItem_fragment_POS_screen.userLongitude = String.valueOf(userLongitude);
                       // newOrders_menuItem_fragment_POS_screen.deliveryDistance = (String.valueOf(deliveryDistance));
                        newOrders_menuItem_fragment_POS_screen.selected_Address_modal.setDeliverydistance(String.valueOf(deliveryDistance));

                        newOrders_menuItem_fragment_POS_screen.selected_Address_modal.setUserkey(userKey);
                        newOrders_menuItem_fragment_POS_screen.selectedAddress_textWidget.setText(String.valueOf(addressLine1 + " , " + addressLine2 + " - " + pincode));
                        for(int iterator = 0; iterator < newOrders_menuItem_fragment_POS_screen.deliverySlabDetailsArrayList.size(); iterator++){
                            Modal_DeliverySlabDetails modal_deliverySlabDetails = newOrders_menuItem_fragment_POS_screen.deliverySlabDetailsArrayList.get(iterator);
                            double maxkms =0, minkms = 0 , deliveryCharge =0;

                            try{
                                maxkms  = Double.parseDouble(modal_deliverySlabDetails.getMaxkms());
                            }
                            catch (Exception e){
                                maxkms =0 ;
                                e.printStackTrace();
                            }

                            try{
                                minkms  = Double.parseDouble(modal_deliverySlabDetails.getMinkms());

                            }
                            catch (Exception e){
                                minkms = 0;
                                e.printStackTrace();
                            }

                            try{
                                deliveryCharge  = Double.parseDouble(modal_deliverySlabDetails.getDeliverycharge());

                            }
                            catch (Exception e){
                                deliveryCharge =0;
                                e.printStackTrace();
                            }

                            try{
                                deliveryCharge  = Double.parseDouble(modal_deliverySlabDetails.getDeliverycharge());

                            }
                            catch (Exception e){
                                deliveryCharge =0;
                                e.printStackTrace();
                            }
                            try{
                                deliveryDistance_double = Double.parseDouble(deliveryDistance);
                            }
                            catch (Exception e){
                                deliveryDistance_double =0;
                                e.printStackTrace();
                            }

                            if(deliveryDistance_double>newOrders_menuItem_fragment_POS_screen.maxi_deliverableDistance_inSlabDetails){

                                newOrders_menuItem_fragment_POS_screen.selected_Address_modal.setDeliveryCharge(String.valueOf(newOrders_menuItem_fragment_POS_screen.deliveryAmt_fromMaxiDistance_inSlabDetails));
                                newOrders_menuItem_fragment_POS_screen.deliveryChargestext_widget.setText(String.valueOf(newOrders_menuItem_fragment_POS_screen.deliveryAmt_fromMaxiDistance_inSlabDetails));
                                newOrders_menuItem_fragment_POS_screen.deliveryAmount_for_this_order = (String.valueOf(newOrders_menuItem_fragment_POS_screen.deliveryAmt_fromMaxiDistance_inSlabDetails));
                                newOrders_menuItem_fragment_POS_screen.add_amount_ForBillDetails();

                            }
                            else if(minkms<=deliveryDistance_double && deliveryDistance_double <maxkms) {
                                newOrders_menuItem_fragment_POS_screen.selected_Address_modal.setDeliveryCharge(String.valueOf(deliveryCharge));
                                newOrders_menuItem_fragment_POS_screen.deliveryChargestext_widget.setText(String.valueOf(deliveryCharge));
                                newOrders_menuItem_fragment_POS_screen.deliveryAmount_for_this_order = String.valueOf(deliveryCharge);
                                newOrders_menuItem_fragment_POS_screen.add_amount_ForBillDetails();
                            }
                        }

                    } else {
                        newOrders_menuItem_fragment_POS_screen.userAddressArrayList.get(i).setAddressSelected(false);


                    }
                } else {
                    newOrders_menuItem_fragment_POS_screen.selectedAddress_textWidget.setText("");
                    /*newOrders_menuItem_fragment_POS_screen.selectedAddress = String.valueOf("");
                    newOrders_menuItem_fragment_POS_screen.selectedAddressKey = String.valueOf("");
                    newOrders_menuItem_fragment_POS_screen.userLatitude = String.valueOf("0");
                    newOrders_menuItem_fragment_POS_screen.userLongitude = String.valueOf("0");

                     */
                    newOrders_menuItem_fragment_POS_screen.selectedAddress_textWidget.setHint("Please select an Address");
                    newOrders_menuItem_fragment_POS_screen.userAddressArrayList.get(i).setAddressSelected(false);
                    newOrders_menuItem_fragment_POS_screen.selected_Address_modal = new Modal_Address();
                  //  newOrders_menuItem_fragment_POS_screen.deliveryDistance = (String.valueOf("0"));
                    newOrders_menuItem_fragment_POS_screen.selected_Address_modal.setDeliverydistance(String.valueOf("0"));
                    newOrders_menuItem_fragment_POS_screen.deliveryChargestext_widget.setText(String.valueOf("0"));

                }


            }

        }
        else {
            for (int i = 0; i < newOrderScreenFragment_mobile.userAddressArrayList.size(); i++) {
                Modal_Address modal_address = new Modal_Address();
                modal_address = newOrderScreenFragment_mobile.userAddressArrayList.get(i);
                String keyFromarray = "", addressLine1 = "", addressLine2 = "", pincode = "", userLatitude = "0", userLongitude = "0",deliveryDistance ="0";
                try {
                    keyFromarray = String.valueOf(modal_address.getKey());

                } catch (Exception e) {
                    keyFromarray = "";
                    e.printStackTrace();
                }

                try {
                    addressLine1 = String.valueOf(modal_address.getAddressline1());

                } catch (Exception e) {
                    addressLine1 = "";
                    e.printStackTrace();
                }

                try {
                    addressLine2 = String.valueOf(modal_address.getAddressline2());

                } catch (Exception e) {
                    addressLine2 = "";
                    e.printStackTrace();
                }

                try {
                    pincode = String.valueOf(modal_address.getPincode());

                } catch (Exception e) {
                    pincode = "";
                    e.printStackTrace();
                }

                try {
                    userLatitude = String.valueOf(modal_address.getLocationlat());

                } catch (Exception e) {
                    userLatitude = "0";
                    e.printStackTrace();
                }


                try {
                    userLongitude = String.valueOf(modal_address.getLocationlong());

                } catch (Exception e) {
                    userLongitude = "0";
                    e.printStackTrace();
                }
                try {
                    deliveryDistance = String.valueOf(modal_address.getDeliverydistance());

                } catch (Exception e) {
                    deliveryDistance = "0";
                    e.printStackTrace();
                }






                if (status) {
                    if (key_address.equals(keyFromarray)) {
                        newOrderScreenFragment_mobile.userAddressArrayList.get(i).setAddressSelected(true);
                        newOrderScreenFragment_mobile.selected_Address_modal.setAddressSelected(true);
                        newOrderScreenFragment_mobile.selectedAddress = String.valueOf(addressLine1 + " , " + addressLine2 + " - " + pincode);
                        newOrderScreenFragment_mobile.selected_Address_modal.setAddressline1( String.valueOf(addressLine1 + " , " + addressLine2 + " - " + pincode));
                        newOrderScreenFragment_mobile.selectedAddressKey = String.valueOf(keyFromarray);
                        newOrderScreenFragment_mobile.selected_Address_modal.setKey(String.valueOf(keyFromarray));
                        newOrderScreenFragment_mobile.selected_Address_modal.setLocationlat(String.valueOf(userLatitude));
                        newOrderScreenFragment_mobile.selected_Address_modal.setLocationlong(String.valueOf(userLongitude));
                        newOrderScreenFragment_mobile.userLatitude = String.valueOf(userLatitude);
                        newOrderScreenFragment_mobile.userLongitude = String.valueOf(userLongitude);
                        newOrderScreenFragment_mobile.selected_Address_modal.setUserkey(userKey);
                        newOrderScreenFragment_mobile.selected_Address_modal.setDeliverydistance(String.valueOf(deliveryDistance));
                        newOrderScreenFragment_mobile.deliveryDistance = (String.valueOf(deliveryDistance));

                        newOrderScreenFragment_mobile.fulladdress_textview.setText(String.valueOf(addressLine1 + " , " + addressLine2 + " - " + pincode));
                        for(int iterator = 0; iterator < newOrderScreenFragment_mobile.deliverySlabDetailsArrayList.size(); iterator++){
                            Modal_DeliverySlabDetails modal_deliverySlabDetails = newOrderScreenFragment_mobile.deliverySlabDetailsArrayList.get(iterator);
                            double maxkms =0, minkms = 0 , deliveryCharge =0;

                            try{
                                maxkms  = Double.parseDouble(modal_deliverySlabDetails.getMaxkms());
                            }
                            catch (Exception e){
                                maxkms =0 ;
                                e.printStackTrace();
                            }

                            try{
                                minkms  = Double.parseDouble(modal_deliverySlabDetails.getMinkms());

                            }
                            catch (Exception e){
                                minkms = 0;
                                e.printStackTrace();
                            }

                            try{
                                deliveryCharge  = Double.parseDouble(modal_deliverySlabDetails.getDeliverycharge());

                            }
                            catch (Exception e){
                                deliveryCharge =0;
                                e.printStackTrace();
                            }

                            try{
                                deliveryCharge  = Double.parseDouble(modal_deliverySlabDetails.getDeliverycharge());

                            }
                            catch (Exception e){
                                deliveryCharge =0;
                                e.printStackTrace();
                            }

                            try{
                                deliveryDistance_double = Double.parseDouble(deliveryDistance);
                            }
                            catch (Exception e){
                                deliveryDistance_double =0;
                                e.printStackTrace();
                            }

                            if(deliveryDistance_double>newOrderScreenFragment_mobile.maxi_deliverableDistance_inSlabDetails){

                                newOrderScreenFragment_mobile.selected_Address_modal.setDeliveryCharge(String.valueOf(newOrderScreenFragment_mobile.deliveryAmt_fromMaxiDistance_inSlabDetails));
                               // newOrderScreenFragment_mobile.deliveryChargeTextWidget.setText(String.valueOf(newOrderScreenFragment_mobile.deliveryAmt_fromMaxiDistance_inSlabDetails));
                                newOrderScreenFragment_mobile.deliveryAmount_for_this_order = String.valueOf(newOrderScreenFragment_mobile.deliveryAmt_fromMaxiDistance_inSlabDetails);
                                newOrderScreenFragment_mobile.add_amount_ForBillDetails();

                            }
                            else if(minkms<=deliveryDistance_double && deliveryDistance_double<maxkms) {
                                newOrderScreenFragment_mobile.selected_Address_modal.setDeliveryCharge(String.valueOf(deliveryCharge));
                              //  newOrderScreenFragment_mobile.deliveryChargeTextWidget.setText(String.valueOf(deliveryCharge));
                                newOrderScreenFragment_mobile.deliveryAmount_for_this_order = String.valueOf(deliveryCharge);
                                newOrderScreenFragment_mobile.add_amount_ForBillDetails();
                            }
                        }

                    } else {
                        newOrderScreenFragment_mobile.userAddressArrayList.get(i).setAddressSelected(false);


                    }
                }
                else {
                    newOrderScreenFragment_mobile.fulladdress_textview.setText("");
                    newOrderScreenFragment_mobile.selectedAddress = String.valueOf("");
                    newOrderScreenFragment_mobile.selectedAddressKey = String.valueOf("");
                    newOrderScreenFragment_mobile.userLatitude = String.valueOf("0");
                    newOrderScreenFragment_mobile.userLongitude = String.valueOf("0");
                    newOrderScreenFragment_mobile.fulladdress_textview.setHint("Please select an Address");
                    newOrderScreenFragment_mobile.userAddressArrayList.get(i).setAddressSelected(false);
                    newOrderScreenFragment_mobile.deliveryDistance = (String.valueOf(""));

                    newOrderScreenFragment_mobile.selected_Address_modal = new Modal_Address();
                    newOrderScreenFragment_mobile.selected_Address_modal.setDeliverydistance(String.valueOf(0));
                    newOrderScreenFragment_mobile.deliveryAmount_for_this_order = String.valueOf("0");

                    newOrderScreenFragment_mobile.deliveryChargeTextWidget.setText("0.00");
                    newOrderScreenFragment_mobile.add_amount_ForBillDetails();

                }


            }
        }
        if(isFromPOSOdersScreen){
            newOrders_menuItem_fragment_POS_screen. adapter_addressList.notifyDataSetChanged();
            newOrders_menuItem_fragment_POS_screen.loadingpanelmask_Addressdialog.setVisibility(View.GONE);
            newOrders_menuItem_fragment_POS_screen.loadingPanel_Addressdialog.setVisibility(View.GONE);


        }
        else{
            newOrderScreenFragment_mobile. adapter_addressList.notifyDataSetChanged();
            newOrderScreenFragment_mobile.loadingpanelmask_addressbottomSheet.setVisibility(View.GONE);
            newOrderScreenFragment_mobile.loadingPanel_addressbottomSheet.setVisibility(View.GONE);


        }

    }




    @Nullable
    @Override
    public Modal_Address getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    public int getPosition(@Nullable Modal_Address item) {
        return super.getPosition(item);
    }


}
