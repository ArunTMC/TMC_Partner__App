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

import com.meatchop.tmcpartner.settings.report_activity_model.ListData;
import com.meatchop.tmcpartner.settings.report_activity_model.ListItem;
import com.meatchop.tmcpartner.settings.report_activity_model.ListSection;
import com.meatchop.tmcpartner.R;

import java.util.List;

public class Adapter_Pos_Sales_Report extends BaseAdapter {
    private static final int VIEW_TYPE_NONE = 0;
    private static final int VIEW_TYPE_SECTION = 1;
    private static final int VIEW_TYPE_ITEM = 2;
    private LayoutInflater layoutInflater;
    private List<ListData> dataList;
    int header_count=2;
    Context context;
    private boolean isFromSlotAppordersList;
    public Adapter_Pos_Sales_Report(Context context, List<ListData> dataList,boolean isFromSlotAppordersList) {
        this.dataList = dataList;
        this.layoutInflater = LayoutInflater.from(context);
        this.isFromSlotAppordersList=isFromSlotAppordersList;
        this.context = context;
    }

    public Adapter_Pos_Sales_Report() {

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

                   // Toast.makeText(context, "Token no : " + listItem.getTokens(), Toast.LENGTH_LONG).show();
                    Intent i = new Intent(context,TokenNoShowingActivity.class);
                    i.putExtra("tokenNo",listItem.getTokens());
                    i.putExtra("itemname",listItem.getMessage());
               //     i.putExtra("cutname",listItem.getCutname());

                    i.putExtra("quantity",listItem.getMessageLine2());
                    context.startActivity(i);
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




/*extends ArrayAdapter<Modal_OrderDetails> {
        Context mContext;
    List<String> subCtgyKey;
    List<Modal_OrderDetails> sortedOrders = new ArrayList<>();
    List<Modal_OrderDetails> SubCtgyKey_List;

        List<String> ordersList;
        HashMap<String, Modal_OrderDetails> OrderItem_hashmap;
    HashMap<String, HashMap<String, Modal_OrderDetails>>tmcSubCtgywise_sorted_hashmap;
public Adapter_Pos_Sales_Report(Context context, List<String> order_item_list, HashMap<String, Modal_OrderDetails> orderItem_hashmap, List<String> tmcSubCtgykey, List<Modal_OrderDetails> subCtgyKey_List) {
        super(context, R.layout.pos_sales_report_listitem);

        this.mContext = context;
        this.ordersList = order_item_list;
        this.subCtgyKey = tmcSubCtgykey;
        this.OrderItem_hashmap = orderItem_hashmap;
        this.SubCtgyKey_List = subCtgyKey_List;
        }

@NonNull
@Override
public View getView(int position, @Nullable View convertView, @NonNull ViewGroup view) {
@SuppressLint("ViewHolder") final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.pos_sales_report_listitem, (ViewGroup) view, false);
final TextView name_and_quantity_widget = listViewItem.findViewById(R.id.name_and_quantity);
final TextView price_widget = listViewItem.findViewById(R.id.price);
   // final TextView SubCtgyName = listViewItem.findViewById(R.id.SubCtgyName);
 //   final LinearLayout SubCtgyLinearLayout = listViewItem.findViewById(R.id.SubCtgyLinearLayout);
 //   final ListView listview_subCtgywise = listViewItem.findViewById(R.id.listview_subCtgywise);

      //  Modal_OrderDetails  modal_orderDetails = SubCtgyKey_List.get(position);
       /* String SubCtgyKey = subCtgyDetails.getTmcsubctgykey();
        String SubCtgyNameString = subCtgyDetails.getTmcsubctgyname();
        if(tmcSubCtgywise_sorted_hashmap.containsKey(SubCtgyKey)){
            sortedOrders = tmcSubCtgywise_sorted_hashmap.get(SubCtgyKey);
                Adapter_Pos_sales_report_subCtgy_ListItem adapter_pos_sales_report_subCtgy_listItem = new Adapter_Pos_sales_report_subCtgy_ListItem(mContext, sortedOrders);
                listview_subCtgywise.setAdapter(adapter_pos_sales_report_subCtgy_listItem);

        }
    SubCtgyName.setText(SubCtgyNameString);



    String menuItemid =ordersList.get(position);
    Modal_OrderDetails  modal_orderDetails =  OrderItem_hashmap.get(menuItemid);
    double weightinGrams;

    try {
         weightinGrams = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getWeightingrams());

    }
    catch (Exception e){
        weightinGrams = 1;
    }

    double kilogram = weightinGrams * 0.001;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");
    String KilogramString = String.valueOf(decimalFormat.format(kilogram));

    String name_quantity = modal_orderDetails.getItemname() + " (" + modal_orderDetails.getQuantity() + ") "+String.valueOf(KilogramString)+" Kg" ;


    name_and_quantity_widget.setText(String.valueOf(name_quantity));
    price_widget.setText(String.valueOf(modal_orderDetails.getTmcprice()));






        return listViewItem;
        }

@Override
public int getCount() {
        return OrderItem_hashmap.size();
        }

@Nullable
@Override
public Modal_OrderDetails getItem(int position) {
        return super.getItem(position);
        }

@Override
public int getPosition(@Nullable Modal_OrderDetails item) {
        return super.getPosition(item);
        }
        }
        */