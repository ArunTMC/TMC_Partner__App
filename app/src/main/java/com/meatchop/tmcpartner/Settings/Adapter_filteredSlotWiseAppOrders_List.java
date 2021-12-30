package com.meatchop.tmcpartner.Settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Adapter_filteredSlotWiseAppOrders_List extends BaseAdapter {
    String Itemname,Tokens,Quantity,cutname,finalWeight;
    ListView tokennoListview;
    ArrayList<String> tokenNo;
    ArrayAdapter<String> listAdapter ;

    RecyclerView recycler;
    RecyclerView.LayoutManager manager;
    slotwiseitem_RecyclerviewAdapter adapter;

    List<Modal_ManageOrders_Pojo_Class> filteredArray_MenuItemKey_cutWeightdetails;
    Context mContext;

    public Adapter_filteredSlotWiseAppOrders_List(Context context, List<Modal_ManageOrders_Pojo_Class> filteredArray_menuItemKey_cutWeightdetails) {
    this.filteredArray_MenuItemKey_cutWeightdetails = filteredArray_menuItemKey_cutWeightdetails;
    this.mContext = context;

    }

    @Override
    public int getCount() {
        return filteredArray_MenuItemKey_cutWeightdetails != null ? filteredArray_MenuItemKey_cutWeightdetails.size() : 0;

    }

    @Override
    public Object getItem(int position) {
        if (filteredArray_MenuItemKey_cutWeightdetails.isEmpty()) {
            return null;
        } else {
            return filteredArray_MenuItemKey_cutWeightdetails.get(position);
        }    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.filteredslotwiseapporderlist, (ViewGroup) convertView, false);

        final TextView itemname_textWidget = listViewItem.findViewById(R.id.itemname);
        final TextView ordercount_textWidget = listViewItem.findViewById(R.id.ordercount);
        final TextView itemquantity_textWidget = listViewItem.findViewById(R.id.itemquantity);
        final TextView cutnameTextWidget_textWidget = listViewItem.findViewById(R.id.cutnameTextWidget);

        tokenNo = new ArrayList<>();
        recycler = listViewItem.findViewById(R.id.recyclerView);
        recycler.setHasFixedSize(true);


        try{
            Itemname = filteredArray_MenuItemKey_cutWeightdetails.get(position).getItemName();
            Tokens = filteredArray_MenuItemKey_cutWeightdetails.get(position).getTokenno();
            Quantity =  filteredArray_MenuItemKey_cutWeightdetails.get(position).getQuantity();
            cutname =  filteredArray_MenuItemKey_cutWeightdetails.get(position).getCutname();
            finalWeight =  filteredArray_MenuItemKey_cutWeightdetails.get(position).getItemFinalWeight();
        }
        catch (Exception e){

            e.printStackTrace();
        }


        manager = new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(manager);
        try{
            String[] elements = Tokens.split(",");
            List<String> tokennolist = Arrays.asList(elements);

            tokenNo = new ArrayList<>(tokennolist);

            String separator ="-";

            Collections.sort(tokenNo, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    int posinObj1 = o1.indexOf(separator);
                    String obj1 = o1.substring(posinObj1 + separator.length());
                    // System.out.println("Substring after separator ob1= "+obj1.substring(posinObj1 + separator.length()));

                    int posinObj2 = o2.indexOf(separator);
                    String obj2 = o2.substring(posinObj2 + separator.length());

                    //   System.out.println("Substring after separator ob2 = "+obj2.substring(posinObj2 + separator.length()));


                    int a = Integer.parseInt(obj1.trim().toString());
                    int b = Integer.parseInt(obj2.trim().toString());
                    return Integer.compare(a, b);
                }
            });


            try{
                itemname_textWidget .setText(new StringBuilder().append(" ").append(finalWeight).toString());
                itemquantity_textWidget .setText(MessageFormat.format("{0} Pack", Quantity));
            }
            catch (Exception e){
                e.printStackTrace();
            }


            try{
                cutnameTextWidget_textWidget.setText(cutname);
            }
            catch (Exception e){
                e.printStackTrace();
            }




            try {
                ordercount_textWidget.setText(MessageFormat.format("{0} Token", String.valueOf(tokenNo.size())));
            }
            catch(Exception e){
                e.printStackTrace();
            }


            //   listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tokenNo);
            //   tokennoListview.setAdapter(listAdapter);
            adapter = new slotwiseitem_RecyclerviewAdapter(tokenNo,mContext);
            recycler.setAdapter(adapter);

        }
        catch (Exception e){
            e.printStackTrace();
        }


        return listViewItem;
    }
}
