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

public class Adapter_MenuItemWeightDetails extends ArrayAdapter<Modal_MenuItemWeightDetails> {

    ArrayList<String> weightDetailsKey_arrayList = new ArrayList<>();

    private final ArrayList<String> weightDetailsGrossweight_arrayList = new ArrayList<>();
    private ArrayAdapter weightDetailsName_aAdapter;
    String MenuItemWeightdetailsString="";
    List<Modal_MenuItemWeightDetails> MenuItemWeightDetailsArrayDefaultList = new ArrayList<>();

    String isDefaultString ="",grossweight ="",netweight="",portionsize ="",grossweightgrams="",positiontoSetSelectedValueinSpinner="";

    int check=0;
    boolean ifweightDetailSpinnerClicked =false;

    ChangeMenuItemWeightAndPriceSecondScreen changeMenuItemWeightAndPriceSecondScreen;
    List<Modal_MenuItemWeightDetails> weightDetailsArray  = new ArrayList<>();
    Context context;
    public static BottomSheetDialog bottomSheetDialog;
    Boolean isNetweight_singleEditText =false,isDefaultItem_bottomsheetDialog=false;
    String isdefault_bottomsheetDialog="", netweight_bottomsheetDialog="",netweightingrams_bottomsheetDialog="",portionsize_bottomsheetDialog="",
            grossweight_bottomsheetDialog="",weightkey_bottomsheetDialog="", Selected_weightkey_bottomsheetDialog ="",weightDetailDisplayno="";


    public Adapter_MenuItemWeightDetails(@NonNull Context context, List<Modal_MenuItemWeightDetails> weightDetailsArrayy, ChangeMenuItemWeightAndPriceSecondScreen changeMenuItemWeightAndPriceSecondScreenn, String MenuItemWeightdetailsStringg) {
        super(context, R.layout.menuitem_weightdetails, weightDetailsArrayy);
        this.MenuItemWeightdetailsString = MenuItemWeightdetailsStringg;
        this.weightDetailsArray=weightDetailsArrayy;
        this.changeMenuItemWeightAndPriceSecondScreen=changeMenuItemWeightAndPriceSecondScreenn;
        this.context=context;
        getMenuItemWeightDetailsFromSharedPreferences();
    }
    private void getMenuItemWeightDetailsFromSharedPreferences() {
        final SharedPreferences sharedPreferencesMenuitem = context.getSharedPreferences("MenuItemWeightDetails", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferencesMenuitem.getString("MenuItemWeightDetailsString", "");
        MenuItemWeightdetailsString = sharedPreferencesMenuitem.getString("MenuItemWeightDetailsString", "");
        if (json.isEmpty()) {
            Toast.makeText(context, "There is something error", Toast.LENGTH_LONG).show();
        } else {
            Type type = new TypeToken<List<Modal_MenuItemWeightDetails>>() {
            }.getType();
            MenuItemWeightDetailsArrayDefaultList = gson.fromJson(json, type);
        }
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.menuitem_weightdetails, parent, false
            );
        }
        Modal_MenuItemWeightDetails modal_menuItemWeightDetails = getItem(position);
        CheckBox isdefault_checkbox = convertView.findViewById(R.id.isdefault_checkbox);
        TextView grossweight_text_widget = convertView.findViewById(R.id.grossweight_text_widget);
        TextView netweight_textview_widget = convertView.findViewById(R.id.netweight_textview_widget);
        TextView portionsize_textview_widget = convertView.findViewById(R.id.portionsize_textview_widget);
        LinearLayout edit_weightdetails_layout = convertView.findViewById(R.id.edit_weightdetails_layout);
        LinearLayout isDefaultCheckboxLayout = convertView.findViewById(R.id.isDefaultCheckboxLayout);


        edit_weightdetails_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                bottomSheetDialog = new BottomSheetDialog(context);
                bottomSheetDialog.setContentView(R.layout.menuitemcut_weightdetailseditactivity);
                bottomSheetDialog.setCanceledOnTouchOutside(true);

                LinearLayout weightDetailsLayout =  bottomSheetDialog.findViewById(R.id.weightDetailsLayout);
                CheckBox weightDetails_checkbox = bottomSheetDialog.findViewById(R.id.weightDetails_checkbox);
                EditText edit_grossweight = bottomSheetDialog.findViewById(R.id.edit_grossweight);
                EditText edit_netweight = bottomSheetDialog.findViewById(R.id.edit_netweight_weightDetails);
                EditText edit_portionsize = bottomSheetDialog.findViewById(R.id.edit_portionsize_weightDetails);
                Button saveWeightDetails_button = bottomSheetDialog.findViewById(R.id.saveWeightDetails_button);

                LinearLayout weightDetailsCheckbox_LinearLayout = bottomSheetDialog.findViewById(R.id.weightDetailsCheckbox_LinearLayout);
                LinearLayout cutDetailsLayout =  bottomSheetDialog.findViewById(R.id.cutDetailsLayout);
                LinearLayout two_weightdetails_editbox_linearlayout =  bottomSheetDialog.findViewById(R.id.two_weightdetails_editbox_linearlayout);
                EditText weightDetails_netweight_first_textview_widget = bottomSheetDialog.findViewById(R.id.weightDetails_netweight_first_textview_widget);
                EditText weightDetails_netweight_second_textview_widget = bottomSheetDialog.findViewById(R.id.weightDetails_netweight_second_textview_widget);

