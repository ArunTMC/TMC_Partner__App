package com.meatchop.tmcpartner.mobilescreen_javaclasses.manage_orders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.meatchop.tmcpartner.AlertDialogClass;
import com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders.Adapter_Pos_ManageOrders_ListView;
import com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders.Pos_ManageOrderFragment;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.settings.Adapter_Mobile_SearchOrders_usingMobileNumber_ListView;
import com.meatchop.tmcpartner.settings.Adapter_Pos_SearchOrders_usingMobileNumber;
import com.meatchop.tmcpartner.settings.searchOrdersUsingMobileNumber;

import java.util.List;
import java.util.Objects;

public class Adapter_Mobile_changeWeight_in_itemDesp extends ArrayAdapter<Modal_ManageOrders_Pojo_Class> {
    Context mContext;
    List<Modal_ManageOrders_Pojo_Class> ordersList;
    String calledFrom ="";

    searchOrdersUsingMobileNumber searchOrdersUsingMobileNumber;
    Pos_ManageOrderFragment pos_manageOrderFragment ;
    Mobile_ManageOrders1 mobile_manageOrders1;

    String grossWeightinGramsString ="" , startingRangeGrossWeight = "" , endingRangeGrossWeight = "" , newgrossWeightinGramsString ="",quantity_String ="",cutName_String=""  ,itemFinalWeight_String = "";
    double newgrossWeight_double = 0,grossWeight_double = 0 , double_startingRangeGrossWeight =0 , double_endingRangeGrossWeight =0,quantity_double=0, itemfinalWeight_double =0;
    boolean isFromPOS = false;


    public Adapter_Mobile_changeWeight_in_itemDesp(Context mContext, List<Modal_ManageOrders_Pojo_Class> orderdItems_desp, String calledFrom,Mobile_ManageOrders1 mobile_manageOrders1) {
        super(mContext, R.layout.change_weight_for_ordereditems_bottomsheet, orderdItems_desp);
        this.calledFrom = "ManageOrders";
        this.mContext=mContext;
        this.mobile_manageOrders1= mobile_manageOrders1;
        this.ordersList=orderdItems_desp;
    }

    public Adapter_Mobile_changeWeight_in_itemDesp(Context mContext, List<Modal_ManageOrders_Pojo_Class> orderdItems_desp, String calledFrom, searchOrdersUsingMobileNumber searchOrdersUsingMobileNumberr) {
        super(mContext, R.layout.change_weight_for_ordereditems_bottomsheet, orderdItems_desp);
        this.calledFrom = "AppOrdersList";
        this.mContext=mContext;
        this.searchOrdersUsingMobileNumber= searchOrdersUsingMobileNumberr;
        this.ordersList=orderdItems_desp;
    }

    public Adapter_Mobile_changeWeight_in_itemDesp(Context mContext, List<Modal_ManageOrders_Pojo_Class> orderdItems_desp, String appOrdersList, searchOrdersUsingMobileNumber searchOrdersUsingMobileNumberr, boolean isFromPOS) {
        super(mContext, R.layout.change_weight_for_ordereditems_bottomsheet, orderdItems_desp);
        this.calledFrom = "AppOrdersList";
        this.mContext=mContext;
        this.searchOrdersUsingMobileNumber= searchOrdersUsingMobileNumberr;
        this.ordersList=orderdItems_desp;
        this.isFromPOS = isFromPOS;

    }

