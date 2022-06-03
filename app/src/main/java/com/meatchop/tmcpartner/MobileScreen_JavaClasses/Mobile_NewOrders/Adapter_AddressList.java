package com.meatchop.tmcpartner.MobileScreen_JavaClasses.Mobile_NewOrders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.Settings.Modal_Address;

import java.util.ArrayList;
import java.util.List;

public class Adapter_AddressList extends ArrayAdapter<Modal_Address> {
    Context mContext;
    List<Modal_Address> addressList = new ArrayList<>();
    NewOrderScreenFragment_mobile newOrderScreenFragment_mobile ;


    public Adapter_AddressList(Context mContext, List<Modal_Address> addressListt, NewOrderScreenFragment_mobile newOrderScreenFragment_mobile) {
        super(mContext, R.layout.address_list_item_listview, addressListt);
        this.newOrderScreenFragment_mobile = newOrderScreenFragment_mobile;
        this.mContext=mContext;
        this.addressList=addressListt;



    }

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
            contactPersonMobileNo.setText(String.valueOf(modal_address.getContactpersonmobileno()));

        }
        catch (Exception e){
            e.printStackTrace();
        }



        try{
            addresstext.setText(String.valueOf(modal_address.getAddressline1()) + " , " +  String.valueOf(modal_address.getAddressline2()) +" - "  + String.valueOf(modal_address.getPincode()));

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
            selectAddress_checkbox.setChecked(Boolean.valueOf(modal_address.getisAddressSelected()));

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
                    newOrderScreenFragment_mobile. isAddressForPhoneOrderSelected  = false;

                    changeselectedAddressStatus(false,key_address);
                }
                else{
                    newOrderScreenFragment_mobile. isAddressForPhoneOrderSelected  = true;

                    selectAddress_checkbox.setChecked(true);
                    changeselectedAddressStatus(true,key_address);

                }
            }
        });



        return  listViewItem;
    }

    private void changeselectedAddressStatus(boolean status , String key_address) {
        newOrderScreenFragment_mobile.loadingpanelmask_addressbottomSheet.setVisibility(View.VISIBLE);
        newOrderScreenFragment_mobile.loadingPanel_addressbottomSheet.setVisibility(View.VISIBLE);

        for(int i =0; i< newOrderScreenFragment_mobile.userAddressArrayList.size();i++){
        Modal_Address modal_address = new Modal_Address();
        modal_address = newOrderScreenFragment_mobile.userAddressArrayList.get(i);
        String keyFromarray ="",addressLine1="",addressLine2 ="", pincode ="";
        try{
            keyFromarray = String.valueOf(modal_address.getKey());

        }
        catch (Exception e){
            keyFromarray ="";
            e.printStackTrace();
        }

        try{
            addressLine1 = String.valueOf(modal_address.getAddressline1());

        }
        catch (Exception e){
            addressLine1 ="";
            e.printStackTrace();
        }

        try{
            addressLine2 = String.valueOf(modal_address.getAddressline2());

        }
        catch (Exception e){
            addressLine2 ="";
            e.printStackTrace();
        }

        try{
            pincode = String.valueOf(modal_address.getPincode());

        }
        catch (Exception e){
            pincode ="";
            e.printStackTrace();
        }






            if(status){
            if(key_address.equals(keyFromarray)){
                newOrderScreenFragment_mobile.userAddressArrayList.get(i).setAddressSelected(true);
                newOrderScreenFragment_mobile.fulladdress_textview.setText(String.valueOf(addressLine1+" , "+addressLine2+" - "+pincode));

            }
            else{
                newOrderScreenFragment_mobile.userAddressArrayList.get(i).setAddressSelected(false);

            }
        }
        else{
                newOrderScreenFragment_mobile.fulladdress_textview.setText("");

                newOrderScreenFragment_mobile.fulladdress_textview.setHint("Please select an Address");
            newOrderScreenFragment_mobile.userAddressArrayList.get(i).setAddressSelected(false);

        }


    }
        newOrderScreenFragment_mobile. adapter_addressList.notifyDataSetChanged();
        newOrderScreenFragment_mobile.loadingpanelmask_addressbottomSheet.setVisibility(View.GONE);
        newOrderScreenFragment_mobile.loadingPanel_addressbottomSheet.setVisibility(View.GONE);


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
