package com.meatchop.tmcpartner.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meatchop.tmcpartner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class Adapter_MenuItemCutDetails extends ArrayAdapter<Modal_MenuItemCutDetails> {

    private final ArrayList<String> cutDetailsName_arrayList = new ArrayList<>();
    private ArrayAdapter cutDetailsName_aAdapter;


    String MenuItemCutdetailsString="";
    ChangeMenuItemWeightAndPriceSecondScreen changeMenuItemWeightAndPriceSecondScreen;
    List<Modal_MenuItemCutDetails> cuttDetailsArray  = new ArrayList<>();
    Context context;
    public static BottomSheetDialog bottomSheetDialog;
    String default_cutdesp_bottomsheetDialog ="",default_cutdisplayno_bottomsheetDialog="",default_cutimagename_bottomsheetDialog="",default_cutkey_bottomsheetDialog="",default_cutname_bottomsheetDialog="",default_isdefault_bottomsheetDialog="",
            default_netweight_bottomsheetDialog="",default_netweightingrams_bottomsheetDialog="",default_portionsize_bottomsheetDialog="",
            default_grossweight_bottomsheetDialog="";


    String cutdesp_bottomsheetDialog ="",cutdisplayno_bottomsheetDialog="",cutimagename_bottomsheetDialog="",cutkey_bottomsheetDialog="",cutname_bottomsheetDialog="",isdefault_bottomsheetDialog="",
            netweight_bottomsheetDialog="",netweightingrams_bottomsheetDialog="",portionsize_bottomsheetDialog="",
            grossweight_bottomsheetDialog="";

    String isDefaultString_list ="",grossweight_list ="",netweight_list="",portionsize_list ="",grossweightgrams_list="",cutdesp_list="",cutimagename_list="",cutname_list="";
    List<Modal_MenuItemCutDetails> MenuItemCutDetailsArrayDefaultList = new ArrayList<>();

    String AdapterCalledFrom = "",positiontoSetSelectedValueinSpinner="";

    int check =0;
    boolean ifCutnameSpinnerClicked =false,isItemLoaded =false;
    public Adapter_MenuItemCutDetails(@NonNull Context context, List<Modal_MenuItemCutDetails> cuttDetailsArrayy, ChangeMenuItemWeightAndPriceSecondScreen changeMenuItemWeightAndPriceSecondScreenn, String MenuItemCutdetailsStringg) {
        super(context, R.layout.menuitem_cutdetails, cuttDetailsArrayy);
        this.MenuItemCutdetailsString = MenuItemCutdetailsStringg;
        this.cuttDetailsArray=cuttDetailsArrayy;
        this.changeMenuItemWeightAndPriceSecondScreen=changeMenuItemWeightAndPriceSecondScreenn;
        this.context=context;
        getMenuItemCutDetailsFromSharedPreferences();
    }


    private void getMenuItemCutDetailsFromSharedPreferences() {
        final SharedPreferences sharedPreferencesMenuitem = context.getSharedPreferences("MenuItemCutDetails", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferencesMenuitem.getString("MenuItemCutDetailsString", "");
        MenuItemCutdetailsString = sharedPreferencesMenuitem.getString("MenuItemCutDetailsString", "");
        if (json.isEmpty()) {
            Toast.makeText(context, "There is something error", Toast.LENGTH_LONG).show();
        } else {
            Type type = new TypeToken<List<Modal_MenuItemCutDetails>>() {
            }.getType();
            MenuItemCutDetailsArrayDefaultList = gson.fromJson(json, type);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.menuitem_cutdetails, parent, false
            );
        }
        Modal_MenuItemCutDetails modal_menuItemCutDetails = getItem(position);
        CheckBox isdefault_checkbox = convertView.findViewById(R.id.isdefault_checkbox);
        TextView grossweight_text_widget = convertView.findViewById(R.id.grossweight_text_widget);
        TextView netweight_textview_widget = convertView.findViewById(R.id.netweight_textview_widget);
        TextView portionsize_textview_widget = convertView.findViewById(R.id.portionsize_textview_widget);
        TextView cutname_textview_widget = convertView.findViewById(R.id.cutname_textview_widget);
        TextView cutDesc_textview_widget = convertView.findViewById(R.id.cutDesc_textview_widget);


        LinearLayout edit_cutdetails_layout = convertView.findViewById(R.id.edit_cutdetails_layout);
        LinearLayout isDefaultCheckboxLayout = convertView.findViewById(R.id.isDefaultCheckboxLayout);
        Button addNewCutItemtoArrayButton = convertView.findViewById(R.id.addNewCutItemtoArrayButton);

        if(AdapterCalledFrom.equals("FromAddNewCutDetails")){
            addNewCutItemtoArrayButton.setVisibility(View.VISIBLE);
            isDefaultCheckboxLayout.setVisibility(View.GONE);
            edit_cutdetails_layout.setVisibility(View.GONE);

        }
        if(AdapterCalledFrom.equals("FromCutDetailsJsonList")){
            addNewCutItemtoArrayButton.setVisibility(View.GONE);
            isDefaultCheckboxLayout.setVisibility(View.VISIBLE);
            edit_cutdetails_layout.setVisibility(View.VISIBLE);

        }
/*
        addNewCutItemtoArrayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog = new BottomSheetDialog(context);
                bottomSheetDialog.setContentView(R.layout.menuitemcut_weightdetailseditactivity);
                bottomSheetDialog.setCanceledOnTouchOutside(true);

                LinearLayout weightDetailsLayout =  bottomSheetDialog.findViewById(R.id.weightDetailsLayout);
                LinearLayout cutDetailsLayout =  bottomSheetDialog.findViewById(R.id.cutDetailsLayout);
                EditText edit_cutname = bottomSheetDialog.findViewById(R.id.edit_cutname);
                TextView cutname_textview = bottomSheetDialog.findViewById(R.id.edit_cutname_textview);
                TextView cutDescription_textview = bottomSheetDialog.findViewById(R.id.edit_cutDescription_textview);

                EditText edit_cutDescription = bottomSheetDialog.findViewById(R.id.edit_cutDescription);
                EditText edit_grossweight_cutdetail = bottomSheetDialog.findViewById(R.id.edit_grossweight_cutdetail);
                EditText cutDetails_netweight_first_textview_widget = bottomSheetDialog.findViewById(R.id.cutDetails_netweight_first_textview_widget);
                EditText cutDetails_netweight_second_textview_widget = bottomSheetDialog.findViewById(R.id.cutDetails_netweight_second_textview_widget);
                EditText edit_portionsize_cutdetail = bottomSheetDialog.findViewById(R.id.edit_portionsize_cutdetail);
                EditText edit_netweight_cutdetail = bottomSheetDialog.findViewById(R.id.edit_netweight_cutdetail);
                Spinner cutDetailsName_spinner = bottomSheetDialog.findViewById(R.id.cutDetailsName_spinner);
                LinearLayout two_cutdetails_editbox_linearlayout =  bottomSheetDialog.findViewById(R.id.two_cutdetails_editbox_linearlayout);

                ListView defaultItemCutDetailsListview =  bottomSheetDialog.findViewById(R.id.defaultItemCutDetailsListview);
                ListView defaultIteWeightDetailsListview =  bottomSheetDialog.findViewById(R.id.defaultIteWeightDetailsListview);


                CheckBox cutDetails_checkbox = bottomSheetDialog.findViewById(R.id.cutDetails_checkbox);

                Button saveCutDetails_button = bottomSheetDialog.findViewById(R.id.saveCutDetails_button);


                assert cutDetailsLayout != null;
                cutDetailsLayout.setVisibility(View.VISIBLE);
                Objects.requireNonNull(weightDetailsLayout).setVisibility(View.GONE);
                defaultItemCutDetailsListview.setVisibility(View.GONE);
                defaultIteWeightDetailsListview.setVisibility(View.GONE);
                cutDetailsName_spinner.setVisibility(View.GONE);
                edit_cutname.setVisibility(View.GONE);
                edit_cutDescription.setVisibility(View.GONE);
                cutname_textview.setVisibility(View.VISIBLE);
                cutDescription_textview.setVisibility(View.VISIBLE);

                cutname_textview.setText(getItem(position).getCutname());
                cutDescription_textview.setText(getItem(position).getCutdesp());
                edit_grossweight_cutdetail.setText(getItem(position).getGrossweight());
                edit_portionsize_cutdetail.setText(getItem(position).getPortionsize());



                try{
                    default_isdefault_bottomsheetDialog =  String.valueOf(Objects.requireNonNull(getItem(position)).getIsdefault().toUpperCase());
                }
                catch (Exception e){
                    default_isdefault_bottomsheetDialog ="FALSE";
                    e.printStackTrace();
                }
                try{
                    default_cutdesp_bottomsheetDialog =  String.valueOf(Objects.requireNonNull(getItem(position)).getCutdesp());

                }
                catch (Exception e){
                    default_cutdesp_bottomsheetDialog = "";
                    e.printStackTrace();
                }
                try{
                    default_cutdisplayno_bottomsheetDialog =  String.valueOf(Objects.requireNonNull(getItem(position)).getCutdisplayno());

                }
                catch (Exception e){
                    default_cutdisplayno_bottomsheetDialog = "";
                    e.printStackTrace();
                }

                try{
                    default_cutkey_bottomsheetDialog =  String.valueOf(Objects.requireNonNull(getItem(position)).getCutkey());
                }
                catch (Exception e){
                    default_cutkey_bottomsheetDialog ="";
                    e.printStackTrace();
                }

                try{
                    default_cutname_bottomsheetDialog =  String.valueOf(Objects.requireNonNull(getItem(position)).getCutname());

                }
                catch (Exception e){
                    default_cutname_bottomsheetDialog ="";
                    e.printStackTrace();
                }

                try{

                    default_netweight_bottomsheetDialog =  String.valueOf(Objects.requireNonNull(getItem(position)).getNetweight());



                    default_netweight_bottomsheetDialog = (getItem(position).getNetweight());

                    try {

                        if (default_netweight_bottomsheetDialog.contains("to") || default_netweight_bottomsheetDialog.contains("-")) {
                            Objects.requireNonNull(two_cutdetails_editbox_linearlayout).setVisibility(View.VISIBLE);
                            edit_netweight_cutdetail.setVisibility(View.GONE);


                            if (default_netweight_bottomsheetDialog.contains("to")) {
                                String[] split = netweight_bottomsheetDialog.split("to");
                                String firstSubString = split[0];
                                String secondSubString = split[1];
                                firstSubString = firstSubString.trim();
                                secondSubString = secondSubString.trim();
                                cutDetails_netweight_first_textview_widget.setText(firstSubString);
                                cutDetails_netweight_second_textview_widget.setText(secondSubString);
                            } else {
                                String[] split = default_netweight_bottomsheetDialog.split("-");
                                String firstSubString = split[0];
                                String secondSubString = split[1];
                                firstSubString = firstSubString.trim();
                                secondSubString = secondSubString.trim();
                                cutDetails_netweight_first_textview_widget.setText(firstSubString);
                                cutDetails_netweight_second_textview_widget.setText(secondSubString);


                            }


                        } else {
                            two_cutdetails_editbox_linearlayout.setVisibility(View.GONE);
                            edit_netweight_cutdetail.setVisibility(View.VISIBLE);



                            edit_netweight_cutdetail.setText(default_netweight_bottomsheetDialog);

                        }


                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }



                }
                catch (Exception e){
                    default_netweight_bottomsheetDialog="";
                    e.printStackTrace();
                }

                try{
                    default_portionsize_bottomsheetDialog =  String.valueOf(Objects.requireNonNull(getItem(position)).getPortionsize());

                }
                catch (Exception e){
                    default_portionsize_bottomsheetDialog ="";
                    e.printStackTrace();
                }


                try{
                    default_netweightingrams_bottomsheetDialog =  String.valueOf(Objects.requireNonNull(getItem(position)).getNetweightingrams());

                }
                catch (Exception e){
                    default_netweightingrams_bottomsheetDialog="0";
                    e.printStackTrace();
                }

                try{
                    default_grossweight_bottomsheetDialog =  String.valueOf(Objects.requireNonNull(getItem(position)).getGrossweight());
                }
                catch (Exception e){
                    default_grossweight_bottomsheetDialog = "p";
                    e.printStackTrace();
                }





                try{
                    Objects.requireNonNull(cutname_textview).setText(default_cutname_bottomsheetDialog);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    Objects.requireNonNull(cutDescription_textview).setText(default_cutdesp_bottomsheetDialog);

                }
                catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    Objects.requireNonNull(edit_grossweight_cutdetail).setText(default_grossweight_bottomsheetDialog);

                }
                catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    Objects.requireNonNull(edit_netweight_cutdetail).setText(default_netweight_bottomsheetDialog);

                }
                catch (Exception e){
                    e.printStackTrace();
                }


                try{
                    Objects.requireNonNull(edit_portionsize_cutdetail).setText(default_portionsize_bottomsheetDialog);

                }
                catch (Exception e){
                    e.printStackTrace();
                }
                if(changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.size()>1) {
                    Objects.requireNonNull(cutDetails_checkbox).setChecked(false);
                }
                else{
                    Objects.requireNonNull(cutDetails_checkbox).setChecked(true);
                }

                cutDetailsLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.size()>1){
                            if(Objects.requireNonNull(cutDetails_checkbox).isChecked()){
                                cutDetails_checkbox.setChecked(false);

                            }
                            else{
                                cutDetails_checkbox.setChecked(true);

                            }
                        }
                        else{
                            Objects.requireNonNull(cutDetails_checkbox).setChecked(true);
                            Toast.makeText(context, "First Additional weight should be default one", Toast.LENGTH_SHORT).show();

                        }


                    }
                });



                Objects.requireNonNull(saveCutDetails_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        cutname_bottomsheetDialog =  Objects.requireNonNull(edit_cutname).getText().toString();
                        cutdesp_bottomsheetDialog = Objects.requireNonNull(edit_cutDescription).getText().toString();
                        grossweight_bottomsheetDialog = Objects.requireNonNull(edit_grossweight_cutdetail).getText().toString();
                        netweight_bottomsheetDialog = Objects.requireNonNull(edit_netweight_cutdetail).getText().toString();
                        portionsize_bottomsheetDialog = Objects.requireNonNull(edit_portionsize_cutdetail).getText().toString();

                        isdefault_bottomsheetDialog = String.valueOf(cutDetails_checkbox.isChecked()).toUpperCase();
                       // changeDataInLocalArray(cutname_bottomsheetDialog,cutdesp_bottomsheetDialog,grossweight_bottomsheetDialog,netweight_bottomsheetDialog,portionsize_bottomsheetDialog,isdefault_bottomsheetDialog,cutkey_bottomsheetDialog);
                       // if(!Objects.requireNonNull(edit_grossweight_cutdetail).getText().toString().equals("null") && !Objects.requireNonNull(edit_grossweight_cutdetail).getText().toString().equals("")){
                            if((!Objects.requireNonNull(cutDetails_netweight_first_textview_widget).getText().toString().equals("null") && !Objects.requireNonNull(cutDetails_netweight_first_textview_widget).getText().toString().equals(""))&&(!Objects.requireNonNull(cutDetails_netweight_second_textview_widget).getText().toString().equals("null") && !Objects.requireNonNull(cutDetails_netweight_second_textview_widget).getText().toString().equals(""))){


                                if(!Objects.requireNonNull(edit_portionsize_cutdetail).getText().toString().equals("null") && !Objects.requireNonNull(edit_portionsize_cutdetail).getText().toString().equals("")){







                                    grossweight_bottomsheetDialog = Objects.requireNonNull(edit_grossweight_cutdetail).getText().toString();


                                    String firstnetweightfromString="", secondnetweightfromString ="";
                                    try{
                                        firstnetweightfromString = cutDetails_netweight_first_textview_widget.getText().toString();
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    try{
                                        secondnetweightfromString = cutDetails_netweight_second_textview_widget.getText().toString();

                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }






                                    if(firstnetweightfromString.equals("")||firstnetweightfromString.equals(null)){
                                        Toast.makeText(context, " NetWeight Edit box can't be empty", Toast.LENGTH_SHORT).show();

                                    }
                                    else if(secondnetweightfromString.equals("")||secondnetweightfromString.equals(null)){
                                        Toast.makeText(context, " NetWeight Edit box can't be empty", Toast.LENGTH_SHORT).show();

                                    }
                                    else {
                                        try {
                                            firstnetweightfromString = firstnetweightfromString.replaceAll("[^\\d.]", "");
                                            firstnetweightfromString = firstnetweightfromString.trim();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        try {
                                            secondnetweightfromString = secondnetweightfromString.replaceAll("[^\\d.]", "");
                                            secondnetweightfromString = secondnetweightfromString.trim();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    netweight_bottomsheetDialog =firstnetweightfromString +"g - "+secondnetweightfromString;

                                    //   netweight_bottomsheetDialog = Objects.requireNonNull(edit_netweight).getText().toString();
                                    portionsize_bottomsheetDialog = Objects.requireNonNull(edit_portionsize_cutdetail).getText().toString();
                                    isdefault_bottomsheetDialog = String.valueOf(cutDetails_checkbox.isChecked()).toUpperCase();
                                    AddDataInLocalArray(cutname_bottomsheetDialog,cutdesp_bottomsheetDialog,grossweight_bottomsheetDialog,netweight_bottomsheetDialog,portionsize_bottomsheetDialog,isdefault_bottomsheetDialog,cutkey_bottomsheetDialog);

                                }
                                else
                                {
                                    Toast.makeText(context, "Portion size can't be Empty", Toast.LENGTH_SHORT).show();
                                }

                            }
                            else
                            {
                                Toast.makeText(context, "NetWeight can't be Empty", Toast.LENGTH_SHORT).show();
                            }


tg
                       }else
                        {
                            Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Grossweight can't be Empty", Toast.LENGTH_SHORT).show();
                        }






                    }
                });


                bottomSheetDialog.show();



            }
        });

*/

        edit_cutdetails_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                bottomSheetDialog = new BottomSheetDialog(context);
                bottomSheetDialog.setContentView(R.layout.menuitemcut_weightdetailseditactivity);
                bottomSheetDialog.setCanceledOnTouchOutside(true);


                LinearLayout weightDetailsLayout =  bottomSheetDialog.findViewById(R.id.weightDetailsLayout);
                LinearLayout cutDetailsLayout =  bottomSheetDialog.findViewById(R.id.cutDetailsLayout);

                CheckBox cutDetails_checkbox = bottomSheetDialog.findViewById(R.id.cutDetails_checkbox);
                EditText edit_cutname = bottomSheetDialog.findViewById(R.id.edit_cutname);
                EditText edit_cutDescription = bottomSheetDialog.findViewById(R.id.edit_cutDescription);
                EditText edit_grossweight_cutdetail = bottomSheetDialog.findViewById(R.id.edit_grossweight_cutdetail);
                EditText cutDetails_netweight_first_textview_widget = bottomSheetDialog.findViewById(R.id.cutDetails_netweight_first_textview_widget);
                EditText cutDetails_netweight_second_textview_widget = bottomSheetDialog.findViewById(R.id.cutDetails_netweight_second_textview_widget);
                EditText edit_portionsize_cutdetail = bottomSheetDialog.findViewById(R.id.edit_portionsize_cutdetail);
                EditText edit_netweight_cutdetail = bottomSheetDialog.findViewById(R.id.edit_netweight_cutdetail);
                Spinner cutDetailsName_spinner = bottomSheetDialog.findViewById(R.id.cutDetailsName_spinner);
                LinearLayout two_cutdetails_editbox_linearlayout =  bottomSheetDialog.findViewById(R.id.two_cutdetails_editbox_linearlayout);
                TextView edit_cutDescription_textview = bottomSheetDialog.findViewById(R.id.edit_cutDescription_textview);

                ListView defaultItemCutDetailsListview =  bottomSheetDialog.findViewById(R.id.defaultItemCutDetailsListview);
                ListView defaultIteWeightDetailsListview =  bottomSheetDialog.findViewById(R.id.defaultIteWeightDetailsListview);
                Button saveCutDetails_button = bottomSheetDialog.findViewById(R.id.saveCutDetails_button);

                LinearLayout cutDetailsCheckbox_LinearLayout =  bottomSheetDialog.findViewById(R.id.cutDetailsCheckbox_LinearLayout);



                assert cutDetailsLayout != null;
                cutDetailsLayout.setVisibility(View.VISIBLE);
                Objects.requireNonNull(weightDetailsLayout).setVisibility(View.GONE);


                for(int i=0;i<  MenuItemCutDetailsArrayDefaultList.size();i++){
                    Modal_MenuItemCutDetails modal_menuItemCutDetails = MenuItemCutDetailsArrayDefaultList.get(i);
                    String CutDesp = "",Cutname = "" ;

                    try{
                        CutDesp = modal_menuItemCutDetails.getCutdesp().toString();

                    }
                    catch (Exception  e){
                        e.printStackTrace();
                    }


                    try{
                        Cutname  = modal_menuItemCutDetails.getCutname().toString();

                    }
                    catch (Exception  e){
                        e.printStackTrace();
                    }




                    try {
                        if (!cutDetailsName_arrayList.contains(Cutname)) {
                            cutDetailsName_arrayList.add(Cutname);
                        }

                        cutDetailsName_aAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, cutDetailsName_arrayList);
                        Objects.requireNonNull(cutDetailsName_spinner).setAdapter(cutDetailsName_aAdapter);


                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }


                try{
                    isdefault_bottomsheetDialog =  String.valueOf(Objects.requireNonNull(getItem(position)).getIsdefault().toUpperCase());
                }
                catch (Exception e){
                    isdefault_bottomsheetDialog ="FALSE";
                    e.printStackTrace();
                }





                try{
                    cutdesp_bottomsheetDialog =  String.valueOf(Objects.requireNonNull(getItem(position)).getCutdesp());

                }
                catch (Exception e){
                    cutdesp_bottomsheetDialog = "";
                    e.printStackTrace();
                }
                try{
                    cutdisplayno_bottomsheetDialog =  String.valueOf(Objects.requireNonNull(getItem(position)).getCutdisplayno());

                }
                catch (Exception e){
                    cutdisplayno_bottomsheetDialog = "";
                    e.printStackTrace();
                }
                    try{
                        cutimagename_bottomsheetDialog =  String.valueOf(Objects.requireNonNull(getItem(position)).getCutimagename());

                    }
                    catch (Exception e){
                        cutimagename_bottomsheetDialog = "";
                        e.printStackTrace();
                    }

                try{
                    cutkey_bottomsheetDialog =  String.valueOf(Objects.requireNonNull(getItem(position)).getCutkey());
                }
                catch (Exception e){
                    cutkey_bottomsheetDialog ="";
                    e.printStackTrace();
                }
                try{
                    positiontoSetSelectedValueinSpinner = getMenuItemCutDataPosition(cutkey_bottomsheetDialog);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    cutname_bottomsheetDialog =  String.valueOf(Objects.requireNonNull(getItem(position)).getCutname());

                }
                catch (Exception e){
                    cutname_bottomsheetDialog ="";
                    e.printStackTrace();
                }

                try{
                    netweight_bottomsheetDialog =  String.valueOf(Objects.requireNonNull(getItem(position)).getNetweight());

                }
                catch (Exception e){
                    netweight_bottomsheetDialog="";
                    e.printStackTrace();
                }

                try{
                    portionsize_bottomsheetDialog =  String.valueOf(Objects.requireNonNull(getItem(position)).getPortionsize());

                }
                catch (Exception e){
                    portionsize_bottomsheetDialog ="";
                    e.printStackTrace();
                }


                try{
                    netweightingrams_bottomsheetDialog =  String.valueOf(Objects.requireNonNull(getItem(position)).getNetweightingrams());

                }
                catch (Exception e){
                    netweightingrams_bottomsheetDialog="0";
                    e.printStackTrace();
                }

                try{
                    grossweight_bottomsheetDialog =  String.valueOf(Objects.requireNonNull(getItem(position)).getGrossweight());
                }
                catch (Exception e){
                    grossweight_bottomsheetDialog = "p";
                    e.printStackTrace();
                }
                try{
                    Objects.requireNonNull(edit_cutname).setText(cutname_bottomsheetDialog);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    Objects.requireNonNull(edit_cutDescription).setText(cutdesp_bottomsheetDialog);

                }
                catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    Objects.requireNonNull(edit_cutDescription_textview).setText(cutdesp_bottomsheetDialog);

                }
                catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    Objects.requireNonNull(edit_grossweight_cutdetail).setText(grossweight_bottomsheetDialog);

                }
                catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    
                    Objects.requireNonNull(edit_netweight_cutdetail).setText(netweight_bottomsheetDialog);

                    try {

                        if (netweight_bottomsheetDialog.contains("to") || netweight_bottomsheetDialog.contains("-")) {
                            Objects.requireNonNull(two_cutdetails_editbox_linearlayout).setVisibility(View.VISIBLE);
                            edit_netweight_cutdetail.setVisibility(View.GONE);


                            if (netweight_bottomsheetDialog.contains("to")) {
                                String[] split = netweight_bottomsheetDialog.split("to");
                                String firstSubString = split[0];
                                String secondSubString = split[1];
                                firstSubString = firstSubString.trim();
                                secondSubString = secondSubString.trim();
                                cutDetails_netweight_first_textview_widget.setText(firstSubString);
                                cutDetails_netweight_second_textview_widget.setText(secondSubString);
                            } else {
                                String[] split = netweight_bottomsheetDialog.split("-");
                                String firstSubString = split[0];
                                String secondSubString = split[1];
                                firstSubString = firstSubString.trim();
                                secondSubString = secondSubString.trim();
                                cutDetails_netweight_first_textview_widget.setText(firstSubString);
                                cutDetails_netweight_second_textview_widget.setText(secondSubString);


                            }


                        } else {
                            two_cutdetails_editbox_linearlayout.setVisibility(View.GONE);
                            edit_netweight_cutdetail.setVisibility(View.VISIBLE);



                            edit_netweight_cutdetail.setText(netweight_bottomsheetDialog);

                        }


                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }


                try{
                    Objects.requireNonNull(edit_portionsize_cutdetail).setText(portionsize_bottomsheetDialog);

                }
                catch (Exception e){
                    e.printStackTrace();
                }
                    try{
                        Objects.requireNonNull(cutDetails_checkbox).setChecked(Boolean.parseBoolean(isdefault_bottomsheetDialog));

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }


                cutDetailsLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(getCount()>1){

                            if(Objects.requireNonNull(cutDetails_checkbox).isChecked()){
                            cutDetails_checkbox.setChecked(false);

                        }
                        else{
                            cutDetails_checkbox.setChecked(true);

                        }
                        }
                        else{
                            Objects.requireNonNull(cutDetails_checkbox).setChecked(true);
                            Toast.makeText(context, "First Additional Cutdetail should be default one", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                cutDetailsName_spinner.setSelection(Integer.parseInt(positiontoSetSelectedValueinSpinner),false);

                Objects.requireNonNull(cutDetailsName_spinner).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(++check >= 1) {
                            ifCutnameSpinnerClicked = true;
                            default_cutkey_bottomsheetDialog = getMenuItemCutData(position,"cutkey");
                            cutimagename_bottomsheetDialog = getMenuItemCutData(position,"cutimagename");
                            cutdisplayno_bottomsheetDialog = getMenuItemCutData(position,"cutdisplayno");
                        }
                        else{
                            ifCutnameSpinnerClicked = false;

                        }

                            String cutdesp = getMenuItemCutData(position,"cutdesp");
                        String cutdisplayno = getMenuItemCutData(position,"cutdisplayno");
                        String cutimagename = getMenuItemCutData(position,"cutimagename");
                        String cutkey = getMenuItemCutData(position,"cutkey");
                        String isdefault = getMenuItemCutData(position,"isdefault");
                        String cutname = getMenuItemCutData(position,"cutname");
                        String netweight = getMenuItemCutData(position,"netweight");
                        String netweightingrams = getMenuItemCutData(position,"netweightingrams");
                        String portionsize = getMenuItemCutData(position,"portionsize");
                        String grossweight = getMenuItemCutData(position,"grossweight");



                        Objects.requireNonNull(edit_cutDescription).setText(cutdesp.toString());
                        Objects.requireNonNull(edit_grossweight_cutdetail).setText(grossweight.toString());
                        Objects.requireNonNull(edit_cutDescription_textview).setText(cutdesp.toString());
                        Objects.requireNonNull(edit_cutname).setText(cutname.toString());
                        Objects.requireNonNull(edit_portionsize_cutdetail).setText(portionsize.toString());
                        Objects.requireNonNull(edit_netweight_cutdetail).setText(netweight.toString());
                        Objects.requireNonNull(cutDetails_netweight_first_textview_widget).setText(netweight.toString());
                        Objects.requireNonNull(cutDetails_netweight_second_textview_widget).setText(netweight.toString());





                        try {

                            if (netweight.contains("to") || netweight.contains("-")) {
                                Objects.requireNonNull(two_cutdetails_editbox_linearlayout).setVisibility(View.VISIBLE);
                                edit_netweight_cutdetail.setVisibility(View.GONE);


                                if (netweight.contains("to")) {
                                    String[] split = netweight.split("to");
                                    String firstSubString = split[0];
                                    String secondSubString = split[1];
                                    firstSubString = firstSubString.trim();
                                    secondSubString = secondSubString.trim();
                                    cutDetails_netweight_first_textview_widget.setText(firstSubString);
                                    cutDetails_netweight_second_textview_widget.setText(secondSubString);
                                } else {
                                    String[] split = netweight.split("-");
                                    String firstSubString = split[0];
                                    String secondSubString = split[1];
                                    firstSubString = firstSubString.trim();
                                    secondSubString = secondSubString.trim();
                                    cutDetails_netweight_first_textview_widget.setText(firstSubString);
                                    cutDetails_netweight_second_textview_widget.setText(secondSubString);


                                }


                            } else {
                                two_cutdetails_editbox_linearlayout.setVisibility(View.GONE);
                                edit_netweight_cutdetail.setVisibility(View.VISIBLE);



                                edit_netweight_cutdetail.setText(netweight);

                            }


                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                Objects.requireNonNull(saveCutDetails_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            cutname_bottomsheetDialog = Objects.requireNonNull(edit_cutname).getText().toString();
                            cutdesp_bottomsheetDialog = Objects.requireNonNull(edit_cutDescription_textview).getText().toString();
                            grossweight_bottomsheetDialog = Objects.requireNonNull(edit_grossweight_cutdetail).getText().toString();
                            netweight_bottomsheetDialog = Objects.requireNonNull(edit_netweight_cutdetail).getText().toString();
                            portionsize_bottomsheetDialog = Objects.requireNonNull(edit_portionsize_cutdetail).getText().toString();
                            isdefault_bottomsheetDialog = String.valueOf(cutDetails_checkbox.isChecked()).toUpperCase();
                            // changeDataInLocalArray(cutname_bottomsheetDialog,cutdesp_bottomsheetDialog,grossweight_bottomsheetDialog,netweight_bottomsheetDialog,portionsize_bottomsheetDialog,isdefault_bottomsheetDialog,cutkey_bottomsheetDialog);
                            // if(!Objects.requireNonNull(edit_grossweight_cutdetail).getText().toString().equals("null") && !Objects.requireNonNull(edit_grossweight_cutdetail).getText().toString().equals("")){
                            if ((!Objects.requireNonNull(cutDetails_netweight_first_textview_widget).getText().toString().equals("null") && !Objects.requireNonNull(cutDetails_netweight_first_textview_widget).getText().toString().equals("")) && (!Objects.requireNonNull(cutDetails_netweight_second_textview_widget).getText().toString().equals("null") && !Objects.requireNonNull(cutDetails_netweight_second_textview_widget).getText().toString().equals(""))) {


                                //   if(!Objects.requireNonNull(edit_portionsize_cutdetail).getText().toString().equals("null") && !Objects.requireNonNull(edit_portionsize_cutdetail).getText().toString().equals("")){


                                grossweight_bottomsheetDialog = Objects.requireNonNull(edit_grossweight_cutdetail).getText().toString();


                                String firstnetweightfromString = "", secondnetweightfromString = "";
                                try {
                                    firstnetweightfromString = cutDetails_netweight_first_textview_widget.getText().toString();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    secondnetweightfromString = cutDetails_netweight_second_textview_widget.getText().toString();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                if (firstnetweightfromString.equals("") || firstnetweightfromString.equals(null)) {
                                    Toast.makeText(context, " NetWeight Edit box can't be empty", Toast.LENGTH_SHORT).show();

                                } else if (secondnetweightfromString.equals("") || secondnetweightfromString.equals(null)) {
                                    Toast.makeText(context, " NetWeight Edit box can't be empty", Toast.LENGTH_SHORT).show();

                                } else {
                                    try {
                                        firstnetweightfromString = firstnetweightfromString.replaceAll("[^\\d.]", "");
                                        firstnetweightfromString = firstnetweightfromString.trim();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        secondnetweightfromString = secondnetweightfromString.replaceAll("[^\\d.]", "");
                                        secondnetweightfromString = secondnetweightfromString.trim();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                netweight_bottomsheetDialog = firstnetweightfromString + "g - " + secondnetweightfromString + "g";

                                //   netweight_bottomsheetDialog = Objects.requireNonNull(edit_netweight).getText().toString();
                                if (!Objects.requireNonNull(edit_portionsize_cutdetail).getText().toString().equals("null") && !Objects.requireNonNull(edit_portionsize_cutdetail).getText().toString().equals("")) {

                                    portionsize_bottomsheetDialog = Objects.requireNonNull(edit_portionsize_cutdetail).getText().toString() ;

                                }
                                isdefault_bottomsheetDialog = String.valueOf(cutDetails_checkbox.isChecked()).toUpperCase();
                                // AddCutDetailsDataInLocalArray(cutname_bottomsheetDialog,cutdesp_bottomsheetDialog,grossweight_bottomsheetDialog,netweight_bottomsheetDialog,portionsize_bottomsheetDialog,isdefault_bottomsheetDialog,cutkey_bottomsheetDialog);
                                if(ifCutnameSpinnerClicked) {
                                    changeDataInLocalArray(cutname_bottomsheetDialog, cutdesp_bottomsheetDialog, grossweight_bottomsheetDialog, netweight_bottomsheetDialog, portionsize_bottomsheetDialog, isdefault_bottomsheetDialog, cutkey_bottomsheetDialog, default_cutkey_bottomsheetDialog);
                                }
                                else{
                                    changeDataInLocalArray(cutname_bottomsheetDialog, cutdesp_bottomsheetDialog, grossweight_bottomsheetDialog, netweight_bottomsheetDialog, portionsize_bottomsheetDialog, isdefault_bottomsheetDialog, cutkey_bottomsheetDialog, cutkey_bottomsheetDialog);

                                }
                          /*  }
                            else
                            {
                                Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Portion size can't be Empty", Toast.LENGTH_SHORT).show();
                            }

                           */

                            } else {
                                Toast.makeText(context, "NetWeight can't be Empty", Toast.LENGTH_SHORT).show();
                            }


                       /*
                       }else
                        {
                            Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Grossweight can't be Empty", Toast.LENGTH_SHORT).show();
                        }


                        */

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });

         /*       Objects.requireNonNull(saveCutDetails_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        cutname_bottomsheetDialog =  Objects.requireNonNull(edit_cutname).getText().toString();
                        cutdesp_bottomsheetDialog = Objects.requireNonNull(edit_cutDescription).getText().toString();
                        grossweight_bottomsheetDialog = Objects.requireNonNull(edit_grossweight_cutdetail).getText().toString();
                        netweight_bottomsheetDialog = Objects.requireNonNull(edit_netweight_cutdetail).getText().toString();
                        portionsize_bottomsheetDialog = Objects.requireNonNull(edit_portionsize_cutdetail).getText().toString();

                        isdefault_bottomsheetDialog = String.valueOf(cutDetails_checkbox.isChecked()).toUpperCase();

                        changeDataInLocalArray(cutname_bottomsheetDialog,cutdesp_bottomsheetDialog,grossweight_bottomsheetDialog,netweight_bottomsheetDialog,portionsize_bottomsheetDialog,isdefault_bottomsheetDialog,cutkey_bottomsheetDialog);
                    }
                });


          */

                bottomSheetDialog.show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });



        isDefaultCheckboxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        try{
            isDefaultString_list = Objects.requireNonNull(modal_menuItemCutDetails).getIsdefault().toString().toUpperCase();

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            grossweight_list = Objects.requireNonNull(getItem(position)).getGrossweight().toString();

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            netweight_list = Objects.requireNonNull(getItem(position)).getNetweight().toString();

        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            portionsize_list = Objects.requireNonNull(getItem(position)).getPortionsize().toString();

        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            isDefaultString_list = Objects.requireNonNull(getItem(position)).getIsdefault().toString().toUpperCase();

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            cutdesp_list = Objects.requireNonNull(getItem(position)).getCutdesp().toString();

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            cutimagename_list = Objects.requireNonNull(getItem(position)).getCutimagename().toString();

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            cutname_list = Objects.requireNonNull(getItem(position)).getCutname().toString();

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            if(isDefaultString_list.equals("TRUE")){
                isdefault_checkbox.setChecked(true);
            }
            else if(isDefaultString_list.equals("FALSE")){
                isdefault_checkbox.setChecked(false);

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }



        try{
            grossweight_text_widget.setText(grossweight_list);
        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            netweight_textview_widget.setText(netweight_list);
        }
        catch (Exception e){
            e.printStackTrace();
        }



        try{
            cutname_textview_widget.setText(cutname_list);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            cutDesc_textview_widget.setText(cutdesp_list);
        }
        catch (Exception e){
            e.printStackTrace();
        }



        try{
            portionsize_textview_widget.setText(portionsize_list);
        }
        catch (Exception e){
            e.printStackTrace();
        }









        return convertView;
    }

    private String getMenuItemCutDataPosition(String fieldName){
        String data="";
        try {
            JSONArray jsonArray = new JSONArray(MenuItemCutdetailsString);
            for(int i = 0;i<jsonArray.length();i++){
                JSONObject json = jsonArray.getJSONObject(i);
                String key = json.getString("cutkey");
                if(key.equals(fieldName)){
                    data = String.valueOf(i);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }


    //Doing the same with this method as we did with getName()
    private String getMenuItemCutData(int position, String fieldName){
        String data="";
        try {
            JSONArray jsonArray = new JSONArray(MenuItemCutdetailsString);
            JSONObject json = jsonArray.getJSONObject(position);
            data = json.getString(fieldName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
    private void AddDataInLocalArray(String cutname_bottomsheetDialog, String cutdesp_bottomsheetDialog, String grossweight_bottomsheetDialog, String netweight_bottomsheetDialog, String portionsize_bottomsheetDialog, String isdefault_bottomsheetDialog, String cutkey_bottomsheetDialog) {

        if((default_netweight_bottomsheetDialog.toUpperCase().contains("PCS"))||(default_netweight_bottomsheetDialog.toUpperCase().contains("PC"))){
            try{

                netweight_bottomsheetDialog = String.valueOf(netweight_bottomsheetDialog+"Pcs");
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        if((default_netweight_bottomsheetDialog.toUpperCase().contains("G"))||(default_netweight_bottomsheetDialog.toUpperCase().contains("GRAMS"))||(default_netweight_bottomsheetDialog.toUpperCase().contains("GMS"))){
            try{
                netweight_bottomsheetDialog = String.valueOf(netweight_bottomsheetDialog+"g");

            } catch (Exception e){
                e.printStackTrace();
            }
        }

        if((default_grossweight_bottomsheetDialog.toUpperCase().contains("G"))||(default_grossweight_bottomsheetDialog.toUpperCase().contains("GRAMS"))||(default_grossweight_bottomsheetDialog.toUpperCase().contains("GMS"))){
            try{
                grossweight_bottomsheetDialog = String.valueOf(grossweight_bottomsheetDialog+"g");


            } catch (Exception e){
                e.printStackTrace();
            }
        }

        if((default_portionsize_bottomsheetDialog.toUpperCase().contains("PCS"))||(default_portionsize_bottomsheetDialog.toUpperCase().contains("PC"))){
            try{
                portionsize_bottomsheetDialog = String.valueOf(portionsize_bottomsheetDialog);

            } catch (Exception e){
                e.printStackTrace();
            }
        }

        else  if((default_portionsize_bottomsheetDialog.toUpperCase().contains("G"))||(default_portionsize_bottomsheetDialog.toUpperCase().contains("GRAMS"))||(default_portionsize_bottomsheetDialog.toUpperCase().contains("GMS"))){
            try{
                portionsize_bottomsheetDialog = String.valueOf(portionsize_bottomsheetDialog);

            } catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            portionsize_bottomsheetDialog = String.valueOf(portionsize_bottomsheetDialog);
        }




        Modal_MenuItemCutDetails modal_menuItemCutDetails = new Modal_MenuItemCutDetails();
        modal_menuItemCutDetails.grossweight = grossweight_bottomsheetDialog;
        modal_menuItemCutDetails.netweight = netweight_bottomsheetDialog;
        modal_menuItemCutDetails.portionsize = portionsize_bottomsheetDialog;
        modal_menuItemCutDetails.cutdisplayno = cutdisplayno_bottomsheetDialog;
        modal_menuItemCutDetails.isdefault = isdefault_bottomsheetDialog;
        modal_menuItemCutDetails.cutname = cutname_bottomsheetDialog;
        modal_menuItemCutDetails.cutdesp = cutdesp_bottomsheetDialog;



        try{
           changeMenuItemWeightAndPriceSecondScreen. cutDetailsArray.add(modal_menuItemCutDetails);
            changeMenuItemWeightAndPriceSecondScreen.itemcutdetailsString ="cutdetails added";
        }
        catch (Exception e){
            e.printStackTrace();
        }





        try{
            changeMenuItemWeightAndPriceSecondScreen. FormatAndDisplaytheDataa();

        }
        catch (Exception e){
            e.printStackTrace();
        }






    }

    private void changeDataInLocalArray(String cutname_bottomsheetDialog, String cutdesp_bottomsheetDialog, String grossweight_bottomsheetDialog, String netweight_bottomsheetDialog, String portionsize_bottomsheetDialog, String isdefault_bottomsheetDialog, String cutkey_bottomsheetDialog, String cutkey_bottomsheetDialog2) {


        boolean isArrayContainsdefault = false;
        if (isdefault_bottomsheetDialog.toUpperCase().equals("TRUE")) {
            for (int i = 0; i < changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.size(); i++) {
                String isdefaultCutdetailsarraylist = "";
                isdefaultCutdetailsarraylist = changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).getIsdefault().toString();
                if (isdefaultCutdetailsarraylist.toUpperCase().equals("TRUE")) {
                    isArrayContainsdefault = true;
                }
            }
            if (isArrayContainsdefault) {
                Toast.makeText(changeMenuItemWeightAndPriceSecondScreen, "Already Have an default item in Array", Toast.LENGTH_SHORT).show();
            } else {
                String grossweightingrams_bottomsheetDialog="",netweightingrams_bottomsheetDialog="";
                try{
                    try {

                        if (netweight_bottomsheetDialog.contains("to") || netweight_bottomsheetDialog.contains("-")) {



                            if (netweight_bottomsheetDialog.contains("to")) {
                                String[] split = netweight_bottomsheetDialog.split("to");
                                String firstSubString = split[0];
                                String secondSubString = split[1];
                                firstSubString = firstSubString.trim();
                                secondSubString = secondSubString.trim();
                                netweightingrams_bottomsheetDialog = secondSubString;

                            } else {
                                String[] split = netweight_bottomsheetDialog.split("-");
                                String firstSubString = split[0];
                                String secondSubString = split[1];
                                firstSubString = firstSubString.trim();
                                secondSubString = secondSubString.trim();
                                netweightingrams_bottomsheetDialog = secondSubString;


                            }


                        } else {


                            netweightingrams_bottomsheetDialog = netweight_bottomsheetDialog;


                        }


                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try {
                        netweightingrams_bottomsheetDialog = netweightingrams_bottomsheetDialog.replaceAll("[^\\d.]", "");

                    } catch (Exception e) {
                        netweightingrams_bottomsheetDialog = "";
                        e.printStackTrace();
                    }
                }
                catch (Exception e ){
                    e.printStackTrace();
                }
                try {
                    if(!cutkey_bottomsheetDialog.equals(cutkey_bottomsheetDialog2)){
                        if(!changeMenuItemWeightAndPriceSecondScreen.cutDetailsKey_arrayList.contains(cutkey_bottomsheetDialog2)){
                            changeMenuItemWeightAndPriceSecondScreen.cutDetailsKey_arrayList.remove(cutkey_bottomsheetDialog);
                            changeMenuItemWeightAndPriceSecondScreen.cutDetailsKey_arrayList.add(cutkey_bottomsheetDialog2);
                            for (int i = 0; i < changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.size(); i++) {
                                String cutkeyfromCutdetailsarraylist = "";
                                cutkeyfromCutdetailsarraylist = changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).getCutkey().toString();
                                if (cutkeyfromCutdetailsarraylist.equals(cutkey_bottomsheetDialog)) {



                                    changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setCutkey(cutkey_bottomsheetDialog2);
                                    changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setNetweightingrams(netweightingrams_bottomsheetDialog);
                                    changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setCutimagename(cutimagename_bottomsheetDialog);
                                    changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setCutdisplayno(cutdisplayno_bottomsheetDialog);

                                    changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setCutname(cutname_bottomsheetDialog);
                                    changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setCutdesp(cutdesp_bottomsheetDialog);
                                    changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setGrossweight(grossweight_bottomsheetDialog);
                                    changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setNetweight(netweight_bottomsheetDialog);
                                    changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setPortionsize(portionsize_bottomsheetDialog);
                                    changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setIsdefault(isdefault_bottomsheetDialog);
                                    notifyDataSetChanged();
                                    bottomSheetDialog.cancel();


                                }


                            }
                        }
                        else{
                            Toast.makeText(changeMenuItemWeightAndPriceSecondScreen, "This Cut item Already Added in the list  ", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        for (int i = 0; i < changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.size(); i++) {
                            String cutkeyfromCutdetailsarraylist = "";
                            cutkeyfromCutdetailsarraylist = changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).getCutkey().toString();
                            if (cutkeyfromCutdetailsarraylist.equals(cutkey_bottomsheetDialog)) {

                                changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setCutkey(cutkey_bottomsheetDialog2);
                                changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setNetweightingrams(netweightingrams_bottomsheetDialog);
                                changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setCutimagename(cutimagename_bottomsheetDialog);
                                changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setCutdisplayno(cutdisplayno_bottomsheetDialog);
                                changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setCutname(cutname_bottomsheetDialog);
                                changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setCutdesp(cutdesp_bottomsheetDialog);
                                changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setGrossweight(grossweight_bottomsheetDialog);
                                changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setNetweight(netweight_bottomsheetDialog);
                                changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setPortionsize(portionsize_bottomsheetDialog);
                                changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setIsdefault(isdefault_bottomsheetDialog);
                                notifyDataSetChanged();
                                bottomSheetDialog.cancel();


                            }


                        }
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }


        }
        else {
            String grossweightingrams_bottomsheetDialog = "", netweightingrams_bottomsheetDialog = "";
            try {
                try {

                    if (netweight_bottomsheetDialog.contains("to") || netweight_bottomsheetDialog.contains("-")) {


                        if (netweight_bottomsheetDialog.contains("to")) {
                            String[] split = netweight_bottomsheetDialog.split("to");
                            String firstSubString = split[0];
                            String secondSubString = split[1];
                            firstSubString = firstSubString.trim();
                            secondSubString = secondSubString.trim();
                            netweightingrams_bottomsheetDialog = secondSubString;

                        } else {
                            String[] split = netweight_bottomsheetDialog.split("-");
                            String firstSubString = split[0];
                            String secondSubString = split[1];
                            firstSubString = firstSubString.trim();
                            secondSubString = secondSubString.trim();
                            netweightingrams_bottomsheetDialog = secondSubString;


                        }


                    } else {


                        netweightingrams_bottomsheetDialog = netweight_bottomsheetDialog;


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    netweightingrams_bottomsheetDialog = netweightingrams_bottomsheetDialog.replaceAll("[^\\d.]", "");

                } catch (Exception e) {
                    netweightingrams_bottomsheetDialog = "";
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (!cutkey_bottomsheetDialog.equals(cutkey_bottomsheetDialog2)) {
                    if (!changeMenuItemWeightAndPriceSecondScreen.cutDetailsKey_arrayList.contains(cutkey_bottomsheetDialog2)) {
                        changeMenuItemWeightAndPriceSecondScreen.cutDetailsKey_arrayList.remove(cutkey_bottomsheetDialog);
                        changeMenuItemWeightAndPriceSecondScreen.cutDetailsKey_arrayList.add(cutkey_bottomsheetDialog2);
                        for (int i = 0; i < changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.size(); i++) {
                            String cutkeyfromCutdetailsarraylist = "";
                            cutkeyfromCutdetailsarraylist = changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).getCutkey().toString();
                            if (cutkeyfromCutdetailsarraylist.equals(cutkey_bottomsheetDialog)) {


                                changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setCutkey(cutkey_bottomsheetDialog2);
                                changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setNetweightingrams(netweightingrams_bottomsheetDialog);
                                changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setCutimagename(cutimagename_bottomsheetDialog);
                                changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setCutdisplayno(cutdisplayno_bottomsheetDialog);

                                changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setCutname(cutname_bottomsheetDialog);
                                changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setCutdesp(cutdesp_bottomsheetDialog);
                                changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setGrossweight(grossweight_bottomsheetDialog);
                                changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setNetweight(netweight_bottomsheetDialog);
                                changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setPortionsize(portionsize_bottomsheetDialog);
                                changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setIsdefault(isdefault_bottomsheetDialog);
                                notifyDataSetChanged();
                                bottomSheetDialog.cancel();


                            }


                        }
                    } else {
                        Toast.makeText(changeMenuItemWeightAndPriceSecondScreen, "This Cut item Already Added in the list  ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    for (int i = 0; i < changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.size(); i++) {
                        String cutkeyfromCutdetailsarraylist = "";
                        cutkeyfromCutdetailsarraylist = changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).getCutkey().toString();
                        if (cutkeyfromCutdetailsarraylist.equals(cutkey_bottomsheetDialog)) {

                            changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setCutkey(cutkey_bottomsheetDialog2);
                            changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setNetweightingrams(netweightingrams_bottomsheetDialog);
                            changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setCutimagename(cutimagename_bottomsheetDialog);
                            changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setCutdisplayno(cutdisplayno_bottomsheetDialog);
                            changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setCutname(cutname_bottomsheetDialog);
                            changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setCutdesp(cutdesp_bottomsheetDialog);
                            changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setGrossweight(grossweight_bottomsheetDialog);
                            changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setNetweight(netweight_bottomsheetDialog);
                            changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setPortionsize(portionsize_bottomsheetDialog);
                            changeMenuItemWeightAndPriceSecondScreen.cutDetailsArray.get(i).setIsdefault(isdefault_bottomsheetDialog);
                            notifyDataSetChanged();
                            bottomSheetDialog.cancel();


                        }


                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Nullable
    @Override
    public Modal_MenuItemCutDetails getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return super.getFilter();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
