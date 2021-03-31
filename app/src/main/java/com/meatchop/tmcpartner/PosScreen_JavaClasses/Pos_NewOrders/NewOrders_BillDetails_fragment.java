package com.meatchop.tmcpartner.PosScreen_JavaClasses.Pos_NewOrders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewOrders_BillDetails_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewOrders_BillDetails_fragment extends Fragment {

  //  TextView item_total_text_widget,taxes_and_charges_text_widget,to_pay_text_widget;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Context mContext;
    public NewOrders_BillDetails_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewOrders_BillDetails_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewOrders_BillDetails_fragment newInstance(String param1, String param2) {
        NewOrders_BillDetails_fragment fragment = new NewOrders_BillDetails_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity().getWindow().getContext();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver,new IntentFilter("creatingBillDetails"));

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.neworders_bill_details_fragment, container, false);
        rootView.setTag("RecyclerViewFragment");
      //  item_total_text_widget=rootView.findViewById(R.id.item_total_text_widget);
       // taxes_and_charges_text_widget=rootView.findViewById(R.id.taxes_and_charges_text_widget);
       // to_pay_text_widget=rootView.findViewById(R.id.to_pay_text_widget);


        return rootView;


    }
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(Constants.TAG,"createBillDetails in bill details");

            String newTotalAmount = intent.getStringExtra("newTotalAmount");
            String newTaxandChargesAmount = intent.getStringExtra("newTaxandChargesAmount");
            String newAmount_toPay = intent.getStringExtra("newAmount_toPay");


            Log.i(Constants.TAG,"createBillDetails in bill details  "+newTotalAmount);

        }
    };




}