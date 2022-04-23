package com.meatchop.tmcpartner.MobileScreen_JavaClasses.Mobile_NewOrders;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.meatchop.tmcpartner.PosScreen_JavaClasses.Pos_NewOrders.Modal_WholeSaleCustomers;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Pos_NewOrders.NewOrders_MenuItem_Fragment;
import com.meatchop.tmcpartner.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_AutoCompleteWholeSaleCustomers_Mobile extends ArrayAdapter<Modal_WholeSaleCustomers> {
    private Context context;
    List<Modal_WholeSaleCustomers> wholeSaleCustomersArrayList = new ArrayList<>();
    private Handler handler;

    NewOrderScreenFragment_mobile newOrderScreenFragment_mobile;


    public Adapter_AutoCompleteWholeSaleCustomers_Mobile(@NonNull Context context, List<Modal_WholeSaleCustomers> wholeSaleCustomersList, NewOrderScreenFragment_mobile newOrderScreenFragment_mobile) {
        super(context, 0);
        this.wholeSaleCustomersArrayList = wholeSaleCustomersList;
        this.context = context;
        this.newOrderScreenFragment_mobile = newOrderScreenFragment_mobile;
    }

    @NonNull
    @Override
    public android.widget.Filter getFilter() {
        return Filter;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    private void sendHandlerMessage(String mobilestr) {

        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("dropdown", "dropdown");
        bundle.putString("mobileno", mobilestr);

        msg.setData(bundle);


        handler.sendMessage(msg);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.neworders_autocomplete_text_item_mobile, parent, false
            );
        }

        TextView menuItemName_widget = convertView.findViewById(R.id.menuItemName_widget);
        Modal_WholeSaleCustomers modal_wholeSaleCustomers = getItem(position);
        menuItemName_widget.setText(modal_wholeSaleCustomers.getCustomerName());
        CardView Customername_CardLayout = convertView.findViewById(R.id.addItem_to_Cart_Layout);
        Customername_CardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Modal_WholeSaleCustomers modal_wholeSaleCustomers_new = new Modal_WholeSaleCustomers();

                try{
                    modal_wholeSaleCustomers_new.setCustomerName(String.valueOf(modal_wholeSaleCustomers.getCustomerName()));

                }
                catch(Exception e){
                    Toast.makeText(context,"Can't Get customer name at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }



                try{
                    modal_wholeSaleCustomers_new.setMobileno(String.valueOf(modal_wholeSaleCustomers.getMobileno()));

                }
                catch(Exception e){
                    Toast.makeText(context,"Can't Get customer mobile number at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }

                //sendHandlerMessage(String.valueOf(modal_wholeSaleCustomers.getMobileno()));
                String mobileno = String.valueOf(modal_wholeSaleCustomers.getMobileno());
                mobileno = mobileno.replace("+91","");
               EditText editText =  newOrderScreenFragment_mobile.bottomSheetDialog.findViewById(R.id.customermobileno_editwidget);
                editText.setText(String.valueOf(mobileno));
                AutoCompleteTextView autoCompleteTextView = newOrderScreenFragment_mobile.bottomSheetDialog.findViewById(R.id.autoCompleteCustomerName_widget);
                autoCompleteTextView.setText(String.valueOf(modal_wholeSaleCustomers.getCustomerName()));
                autoCompleteTextView.dismissDropDown();

            }
        });


        return convertView;

    }



    private Filter Filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Modal_WholeSaleCustomers> suggestions = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(wholeSaleCustomersArrayList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Modal_WholeSaleCustomers item : wholeSaleCustomersArrayList) {
                    if (item.getCustomerName().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                       // newOrders_menuItem_fragment.mobileNo_Edit_widget.setFocusable(true );

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

            return ((Modal_WholeSaleCustomers) resultValue).getCustomerName();
        }
    };
}

