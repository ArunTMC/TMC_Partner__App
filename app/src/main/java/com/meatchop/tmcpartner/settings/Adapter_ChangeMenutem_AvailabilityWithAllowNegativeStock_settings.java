 package com.meatchop.tmcpartner.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.TMCAlertDialogClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Adapter_ChangeMenutem_AvailabilityWithAllowNegativeStock_settings extends ArrayAdapter<Modal_MenuItem_Settings> {
    Context mContext;
    boolean isCalledFromchangeMenuItemStatus_Settings =false;
    boolean isoverlaplayoutClicked =false;
    boolean isoverlaplayoutAllowNegativeClicked =false;
    RecyclerView recycler;
    RecyclerView.LayoutManager manager;
    boolean isinventorycheck = false;


    Adapter_ChangeMenuItemAvail_InventoryDetailsJson adapter;
    int total_no_of_item=0;
    int total_no_of_item_Available = 0 ;
    double total_no_of_item_Available_inPercentage = 0;
    List<Modal_MenuItem_Settings> menuList;
    List<Modal_MenuItem_Settings> menuitem_InventoryDetailsJson = new ArrayList<>();
    ChangeMenuItemStatus_AllowNegativeStock_Settings changeMenuItemStatus_allowNegativeStock_settings;
    public Adapter_ChangeMenutem_AvailabilityWithAllowNegativeStock_settings(Context mContext, List<Modal_MenuItem_Settings> menuList, ChangeMenuItemStatus_AllowNegativeStock_Settings changeMenuItemStatus_allowNegativeStock_settingsd, boolean isinventorycheck) {
        super(mContext, R.layout.settings_toggle_switch_with_allow_negativestock_child, menuList);
        this.changeMenuItemStatus_allowNegativeStock_settings=changeMenuItemStatus_allowNegativeStock_settingsd;
        this.mContext=mContext;
        this.menuList = menuList;
        this.isinventorycheck = isinventorycheck;
        isCalledFromchangeMenuItemStatus_Settings = true;

    }


    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public Modal_MenuItem_Settings getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Modal_MenuItem_Settings item) {
        return super.getPosition(item);
    }

    @SuppressLint("ViewHolder")
    public View getView(final int pos, View view, ViewGroup v) {
        View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.settings_toggle_switch_with_allow_negativestock_child, (ViewGroup) view, false);
        final LinearLayout overlapLayout_allowNegativeToggle = listViewItem.findViewById(R.id.overlapLayout_allowNegativeToggle);

        final LinearLayout negativeStckSwitch_parentLayout = listViewItem.findViewById(R.id.negativeStckSwitch_parentLayout);
        recycler = listViewItem.findViewById(R.id.recyclerView);
        recycler.setHasFixedSize(true);
        manager = new GridLayoutManager(mContext, 1, GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(manager);
        recycler.setVisibility(View.GONE);

        final TextView itemName_widget = listViewItem.findViewById(R.id.child);
        final LinearLayout overlapLayout = listViewItem.findViewById(R.id.overlapLayout);
        @SuppressLint("UseSwitchCompatOrMaterialCode") final Switch menuItemAvailabiltySwitch = listViewItem.findViewById(R.id.menuItemAvailabiltySwitch);
        @SuppressLint("UseSwitchCompatOrMaterialCode") final Switch menuItemNegativeStockAvailabiltySwitch = listViewItem.findViewById(R.id.menuItem_allowNegativeToggleSwitch);
        boolean isAllowNegative = false ;

        boolean isItemAvailable = false ;
        Modal_MenuItem_Settings modal_manageOrders_pojo_class = menuList.get(pos);
        // itemName_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getItemname()));
        menuItemAvailabiltySwitch.setText(String.valueOf(modal_manageOrders_pojo_class.getItemname()));
        String  itemAvailability =String.valueOf(modal_manageOrders_pojo_class.getItemavailability_AvlDetails()).toUpperCase();

        String  allowNegativeStock =String.valueOf(modal_manageOrders_pojo_class.getAllownegativestock()).toUpperCase();

        if(itemAvailability.equals("") || itemAvailability.equals("NIL") || itemAvailability.equals("null") || itemAvailability.equals(null) || itemAvailability.equals("NULL")){
            itemAvailability =String.valueOf(modal_manageOrders_pojo_class.getItemavailability()).toUpperCase();
        }
        if(allowNegativeStock.equals("") || allowNegativeStock.equals("NIL") || allowNegativeStock.equals("null") || allowNegativeStock.equals(null) || allowNegativeStock.equals("NULL")){
            negativeStckSwitch_parentLayout.setVisibility(View.GONE);

        }
        else{
            negativeStckSwitch_parentLayout.setVisibility(View.VISIBLE);

        }

        try{
            isAllowNegative = Boolean.parseBoolean(String.valueOf(allowNegativeStock));
        }
        catch (Exception e){
            e.printStackTrace();
        }



        try {
            isItemAvailable = Boolean.parseBoolean(String.valueOf(itemAvailability));
        }
        catch (Exception e){
            e.printStackTrace();
        }



        if(isItemAvailable){
            menuItemAvailabiltySwitch . setChecked(true);

            /*if(!changeMenuItemStatus_allowNegativeStock_settings.subctgy_on_Off_Switch.isChecked()){
                changeMenuItemStatus_allowNegativeStock_settings.subctgy_on_Off_Switch.setChecked(true);
            }

             */


        }
        else{
            menuItemAvailabiltySwitch . setChecked(false);

        }


        if(isAllowNegative){
            menuItemNegativeStockAvailabiltySwitch . setChecked(true);
        }
        else{
            menuItemNegativeStockAvailabiltySwitch . setChecked(false);

        }
        String stockbalance ="0";
        double stockbalance_double =0 ;
        try{
             stockbalance = modal_manageOrders_pojo_class.getStockbalance_AvlDetails().toString();

        }
        catch (Exception e ){
            e.printStackTrace();
        }
        try{
            stockbalance_double = Double.parseDouble(stockbalance);
        }
        catch ( Exception e){
            stockbalance_double=0;
          //  e.printStackTrace();
        }
        if ((stockbalance_double<=0) && (isinventorycheck)){
            menuItemNegativeStockAvailabiltySwitch.setTextColor(Color.RED);
            menuItemAvailabiltySwitch.setTextColor(Color.RED);
        }
        else{
            menuItemAvailabiltySwitch.setTextColor(Color.BLACK);
            menuItemNegativeStockAvailabiltySwitch.setTextColor(Color.BLACK);

        }
        String inventoryDetails = modal_manageOrders_pojo_class.getInventorydetails().toString();

        if(!inventoryDetails.equals("") &&(! inventoryDetails.toString().equals("nil") )  ) {
          //  recycler.setVisibility(View.VISIBLE);
            //menuitem_InventoryDetailsJson.clear();
            String menuItemKeyFromInventoryDetails="",itemnameFromInventoryDetails ="",relationtypeFromInventoryDetails ="";
            double grossweightinGramsFromInventoryDetails=0, netweightingramsFromInventoryDetails = 0;
            boolean isshowavailabilityFromInventoryDetails = false;
            menuitem_InventoryDetailsJson.clear();

            try{
                JSONArray jsonArray = new JSONArray(String.valueOf(inventoryDetails));
                int jsonArrayIterator = 0;
                int jsonArrayCount = jsonArray.length();
                for (; jsonArrayIterator < (jsonArrayCount); jsonArrayIterator++) {
                   // menuitem_InventoryDetailsJson.clear();
                    try {
                        JSONObject json_InventoryDetails = jsonArray.getJSONObject(jsonArrayIterator);
                         menuItemKeyFromInventoryDetails="";isshowavailabilityFromInventoryDetails=false;itemnameFromInventoryDetails ="";relationtypeFromInventoryDetails ="";
                         grossweightinGramsFromInventoryDetails=0; netweightingramsFromInventoryDetails=0;

                        menuItemKeyFromInventoryDetails = "";
                        grossweightinGramsFromInventoryDetails = 0;
                        netweightingramsFromInventoryDetails = 0;
                        try {

                            if(json_InventoryDetails.has("relationtype")){
                                relationtypeFromInventoryDetails = json_InventoryDetails.getString("relationtype");

                            }
                            else{
                                relationtypeFromInventoryDetails = "CHILD";
                            }


                        } catch (Exception e) {
                            relationtypeFromInventoryDetails = "CHILD";
                            e.printStackTrace();
                        }


                        if(!relationtypeFromInventoryDetails.toUpperCase().equals("PARENT")) {
                            try {
                                if (json_InventoryDetails.has("menuitemkey")) {
                                    menuItemKeyFromInventoryDetails = json_InventoryDetails.getString("menuitemkey");

                                } else {
                                    menuItemKeyFromInventoryDetails = "";
                                }
                            } catch (Exception e) {
                                menuItemKeyFromInventoryDetails = "";
                                e.printStackTrace();
                            }

                            try {

                                if (json_InventoryDetails.has("grossweightingrams")) {
                                    grossweightinGramsFromInventoryDetails = Double.parseDouble(String.valueOf(json_InventoryDetails.getString("grossweightingrams")));

                                } else {
                                    grossweightinGramsFromInventoryDetails = 0;
                                }


                            } catch (Exception e) {
                                grossweightinGramsFromInventoryDetails = 0;
                                e.printStackTrace();
                            }

                            try {

                                if (json_InventoryDetails.has("isshowavailability")) {
                                    isshowavailabilityFromInventoryDetails = Boolean.parseBoolean(json_InventoryDetails.getString("isshowavailability"));

                                } else {
                                    isshowavailabilityFromInventoryDetails = false;
                                }


                            } catch (Exception e) {
                                isshowavailabilityFromInventoryDetails = false;
                                e.printStackTrace();
                            }

                            try {


                                if (json_InventoryDetails.has("itemname")) {
                                    itemnameFromInventoryDetails = json_InventoryDetails.getString("itemname");

                                } else {
                                    itemnameFromInventoryDetails = "";
                                }


                            } catch (Exception e) {
                                itemnameFromInventoryDetails = "";
                                e.printStackTrace();
                            }

                            try {


                                if (json_InventoryDetails.has("netweightingrams")) {
                                    netweightingramsFromInventoryDetails = Double.parseDouble(String.valueOf(json_InventoryDetails.getString("netweightingrams")));

                                } else {
                                    netweightingramsFromInventoryDetails = 0;
                                }

                            } catch (Exception e) {
                                netweightingramsFromInventoryDetails = 0;
                                e.printStackTrace();
                            }


                            try {
                                Modal_MenuItem_Settings modal_menuItem_settings = new Modal_MenuItem_Settings();

                                for (int i = 0; i < changeMenuItemStatus_allowNegativeStock_settings.MenuItem.size(); i++) {

                                    String MenuItemKey = "";

                                    try {
                                        MenuItemKey = changeMenuItemStatus_allowNegativeStock_settings.MenuItem.get(i).getMenuItemId().toString();


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        if (MenuItemKey.equals(menuItemKeyFromInventoryDetails)) {
                                            if(!relationtypeFromInventoryDetails.toUpperCase().equals("PARENT")) {

                                                modal_menuItem_settings = changeMenuItemStatus_allowNegativeStock_settings.MenuItem.get(i);
                                                modal_menuItem_settings.setNetweightingramsFromInventoryDetails(String.valueOf(netweightingramsFromInventoryDetails));
                                                modal_menuItem_settings.setNetweightingramsFromInventoryDetails(String.valueOf(grossweightinGramsFromInventoryDetails));
                                                modal_menuItem_settings.setRelationtypeFromInventoryDetails(relationtypeFromInventoryDetails);
                                                modal_menuItem_settings.setItemnameFromInventoryDetails(itemnameFromInventoryDetails);
                                                modal_menuItem_settings.setIsshowavailabilityFromInventoryDetails(isshowavailabilityFromInventoryDetails);
                                                modal_menuItem_settings.setMenuItemKeyFromInventoryDetails(menuItemKeyFromInventoryDetails);
                                                menuitem_InventoryDetailsJson.add(modal_menuItem_settings);
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (jsonArrayCount - jsonArrayIterator == 1) {
                                if(!relationtypeFromInventoryDetails.toUpperCase().equals("PARENT")) {
                                    recycler.setVisibility(View.VISIBLE);
                                    adapter = new Adapter_ChangeMenuItemAvail_InventoryDetailsJson(menuitem_InventoryDetailsJson, mContext, changeMenuItemStatus_allowNegativeStock_settings);
                                    recycler.setAdapter(adapter);

                                } else {
                                    recycler.setVisibility(View.GONE);

                                }

                            }else{
                                recycler.setVisibility(View.GONE);

                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            catch (Exception e){
                e.printStackTrace();
            }



        }

        else{
            recycler.setVisibility(View.GONE);
        }



        menuItemAvailabiltySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                  //  menuItemAvailabiltySwitch.setChecked(false);

                } else {
                  //  menuItemAvailabiltySwitch.setChecked(true);
                }}
        });


        menuItemNegativeStockAvailabiltySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

               //     menuItemNegativeStockAvailabiltySwitch.setChecked(false);

                } else {
                 //   menuItemNegativeStockAvailabiltySwitch.setChecked(true);
                }}
        });

        overlapLayout_allowNegativeToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(isoverlaplayoutAllowNegativeClicked){
                    return;
                }
                String availability = "";
                availability =String.valueOf(modal_manageOrders_pojo_class.getItemavailability_AvlDetails()).toUpperCase();
                if(availability.equals("") || availability.equals("NIL") || availability.equals("null") || availability.equals(null) || availability.equals("NULL")){
                    availability= ( modal_manageOrders_pojo_class.getItemavailability()).toUpperCase();
                }

                //Toast.makeText(mContext,availability   , Toast.LENGTH_SHORT).show();
                if(availability.equals("TRUE")) {

                    String allowNegativeStock = "";
                    isoverlaplayoutAllowNegativeClicked = true;
                    allowNegativeStock = String.valueOf(modal_manageOrders_pojo_class.getAllownegativestock()).toUpperCase();


                    if (allowNegativeStock.equals("") || allowNegativeStock.equals("NIL") || allowNegativeStock.equals("null") || allowNegativeStock.equals(null) || allowNegativeStock.equals("NULL")) {
                        Toast.makeText(mContext, "allowNegativeStock is null", Toast.LENGTH_SHORT).show();
                    }


                    //Toast.makeText(mContext,availability   , Toast.LENGTH_SHORT).show();
                    if (allowNegativeStock.equals("TRUE")) {


                        new TMCAlertDialogClass(mContext, R.string.app_name, R.string.TurnOffAllowNegativeStock,
                                R.string.Yes_Text, R.string.No_Text,
                                new TMCAlertDialogClass.AlertListener() {
                                    @Override
                                    public void onYes() {
                                        menuItemNegativeStockAvailabiltySwitch.setChecked(false);


                                        changeMenuItemStatus_allowNegativeStock_settings.Adjusting_Widgets_Visibility(true);
                                        String menuItemname = String.valueOf(modal_manageOrders_pojo_class.getItemname());

                                        String menuItemkey = String.valueOf(modal_manageOrders_pojo_class.getKey());
                                        String tmcsubCtgykey = String.valueOf(modal_manageOrders_pojo_class.getTmcsubctgykey());
                                        String menuItemStockAvlDetailskey = String.valueOf(modal_manageOrders_pojo_class.getKey_AvlDetails());
                                        boolean allowNegative = false;

                                        boolean itemAvailability = false;
                                        itemAvailability = Boolean.parseBoolean(String.valueOf(modal_manageOrders_pojo_class.getItemavailability_AvlDetails().toUpperCase()));
                                        if(!menuItemStockAvlDetailskey.equals("") && !menuItemStockAvlDetailskey.equals("nil") && ! menuItemStockAvlDetailskey.equals("null") && menuItemStockAvlDetailskey != null && ! menuItemStockAvlDetailskey.equals("NULL")) {


                                            changeMenuItemStatus_allowNegativeStock_settings.ChangeMenuitemStockAvlDetailsData(menuItemStockAvlDetailskey, allowNegative, itemAvailability, menuItemname, menuItemkey, tmcsubCtgykey);

                                        }
                                        //       changeMenuItemStatus_allowNegativeStock_settings.subctgy_on_Off_Switch.setChecked(false);

                                        modal_manageOrders_pojo_class.setAllownegativestock("FALSE");
                                        notifyDataSetChanged();


                                    }

                                    @Override
                                    public void onNo() {
                                    }
                                });
                    } else {
                        new TMCAlertDialogClass(mContext, R.string.app_name, R.string.TurnOnAllowNegativeStock,
                                R.string.Yes_Text, R.string.No_Text,
                                new TMCAlertDialogClass.AlertListener() {
                                    @Override
                                    public void onYes() {
                                        menuItemAvailabiltySwitch.setChecked(true);


                                        changeMenuItemStatus_allowNegativeStock_settings.Adjusting_Widgets_Visibility(true);

                                        String menuItemname = String.valueOf(modal_manageOrders_pojo_class.getItemname());

                                        String menuItemkey = String.valueOf(modal_manageOrders_pojo_class.getKey());
                                        String tmcsubCtgykey = String.valueOf(modal_manageOrders_pojo_class.getTmcsubctgykey());
                                        String menuItemStockAvlDetailskey = String.valueOf(modal_manageOrders_pojo_class.getKey_AvlDetails());
                                        boolean allowNegative = true;
                                        // allowNegative = Boolean.parseBoolean(String.valueOf(modal_manageOrders_pojo_class.getAllownegativestock().toUpperCase()));
                                        boolean itemAvailability = false;
                                        itemAvailability = Boolean.parseBoolean(String.valueOf(modal_manageOrders_pojo_class.getItemavailability_AvlDetails().toUpperCase()));

                                        if(!menuItemStockAvlDetailskey.equals("") && ! menuItemStockAvlDetailskey.equals("null") &&  !menuItemStockAvlDetailskey.equals("nil") && menuItemStockAvlDetailskey != null && ! menuItemStockAvlDetailskey.equals("NULL")) {

                                            changeMenuItemStatus_allowNegativeStock_settings.ChangeMenuitemStockAvlDetailsData(menuItemStockAvlDetailskey, allowNegative, itemAvailability, menuItemname, menuItemkey, tmcsubCtgykey);
                                        }
                                        //   changeMenuItemStatus_allowNegativeStock_settings.subctgy_on_Off_Switch.setChecked(true);

                                        modal_manageOrders_pojo_class.setAllownegativestock("TRUE");


                                        // //changeMenuItemStatus_allowNegativeStock_settings.ChangeMenuitemAvailabilityStatus(menuItemkey,"TRUE",itemuniquecode);
                                        // modal_manageOrders_pojo_class.setItemavailability("TRUE");
                                        notifyDataSetChanged();


                                    }

                                    @Override
                                    public void onNo() {
                                    }
                                });
                    }
                    isoverlaplayoutAllowNegativeClicked = false;


                }
                else{
                    Toast.makeText(mContext, "Can't Allow Negative Stock When item availability is OFF ", Toast.LENGTH_SHORT).show();



                }




        }
        });

        overlapLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isoverlaplayoutClicked){
                    return;
                }

                isoverlaplayoutClicked = true;
                String availability = "";
                availability =String.valueOf(modal_manageOrders_pojo_class.getItemavailability_AvlDetails()).toUpperCase();
                if(availability.equals("") || availability.equals("NIL") || availability.equals("null") || availability.equals(null) || availability.equals("NULL")){
                    availability= ( modal_manageOrders_pojo_class.getItemavailability()).toUpperCase();
                }

                //Toast.makeText(mContext,availability   , Toast.LENGTH_SHORT).show();
                if(availability.equals("TRUE")) {


                    new TMCAlertDialogClass(mContext, R.string.app_name, R.string.TurnOffMenuInstruction,
                            R.string.Yes_Text, R.string.No_Text,
                            new TMCAlertDialogClass.AlertListener() {
                                @Override
                                public void onYes() {
                                    menuItemAvailabiltySwitch.setChecked(false);




                                    changeMenuItemStatus_allowNegativeStock_settings. Adjusting_Widgets_Visibility(true);

                                    String menuItemkey = String.valueOf(modal_manageOrders_pojo_class.getKey());
                                    String itemuniquecode = String.valueOf(modal_manageOrders_pojo_class.getItemuniquecode());

                                    boolean isMarinadeItem = false;
                                    try {
                                        isMarinadeItem = modal_manageOrders_pojo_class.getisMarinadeItem();
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                        isMarinadeItem = false;
                                    }


                                    if(isMarinadeItem){
                                        String marinadeItem_itemuniquecode = modal_manageOrders_pojo_class.getMarinadeItemUniqueCode();
                                        String marinadeItemKey = modal_manageOrders_pojo_class.getMarinadeKey();
                                        if(marinadeItem_itemuniquecode.equals(itemuniquecode)){

                                            changeMenuItemStatus_allowNegativeStock_settings.ChangeMarinadeMenuitemAvailabilityStatus(marinadeItemKey,"FALSE",itemuniquecode);

                                        }

                                    }



                                    String menuItemname = String.valueOf(modal_manageOrders_pojo_class.getItemname());

                                    String tmcsubCtgykey = String.valueOf(modal_manageOrders_pojo_class.getTmcsubctgykey());
                                    String menuItemStockAvlDetailskey = String.valueOf(modal_manageOrders_pojo_class.getKey_AvlDetails());
                                    boolean allowNegative = false;

                                    boolean itemAvailability = false;


                                    if(!menuItemStockAvlDetailskey.equals("") && !menuItemStockAvlDetailskey.equals("nil") && ! menuItemStockAvlDetailskey.equals("null") && menuItemStockAvlDetailskey != null && ! menuItemStockAvlDetailskey.equals("NULL")) {

                                        changeMenuItemStatus_allowNegativeStock_settings.ChangeMenuitemStockAvlDetailsData(menuItemStockAvlDetailskey, allowNegative, itemAvailability, menuItemname, menuItemkey, tmcsubCtgykey);


                                    }

                                    //       changeMenuItemStatus_allowNegativeStock_settings.subctgy_on_Off_Switch.setChecked(false);
                                    if(allowNegativeStock.equals("") || allowNegativeStock.equals("nil") || allowNegativeStock.equals("null") || allowNegativeStock.equals(null) || allowNegativeStock.equals("NULL")){

                                    }
                                    else{
                                        modal_manageOrders_pojo_class.setAllownegativestock("FALSE");

                                    }


                                    changeMenuItemStatus_allowNegativeStock_settings.ChangeMenuitemAvailabilityStatus(menuItemkey,"FALSE", itemuniquecode,menuItemStockAvlDetailskey);
                                    changeMenuItemStatus_allowNegativeStock_settings.subctgy_on_Off_Switch.setChecked(false);
                                    modal_manageOrders_pojo_class.setItemavailability_AvlDetails("FALSE");

                                    modal_manageOrders_pojo_class.setItemavailability("FALSE");
                                    notifyDataSetChanged();





                                }

                                @Override
                                public void onNo() {
                                }
                            });
                }
                else{
                    new TMCAlertDialogClass(mContext, R.string.app_name, R.string.TurnOnMenuInstruction,
                            R.string.Yes_Text, R.string.No_Text,
                            new TMCAlertDialogClass.AlertListener() {
                                @Override
                                public void onYes() {
                                    menuItemAvailabiltySwitch.setChecked(true);



                                    changeMenuItemStatus_allowNegativeStock_settings. Adjusting_Widgets_Visibility(true);

                                    String menuItemkey = String.valueOf(modal_manageOrders_pojo_class.getKey());
                                    String itemuniquecode = String.valueOf(modal_manageOrders_pojo_class.getItemuniquecode());




                                     boolean isMarinadeItem = false;
                                    try {
                                        isMarinadeItem = modal_manageOrders_pojo_class.getisMarinadeItem();
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                        isMarinadeItem = false;
                                    }
                                    if(isMarinadeItem){
                                        String marinadeItem_itemuniquecode = modal_manageOrders_pojo_class.getMarinadeItemUniqueCode();
                                        String marinadeItemKey = modal_manageOrders_pojo_class.getMarinadeKey();
                                        if(marinadeItem_itemuniquecode.equals(itemuniquecode)){

                                            changeMenuItemStatus_allowNegativeStock_settings.ChangeMarinadeMenuitemAvailabilityStatus(marinadeItemKey,"TRUE",itemuniquecode);

                                        }

                                    }




                                    String menuItemname = String.valueOf(modal_manageOrders_pojo_class.getItemname());

                                    String tmcsubCtgykey = String.valueOf(modal_manageOrders_pojo_class.getTmcsubctgykey());
                                    String menuItemStockAvlDetailskey = String.valueOf(modal_manageOrders_pojo_class.getKey_AvlDetails());
                                    boolean allowNegative = false;

                                    boolean itemAvailability = true;


                                    if(!menuItemStockAvlDetailskey.equals("") && ! menuItemStockAvlDetailskey.equals("nil")  && ! menuItemStockAvlDetailskey.equals("null") && menuItemStockAvlDetailskey != null && ! menuItemStockAvlDetailskey.equals("NULL")) {

                                        changeMenuItemStatus_allowNegativeStock_settings.ChangeMenuitemStockAvlDetailsData(menuItemStockAvlDetailskey, allowNegative, itemAvailability, menuItemname, menuItemkey, tmcsubCtgykey);
                                    }

                                    //       changeMenuItemStatus_allowNegativeStock_settings.subctgy_on_Off_Switch.setChecked(false);

                                    if(allowNegativeStock.equals("") || allowNegativeStock.equals("nil") || allowNegativeStock.equals("null") || allowNegativeStock.equals(null) || allowNegativeStock.equals("NULL")){

                                    }
                                    else{
                                        modal_manageOrders_pojo_class.setAllownegativestock("FALSE");

                                    }


                                    changeMenuItemStatus_allowNegativeStock_settings.subctgy_on_Off_Switch.setChecked(true);

                                    changeMenuItemStatus_allowNegativeStock_settings.ChangeMenuitemAvailabilityStatus(menuItemkey,"TRUE",itemuniquecode, menuItemStockAvlDetailskey);
                                    modal_manageOrders_pojo_class.setItemavailability("TRUE");
                                    modal_manageOrders_pojo_class.setItemavailability_AvlDetails("TRUE");
                                    notifyDataSetChanged();





                                }

                                @Override
                                public void onNo() {
                                }
                            });
                }
                isoverlaplayoutClicked=false;
            }



        });






      //  changeMenuItemStatus_allowNegativeStock_settings.Adjusting_Widgets_Visibility(false);


        return  listViewItem ;
    }

}