    public Adapter_Mobile_changeWeight_in_itemDesp(Context mContext, List<Modal_ManageOrders_Pojo_Class> orderdItems_desp, String appOrdersList, Pos_ManageOrderFragment pos_manageOrderFragmentt, boolean isFromPOS) {
        super(mContext, R.layout.change_weight_for_ordereditems_bottomsheet, orderdItems_desp);
        this.calledFrom = "ManageOrders";
        this.mContext=mContext;
        this.pos_manageOrderFragment= pos_manageOrderFragmentt;
        this.ordersList=orderdItems_desp;
        this.isFromPOS = isFromPOS;

    }


    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public Modal_ManageOrders_Pojo_Class getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Modal_ManageOrders_Pojo_Class item) {
        return super.getPosition(item);
    }

    public View getView(final int pos, View view, ViewGroup v) {
        @SuppressLint("ViewHolder") final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.change_weight_for_ordereditems_bottomsheet, (ViewGroup) view, false);

        LinearLayout listviewchilditemLayout = listViewItem.findViewById(R.id.listviewchilditemLayout);
        LinearLayout listviewParentLayout = listViewItem.findViewById(R.id.listviewParentLayout);
        LinearLayout loadingpanelmask = listViewItem.findViewById(R.id.loadingpanelmaskt);
        LinearLayout loadingPanel = listViewItem.findViewById(R.id.loadingPanel);


        listviewParentLayout.setVisibility(View.GONE);
        listviewchilditemLayout.setVisibility(View.VISIBLE);
        loadingPanel.setVisibility(View.GONE);
        loadingpanelmask.setVisibility(View.GONE);

        final TextView itemName_widget = listViewItem.findViewById(R.id.itemName_textWidget);
        final TextView netweightRange_textWidget = listViewItem.findViewById(R.id.netweightRange_textWidget);
        final TextView currentGrossweight_textWidget = listViewItem.findViewById(R.id.currentGrossweight_textWidget);
        final EditText newGrossWeight_edittextWidget = listViewItem.findViewById(R.id.newGrossWeight_edittextWidget);
        final Button saveWeightDetails_button = listViewItem.findViewById(R.id.saveWeightDetails_button);
        final CheckBox correctWeightCheckbox = listViewItem.findViewById(R.id.correctWeightCheckbox);
        final LinearLayout correctWeightCheckboxLayout = listViewItem.findViewById(R.id.correctWeightCheckboxLayout);
        final TextView itemCountWidget = listViewItem.findViewById(R.id.itemCount_textWidget);

        final LinearLayout tmcPrice_invenDetailsTextLayout = listViewItem.findViewById(R.id.tmcPrice_invenDetailsTextLayout);
        final TextView mainMenuItem_textWidget = listViewItem.findViewById(R.id.mainMenuItem_textWidget);
        final TextView grossweight_textWidget = listViewItem.findViewById(R.id.grossweight_textWidget);

        final TextView maskingtextLayout = listViewItem.findViewById(R.id.maskingtextLayout);
        final TextView quantity_textWidget = listViewItem.findViewById(R.id.quantity_textWidget);
        final TextView cutName_textWidget = listViewItem.findViewById(R.id.cutName_textWidget);
        final LinearLayout cutnamae_layout = listViewItem.findViewById(R.id.cutnamae_layout);

        final LinearLayout layoutmask = listViewItem.findViewById(R.id.layoutmask);

        grossWeightinGramsString ="" ; startingRangeGrossWeight = "" ; endingRangeGrossWeight = "" ; newgrossWeightinGramsString ="" ;quantity_String ="";cutName_String="" ;
         newgrossWeight_double = 0 ;grossWeight_double = 0 ; double_startingRangeGrossWeight =0 ; double_endingRangeGrossWeight =0; quantity_double=0;

        correctWeightCheckbox.setFocusable(false);
        correctWeightCheckbox.setClickable(false);
        Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = ordersList.get(pos);
        itemName_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getItemName()));
     //   netweightRange_textWidget.setText(String.valueOf(modal_manageOrders_pojo_class.getNetweight()));
        try{
            if(modal_manageOrders_pojo_class.getPricetypeforpos().toString().toLowerCase().equals("tmcpriceperkg") ) {
                if (modal_manageOrders_pojo_class.getItemFinalWeight().toString().contains("null") || modal_manageOrders_pojo_class.getItemFinalWeight().toString().equals("") || modal_manageOrders_pojo_class.getItemFinalWeight().toString().equals("0") || modal_manageOrders_pojo_class.getItemFinalWeight().toString().equals(null)) {
                  //  newGrossWeight_edittextWidget.setText(String.valueOf("1"));
                    currentGrossweight_textWidget .setText(String.valueOf("1"));
                    newGrossWeight_edittextWidget.setText(String.valueOf("-"));
                    grossweight_textWidget.setText(String.valueOf("-"));
                    currentGrossweight_textWidget.setText("-");


                    maskingtextLayout.setVisibility(View.VISIBLE);
                    newGrossWeight_edittextWidget.setVisibility(View.GONE);

                } else {

                    //newGrossWeight_edittextWidget.setText(String.valueOf(modal_manageOrders_pojo_class.getItemFinalWeight()));
                  //  currentGrossweight_textWidget.setText(String.valueOf(modal_manageOrders_pojo_caass.getItemFinalWeight()+"g"));
                  //  currentGrossweight_textWidget.setText(String.valueOf(modal_manageOrders_pojo_class.getItemFinalWeight()+"g"));


                    maskingtextLayout.setVisibility(View.GONE);
                    newGrossWeight_edittextWidget.setVisibility(View.VISIBLE);
                }

                try{
                    itemFinalWeight_String = String.valueOf(modal_manageOrders_pojo_class.getItemFinalWeight());
                }
                catch (Exception e){
                    e.printStackTrace();
                }


                try{
                    itemFinalWeight_String = itemFinalWeight_String.replaceAll("[^\\d.]", "");
                }
                catch (Exception e){
                    e.printStackTrace();
                }


                try{
                    itemfinalWeight_double= Double.parseDouble(itemFinalWeight_String);
                }
                catch (Exception e){
                    itemfinalWeight_double =1;
                    e.printStackTrace();
                }



                try{
                    newGrossWeight_edittextWidget.setText(String.valueOf(Integer.valueOf((int) itemfinalWeight_double)));


                }
                catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    currentGrossweight_textWidget.setText(String.valueOf(Integer.valueOf((int) itemfinalWeight_double))+"g");


                }
                catch (Exception e){
                    e.printStackTrace();
                }


            }
            else{
                maskingtextLayout.setVisibility(View.VISIBLE);

                newGrossWeight_edittextWidget.setText(String.valueOf("-"));
                grossweight_textWidget.setText(String.valueOf("-"));
                currentGrossweight_textWidget.setText("-");

                newGrossWeight_edittextWidget.setVisibility(View.GONE);

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
       /* if(ordersList.get(pos).getPricetypeforpos().toUpperCase().equals("TMCPRICE")) {
            maskingtextLayout.setVisibility(View.VISIBLE);
        }

        */
        try{
                if(modal_manageOrders_pojo_class.ischilditem){
                    tmcPrice_invenDetailsTextLayout.setVisibility(View.VISIBLE);
                    mainMenuItem_textWidget.setText(String.valueOf(modal_manageOrders_pojo_class.getParentItemName()));
                }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            cutName_String  = String.valueOf(modal_manageOrders_pojo_class.getCutname());

        }
        catch (Exception e){
            e.printStackTrace();
        }



        try{
            quantity_String = String.valueOf(modal_manageOrders_pojo_class.getQuantity());

        }
        catch (Exception e )
        {
            e.printStackTrace();
        }


        try{

            quantity_String = quantity_String.replaceAll("[^\\d.]", "");
        }
        catch (Exception e )
        {
            quantity_String ="1";
            e.printStackTrace();
        }

        try{
            quantity_textWidget.setText(String.valueOf(quantity_String));

        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{

            quantity_double = Double.parseDouble(quantity_String);
        }
        catch (Exception e )
        {
            quantity_double =1;
            e.printStackTrace();
        }



        try{
            if(cutName_String.equals("") || cutName_String.equals("null") ||cutName_String.equals(null) || cutName_String.equals("nil")){
                cutnamae_layout.setVisibility(View.GONE);

            }
            else{
                cutnamae_layout.setVisibility(View.VISIBLE);
                cutName_textWidget.setText(String.valueOf(cutName_String));

            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        itemCountWidget.setText(String.valueOf(pos+1));
        try{
            if(modal_manageOrders_pojo_class.isCorrectGrossweight()){
                correctWeightCheckbox.setChecked(true);
            }
            else{
                correctWeightCheckbox.setChecked(false);

            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            grossWeightinGramsString = String.valueOf(modal_manageOrders_pojo_class.getGrossweightingrams());
            if(grossWeightinGramsString.equals("") || grossWeightinGramsString.equals("null")){
                grossWeightinGramsString = String.valueOf(modal_manageOrders_pojo_class.getGrossweight());

            }
        }
        catch (Exception e )
        {
            e.printStackTrace();
        }


        try{

            grossWeightinGramsString = grossWeightinGramsString.replaceAll("[^\\d.]", "");
        }
        catch (Exception e )
        {
            grossWeightinGramsString ="1";
            e.printStackTrace();
        }


        try{

            grossWeight_double = Double.parseDouble(grossWeightinGramsString);
        }
        catch (Exception e )
        {
            grossWeight_double =1;
            e.printStackTrace();
        }




        try{
          //  currentGrossweight_textWidget.setText(String.valueOf(grossWeight_double)+"g");
            grossweight_textWidget.setText(String.valueOf(grossWeight_double+"g"));

        }
        catch (Exception e){
            e.printStackTrace();
        }



        try{
            quantity_textWidget.setText(String.valueOf(quantity_String));

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            grossWeight_double = grossWeight_double * quantity_double;
        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            double valueof15Percent = 0;
            valueof15Percent =  (grossWeight_double/100)*5;
            double_startingRangeGrossWeight = grossWeight_double - valueof15Percent;
            double_endingRangeGrossWeight = grossWeight_double + valueof15Percent;

        }
        catch (Exception e )
        {
            grossWeightinGramsString ="0";
            e.printStackTrace();
        }



        try{
            startingRangeGrossWeight = String.valueOf(double_startingRangeGrossWeight);
            endingRangeGrossWeight = String.valueOf(double_endingRangeGrossWeight);
            netweightRange_textWidget.setText(startingRangeGrossWeight + "g - "+endingRangeGrossWeight +"g ");

        }
        catch (Exception e )
        {
            grossWeightinGramsString ="0";
            e.printStackTrace();
        }


        maskingtextLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Can't Change weight Bcz its Unit Price Item ", Toast.LENGTH_SHORT).show();
            }
        });

        layoutmask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Can't Change weight Bcz its Unit Price Item ", Toast.LENGTH_SHORT).show();
            }
        });



        newGrossWeight_edittextWidget.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                correctWeightCheckbox.setChecked(false);
                modal_manageOrders_pojo_class.isCorrectGrossweight=false;
                modal_manageOrders_pojo_class.isGrossweightEdited=true;

            }
            });
        try {
            newGrossWeight_edittextWidget.setSelection(newGrossWeight_edittextWidget.getText().length());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        newGrossWeight_edittextWidget.setOnEditorActionListener(new TextView.OnEditorActionListener() {


            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {


                    correctWeightCheckboxLayout.performClick();



                }

                return false;
            }
        });


            correctWeightCheckboxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(correctWeightCheckbox.isChecked()){
                    correctWeightCheckbox.setChecked(false);
                    modal_manageOrders_pojo_class.isCorrectGrossweight=false;

                }
                else{
                    loadingPanel.setVisibility(View.VISIBLE);
                    loadingpanelmask.setVisibility(View.VISIBLE);

                    try{
                        hideKeyboard(newGrossWeight_edittextWidget);

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }


                    String rangeWeightFromTextView = netweightRange_textWidget.getText().toString();

                    try {

                        if (rangeWeightFromTextView.contains("to") || rangeWeightFromTextView.contains("-")) {


                            if (rangeWeightFromTextView.contains("to")) {
                                String[] split = rangeWeightFromTextView.split("to");
                                String firstSubString = split[0];
                                String secondSubString = split[1];
                                firstSubString = firstSubString.trim();
                                secondSubString = secondSubString.trim();
                                try{

                                    firstSubString = firstSubString.replaceAll("[^\\d.]", "");
                                    secondSubString = secondSubString.replaceAll("[^\\d.]", "");

                                }
                                catch (Exception e )
                                {
                                    e.printStackTrace();
                                }

                                try{

                                    double_startingRangeGrossWeight = Double.parseDouble(firstSubString);
                                    double_endingRangeGrossWeight = Double.parseDouble(secondSubString);

                                }
                                catch (Exception e )
                                {
                                    e.printStackTrace();
                                }


                            } else {
                                String[] split = rangeWeightFromTextView.split("-");
                                String firstSubString = split[0];
                                String secondSubString = split[1];
                                firstSubString = firstSubString.trim();
                                secondSubString = secondSubString.trim();
                                try{

                                    firstSubString = firstSubString.replaceAll("[^\\d.]", "");
                                    secondSubString = secondSubString.replaceAll("[^\\d.]", "");

                                }
                                catch (Exception e )
                                {
                                    e.printStackTrace();
                                }

                                try{

                                    double_startingRangeGrossWeight = Double.parseDouble(firstSubString);
                                    double_endingRangeGrossWeight = Double.parseDouble(secondSubString);

                                }
                                catch (Exception e )
                                {
                                    e.printStackTrace();
                                }

                            }


                        } else {
                            try{

                                rangeWeightFromTextView = rangeWeightFromTextView.replaceAll("[^\\d.]", "");

                            }
                            catch (Exception e )
                            {
                                e.printStackTrace();
                            }

                            try{

                                double_startingRangeGrossWeight = Double.parseDouble(rangeWeightFromTextView);
                                double_endingRangeGrossWeight = Double.parseDouble(rangeWeightFromTextView);

                            }
                            catch (Exception e )
                            {
                                e.printStackTrace();
                            }


                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                    newgrossWeightinGramsString =  newGrossWeight_edittextWidget.getText().toString();
                    try{

                        newgrossWeightinGramsString = newgrossWeightinGramsString.replaceAll("[^\\d.]", "");
                    }
                    catch (Exception e )
                    {
                        e.printStackTrace();
                    }


                    try{

                        newgrossWeightinGramsString = newgrossWeightinGramsString.replaceAll("[^\\d.]", "");
                    }
                    catch (Exception e )
                    {
                        e.printStackTrace();
                    }


                    try{

                        newgrossWeight_double = Double.parseDouble(newgrossWeightinGramsString);
                    }
                    catch (Exception e )
                    {
                        newgrossWeight_double =0;
                        e.printStackTrace();
                    }

                    if(newgrossWeight_double>=double_startingRangeGrossWeight && newgrossWeight_double<=double_endingRangeGrossWeight){
                        modal_manageOrders_pojo_class .setItemFinalWeight(newgrossWeightinGramsString);
                        if(calledFrom.equals("ManageOrders")){
                            if(isFromPOS){
                                Adapter_Pos_ManageOrders_ListView.OrderdItems_desp.get(pos).setItemFinalWeight(newgrossWeightinGramsString);

                            }
                            else{
                                Adapter_Mobile_ManageOrders_ListView1.OrderdItems_desp.get(pos).setItemFinalWeight(newgrossWeightinGramsString);

                            }

                        }
                        else   if(calledFrom.equals("AppOrdersList")){
                            if(isFromPOS){
                                Adapter_Pos_SearchOrders_usingMobileNumber.OrderdItems_desp.get(pos).setItemFinalWeight(newgrossWeightinGramsString);

                            }
                            else{
                                Adapter_Mobile_SearchOrders_usingMobileNumber_ListView.OrderdItems_desp.get(pos).setItemFinalWeight(newgrossWeightinGramsString);

                            }

                        }

                        currentGrossweight_textWidget.setText(newgrossWeightinGramsString+"g");
                        notifyDataSetChanged();

                        correctWeightCheckbox.setChecked(true);
                        modal_manageOrders_pojo_class.isCorrectGrossweight=true;

                    }
                    else{
                        if(calledFrom.equals("ManageOrders")){
                            if(isFromPOS){
                                AlertDialogClass.showDialog(pos_manageOrderFragment.getActivity(),R.string.grossweightChangingInstruction);

                            }
                            else{
                                AlertDialogClass.showDialog(mobile_manageOrders1.getActivity(),R.string.grossweightChangingInstruction);

                            }

                        }

                        else   if(calledFrom.equals("AppOrdersList")){
                            AlertDialogClass.showDialog(searchOrdersUsingMobileNumber,R.string.grossweightChangingInstruction);

                        }

                    }
                    loadingPanel.setVisibility(View.GONE);
                    loadingpanelmask.setVisibility(View.GONE);


                }
            }
        });


            saveWeightDetails_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPanel.setVisibility(View.VISIBLE);
                loadingpanelmask.setVisibility(View.VISIBLE);
                String rangeWeightFromTextView = netweightRange_textWidget.getText().toString();
                try {

                    if (rangeWeightFromTextView.contains("to") || rangeWeightFromTextView.contains("-")) {


                        if (rangeWeightFromTextView.contains("to")) {
                            String[] split = rangeWeightFromTextView.split("to");
                            String firstSubString = split[0];
                            String secondSubString = split[1];
                            firstSubString = firstSubString.trim();
                            secondSubString = secondSubString.trim();
                            try{

                                firstSubString = firstSubString.replaceAll("[^\\d.]", "");
                                secondSubString = secondSubString.replaceAll("[^\\d.]", "");

                            }
                            catch (Exception e )
                            {
                                e.printStackTrace();
                            }

                            try{

                                double_startingRangeGrossWeight = Double.parseDouble(firstSubString);
                                double_endingRangeGrossWeight = Double.parseDouble(secondSubString);

                            }
                            catch (Exception e )
                            {
                                e.printStackTrace();
                            }


                        } else {
                            String[] split = rangeWeightFromTextView.split("-");
                            String firstSubString = split[0];
                            String secondSubString = split[1];
                            firstSubString = firstSubString.trim();
                            secondSubString = secondSubString.trim();
                            try{

                                firstSubString = firstSubString.replaceAll("[^\\d.]", "");
                                secondSubString = secondSubString.replaceAll("[^\\d.]", "");

                            }
                            catch (Exception e )
                            {
                                e.printStackTrace();
                            }

                            try{

                                double_startingRangeGrossWeight = Double.parseDouble(firstSubString);
                                double_endingRangeGrossWeight = Double.parseDouble(secondSubString);

                            }
                            catch (Exception e )
                            {
                                e.printStackTrace();
                            }

                        }


                    } else {
                        try{

                            rangeWeightFromTextView = rangeWeightFromTextView.replaceAll("[^\\d.]", "");

                        }
                        catch (Exception e )
                        {
                            e.printStackTrace();
                        }

                        try{

                            double_startingRangeGrossWeight = Double.parseDouble(rangeWeightFromTextView);
                            double_endingRangeGrossWeight = Double.parseDouble(rangeWeightFromTextView);

                        }
                        catch (Exception e )
                        {
                            e.printStackTrace();
                        }


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }



                newgrossWeightinGramsString =  newGrossWeight_edittextWidget.getText().toString();
                try{

                    newgrossWeightinGramsString = newgrossWeightinGramsString.replaceAll("[^\\d.]", "");
                }
                catch (Exception e )
                {
                    e.printStackTrace();
                }


                try{

                    newgrossWeightinGramsString = newgrossWeightinGramsString.replaceAll("[^\\d.]", "");
                }
                catch (Exception e )
                {
                    e.printStackTrace();
                }


                try{

                    newgrossWeight_double = Double.parseDouble(newgrossWeightinGramsString);
                }
                catch (Exception e )
                {
                    newgrossWeight_double =0;
                    e.printStackTrace();
                }

                if(newgrossWeight_double>=double_startingRangeGrossWeight && newgrossWeight_double<=double_endingRangeGrossWeight){
                    modal_manageOrders_pojo_class .setItemFinalWeight(newgrossWeightinGramsString);

                    if(calledFrom.equals("ManageOrders")){
                        if(isFromPOS){
                            Adapter_Pos_ManageOrders_ListView.OrderdItems_desp.get(pos).setItemFinalWeight(newgrossWeightinGramsString);

                        }
                        else{
                            Adapter_Mobile_ManageOrders_ListView1.OrderdItems_desp.get(pos).setItemFinalWeight(newgrossWeightinGramsString);

                        }

                    }

                    else   if(calledFrom.equals("AppOrdersList")){

                        if(isFromPOS){
                            Adapter_Pos_SearchOrders_usingMobileNumber.OrderdItems_desp.get(pos).setItemFinalWeight(newgrossWeightinGramsString);

                        }
                        else{
                            Adapter_Mobile_SearchOrders_usingMobileNumber_ListView.OrderdItems_desp.get(pos).setItemFinalWeight(newgrossWeightinGramsString);

                        }



                    }

                    currentGrossweight_textWidget.setText(newgrossWeightinGramsString+"g");
                    notifyDataSetChanged();

                }
                else{

                    if(calledFrom.equals("ManageOrders")){
                        if(isFromPOS){
                            AlertDialogClass.showDialog(pos_manageOrderFragment.getActivity(),R.string.grossweightChangingInstruction);

                        }
                        else{
                            AlertDialogClass.showDialog(mobile_manageOrders1.getActivity(),R.string.grossweightChangingInstruction);

                        }

                    }
                    else   if(calledFrom.equals("AppOrdersList")){
                        AlertDialogClass.showDialog(searchOrdersUsingMobileNumber,R.string.grossweightChangingInstruction);

                    }

                }
                loadingPanel.setVisibility(View.GONE);
                loadingpanelmask.setVisibility(View.GONE);



            }
        });



        return  listViewItem ;
    }

    private void openKeyboard(EditText editText) {

        final Handler handler = new Handler();
        editText.requestFocus();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                InputMethodManager mgr = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                editText.setSelection(editText.getText().length());
            }
        },10);
    }

    private void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) mContext.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }






}
