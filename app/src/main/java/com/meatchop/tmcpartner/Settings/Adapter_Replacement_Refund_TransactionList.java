package com.meatchop.tmcpartner.Settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.Replacement_RefundClasses.ReplacementRefundListFragment;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.Settings.Add_Replacement_Refund_Order.Modal_ReplacementOrderDetails;
import com.meatchop.tmcpartner.Settings.Add_Replacement_Refund_Order.Modal_ReplacementTransactionDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.meatchop.tmcpartner.R.layout.replacement_refund_itemslist;

public class Adapter_Replacement_Refund_TransactionList extends ArrayAdapter<Modal_ReplacementTransactionDetails> {
    Context mContext;
    List<Modal_ReplacementTransactionDetails> transactionDetailsListList;
    Replacement_Refund_Transaction_Report replacement_refund_transaction_report;

    public Adapter_Replacement_Refund_TransactionList(Context mContext, List<Modal_ReplacementTransactionDetails> transactionDetailsListListt, Replacement_Refund_Transaction_Report replacement_refund_transaction_reportt) {
        super(mContext, R.layout.replacement_transaction_listview_item, transactionDetailsListListt);
        this.mContext = mContext;
        this.transactionDetailsListList = transactionDetailsListListt;
        this.replacement_refund_transaction_report = replacement_refund_transaction_reportt;
    }


