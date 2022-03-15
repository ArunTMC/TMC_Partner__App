package com.meatchop.tmcpartner.Settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.TMCAlertDialogClass;

import java.util.ArrayList;
import java.util.List;

public class Adapter_ChangeMenuItemAvail_InventoryDetailsJson extends RecyclerView.Adapter<Adapter_ChangeMenuItemAvail_InventoryDetailsJson.ViewHolder> {
    boolean isoverlaplayoutClicked =false;
    boolean isoverlaplayoutAllowNegativeClicked =false;
    List<Modal_MenuItem_Settings> menuitem_InventoryDetailsJson = new ArrayList<>();;
    private Context mContext;
    ChangeMenuItemStatus_AllowNegativeStock_Settings changeMenuItemStatus_allowNegativeStock_settings;

    int blackgroundfinalposition=0, blackgroundstartposition=0;
    boolean startchangecolor = false;
    int numberX=0,numberY=0;
    public Adapter_ChangeMenuItemAvail_InventoryDetailsJson(List<Modal_MenuItem_Settings> list, Context context,ChangeMenuItemStatus_AllowNegativeStock_Settings changeMenuItemStatus_allowNegativeStock_settingsd) {
        this.menuitem_InventoryDetailsJson = list;
        this.mContext = context;
        this.changeMenuItemStatus_allowNegativeStock_settings=changeMenuItemStatus_allowNegativeStock_settingsd;

    }

    @Override
    public Adapter_ChangeMenuItemAvail_InventoryDetailsJson.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.settings_toggle_switch_with_allow_negativestock_child, parent, false);
        Adapter_ChangeMenuItemAvail_InventoryDetailsJson.ViewHolder holder = new Adapter_ChangeMenuItemAvail_InventoryDetailsJson.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Modal_MenuItem_Settings modal_manageOrders_pojo_class = menuitem_InventoryDetailsJson.get(position);

   /*
        if(position!=0) {

            if(startchangecolor){
                if((position>=blackgroundstartposition)&&(position<=blackgroundfinalposition)) {
                    holder.textView.setBackground(mContext.getResources().getDrawable(R.drawable.orange_border_button));
                    if (position == blackgroundfinalposition - 1) {
                        startchangecolor = false;
                    }
                }
            }
            else {


                if (position % 8 == 0) {



                    if(position==8){
                        holder.textView.setBackground(mContext.getResources().getDrawable(R.drawable.orange_border_button));
                        startchangecolor = true;

                    }

                    blackgroundstartposition = (blackgroundfinalposition+8);
                    blackgroundfinalposition = blackgroundstartposition + 8;


                }
                else {

                    holder.textView.setBackground(mContext.getResources().getDrawable(R.drawable.orange_non_selected_button_background));



                }
            }
            if((blackgroundstartposition-1)==position){
                startchangecolor = true;

            }
        }
        else{
            holder.textView.setBackground(mContext.getResources().getDrawable(R.drawable.orange_non_selected_button_background));

        }


    */

        holder. recycler.setVisibility(View.GONE);
       // holder. negativeStckSwitch_parentLayout.setVisibility(View.GONE);

        holder.menuItemAvailabiltySwitch.setText(String.valueOf(modal_manageOrders_pojo_class.getItemname()));
        String  itemAvailability =String.valueOf(modal_manageOrders_pojo_class.getItemavailability_AvlDetails()).toUpperCase();

        String  allowNegativeStock =String.valueOf(modal_manageOrders_pojo_class.getAllownegativestock()).toUpperCase();

        if(itemAvailability.equals("") || itemAvailability.equals("NIL") || itemAvailability.equals("null") || itemAvailability.equals(null) || itemAvailability.equals("NULL")){
            itemAvailability =String.valueOf(modal_manageOrders_pojo_class.getItemavailability()).toUpperCase();
        }
        if(allowNegativeStock.equals("") || allowNegativeStock.equals("NIL") || allowNegativeStock.equals("null") || allowNegativeStock.equals(null) || allowNegativeStock.equals("NULL")){
            holder. negativeStckSwitch_parentLayout.setVisibility(View.GONE);

        }
        else{
            holder.negativeStckSwitch_parentLayout.setVisibility(View.VISIBLE);

        }

        try{
            holder.isAllowNegative = Boolean.parseBoolean(String.valueOf(allowNegativeStock));
        }
        catch (Exception e){
            e.printStackTrace();
        }





        try {
            holder.isItemAvailable = Boolean.parseBoolean(String.valueOf(itemAvailability));
        }
        catch (Exception e){
            e.printStackTrace();
        }



        if(holder.isItemAvailable){
            holder. menuItemAvailabiltySwitch . setChecked(true);


        }
        else{
            holder.menuItemAvailabiltySwitch . setChecked(false);

        }


        if(holder.isAllowNegative){
            holder.menuItemNegativeStockAvailabiltySwitch . setChecked(true);
        }
        else{
            holder.menuItemNegativeStockAvailabiltySwitch . setChecked(false);

        }



        holder.menuItemAvailabiltySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    //  menuItemAvailabiltySwitch.setChecked(false);

                } else {
                    //  menuItemAvailabiltySwitch.setChecked(true);
                }}
        });


        holder.menuItemNegativeStockAvailabiltySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    //     menuItemNegativeStockAvailabiltySwitch.setChecked(false);

                } else {
                    //   menuItemNegativeStockAvailabiltySwitch.setChecked(true);
                }}
        });

        holder.overlapLayout_allowNegativeToggle.setOnClickListener(new View.OnClickListener() {
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
                                        holder.menuItemNegativeStockAvailabiltySwitch.setChecked(false);


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
                                        holder. menuItemAvailabiltySwitch.setChecked(true);


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






        holder. overlapLayout.setOnClickListener(new View.OnClickListener() {
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
                                    holder.menuItemAvailabiltySwitch.setChecked(false);




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
                                    holder. menuItemAvailabiltySwitch.setChecked(true);



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
     //   holder. negativeStckSwitch_parentLayout.setVisibility(View.GONE);

    }


    @Override
    public int getItemCount() {
        return menuitem_InventoryDetailsJson.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView,itemName_widget;
        RecyclerView recycler;
        LinearLayout overlapLayout_allowNegativeToggle,negativeStckSwitch_parentLayout,overlapLayout;
        public Switch menuItemAvailabiltySwitch,menuItemNegativeStockAvailabiltySwitch;
        boolean isAllowNegative = false ;

        boolean isItemAvailable = false ;
        public ViewHolder(View itemView) {
            super(itemView);
            overlapLayout_allowNegativeToggle = itemView.findViewById(R.id.overlapLayout_allowNegativeToggle);

            negativeStckSwitch_parentLayout = itemView.findViewById(R.id.negativeStckSwitch_parentLayout);
            recycler = itemView.findViewById(R.id.recyclerView);
            itemName_widget = itemView.findViewById(R.id.child);
            overlapLayout = itemView.findViewById(R.id.overlapLayout);
            menuItemAvailabiltySwitch = itemView.findViewById(R.id.menuItemAvailabiltySwitch);
            menuItemNegativeStockAvailabiltySwitch = itemView.findViewById(R.id.menuItem_allowNegativeToggleSwitch);

        }
    }
}