                Objects.requireNonNull(two_weightdetails_editbox_linearlayout).setVisibility(View.VISIBLE);
                Objects.requireNonNull(edit_netweight).setVisibility(View.GONE);
                Spinner weightDetailsName_spinner = bottomSheetDialog.findViewById(R.id.weightDetailsName_spinner);


                assert cutDetailsLayout != null;
                cutDetailsLayout.setVisibility(View.GONE);
                Objects.requireNonNull(weightDetailsLayout).setVisibility(View.VISIBLE);






                weightDetailsName_spinner.setVisibility(View.VISIBLE);
                edit_grossweight.setVisibility(View.GONE);



                for(int i=0;i<  MenuItemWeightDetailsArrayDefaultList.size();i++){
                    Modal_MenuItemWeightDetails modal_menuItemWeightDetails = MenuItemWeightDetailsArrayDefaultList.get(i);
                    String portionsize = "",netweight = "",grossweight = "" ;

                    try{
                        portionsize = modal_menuItemWeightDetails.getPortionsize().toString();

                    }
                    catch (Exception  e){
                        e.printStackTrace();
                    }


                    try{
                        grossweight  = modal_menuItemWeightDetails.getGrossweight().toString();

                    }
                    catch (Exception  e){
                        e.printStackTrace();
                    }


                    try{
                        netweight  = modal_menuItemWeightDetails.getNetweight().toString();

                    }
                    catch (Exception  e){
                        e.printStackTrace();
                    }





                    try {
                        if (!weightDetailsGrossweight_arrayList.contains(grossweight)) {
                            weightDetailsGrossweight_arrayList.add(grossweight);
                        }

                        weightDetailsName_aAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, weightDetailsGrossweight_arrayList);
                        Objects.requireNonNull(weightDetailsName_spinner).setAdapter(weightDetailsName_aAdapter);


                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }


                try{
                    isDefaultItem_bottomsheetDialog = Boolean.valueOf(Objects.requireNonNull(getItem(position)).getIsdefault());

                }
                catch (Exception e){
                    isDefaultItem_bottomsheetDialog=false;
                    e.printStackTrace();
                }

                    try{
                        weightDetailDisplayno  = String.valueOf(Objects.requireNonNull(getItem(position)).getWeightdisplayno());

                    }
                    catch (Exception  e){
                        weightDetailDisplayno ="";
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
                    weightkey_bottomsheetDialog = String.valueOf(Objects.requireNonNull(getItem(position)).getWeightkey());
                }
                   catch (Exception e)
                   {
                    e.printStackTrace();
                }

                try{
                    positiontoSetSelectedValueinSpinner = getMenuItemCutDataPosition(weightkey_bottomsheetDialog);
                }
                catch (Exception e){
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
                    Objects.requireNonNull(edit_grossweight).setText(grossweight_bottomsheetDialog);

                }
                catch (Exception e){
                    e.printStackTrace();
                }

                try{

                    if(netweight_bottomsheetDialog.contains("to")||netweight_bottomsheetDialog.contains("-")){
                        two_weightdetails_editbox_linearlayout.setVisibility(View.VISIBLE);
                        edit_netweight.setVisibility(View.GONE);
                        isNetweight_singleEditText = false;



                        if(netweight_bottomsheetDialog.contains("to")){
                            String[] split = netweight_bottomsheetDialog.split("to");
                            String firstSubString = split[0];
                            String secondSubString = split[1];
                            firstSubString = firstSubString.trim();
                            secondSubString = secondSubString.trim();
                            weightDetails_netweight_first_textview_widget.setText(firstSubString);
                            weightDetails_netweight_second_textview_widget.setText(secondSubString);
                        }
                        else{
                            String[] split = netweight_bottomsheetDialog.split("-");
                            String firstSubString = split[0];
                            String secondSubString = split[1];
                            firstSubString = firstSubString.trim();
                            secondSubString = secondSubString.trim();
                            weightDetails_netweight_first_textview_widget.setText(firstSubString);
                            weightDetails_netweight_second_textview_widget.setText(secondSubString);


                        }


                    }
                    else {
                        two_weightdetails_editbox_linearlayout.setVisibility(View.GONE);
                        edit_netweight.setVisibility(View.VISIBLE);
                        isNetweight_singleEditText = true;


                        edit_netweight.setText(netweight_bottomsheetDialog);

                    }



                }
                catch (Exception e){
                    e.printStackTrace();
                }



                try{
                    Objects.requireNonNull(edit_netweight).setText(netweight_bottomsheetDialog);

                }
                catch (Exception e){
                    e.printStackTrace();
                }