    @Nullable
    @Override
    public Modal_ReplacementTransactionDetails getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Modal_ReplacementTransactionDetails item) {
        return super.getPosition(item);
    }

    public View getView(final int pos, View view, ViewGroup v) {
        @SuppressLint("ViewHolder") final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.replacement_transaction_listview_item, (ViewGroup) view, false);
        String transactionType = "";
        final TextView transactiontype_text_widget = listViewItem.findViewById(R.id.transactiontype_text_widget);
        final TextView customermobileno_text_widget = listViewItem.findViewById(R.id.customermobileno_text_widget);
        final TextView orderid_text_widget = listViewItem.findViewById(R.id.orderid_text_widget);
        final TextView transactiondate_text_widget = listViewItem.findViewById(R.id.transactiondate_text_widget);
        final TextView status_text_widget = listViewItem.findViewById(R.id.status_text_widget);
        final TextView refundAmount_text_widget = listViewItem.findViewById(R.id.refundAmount_text_widget);
        final TextView totalreplacementamount_text_widget = listViewItem.findViewById(R.id.totalreplacementamount_text_widget);
        final TextView replacementorderid_text_widget = listViewItem.findViewById(R.id.replacementorderid_text_widget);
        final TextView markedItemDesp_text_widget = listViewItem.findViewById(R.id.markedItemDesp_text_widget);
        final TextView totalreplacementdiscountamount_text_widget = listViewItem.findViewById(R.id.totalreplacementdiscountamount_text_widget);
        final TextView reasonForMarked_text_widget = listViewItem.findViewById(R.id.reasonForMarked_text_widget);

        final LinearLayout refundAmount_Layout = listViewItem.findViewById(R.id.refundAmount_Layout);
        final LinearLayout reaasonForMarkedLayout = listViewItem.findViewById(R.id.reaasonForMarkedLayout);
        final LinearLayout replacementdiscountAmountLayout = listViewItem.findViewById(R.id.replacementdiscountAmountLayout);

        final LinearLayout replacementAmountLayout = listViewItem.findViewById(R.id.replacementAmountLayout);
        final LinearLayout replacementOrderidLayout = listViewItem.findViewById(R.id.replacementOrderidLayout);

        Modal_ReplacementTransactionDetails modal_replacementTransactionDetails = transactionDetailsListList.get(pos);

        try {

            JSONArray array = new JSONArray(modal_replacementTransactionDetails.getMarkeditemdesp_String());

            //Log.i("tag","array.length()"+ array.length());
            String itemDesp = "";
            String subCtgyKey = "";

            for (int i = 0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);

                //Log.i("tag", "array.lengrh(i" + json.length());
                try {
                    if (json.has("tmcsubctgykey")) {
                        subCtgyKey = String.valueOf(json.get("tmcsubctgykey"));
                    } else {
                        subCtgyKey = " ";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                String cutname = "";

                try {
                    if (json.has("cutname")) {
                        cutname = String.valueOf(json.get("cutname"));
                    } else {
                        cutname = "";
                    }
                } catch (Exception e) {
                    cutname = "";
                    e.printStackTrace();
                }

                try {
                    if ((cutname.length() > 0) && (!cutname.equals(null)) && (!cutname.equals("null"))) {
                        cutname = " [ " + cutname + " ] ";
                    } else {
                        //cutname="";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                String itemName = String.valueOf(json.get("itemname"));
                String price = String.valueOf(json.get("tmcprice"));
                String quantity = String.valueOf(json.get("quantity"));
                if (itemDesp.length() > 0) {
                    if (subCtgyKey.equals("tmcsubctgy_16")) {
                        itemDesp = String.format("%s ,\n%s %s * %s", itemDesp, "Grill House " + itemName, cutname, quantity);

                    } else if (subCtgyKey.equals("tmcsubctgy_15")) {
                        itemDesp = String.format("%s ,\n%s  %s * %s", itemDesp, "Ready to Cook  " + itemName, cutname, quantity);

                    } else {
                        itemDesp = String.format("%s ,\n%s  %s * %s", itemDesp, itemName, cutname, quantity);

                    }
                } else {
                    if (subCtgyKey.equals("tmcsubctgy_16")) {
                        itemDesp = String.format("%s  %s * %s", "Grill House " + itemName, cutname, quantity);

                    } else if (subCtgyKey.equals("tmcsubctgy_15")) {
                        itemDesp = String.format("%s  %s * %s", "Ready to Cook  " + itemName, cutname, quantity);

                    } else {
                        itemDesp = String.format("%s  %s * %s", itemName, cutname, quantity);

                    }

                }
                    /*
                    if (itemDesp.length()>0) {

                        itemDesp = String.format("%s ,\n%s * %s", itemDesp, itemName, quantity);
                    } else {
                        itemDesp = String.format("%s * %s", itemName, quantity);

                    }

                     */


                //        orderDetails_text_widget.setText(String.format(itemDesp));
                //Log.i("tag", "array.lengrh(i" + json.length());


            }
            markedItemDesp_text_widget.setText(String.format(itemDesp));

        } catch (JSONException e) {
            e.printStackTrace();

        }

        try {
            transactiontype_text_widget.setText(modal_replacementTransactionDetails.getTransactiontype().toString());
            transactionType = modal_replacementTransactionDetails.getTransactiontype().toString();
        } catch (Exception e) {
            transactiontype_text_widget.setText("");
            transactionType = "";
            e.printStackTrace();
        }


        if (transactionType.toString().toUpperCase().equals("REPLACEMENT")) {
            refundAmount_Layout.setVisibility(View.GONE);
            replacementAmountLayout.setVisibility(View.VISIBLE);
            replacementOrderidLayout.setVisibility(View.VISIBLE);
            markedItemDesp_text_widget.setVisibility(View.GONE);
            replacementdiscountAmountLayout.setVisibility(View.VISIBLE);
            reaasonForMarkedLayout .setVisibility(View.GONE);
        } else if (transactionType.toString().toUpperCase().equals("REFUND")) {
            replacementAmountLayout.setVisibility(View.GONE);
            replacementOrderidLayout.setVisibility(View.GONE);
            replacementdiscountAmountLayout.setVisibility(View.GONE);
            reaasonForMarkedLayout .setVisibility(View.GONE);

            refundAmount_Layout.setVisibility(View.VISIBLE);
            markedItemDesp_text_widget.setVisibility(View.GONE);
        } else if (transactionType.toString().toUpperCase().equals("MARKED")) {
            replacementOrderidLayout.setVisibility(View.GONE);
            replacementAmountLayout.setVisibility(View.GONE);
            replacementdiscountAmountLayout.setVisibility(View.GONE);
            reaasonForMarkedLayout .setVisibility(View.VISIBLE);

            refundAmount_Layout.setVisibility(View.GONE);
            markedItemDesp_text_widget.setVisibility(View.VISIBLE);
        }


        try {
            customermobileno_text_widget.setText(modal_replacementTransactionDetails.getMobileno().toString());
        } catch (Exception e) {
            customermobileno_text_widget.setText("");
            e.printStackTrace();
        }

        try {
            orderid_text_widget.setText(modal_replacementTransactionDetails.getOrderid().toString());
        } catch (Exception e) {
            orderid_text_widget.setText("");
            e.printStackTrace();
        }

        try {
            String transactiontime = modal_replacementTransactionDetails.getTransactiontime().toString();
            String oldFormat = getTimeinOLDFormat(transactiontime);
            transactiondate_text_widget.setText(oldFormat);
        } catch (Exception e) {
            transactiondate_text_widget.setText("");
            e.printStackTrace();
        }

        try {
            status_text_widget.setText(modal_replacementTransactionDetails.getTransactionstatus().toString());
        } catch (Exception e) {
            status_text_widget.setText("");
            e.printStackTrace();
        }

        try {
            refundAmount_text_widget.setText(modal_replacementTransactionDetails.getRefundamount().toString()+" Rs");
        } catch (Exception e) {
            refundAmount_text_widget.setText("");
            e.printStackTrace();
        }


        try {
            totalreplacementamount_text_widget.setText(modal_replacementTransactionDetails.getReplacementorderamount().toString()+" Rs");
        } catch (Exception e) {
            totalreplacementamount_text_widget.setText("");
            e.printStackTrace();
        }


        try {
            replacementorderid_text_widget.setText(modal_replacementTransactionDetails.getReplacementorderid().toString());
        } catch (Exception e) {
            refundAmount_text_widget.setText("");
            e.printStackTrace();
        }

        try {
            totalreplacementdiscountamount_text_widget.setText(modal_replacementTransactionDetails.getDiscountamount().toString()+" Rs");
        } catch (Exception e) {
            totalreplacementdiscountamount_text_widget.setText("0"+" Rs");
            e.printStackTrace();
        }
        try {
            reasonForMarked_text_widget.setText(modal_replacementTransactionDetails.getReasonformarked().toString());
        } catch (Exception e) {
            reasonForMarked_text_widget.setText("");
            e.printStackTrace();
        }

        return listViewItem;

    }

    private String getTimeinOLDFormat(String transactiontime) {
        String CurrentDate1 = "";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = sdf.parse(transactiontime);

                SimpleDateFormat day = new SimpleDateFormat("EEE, d MMM yyyy");
                CurrentDate1 = day.format(date);


            } catch (ParseException e) {
                e.printStackTrace();
            }


        } catch (Exception e) {

            try {
                SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
                try {
                    Date date = sdff.parse(transactiontime);

                    SimpleDateFormat day = new SimpleDateFormat("EEE, d MMM yyyy");
                    CurrentDate1 = day.format(date);

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } catch (Exception e2) {

                try {
                    SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                    try {
                        Date date = sdff.parse(transactiontime);

                        SimpleDateFormat day = new SimpleDateFormat("EEE, d MMM yyyy");
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

