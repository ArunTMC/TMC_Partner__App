package com.meatchop.tmcpartner.settings;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.settings.report_activity_model.ListData;
import com.meatchop.tmcpartner.settings.report_activity_model.ListItem;
import com.meatchop.tmcpartner.settings.report_activity_model.ListSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Adapter_SlotWiseAppOrders_List extends BaseAdapter {
    private static final int VIEW_TYPE_NONE = 0;
    private static final int VIEW_TYPE_SECTION = 1;
    private static final int VIEW_TYPE_ITEM = 2;
    private LayoutInflater layoutInflater;
    private List<ListData> dataList;
    int header_count=2;
    Context context;
    private boolean isFromSlotAppordersList;
    public  HashMap<String, Modal_ManageOrders_Pojo_Class> menuItemKey_CutWeightdetailsHashmap = new HashMap();
    public  List<String> menuItemKey_CutWeightdetails;

    public  HashMap<String, Modal_ManageOrders_Pojo_Class> filteredArray_menuItemKey_CutWeightdetailsHashmap = new HashMap();
    public  List<Modal_ManageOrders_Pojo_Class> filteredArray_menuItemKey_CutWeightdetails = new ArrayList<>();



    public Adapter_SlotWiseAppOrders_List(Context context, List<ListData> dataList,boolean isFromSlotAppordersList) {
        this.dataList = dataList;
        this.layoutInflater = LayoutInflater.from(context);
        this.isFromSlotAppordersList=isFromSlotAppordersList;
        this.context = context;
    }

    public Adapter_SlotWiseAppOrders_List() {

    }

    public Adapter_SlotWiseAppOrders_List(Context context, List<ListData> dataList, boolean isFromSlotAppordersList, HashMap<String, Modal_ManageOrders_Pojo_Class> MenuItemKey_cutWeightdetailsHashmap, List<String> MenuItemKey_CutWeightdetails) {
        this.menuItemKey_CutWeightdetails = MenuItemKey_CutWeightdetails;
        this.dataList = dataList;
        this.layoutInflater = LayoutInflater.from(context);
        this.isFromSlotAppordersList=isFromSlotAppordersList;
        this.context = context;
        this.menuItemKey_CutWeightdetailsHashmap = MenuItemKey_cutWeightdetailsHashmap;
    }

    @Override
    public int getCount() {
        return dataList != null ? dataList.size() : 0;
    }
    @Override
    public ListData getItem(int position) {
        if (dataList.isEmpty()) {
            return null;
        } else {
            return dataList.get(position);
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == VIEW_TYPE_SECTION) {
            return getSectionView(position, convertView, parent);
        } else if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            return getItemView(position, convertView, parent);
        }
        return null;
    }
    @NonNull
    private View getItemView(int position, View convertView, ViewGroup parent) {
     ItemViewHolder itemViewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.pos_sales_report_listitem, parent, false);
            itemViewHolder = new ItemViewHolder(convertView);
            convertView.setTag(itemViewHolder);
        } else {
            itemViewHolder = (ItemViewHolder) convertView.getTag();
        }
        ListItem listItem = (ListItem) getItem(position);
        itemViewHolder.setMessage(listItem.getMessage());
        itemViewHolder.setMessageLine2(listItem.getMessageLine2());
       /* if((listItem.getCutname().equals(null)) || (listItem.getCutname().equals("null"))  || (listItem.getCutname().equals("")) ) {
            itemViewHolder.setCutname("");
        }
        else {
            itemViewHolder.setCutname(listItem.getCutname());
        }

        */
        //  posSalesReport.setHeightforListview();
        if(isFromSlotAppordersList) {
            itemViewHolder.viewtokens.setVisibility(View.VISIBLE);

            itemViewHolder.viewtokens.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        filteredArray_menuItemKey_CutWeightdetails.clear();
                        String filteredArray_menuItemKey_CutWeightdetailsString ="";
                        String menuItemkey = listItem.getMenuitemkey();
                        for(int i =0 ; i<menuItemKey_CutWeightdetails.size();i++){
                            String menuItemKeyforhashmap = menuItemKey_CutWeightdetails.get(i);
                            if(menuItemkey.equals(menuItemKey_CutWeightdetailsHashmap.get(menuItemKeyforhashmap).getMenuItemKey().toString())){
                                filteredArray_menuItemKey_CutWeightdetails.add(menuItemKey_CutWeightdetailsHashmap.get(menuItemKeyforhashmap));
                            }

                            try{
                                if(menuItemKey_CutWeightdetails.size()-i == 1){

                                    try{
                                        Gson gson = new Gson();
                                        filteredArray_menuItemKey_CutWeightdetailsString = gson.toJson(filteredArray_menuItemKey_CutWeightdetails);

                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }


                                    try {
                                        // Toast.makeText(context, "Token no : " + listItem.getTokens(), Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(context, TokenNoShowingActivity.class);
                                        intent.putExtra("tokenNo", listItem.getTokens());
                                        intent.putExtra("itemname", listItem.getMessage());
                                        intent.putExtra("menuItemArray",filteredArray_menuItemKey_CutWeightdetailsString);

                                        intent.putExtra("quantity", listItem.getMessageLine2());
                                        context.startActivity(intent);
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }




                }
            });
        }
        else{
            itemViewHolder.viewtokens.setVisibility(View.GONE);
        }
        return convertView;
    }
    @NonNull
    private View getSectionView(int position, View convertView, ViewGroup parent) {
        SectionViewHolder sectionViewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.pos_sales_report_listitem_header, parent, false);
            sectionViewHolder = new SectionViewHolder(convertView);
            convertView.setTag(sectionViewHolder);
        } else {
            sectionViewHolder = (SectionViewHolder) convertView.getTag();
        }
        if(header_count!=position) {
            sectionViewHolder.setTitle(((ListSection) getItem(position)).getTitle());
            sectionViewHolder.setTvtotalAmount(((ListSection) getItem(position)).getTotalAmount());

            header_count = position + 1;
        }
        return convertView;

    }
    @Override
    public int getItemViewType(int position) {
        if (getCount() > 0) {
            ListData listData = getItem(position);

            if (listData instanceof ListSection) {
                return VIEW_TYPE_SECTION;
            } else if (listData instanceof ListItem) {
                return VIEW_TYPE_ITEM;
            } else {
                return VIEW_TYPE_NONE;
            }
        } else {
            return VIEW_TYPE_NONE;
        }
    }
    @Override
    public int getViewTypeCount() {
        return 3;
    }
    class SectionViewHolder {
        TextView tvTitle,tvtotalAmount;
        public SectionViewHolder(View itemView) {
            tvTitle = (TextView) itemView.findViewById(R.id.text_view_title);
            tvtotalAmount = (TextView) itemView.findViewById(R.id.totalAmount);

        }
        public void setTitle(String title) {
            tvTitle.setGravity(Gravity.TOP);

            tvTitle.setText(title);
        }
        public void setTvtotalAmount(String amount) {
            tvtotalAmount.setText(amount);
        }


    }
    class ItemViewHolder {
        TextView tvMessage, tvMessageLine2,cutName;
        LinearLayout viewtokens,cutnameLayout;
        public ItemViewHolder(View itemView) {
            tvMessage = (TextView) itemView.findViewById(R.id.name_and_quantity);
            tvMessageLine2 = (TextView) itemView.findViewById(R.id.price);
            cutName = (TextView) itemView.findViewById(R.id.cutName);

            viewtokens = (LinearLayout) itemView.findViewById(R.id.viewtokens);
            cutnameLayout = (LinearLayout) itemView.findViewById(R.id.cutnameLayout);

            cutnameLayout.setVisibility(View.GONE);
            viewtokens.setVisibility(View.GONE);
            if(isFromSlotAppordersList){
                viewtokens.setVisibility(View.VISIBLE);
                cutnameLayout.setVisibility(View.VISIBLE);

                tvMessageLine2.setTextColor(context.getResources().getColor(R.color.TMC_Orange));
            }
            else{
                viewtokens.setVisibility(View.GONE);
            }
        }
        public void setCutname(String message) {
            try {
                if(message.equals("")){
                    cutnameLayout.setVisibility(View.GONE);

                }
                else {
                    cutnameLayout.setVisibility(View.VISIBLE);

                    cutName.setText(message);
                }

            }
            catch (Exception e){
                cutnameLayout.setVisibility(View.GONE);

                e.printStackTrace();
            }
        }

        public void setMessage(String message) {
            tvMessage.setText(message);
        }
        public void setMessageLine2(String messageLine2) {
            tvMessageLine2.setText(messageLine2);
        }
    }
}