                try{
                    Objects.requireNonNull(edit_portionsize).setText(portionsize_bottomsheetDialog);

                }
                catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    Objects.requireNonNull(weightDetails_checkbox).setChecked(isDefaultItem_bottomsheetDialog);

                }
                catch (Exception e){
                    e.printStackTrace();
                }


                Objects.requireNonNull(weightDetailsCheckbox_LinearLayout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(getCount()>1){
                            if(Objects.requireNonNull(weightDetails_checkbox).isChecked()){
                                weightDetails_checkbox.setChecked(false);

                            }
                            else{
                                weightDetails_checkbox.setChecked(true);

                            }
                        }
                        else{
                            Objects.requireNonNull(weightDetails_checkbox).setChecked(true);
                            Toast.makeText(context, "First Additional weight should be default one", Toast.LENGTH_SHORT).show();

                        }

                    }
                });


                weightDetailsName_spinner.setSelection(Integer.parseInt(positiontoSetSelectedValueinSpinner),false);



                Objects.requireNonNull(weightDetailsName_spinner).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        if(++check >= 1) {
                            ifweightDetailSpinnerClicked = true;


                        }
                        else{
                            ifweightDetailSpinnerClicked = false;

                        }
                        String grossweight = getMenuItemWeightData(position,"grossweight");
                        String grossweightingrams = getMenuItemWeightData(position,"grossweightingrams");
                        String netweight = getMenuItemWeightData(position,"netweight");
                        String weightkey = getMenuItemWeightData(position,"weightkey");
                        String weight = getMenuItemWeightData(position,"weight");

                        String displayno = getMenuItemWeightData(position,"weightdisplayno");
                        String portionsize = getMenuItemWeightData(position,"portionsize");



                        Selected_weightkey_bottomsheetDialog = getMenuItemWeightData(position,"weightkey");


                        try{
                            Objects.requireNonNull(edit_grossweight).setText(grossweight);

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                        try{
                            weightDetailDisplayno = String.valueOf(displayno);

                        }
                        catch (Exception e){
                            weightDetailDisplayno = "0";
                            e.printStackTrace();
                        }


                        try{
                            Objects.requireNonNull(weightDetails_netweight_first_textview_widget).setText(netweight);

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                        try{
                            Objects.requireNonNull(weightDetails_netweight_second_textview_widget).setText(netweight);

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }


                        try{
                            Objects.requireNonNull(edit_portionsize).setText(portionsize);

                        }
                        catch (Exception e){
                            e.printStackTrace();




                        }






                        try {

                            if (netweight.contains("to") || netweight.contains("-")) {

                                Objects.requireNonNull(two_weightdetails_editbox_linearlayout).setVisibility(View.VISIBLE);
                                Objects.requireNonNull(edit_netweight).setVisibility(View.GONE);

                                if (netweight.contains("to")) {
                                    String[] split = netweight.split("to");
                                    String firstSubString = split[0];
                                    String secondSubString = split[1];
                                    firstSubString = firstSubString.trim();
                                    secondSubString = secondSubString.trim();
                                    weightDetails_netweight_first_textview_widget.setText(firstSubString);
                                    weightDetails_netweight_second_textview_widget.setText(secondSubString);
                                } else {
                                    String[] split = netweight.split("-");
                                    String firstSubString = split[0];
                                    String secondSubString = split[1];
                                    firstSubString = firstSubString.trim();
                                    secondSubString = secondSubString.trim();
                                    weightDetails_netweight_first_textview_widget.setText(firstSubString);
                                    weightDetails_netweight_second_textview_widget.setText(secondSubString);


                                }


                            } else {
                                Objects.requireNonNull(two_weightdetails_editbox_linearlayout).setVisibility(View.GONE);
                                Objects.requireNonNull(edit_netweight).setVisibility(View.VISIBLE);


                                edit_netweight.setText(netweight);

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





                Objects.requireNonNull(saveWeightDetails_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        grossweight_bottomsheetDialog = Objects.requireNonNull(edit_grossweight).getText().toString();
                        grossweight_bottomsheetDialog = grossweight_bottomsheetDialog.replaceAll("[^\\d.]", "");

                        portionsize_bottomsheetDialog = Objects.requireNonNull(edit_portionsize).getText().toString();

                        isdefault_bottomsheetDialog = String.valueOf(Objects.requireNonNull(weightDetails_checkbox).isChecked()).toUpperCase();
                        netweight_bottomsheetDialog = Objects.requireNonNull(edit_netweight).getText().toString();

                        if(!Objects.requireNonNull(edit_grossweight).getText().toString().equals("null") && !Objects.requireNonNull(edit_grossweight).getText().toString().equals("")){
                            if((!Objects.requireNonNull(weightDetails_netweight_first_textview_widget).getText().toString().equals("null") && !Objects.requireNonNull(weightDetails_netweight_first_textview_widget).getText().toString().equals(""))&&(!Objects.requireNonNull(weightDetails_netweight_second_textview_widget).getText().toString().equals("null") && !Objects.requireNonNull(weightDetails_netweight_second_textview_widget).getText().toString().equals(""))){

                         try{
                        if (isNetweight_singleEditText) {
                            try {
                                if (netweight_bottomsheetDialog.contains("-")) {


                                    try {
                                        String[] split = netweight_bottomsheetDialog.split("-");
                                        String firstSubString = split[0];
                                        String secondSubString = split[1];
                                        firstSubString = firstSubString.trim();
                                        firstSubString = firstSubString.replaceAll("[^\\d.]", "");

                                        secondSubString = secondSubString.trim();
                                        secondSubString = secondSubString.replaceAll("[^\\d.]", "");

                                        netweight_bottomsheetDialog = firstSubString + "g - " + secondSubString ;

                                        //configureDatabeforeAdd(grossweight_bottomsheetDialog, netweight_bottomsheetDialog, portionsize_bottomsheetDialog, isdefault_bottomsheetDialog, weightkey_bottomsheetDialog, Selected_weightkey_bottomsheetDialog);
                                        changeDataInLocalArray(grossweight_bottomsheetDialog, netweight_bottomsheetDialog, portionsize_bottomsheetDialog, isdefault_bottomsheetDialog, weightkey_bottomsheetDialog, Selected_weightkey_bottomsheetDialog);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        netweight_bottomsheetDialog = Objects.requireNonNull(edit_netweight).getText().toString();
                                        changeDataInLocalArray(grossweight_bottomsheetDialog, netweight_bottomsheetDialog, portionsize_bottomsheetDialog, isdefault_bottomsheetDialog, weightkey_bottomsheetDialog, Selected_weightkey_bottomsheetDialog);
                                       // configureDatabeforeAdd(grossweight_bottomsheetDialog, netweight_bottomsheetDialog, portionsize_bottomsheetDialog, isdefault_bottomsheetDialog, weightkey_bottomsheetDialog, Selected_weightkey_bottomsheetDialog);

                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            String firstnetweightfromString = "", secondnetweightfromString = "";
                            try {
                                firstnetweightfromString = weightDetails_netweight_first_textview_widget.getText().toString();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                secondnetweightfromString = weightDetails_netweight_second_textview_widget.getText().toString();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            String netweightfromString = "";

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

                                netweightfromString = firstnetweightfromString + "g - " + secondnetweightfromString ;

                                netweight_bottomsheetDialog = netweightfromString;
                                if(ifweightDetailSpinnerClicked) {
                                    changeDataInLocalArray(grossweight_bottomsheetDialog, netweight_bottomsheetDialog, portionsize_bottomsheetDialog, isdefault_bottomsheetDialog, weightkey_bottomsheetDialog, Selected_weightkey_bottomsheetDialog);
                                   // configureDatabeforeAdd(grossweight_bottomsheetDialog, netweight_bottomsheetDialog, portionsize_bottomsheetDialog, isdefault_bottomsheetDialog, weightkey_bottomsheetDialog, Selected_weightkey_bottomsheetDialog);

                                }
                                else{
                                    changeDataInLocalArray(grossweight_bottomsheetDialog, netweight_bottomsheetDialog, portionsize_bottomsheetDialog, isdefault_bottomsheetDialog, weightkey_bottomsheetDialog,weightkey_bottomsheetDialog);
                                   // configureDatabeforeAdd(grossweight_bottomsheetDialog, netweight_bottomsheetDialog, portionsize_bottomsheetDialog, isdefault_bottomsheetDialog, weightkey_bottomsheetDialog,weightkey_bottomsheetDialog);

                                }

                            }


                        }


                    }catch (Exception e) {
                            e.printStackTrace();
                        }
                            }
                            else
                            {
                                Toast.makeText(context, "NetWeight can't be Empty", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                        {
                            Toast.makeText(context, "Grossweight can't be Empty", Toast.LENGTH_SHORT).show();
                        }

                    }
                });



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
             isDefaultString = modal_menuItemWeightDetails.getIsdefault().toString().toUpperCase();

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            grossweight = getItem(position).getGrossweight().toString();

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            netweight = getItem(position).getNetweight().toString();

        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            portionsize = getItem(position).getPortionsize().toString();

        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            isDefaultString = getItem(position).getIsdefault().toString().toUpperCase();

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            grossweightgrams = getItem(position).getGrossweightingrams().toString();

        }
        catch (Exception e){
            e.printStackTrace();
        }


    try{
        if(isDefaultString.equals("TRUE")){
            isdefault_checkbox.setChecked(true);
        }
        else if(isDefaultString.equals("FALSE")){
            isdefault_checkbox.setChecked(false);

        }
    }
    catch (Exception e){
        e.printStackTrace();
    }



        try{
          grossweight_text_widget.setText(grossweight);
        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            netweight_textview_widget.setText(netweight);
        }
        catch (Exception e){
            e.printStackTrace();
        }



        try{
            portionsize_textview_widget.setText(portionsize);
        }
        catch (Exception e){
            e.printStackTrace();
        }



        return convertView;
    }


    private String getMenuItemWeightData(int position, String fieldName) {
        String data="";
        try {
            JSONArray jsonArray = new JSONArray(MenuItemWeightdetailsString);
            JSONObject json = jsonArray.getJSONObject(position);
            data = json.getString(fieldName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    private String getMenuItemCutDataPosition(String fieldName) {
        String data="";
        try {
            JSONArray jsonArray = new JSONArray(MenuItemWeightdetailsString);
            for(int i = 0;i<jsonArray.length();i++){
                JSONObject json = jsonArray.getJSONObject(i);
                String key = json.getString("weightkey");
                if(key.equals(fieldName)){
                    data = String.valueOf(i);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void configureDatabeforeAdd(String grossweight_bottomsheetDialog, String netweight_bottomsheetDialog, String portionsize_bottomsheetDialog, String isdefault_bottomsheetDialog, String weightkey_bottomsheetDialog, String selected_weightkey_bottomsheetDialog) {
        boolean isArrayContainsdefault = false;
        if (isdefault_bottomsheetDialog.toUpperCase().equals("TRUE")) {
            for (int i = 0; i < changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.size(); i++) {
                String isdefaultCutdetailsarraylist = "";
                isdefaultCutdetailsarraylist = changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).getIsdefault().toString();
                if (isdefaultCutdetailsarraylist.toUpperCase().equals("TRUE")) {
                    isArrayContainsdefault = true;
                }
            }
            if (isArrayContainsdefault) {
                Toast.makeText(changeMenuItemWeightAndPriceSecondScreen, "Already Have an default item in Array", Toast.LENGTH_SHORT).show();
            } else {
                  changeDataInLocalArray(grossweight_bottomsheetDialog, netweight_bottomsheetDialog, portionsize_bottomsheetDialog, isdefault_bottomsheetDialog, weightkey_bottomsheetDialog, selected_weightkey_bottomsheetDialog);

            }


        }
    }

    private void changeDataInLocalArray(String grossweight_bottomsheetDialog, String netweight_bottomsheetDialog, String portionsize_bottomsheetDialog, String isdefault_bottomsheetDialog, String weightkeyOld_bottomsheetDialog, String weightkeyNew_bottomsheetDialog2) {
        boolean isArrayContainsdefault = false;
        if (isdefault_bottomsheetDialog.toUpperCase().equals("TRUE")) {
            for (int i = 0; i < changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.size(); i++) {
                String isdefaultCutdetailsarraylist = "";
                isdefaultCutdetailsarraylist = changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).getIsdefault().toString();
                if (isdefaultCutdetailsarraylist.toUpperCase().equals("TRUE")) {
                    if (changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.size()>1) {

                        isArrayContainsdefault = true;
                    }
                }
            }
            if (isArrayContainsdefault) {
                Toast.makeText(changeMenuItemWeightAndPriceSecondScreen, "Already Have an default item in Array", Toast.LENGTH_SHORT).show();
            } else {


        if ((changeMenuItemWeightAndPriceSecondScreen.netweight.toUpperCase().contains("PCS")) || (changeMenuItemWeightAndPriceSecondScreen.netweight.toUpperCase().contains("PC"))) {
            try {

                netweight_bottomsheetDialog = String.valueOf(netweight_bottomsheetDialog + "Pcs");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if ((changeMenuItemWeightAndPriceSecondScreen.netweight.toUpperCase().contains("G")) || (changeMenuItemWeightAndPriceSecondScreen.netweight.toUpperCase().contains("GRAMS")) || (changeMenuItemWeightAndPriceSecondScreen.netweight.toUpperCase().contains("GMS"))) {
            try {
                netweight_bottomsheetDialog = String.valueOf(netweight_bottomsheetDialog + "g");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if ((changeMenuItemWeightAndPriceSecondScreen.grossweight.toUpperCase().contains("G")) || (changeMenuItemWeightAndPriceSecondScreen.grossweight.toUpperCase().contains("GRAMS")) || (changeMenuItemWeightAndPriceSecondScreen.grossweight.toUpperCase().contains("GMS"))) {
            try {
                grossweight_bottomsheetDialog = String.valueOf(grossweight_bottomsheetDialog + "g");


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if ((changeMenuItemWeightAndPriceSecondScreen.portionsize.toUpperCase().contains("PCS")) || (changeMenuItemWeightAndPriceSecondScreen.portionsize.toUpperCase().contains("PC"))) {
            try {
                if (!portionsize_bottomsheetDialog.equals("")) {

                    portionsize_bottomsheetDialog = String.valueOf(portionsize_bottomsheetDialog);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ((changeMenuItemWeightAndPriceSecondScreen.portionsize.toUpperCase().contains("G")) || (changeMenuItemWeightAndPriceSecondScreen.portionsize.toUpperCase().contains("GRAMS")) || (changeMenuItemWeightAndPriceSecondScreen.portionsize.toUpperCase().contains("GMS"))) {
            try {
                if (!portionsize_bottomsheetDialog.equals("")) {

                    portionsize_bottomsheetDialog = String.valueOf(portionsize_bottomsheetDialog);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            portionsize_bottomsheetDialog.trim();
            if (!portionsize_bottomsheetDialog.equals("")) {

                portionsize_bottomsheetDialog = String.valueOf(portionsize_bottomsheetDialog);
            }
        }


        String grossweightingrams_bottomsheetDialog = "", netweightingrams_bottomsheetDialog = "";


        try {
            grossweightingrams_bottomsheetDialog = String.valueOf(grossweight_bottomsheetDialog);
            grossweightingrams_bottomsheetDialog = grossweightingrams_bottomsheetDialog.replaceAll("[^\\d.]", "");

        } catch (Exception e) {
            grossweightingrams_bottomsheetDialog = "";
            e.printStackTrace();
        }


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


        try {
            if (!weightkeyOld_bottomsheetDialog.equals(weightkeyNew_bottomsheetDialog2)) {
                if (!changeMenuItemWeightAndPriceSecondScreen.weightDetailsKey_arrayList.contains(weightkeyNew_bottomsheetDialog2)) {
                    changeMenuItemWeightAndPriceSecondScreen.weightDetailsKey_arrayList.remove(weightkeyOld_bottomsheetDialog);
                    changeMenuItemWeightAndPriceSecondScreen.weightDetailsKey_arrayList.add(weightkeyNew_bottomsheetDialog2);
                    for (int i = 0; i < changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.size(); i++) {
                        String cutkeyfromCutdetailsarraylist = "";
                        cutkeyfromCutdetailsarraylist = changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).getWeightkey().toString();
                        if (cutkeyfromCutdetailsarraylist.equals(weightkeyOld_bottomsheetDialog)) {

                            changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setGrossweightingrams(grossweightingrams_bottomsheetDialog);
                            changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setWeightdisplayno(weightDetailDisplayno);
                            changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setNetweightingrams(netweightingrams_bottomsheetDialog);
                            changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setWeight(grossweight_bottomsheetDialog);

                            changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setWeightkey(weightkeyNew_bottomsheetDialog2);
                            changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setGrossweight(grossweight_bottomsheetDialog);
                            changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setNetweight(netweight_bottomsheetDialog);
                            changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setPortionsize(portionsize_bottomsheetDialog);
                            changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setIsdefault(isdefault_bottomsheetDialog);
                            try {
                                if (isdefault_bottomsheetDialog.toUpperCase().equals("TRUE")) {


                                    try {
                                        changeMenuItemWeightAndPriceSecondScreen.grossweight = String.valueOf(grossweight_bottomsheetDialog);

                                    } catch (Exception e) {
                                        changeMenuItemWeightAndPriceSecondScreen.grossweight = "";
                                        e.printStackTrace();
                                    }

                                    try {

                                        changeMenuItemWeightAndPriceSecondScreen.grossweightingrams = String.valueOf(grossweightingrams_bottomsheetDialog);

                                    } catch (Exception e) {
                                        changeMenuItemWeightAndPriceSecondScreen.grossweightingrams = "";
                                        e.printStackTrace();
                                    }


                                    try {
                                        changeMenuItemWeightAndPriceSecondScreen.portionsize = String.valueOf(portionsize_bottomsheetDialog);

                                    } catch (Exception e) {
                                        changeMenuItemWeightAndPriceSecondScreen.portionsize = "";
                                        e.printStackTrace();
                                    }

                                    try {
                                        changeMenuItemWeightAndPriceSecondScreen.netweight = String.valueOf(netweight_bottomsheetDialog);

                                    } catch (Exception e) {
                                        changeMenuItemWeightAndPriceSecondScreen.netweight = "";
                                        e.printStackTrace();
                                    }


                                    try {
                                        notifyDataSetChanged();
                                        bottomSheetDialog.cancel();

                                        changeMenuItemWeightAndPriceSecondScreen.FormatAndDisplaytheDataa();
                                        ;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                    try {
                                        changeMenuItemWeightAndPriceSecondScreen.isPosPrice_PricePerKgChanged = false;
                                        changeMenuItemWeightAndPriceSecondScreen.isAppPrice_PricePerKgChanged = true;
                                        changeMenuItemWeightAndPriceSecondScreen.computeAppandPosPrice.performClick();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                } else {


                                    changeMenuItemWeightAndPriceSecondScreen.FormatAndDisplaytheDataa();

                                    bottomSheetDialog.cancel();

                                    notifyDataSetChanged();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }


                    }
                } else {
                    Toast.makeText(changeMenuItemWeightAndPriceSecondScreen, "This Weight item Already Added in the list  ", Toast.LENGTH_SHORT).show();
                }
            } else {
                for (int i = 0; i < changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.size(); i++) {
                    String weightkeyfromWeightdetailsarraylist = "";
                    weightkeyfromWeightdetailsarraylist = changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).getWeightkey().toString();
                    if (weightkeyfromWeightdetailsarraylist.equals(weightkeyOld_bottomsheetDialog)) {

                        changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setGrossweightingrams(grossweightingrams_bottomsheetDialog);
                        changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setWeightdisplayno(weightDetailDisplayno);
                        changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setNetweightingrams(netweightingrams_bottomsheetDialog);
                        changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setWeight(grossweight_bottomsheetDialog);

                        changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setWeightkey(weightkeyNew_bottomsheetDialog2);
                        changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setGrossweight(grossweight_bottomsheetDialog);
                        changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setNetweight(netweight_bottomsheetDialog);
                        changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setPortionsize(portionsize_bottomsheetDialog);
                        changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setIsdefault(isdefault_bottomsheetDialog);
                        try {
                            if (isdefault_bottomsheetDialog.toUpperCase().equals("TRUE")) {
                                try {
                                    changeMenuItemWeightAndPriceSecondScreen.grossweight = String.valueOf(grossweight_bottomsheetDialog);

                                } catch (Exception e) {
                                    changeMenuItemWeightAndPriceSecondScreen.grossweight = "";
                                    e.printStackTrace();
                                }
                                try {

                                    changeMenuItemWeightAndPriceSecondScreen.grossweightingrams = String.valueOf(grossweightingrams_bottomsheetDialog);

                                } catch (Exception e) {
                                    changeMenuItemWeightAndPriceSecondScreen.grossweightingrams = "";
                                    e.printStackTrace();
                                }


                                try {
                                    changeMenuItemWeightAndPriceSecondScreen.portionsize = String.valueOf(portionsize_bottomsheetDialog);

                                } catch (Exception e) {
                                    changeMenuItemWeightAndPriceSecondScreen.portionsize = "";
                                    e.printStackTrace();
                                }

                                try {
                                    changeMenuItemWeightAndPriceSecondScreen.netweight = String.valueOf(netweight_bottomsheetDialog);

                                } catch (Exception e) {
                                    changeMenuItemWeightAndPriceSecondScreen.netweight = "";
                                    e.printStackTrace();
                                }
                                try {
                                    notifyDataSetChanged();
                                    bottomSheetDialog.cancel();

                                    changeMenuItemWeightAndPriceSecondScreen.FormatAndDisplaytheDataa();
                                    ;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                try {
                                    changeMenuItemWeightAndPriceSecondScreen.isPosPrice_PricePerKgChanged = false;
                                    changeMenuItemWeightAndPriceSecondScreen.isAppPrice_PricePerKgChanged = true;
                                    changeMenuItemWeightAndPriceSecondScreen.computeAppandPosPrice.performClick();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {


                                changeMenuItemWeightAndPriceSecondScreen.FormatAndDisplaytheDataa();

                                notifyDataSetChanged();
                                bottomSheetDialog.cancel();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }



            }


        }
        else{
            if ((changeMenuItemWeightAndPriceSecondScreen.netweight.toUpperCase().contains("PCS")) || (changeMenuItemWeightAndPriceSecondScreen.netweight.toUpperCase().contains("PC"))) {
                try {

                    netweight_bottomsheetDialog = String.valueOf(netweight_bottomsheetDialog + "Pcs");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if ((changeMenuItemWeightAndPriceSecondScreen.netweight.toUpperCase().contains("G")) || (changeMenuItemWeightAndPriceSecondScreen.netweight.toUpperCase().contains("GRAMS")) || (changeMenuItemWeightAndPriceSecondScreen.netweight.toUpperCase().contains("GMS"))) {
                try {
                    netweight_bottomsheetDialog = String.valueOf(netweight_bottomsheetDialog + "g");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if ((changeMenuItemWeightAndPriceSecondScreen.grossweight.toUpperCase().contains("G")) || (changeMenuItemWeightAndPriceSecondScreen.grossweight.toUpperCase().contains("GRAMS")) || (changeMenuItemWeightAndPriceSecondScreen.grossweight.toUpperCase().contains("GMS"))) {
                try {
                    grossweight_bottomsheetDialog = String.valueOf(grossweight_bottomsheetDialog + "g");


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if ((changeMenuItemWeightAndPriceSecondScreen.portionsize.toUpperCase().contains("PCS")) || (changeMenuItemWeightAndPriceSecondScreen.portionsize.toUpperCase().contains("PC"))) {
                try {
                    if (!portionsize_bottomsheetDialog.equals("")) {

                        portionsize_bottomsheetDialog = String.valueOf(portionsize_bottomsheetDialog);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ((changeMenuItemWeightAndPriceSecondScreen.portionsize.toUpperCase().contains("G")) || (changeMenuItemWeightAndPriceSecondScreen.portionsize.toUpperCase().contains("GRAMS")) || (changeMenuItemWeightAndPriceSecondScreen.portionsize.toUpperCase().contains("GMS"))) {
                try {
                    if (!portionsize_bottomsheetDialog.equals("")) {

                        portionsize_bottomsheetDialog = String.valueOf(portionsize_bottomsheetDialog);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                portionsize_bottomsheetDialog.trim();
                if (!portionsize_bottomsheetDialog.equals("")) {

                    portionsize_bottomsheetDialog = String.valueOf(portionsize_bottomsheetDialog);
                }
            }


            String grossweightingrams_bottomsheetDialog = "", netweightingrams_bottomsheetDialog = "";


            try {
                grossweightingrams_bottomsheetDialog = String.valueOf(grossweight_bottomsheetDialog);
                grossweightingrams_bottomsheetDialog = grossweightingrams_bottomsheetDialog.replaceAll("[^\\d.]", "");

            } catch (Exception e) {
                grossweightingrams_bottomsheetDialog = "";
                e.printStackTrace();
            }


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


            try {
                if (!weightkeyOld_bottomsheetDialog.equals(weightkeyNew_bottomsheetDialog2)) {
                    if (!changeMenuItemWeightAndPriceSecondScreen.weightDetailsKey_arrayList.contains(weightkeyNew_bottomsheetDialog2)) {
                        changeMenuItemWeightAndPriceSecondScreen.weightDetailsKey_arrayList.remove(weightkeyOld_bottomsheetDialog);
                        changeMenuItemWeightAndPriceSecondScreen.weightDetailsKey_arrayList.add(weightkeyNew_bottomsheetDialog2);
                        for (int i = 0; i < changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.size(); i++) {
                            String cutkeyfromCutdetailsarraylist = "";
                            cutkeyfromCutdetailsarraylist = changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).getWeightkey().toString();
                            if (cutkeyfromCutdetailsarraylist.equals(weightkeyOld_bottomsheetDialog)) {

                                changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setGrossweightingrams(grossweightingrams_bottomsheetDialog);
                                changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setWeightdisplayno(weightDetailDisplayno);
                                changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setNetweightingrams(netweightingrams_bottomsheetDialog);
                                changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setWeight(grossweight_bottomsheetDialog);

                                changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setWeightkey(weightkeyNew_bottomsheetDialog2);
                                changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setGrossweight(grossweight_bottomsheetDialog);
                                changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setNetweight(netweight_bottomsheetDialog);
                                changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setPortionsize(portionsize_bottomsheetDialog);
                                changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setIsdefault(isdefault_bottomsheetDialog);
                                try {
                                    if (isdefault_bottomsheetDialog.toUpperCase().equals("TRUE")) {


                                        try {
                                            changeMenuItemWeightAndPriceSecondScreen.grossweight = String.valueOf(grossweight_bottomsheetDialog);

                                        } catch (Exception e) {
                                            changeMenuItemWeightAndPriceSecondScreen.grossweight = "";
                                            e.printStackTrace();
                                        }

                                        try {

                                            changeMenuItemWeightAndPriceSecondScreen.grossweightingrams = String.valueOf(grossweightingrams_bottomsheetDialog);

                                        } catch (Exception e) {
                                            changeMenuItemWeightAndPriceSecondScreen.grossweightingrams = "";
                                            e.printStackTrace();
                                        }


                                        try {
                                            changeMenuItemWeightAndPriceSecondScreen.portionsize = String.valueOf(portionsize_bottomsheetDialog);

                                        } catch (Exception e) {
                                            changeMenuItemWeightAndPriceSecondScreen.portionsize = "";
                                            e.printStackTrace();
                                        }

                                        try {
                                            changeMenuItemWeightAndPriceSecondScreen.netweight = String.valueOf(netweight_bottomsheetDialog);

                                        } catch (Exception e) {
                                            changeMenuItemWeightAndPriceSecondScreen.netweight = "";
                                            e.printStackTrace();
                                        }


                                        try {
                                            notifyDataSetChanged();
                                            bottomSheetDialog.cancel();

                                            changeMenuItemWeightAndPriceSecondScreen.FormatAndDisplaytheDataa();
                                            ;
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                        try {
                                            changeMenuItemWeightAndPriceSecondScreen.isPosPrice_PricePerKgChanged = false;
                                            changeMenuItemWeightAndPriceSecondScreen.isAppPrice_PricePerKgChanged = true;
                                            changeMenuItemWeightAndPriceSecondScreen.computeAppandPosPrice.performClick();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    } else {


                                        changeMenuItemWeightAndPriceSecondScreen.FormatAndDisplaytheDataa();

                                        bottomSheetDialog.cancel();

                                        notifyDataSetChanged();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }


                        }
                    } else {
                        Toast.makeText(changeMenuItemWeightAndPriceSecondScreen, "This Weight item Already Added in the list  ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    for (int i = 0; i < changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.size(); i++) {
                        String weightkeyfromWeightdetailsarraylist = "";
                        weightkeyfromWeightdetailsarraylist = changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).getWeightkey().toString();
                        if (weightkeyfromWeightdetailsarraylist.equals(weightkeyOld_bottomsheetDialog)) {

                            changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setGrossweightingrams(grossweightingrams_bottomsheetDialog);
                            changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setWeightdisplayno(weightDetailDisplayno);
                            changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setNetweightingrams(netweightingrams_bottomsheetDialog);
                            changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setWeight(grossweight_bottomsheetDialog);

                            changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setWeightkey(weightkeyNew_bottomsheetDialog2);
                            changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setGrossweight(grossweight_bottomsheetDialog);
                            changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setNetweight(netweight_bottomsheetDialog);
                            changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setPortionsize(portionsize_bottomsheetDialog);
                            changeMenuItemWeightAndPriceSecondScreen.weightDetailsArray.get(i).setIsdefault(isdefault_bottomsheetDialog);
                            try {
                                if (isdefault_bottomsheetDialog.toUpperCase().equals("TRUE")) {
                                    try {
                                        changeMenuItemWeightAndPriceSecondScreen.grossweight = String.valueOf(grossweight_bottomsheetDialog);

                                    } catch (Exception e) {
                                        changeMenuItemWeightAndPriceSecondScreen.grossweight = "";
                                        e.printStackTrace();
                                    }
                                    try {

                                        changeMenuItemWeightAndPriceSecondScreen.grossweightingrams = String.valueOf(grossweightingrams_bottomsheetDialog);

                                    } catch (Exception e) {
                                        changeMenuItemWeightAndPriceSecondScreen.grossweightingrams = "";
                                        e.printStackTrace();
                                    }


                                    try {
                                        changeMenuItemWeightAndPriceSecondScreen.portionsize = String.valueOf(portionsize_bottomsheetDialog);

                                    } catch (Exception e) {
                                        changeMenuItemWeightAndPriceSecondScreen.portionsize = "";
                                        e.printStackTrace();
                                    }

                                    try {
                                        changeMenuItemWeightAndPriceSecondScreen.netweight = String.valueOf(netweight_bottomsheetDialog);

                                    } catch (Exception e) {
                                        changeMenuItemWeightAndPriceSecondScreen.netweight = "";
                                        e.printStackTrace();
                                    }
                                    try {
                                        notifyDataSetChanged();
                                        bottomSheetDialog.cancel();

                                        changeMenuItemWeightAndPriceSecondScreen.FormatAndDisplaytheDataa();
                                        ;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                    try {
                                        changeMenuItemWeightAndPriceSecondScreen.isPosPrice_PricePerKgChanged = false;
                                        changeMenuItemWeightAndPriceSecondScreen.isAppPrice_PricePerKgChanged = true;
                                        changeMenuItemWeightAndPriceSecondScreen.computeAppandPosPrice.performClick();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }
                                else {


                                    changeMenuItemWeightAndPriceSecondScreen.FormatAndDisplaytheDataa();

                                    notifyDataSetChanged();
                                    bottomSheetDialog.cancel();

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


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
    public Modal_MenuItemWeightDetails getItem(int position) {
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
