package com.meatchop.tmcpartner.posscreen_javaclasses.pos_new_orders;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.meatchop.tmcpartner.R;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewOrdersFragment extends Fragment {

    Context mContext;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewOrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewOrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewOrdersFragment newInstance(String param1, String param2) {
        NewOrdersFragment fragment = new NewOrdersFragment();
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
        loadMenuItemFragment();
        loadCartItemFragment(new NewOrders_BillDetails_fragment());



    }

    void loadMenuItemFragment() {
        {
          //  if (NewOrders_MenuItem_Fragment != null) {
            NewOrders_MenuItem_Fragment NewOrders_MenuItem_Fragment =new NewOrders_MenuItem_Fragment();
                FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.MenuItemFrame, NewOrders_MenuItem_Fragment);
                transaction.commit();
            //}
        }
    }

    private void loadCartItemFragment(Fragment NewOrders_CartItem_fragment) {
        {
            if (NewOrders_CartItem_fragment != null) {
                FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.CartItemFrame, NewOrders_CartItem_fragment);
                transaction.commit();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.pos_neworders_fragment, container, false);
        rootView.setTag("RecyclerViewFragment");

        return rootView;

    }
}