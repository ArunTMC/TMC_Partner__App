package com.meatchop.tmcpartner.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders.Adapter_Mobile_ManageOrders_ListView1;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders.Mobile_ManageOrders1;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.TMCAlertDialogClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import static com.meatchop.tmcpartner.Constants.TAG;

public class ChangeMenuItemStatus_AllowNegativeStock_Settings extends AppCompatActivity {
    private static final String ARG_ITEM_COUNT = "item_count";
    ///  private LinkedHashMap<String, GroupInfo> childCtgyHashmap = new LinkedHashMap<String, GroupInfo>();
    //  private ArrayList<GroupInfo> ctgyList = new ArrayList<GroupInfo>();
    //  ArrayList<ChildInfo> childList;
    LinearLayout loadingPanel,loadingpanelmask,subCtgyMenuSwitch_Layout;
    public static HashMap<String, List<Modal_MenuItem_Settings>> MenuItem_hashmap = new HashMap();
    Spinner subCtgyItem_spinner;
    ArrayAdapter<Modal_SubCtgyList> adapter_subCtgy_spinner;
    String MenuItems ;
    String vendorkey,SubCtgyName,deliverySlotKey,UserPhoneNumber  ;
    ListView MenuItemsListView;
    List<Modal_MenuItem_Settings>MenuItem = new ArrayList<>();
    String SubCtgyKey;
    public static List<Modal_MenuItem_Settings> marinadeMenuList;
    boolean isSubCtgySwitchTouched;
    public static List<Modal_MenuItem_Settings> displaying_menuItems;
    //public static List<Modal_MenuItem_Settings> completemenuItem;
    public static List<Modal_SubCtgyList> subCtgyName_arrayList;
    JSONArray result;
    Adapter_ChangeMenutem_AvailabilityWithAllowNegativeStock_settings adapter_Change_menutem_availabilityWithAllowNegativeStock_settings;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch vendorSlotAvailabiltySwitch,subctgy_on_Off_Switch;
    TextView itemAvailabilityCount_textWidget;
    int total_no_of_item = 0;
    int total_no_item_availability = 0 ;
    boolean isinventorycheck = false;



    String errorCode = "0";
    Dialog dialog ;
    Button restartAgain;
    TextView title,subtitle_Textview;
    ImageView searchButton,searchBarCloseButton;
    EditText searchbarEdit;
    public static List<Modal_MenuItem_Settings> filteredsubctgywise_menuItems;
    boolean isSearchButtonClicked = false ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_menu_item_status__allow_negative_stock__settings);
        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        loadingPanel = findViewById(R.id.loadingPanel);
        subCtgyItem_spinner = findViewById(R.id.subCtgyItem);
        MenuItemsListView = findViewById(R.id.MenuItemsListView);
        vendorSlotAvailabiltySwitch =findViewById(R.id.vendorSlotAvailabiltySwitch);
        subctgy_on_Off_Switch = findViewById(R.id.subctgy_on_Off_Switch);
        subCtgyMenuSwitch_Layout  = findViewById(R.id.subCtgyMenuSwitch_Layout);
        itemAvailabilityCount_textWidget = findViewById(R.id.itemAvailabilityCount_textWidget);
        searchButton =findViewById(R.id.search_button);
        subtitle_Textview =findViewById(R.id.subtitle_Textview);
        searchBarCloseButton =findViewById(R.id.searchBarCloseButton);
        searchbarEdit =findViewById(R.id.searchbarEdit);

        Adjusting_Widgets_Visibility(true);
        SharedPreferences shared = getApplicationContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        vendorkey = (shared.getString("VendorKey", ""));
        UserPhoneNumber =  (shared.getString("UserPhoneNumber", ""));
        isinventorycheck = (shared.getBoolean("inventoryCheckBool", false));
        filteredsubctgywise_menuItems = new ArrayList<>();
        displaying_menuItems = new ArrayList<>();
        subCtgyName_arrayList = new ArrayList<>();
        marinadeMenuList=new ArrayList<>();

        getMenuItemArrayFromSharedPreferences();
        getMenuItemStockAvlDetails();
        getMenuCategoryList();
        getMarinadeMenuItem(vendorkey);
        //Bundle bundle = getIntent().getExtras();
        //    checkforVendorSlotDetails();
        //MenuItems = bundle.getString("key1", "Default");

        // completemenuItem = new ArrayList<>();
        //completemenuItem= getMenuItemfromString(MenuItems);
        //subctgy_on_Off_Switch.setChecked(false);


        subctgy_on_Off_Switch.setChecked(false);

        subCtgyMenuSwitch_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSubCtgySwitchTouched=true;
                if(!subctgy_on_Off_Switch.isChecked()){
                    new TMCAlertDialogClass(ChangeMenuItemStatus_AllowNegativeStock_Settings.this, R.string.app_name, R.string.TurnOnAllMenuSubCtgywiseInstruction,
                            R.string.Yes_Text, R.string.No_Text,
                            new TMCAlertDialogClass.AlertListener() {
                                @Override
                                public void onYes() {
                                    subctgy_on_Off_Switch.setChecked(true);
                                    if(isSubCtgySwitchTouched) {
                                        ChangeAvailability_subctgywise_InMenuItemDB(true);
                                        // Toast.makeText(ChangeMenuItemStatus_AllowNegativeStock_Settings.this,String.valueOf(isChecked),Toast.LENGTH_LONG).show();
                                    }

                                }

                                @Override
                                public void onNo() {
                                }
                            });
                }
                else{
                    new TMCAlertDialogClass(ChangeMenuItemStatus_AllowNegativeStock_Settings.this, R.string.app_name, R.string.TurnOffAllMenuSubCtgywiseInstruction,
                            R.string.Yes_Text, R.string.No_Text,
                            new TMCAlertDialogClass.AlertListener() {
                                @Override
                                public void onYes() {
                                    subctgy_on_Off_Switch.setChecked(false);
                                    ChangeAvailability_subctgywise_InMenuItemDB(false);


                                }

                                @Override
                                public void onNo() {
                                }
                            });

                }
            }
        });

        subctgy_on_Off_Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {



            }
        });
        subCtgyItem_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.d(Constants.TAG, "displaying_menuItems: " + displaying_menuItems.size());
                subctgy_on_Off_Switch.setChecked(false);
                SubCtgyName = getVendorData(i,"subctgyname");
                SubCtgyKey=getVendorData(i,"key");
                getMenuItemsbasedOnSubCtgy(SubCtgyKey);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        vendorSlotAvailabiltySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //  changeStatusintheDeliverySlot("ACTIVE");
                    //   changeStatusintheMobiledataDeliverySlot("");
                } else {
                    //  changeStatusintheDeliverySlot("INACTIVE");
                    // changeStatusintheMobiledataDeliverySlot("");


                }
            }
        });


        searchBarCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(searchbarEdit);
                closeSearchBarEditText();
                searchbarEdit.setText("");
                isSearchButtonClicked =false;
             //   callAdapter(displaying_menuItems);
                getMenuItemsbasedOnSubCtgy(SubCtgyKey);

            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int textlength = searchbarEdit.getText().toString().length();
                isSearchButtonClicked =true;
                showKeyboard(searchbarEdit);
                showSearchBarEditText();
            }
        });


        searchbarEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filteredsubctgywise_menuItems.clear();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filteredsubctgywise_menuItems.clear();
                isSearchButtonClicked =true;
                String itemnameFromEditText = (editable.toString());
                if(!itemnameFromEditText.equals("")) {
                    String relationtypeFromInventoryDetails = "";

                    for (int i = 0; i < displaying_menuItems.size(); i++) {
                        try {

                            final Modal_MenuItem_Settings modal_menuItem_sedisplaying_menuItemsttings = new Modal_MenuItem_Settings();
                            final Modal_MenuItem_Settings modal_menuItemSettings = displaying_menuItems.get(i);
                            String inventoryDetails = modal_menuItemSettings.getInventorydetails().toString();

                            String itemName = modal_menuItemSettings.getItemname();
                              if (itemName.toUpperCase().contains(itemnameFromEditText.toUpperCase())) {
                                  if(!inventoryDetails.equals("") &&(! inventoryDetails.toString().equals("nil") )  ) {

                                      try {
                                          modal_menuItem_sedisplaying_menuItemsttings.relationtypeFromInventoryDetails = modal_menuItemSettings.getRelationtypeFromInventoryDetails();
                                          relationtypeFromInventoryDetails = modal_menuItemSettings.getRelationtypeFromInventoryDetails();
                                      } catch (Exception e) {
                                          relationtypeFromInventoryDetails = "CHILD";
                                          modal_menuItem_sedisplaying_menuItemsttings.relationtypeFromInventoryDetails = "CHILD";
                                          e.printStackTrace();
                                      }
                                      if(!relationtypeFromInventoryDetails.toUpperCase().equals("PARENT")) {



                                          modal_menuItem_sedisplaying_menuItemsttings.key = String.valueOf(modal_menuItemSettings.getKey());
                                          modal_menuItem_sedisplaying_menuItemsttings.key_AvlDetails = String.valueOf(modal_menuItemSettings.getKey_AvlDetails());
                                          modal_menuItem_sedisplaying_menuItemsttings.itemname = String.valueOf(modal_menuItemSettings.getItemname());
                                          modal_menuItem_sedisplaying_menuItemsttings.menuItemId = String.valueOf(modal_menuItemSettings.getMenuItemId());
                                          modal_menuItem_sedisplaying_menuItemsttings.tmcsubctgykey = String.valueOf(modal_menuItemSettings.getTmcsubctgykey());
                                          modal_menuItem_sedisplaying_menuItemsttings.barcode = String.valueOf(modal_menuItemSettings.getBarcode());
                                          modal_menuItem_sedisplaying_menuItemsttings.itemuniquecode = String.valueOf(modal_menuItemSettings.getItemuniquecode());
                                          modal_menuItem_sedisplaying_menuItemsttings.displayno = String.valueOf(modal_menuItemSettings.getDisplayno());
                                          modal_menuItem_sedisplaying_menuItemsttings.itemavailability_AvlDetails = String.valueOf(modal_menuItemSettings.getItemavailability_AvlDetails());
                                          modal_menuItem_sedisplaying_menuItemsttings.allownegativestock = String.valueOf(modal_menuItemSettings.getAllownegativestock());
                                          modal_menuItem_sedisplaying_menuItemsttings.itemavailability = String.valueOf(modal_menuItemSettings.getItemavailability());
                                          modal_menuItem_sedisplaying_menuItemsttings.inventorydetails = String.valueOf(modal_menuItemSettings.getInventorydetails());
                                          modal_menuItem_sedisplaying_menuItemsttings.stockbalance_AvlDetails = String.valueOf(modal_menuItemSettings.getStockbalance_AvlDetails());

                                          try {
                                              modal_menuItem_sedisplaying_menuItemsttings.marinadeKey = String.valueOf(modal_menuItemSettings.getMarinadeKey());
                                              modal_menuItem_sedisplaying_menuItemsttings.isMarinadeItem = true;
                                              modal_menuItem_sedisplaying_menuItemsttings.marinadeItemAvailability = String.valueOf(modal_menuItemSettings.getMarinadeItemAvailability());
                                              modal_menuItem_sedisplaying_menuItemsttings.marinadeBarcode = String.valueOf(modal_menuItemSettings.getMarinadeBarcode());
                                              modal_menuItem_sedisplaying_menuItemsttings.marinadeItemUniqueCode = String.valueOf(modal_menuItemSettings.getMarinadeItemUniqueCode());
                                          } catch (Exception e) {
                                              modal_menuItem_sedisplaying_menuItemsttings.marinadeKey = "";
                                              modal_menuItem_sedisplaying_menuItemsttings.marinadeItemAvailability = "";
                                              modal_menuItem_sedisplaying_menuItemsttings.marinadeBarcode = "";
                                              modal_menuItem_sedisplaying_menuItemsttings.marinadeItemUniqueCode = "";
                                              modal_menuItem_sedisplaying_menuItemsttings.isMarinadeItem = false;
                                          }
                                          if (String.valueOf(modal_menuItemSettings.getItemavailability()).equals("TRUE")) {
                                              total_no_item_availability = total_no_item_availability + 1;
                                          }

                                          filteredsubctgywise_menuItems.add(modal_menuItem_sedisplaying_menuItemsttings);

                                      }

                                  }
                                  else{


                                      modal_menuItem_sedisplaying_menuItemsttings.key = String.valueOf(modal_menuItemSettings.getKey());
                                      modal_menuItem_sedisplaying_menuItemsttings.key_AvlDetails = String.valueOf(modal_menuItemSettings.getKey_AvlDetails());
                                      modal_menuItem_sedisplaying_menuItemsttings.itemname = String.valueOf(modal_menuItemSettings.getItemname());
                                      modal_menuItem_sedisplaying_menuItemsttings.menuItemId = String.valueOf(modal_menuItemSettings.getMenuItemId());
                                      modal_menuItem_sedisplaying_menuItemsttings.tmcsubctgykey = String.valueOf(modal_menuItemSettings.getTmcsubctgykey());
                                      modal_menuItem_sedisplaying_menuItemsttings.barcode = String.valueOf(modal_menuItemSettings.getBarcode());
                                      modal_menuItem_sedisplaying_menuItemsttings.itemuniquecode = String.valueOf(modal_menuItemSettings.getItemuniquecode());
                                      modal_menuItem_sedisplaying_menuItemsttings.displayno = String.valueOf(modal_menuItemSettings.getDisplayno());
                                      modal_menuItem_sedisplaying_menuItemsttings.itemavailability_AvlDetails = String.valueOf(modal_menuItemSettings.getItemavailability_AvlDetails());
                                      modal_menuItem_sedisplaying_menuItemsttings.allownegativestock = String.valueOf(modal_menuItemSettings.getAllownegativestock());
                                      modal_menuItem_sedisplaying_menuItemsttings.itemavailability = String.valueOf(modal_menuItemSettings.getItemavailability());
                                      modal_menuItem_sedisplaying_menuItemsttings.inventorydetails = String.valueOf(modal_menuItemSettings.getInventorydetails());
                                      modal_menuItem_sedisplaying_menuItemsttings.stockbalance_AvlDetails = String.valueOf(modal_menuItemSettings.getStockbalance_AvlDetails());

                                      try {
                                          modal_menuItem_sedisplaying_menuItemsttings.marinadeKey = String.valueOf(modal_menuItemSettings.getMarinadeKey());
                                          modal_menuItem_sedisplaying_menuItemsttings.isMarinadeItem = true;
                                          modal_menuItem_sedisplaying_menuItemsttings.marinadeItemAvailability = String.valueOf(modal_menuItemSettings.getMarinadeItemAvailability());
                                          modal_menuItem_sedisplaying_menuItemsttings.marinadeBarcode = String.valueOf(modal_menuItemSettings.getMarinadeBarcode());
                                          modal_menuItem_sedisplaying_menuItemsttings.marinadeItemUniqueCode = String.valueOf(modal_menuItemSettings.getMarinadeItemUniqueCode());
                                      } catch (Exception e) {
                                          modal_menuItem_sedisplaying_menuItemsttings.marinadeKey = "";
                                          modal_menuItem_sedisplaying_menuItemsttings.marinadeItemAvailability = "";
                                          modal_menuItem_sedisplaying_menuItemsttings.marinadeBarcode = "";
                                          modal_menuItem_sedisplaying_menuItemsttings.marinadeItemUniqueCode = "";
                                          modal_menuItem_sedisplaying_menuItemsttings.isMarinadeItem = false;
                                      }
                                      if (String.valueOf(modal_menuItemSettings.getItemavailability()).equals("TRUE")) {
                                          total_no_item_availability = total_no_item_availability + 1;
                                      }

                                      filteredsubctgywise_menuItems.add(modal_menuItem_sedisplaying_menuItemsttings);
                                  }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                    try {
                     callAdapter(filteredsubctgywise_menuItems);
                    } catch (Exception E) {
                        E.printStackTrace();
                    }


                }
                else{
                    callAdapter(filteredsubctgywise_menuItems);


                }

            }
        });


    }

    private void getMenuItemStockAvlDetails() {
        Adjusting_Widgets_Visibility(true);

        Log.d(TAG, "starting:getfullMenuItemStockavldetailsUsingStoreID ");
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofMenuItemStockAvlDetails+vendorkey,
                    null, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(@NonNull JSONObject response) {

                    try{
                        Log.d(TAG, "starting:onResponse ");

                        Log.d(TAG, "response for addMenuListAdaptertoListView: " + response.length());

                        try {
                            JSONArray JArray = response.getJSONArray("content");

                            Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                            int i1 = 0;
                            int arrayLength = JArray.length();
                            Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);

                            for (; i1 < (arrayLength); i1++) {
                                String menuItemkeyFromStockAvl = "";

                                try {
                                    JSONObject json = JArray.getJSONObject(i1);
                                    try{
                                        if(json.has("menuitemkey")){

                                                menuItemkeyFromStockAvl =    String.valueOf(json.get("menuitemkey"));




                                            for(int iterator = 0; iterator<MenuItem.size(); iterator++) {
                                                Modal_MenuItem_Settings modal_menuItem = MenuItem.get(iterator);
                                                String menuItemKey = "";
                                                try {
                                                    menuItemKey = modal_menuItem.getKey().toString();
                                                } catch (Exception e) {
                                                    menuItemKey = "";
                                                    e.printStackTrace();
                                                }

                                                if (menuItemKey.equals(menuItemkeyFromStockAvl)) {


                                                    if (json.has("key")) {
                                                        modal_menuItem.key_AvlDetails = String.valueOf(json.get("key"));
                                                    } else {
                                                        modal_menuItem.key_AvlDetails = "";
                                                        Log.d(Constants.TAG, "There is no key for this Menu: ");


                                                    }

                                                    if (json.has("allownegativestock")) {
                                                        modal_menuItem.allownegativestock = String.valueOf(json.get("allownegativestock"));
                                                    } else {
                                                        modal_menuItem.allownegativestock = "";
                                                        Log.d(Constants.TAG, "There is no allownegativestock for this Menu: ");


                                                    }


                                                    if (json.has("barcode")) {
                                                        modal_menuItem.barcode_AvlDetails = String.valueOf(json.get("barcode"));

                                                    } else {
                                                        modal_menuItem.barcode_AvlDetails = "";



                                                    }
                                                    if (json.has("itemavailability")) {
                                                        modal_menuItem.itemavailability_AvlDetails = String.valueOf(json.get("itemavailability"));

                                                    } else {
                                                        modal_menuItem.itemavailability_AvlDetails = "";



                                                    }
                                                    if (json.has("lastupdatedtime")) {
                                                        modal_menuItem.lastupdatedtime_AvlDetails = String.valueOf(json.get("lastupdatedtime"));
                                                        if (String.valueOf(json.get("lastupdatedtime")).equals("")) {
                                                            modal_menuItem.lastupdatedtime_AvlDetails = "0";

                                                        } else if (String.valueOf(json.get("lastupdatedtime")).equals("\r")) {

                                                            modal_menuItem.lastupdatedtime_AvlDetails = "0";

                                                        }

                                                    } else {
                                                        modal_menuItem.lastupdatedtime_AvlDetails = "0";


                                                    }


                                                    if (json.has("menuitemkey")) {
                                                        modal_menuItem.menuitemkey_AvlDetails = String.valueOf(json.get("menuitemkey"));
                                                        if (String.valueOf(json.get("menuitemkey")).equals("")) {
                                                            modal_menuItem.menuitemkey_AvlDetails = "0";

                                                        } else if (String.valueOf(json.get("menuitemkey")).equals("\r")) {

                                                            modal_menuItem.menuitemkey_AvlDetails = "0";

                                                        }

                                                    } else {
                                                        modal_menuItem.menuitemkey_AvlDetails = "0";


                                                    }


                                                    if (json.has("stockbalance")) {
                                                        modal_menuItem.stockbalance_AvlDetails = String.valueOf(json.get("stockbalance"));
                                                        if (String.valueOf(json.get("stockbalance")).equals("")) {
                                                            modal_menuItem.stockbalance_AvlDetails = "0";

                                                        } else if (String.valueOf(json.get("stockbalance")).equals("\r")) {

                                                            modal_menuItem.stockbalance_AvlDetails = "0";

                                                        }
                                                    } else {
                                                        modal_menuItem.stockbalance_AvlDetails = "0";


                                                    }

                                                    if (json.has("stockincomingkey")) {
                                                        modal_menuItem.stockincomingkey_AvlDetails = String.valueOf(json.get("stockincomingkey"));

                                                    } else {
                                                        modal_menuItem.stockincomingkey_AvlDetails = "";


                                                    }
                                                    if (json.has("vendorkey")) {
                                                        modal_menuItem.vendorkey_AvlDetails = String.valueOf(json.get("vendorkey"));

                                                    } else {
                                                        modal_menuItem.vendorkey_AvlDetails = "";

                                                    }
                                                }
                                            }

                                        }
                                        else{
                                            menuItemkeyFromStockAvl = "";
                                        }
                                    }
                                    catch (Exception e){
                                        menuItemkeyFromStockAvl = "";
                                        e.printStackTrace();
                                    }



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                                    Log.d(Constants.TAG, "e: " + e.getMessage());
                                    Log.d(Constants.TAG, "e: " + e.toString());

                                }
                                if(arrayLength - i1 ==1) {
                                    final SharedPreferences sharedPreferences = getSharedPreferences("MenuList", MODE_PRIVATE);

                                    Gson gson = new Gson();
                                    String json = gson.toJson(MenuItem);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("MenuList", json);
                                    editor.apply();
                                    getMenuItemsbasedOnSubCtgy(SubCtgyKey);

                                }

                            }



                        } catch (JSONException e) {
                            Adjusting_Widgets_Visibility(false);

                            e.printStackTrace();
                        }














                    }catch(Exception e){
                        Adjusting_Widgets_Visibility(false);

                        e.printStackTrace();
                    }




                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {
                    Log.d(TAG, "Error: " + error.getLocalizedMessage());
                    Log.d(TAG, "Error: " + error.getMessage());
                    Log.d(TAG, "Error: " + error.toString());

                    if (error instanceof TimeoutError) {
                        errorCode = "Timeout Error";
                    } else if (error instanceof NoConnectionError) {
                        errorCode = "No Connection Error";

                    } else if (error instanceof AuthFailureError) {
                        errorCode = "Auth_Failure Error";
                    } else if (error instanceof ServerError) {
                        errorCode = "Server Error";
                    } else if (error instanceof NetworkError) {
                        errorCode = "Network Error";
                    } else if (error instanceof ParseError) {
                        errorCode = "Parse Error";
                    }
                    Toast.makeText(ChangeMenuItemStatus_AllowNegativeStock_Settings.this,"Error in Getting  Menuitem stock Avl details  error code :  "+errorCode,Toast.LENGTH_LONG).show();
                    Adjusting_Widgets_Visibility(false);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(dialog.isShowing()){

                                }
                                else {
                                    title.setText(new StringBuilder().append("").append(errorCode).append(" .  Please Try Again !!!! ").toString());
                                    restartAgain.setText("Click to Retry !!!");

                                    restartAgain.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            SharedPreferences preferences =getSharedPreferences("MenuItemStockAvlDetails",Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.clear();
                                            editor.apply();


                                            dialog.cancel();
                                            checkForInternetConnectionAndGetMenuItemAndMobileAppData();


                                        }
                                    });


                                    dialog.show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    error.printStackTrace();
                }
            }) {


                @NonNull
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");


                    return params;
                }
            };
            RetryPolicy policy = new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);

            // Make the request
            Volley.newRequestQueue(ChangeMenuItemStatus_AllowNegativeStock_Settings.this).add(jsonObjectRequest);

        }


    private void checkForInternetConnectionAndGetMenuItemAndMobileAppData() {
        try {
            if (isConnected()) {
                //Toast.makeText(getApplicationContext(), "Internet Connected", Toast.LENGTH_SHORT).show();
                //  Log.i("Start time ",getDate_and_time());
                String time = getDate_and_time();
                Log.i("1111111111Statrt time ",getDate_and_time());




                getMenuItemStockAvlDetails();




            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(dialog.isShowing()){

                            }
                            else {
                                title.setText("Poor Internet Connection .Please Try Again !!!! ");
                                restartAgain.setText("Click to Retry !!!");

                                restartAgain.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.cancel();

                                        checkForInternetConnectionAndGetMenuItemAndMobileAppData();

                                    }
                                });


                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }




    }







    private void ChangeAvailability_subctgywise_InMenuItemDB(boolean isChecked) {
        String checked_or_not =  String.valueOf(isChecked).toUpperCase();
        String dateandtime = getDate_and_time();
        isSubCtgySwitchTouched =false;
        loadingPanel.setVisibility(View.VISIBLE);
        loadingpanelmask.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("subctgykey", SubCtgyKey);
            jsonObject.put("itemavailability",checked_or_not);
            jsonObject.put("vendorkey", vendorkey);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "change menu  jsonObject  " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_Update_ChangeMenuItemAvailability_SubCtgywise,
                jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                String message ="";
                Log.d(TAG, "change menu Item " + response.length());
                try {
                    message = response.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                uploadMenuAvailabilityStatusTranscationinDB(UserPhoneNumber,SubCtgyName, Boolean.parseBoolean(checked_or_not),SubCtgyKey,vendorkey,dateandtime,"",message, "", false, "",true);




                try {
                    for (int i = 0; i < MenuItem.size(); i++) {
                        Modal_MenuItem_Settings modal_menuItemSettings = MenuItem.get(i);
                        String tmcsubctgykey = modal_menuItemSettings.getTmcsubctgykey().toString();
                        if (tmcsubctgykey.equals(SubCtgyKey)) {
                            String availability_avlDetails = "";
                            availability_avlDetails =String.valueOf(modal_menuItemSettings.getItemavailability_AvlDetails()).toUpperCase();
                            if(!availability_avlDetails.equals("") && !availability_avlDetails.equals("null") && ! availability_avlDetails.equals(null) && ! availability_avlDetails.equals("NULL")) {
                                modal_menuItemSettings.setItemavailability_AvlDetails(checked_or_not);
                                if(checked_or_not.toUpperCase().equals("FALSE")){
                                    modal_menuItemSettings.setAllownegativestock("FALSE");

                                }
                            }

                            modal_menuItemSettings.setItemavailability(checked_or_not);

                            adapter_Change_menutem_availabilityWithAllowNegativeStock_settings.notifyDataSetChanged();

                        }


                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    for (int i = 0; i < displaying_menuItems.size(); i++) {
                        Modal_MenuItem_Settings modal_menuItemSettings = displaying_menuItems.get(i);
                        String tmcsubctgykey = modal_menuItemSettings.getTmcsubctgykey().toString();
                        if (tmcsubctgykey.equals(SubCtgyKey)) {
                            String availability_avlDetails = "";
                            availability_avlDetails =String.valueOf(modal_menuItemSettings.getItemavailability_AvlDetails()).toUpperCase();
                            if(!availability_avlDetails.equals("") && !availability_avlDetails.equals("null") && ! availability_avlDetails.equals(null) && ! availability_avlDetails.equals("NULL")) {
                                modal_menuItemSettings.setItemavailability_AvlDetails(checked_or_not);
                                if(checked_or_not.toUpperCase().equals("FALSE")){
                                    modal_menuItemSettings.setAllownegativestock("FALSE");

                                }
                            }
                            modal_menuItemSettings.setItemavailability(checked_or_not);
                            adapter_Change_menutem_availabilityWithAllowNegativeStock_settings.notifyDataSetChanged();

                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }





                /*
                JSONArray JArray = null;
                try {
                    JArray = response.getJSONArray("content");

                    int i1 = 0;
                    int arrayLength = JArray.length();
                    Log.d("Constants.TAG", "change menu Item Response: " + arrayLength);


                    for (; i1 < (arrayLength); i1++) {

                        try {
                            JSONObject json = JArray.getJSONObject(i1);
                            Log.d(Constants.TAG, "change menu Item menuListFull: " + json);
                            String key = json.getString("key");
                            try {
                                for (int i = 0; i < MenuItem.size(); i++) {
                                    Modal_MenuItem_Settings modal_menuItemSettings = MenuItem.get(i);
                                    String MenuItemkey = modal_menuItemSettings.getKey();
                                   if (MenuItemkey.equals(key)) {
                                       String availability_avlDetails = "";
                                       availability_avlDetails =String.valueOf(modal_menuItemSettings.getItemavailability_AvlDetails()).toUpperCase();
                                       if(!availability_avlDetails.equals("") && !availability_avlDetails.equals("null") && ! availability_avlDetails.equals(null) && ! availability_avlDetails.equals("NULL")) {
                                           modal_menuItemSettings.setItemavailability_AvlDetails(checked_or_not);
                                                if(checked_or_not.toUpperCase().equals("FALSE")){
                                                    modal_menuItemSettings.setAllownegativestock("FALSE");

                                                }
                                       }

                                       modal_menuItemSettings.setItemavailability(checked_or_not);

                                        adapter_Change_menutem_availabilityWithAllowNegativeStock_settings.notifyDataSetChanged();

                                    }


                                }
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                            try {
                                for (int i = 0; i < displaying_menuItems.size(); i++) {
                                    Modal_MenuItem_Settings modal_menuItemSettings = displaying_menuItems.get(i);
                                    String MenuItemkey = modal_menuItemSettings.getKey();
                                    if (MenuItemkey.equals(key)) {
                                        String availability_avlDetails = "";
                                        availability_avlDetails =String.valueOf(modal_menuItemSettings.getItemavailability_AvlDetails()).toUpperCase();
                                        if(!availability_avlDetails.equals("") && !availability_avlDetails.equals("null") && ! availability_avlDetails.equals(null) && ! availability_avlDetails.equals("NULL")) {
                                            modal_menuItemSettings.setItemavailability_AvlDetails(checked_or_not);
                                            if(checked_or_not.toUpperCase().equals("FALSE")){
                                                modal_menuItemSettings.setAllownegativestock("FALSE");

                                            }
                                        }
                                        modal_menuItemSettings.setItemavailability(checked_or_not);
                                        adapter_Change_menutem_availabilityWithAllowNegativeStock_settings.notifyDataSetChanged();

                                    }
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                            //Log.d(Constants.TAG, "e: " + e.getMessage());
                            //Log.d(Constants.TAG, "e: " + e.toString());
                            Log.d(Constants.TAG, "change menu Item menuListFull11111111111: " + e);

                        }


                    }
                    savedMenuIteminSharedPrefrences(MenuItem);
                    adapter_Change_menutem_availabilityWithAllowNegativeStock_settings.notifyDataSetChanged();

                    loadingpanelmask.setVisibility(View.GONE);
                    loadingPanel.setVisibility(View.GONE);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(Constants.TAG, "change menu Item menuListFull: 22222 " + e);

                }
*/







            }


        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {

                //Log.d(TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(TAG, "Error: " + error.getMessage());
                //Log.d(TAG, "Error: " + error.toString());
                Log.d(Constants.TAG, "change menu Item menuListFull: 333333333333333333" + error);
                uploadMenuAvailabilityStatusTranscationinDB(UserPhoneNumber,SubCtgyName, Boolean.parseBoolean(checked_or_not),SubCtgyKey,vendorkey,dateandtime,"","Api Call Failed", "", false, "",true);

                error.printStackTrace();
            }
        }) {


            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("storeid", vendorkey);

                return params;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(60000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(ChangeMenuItemStatus_AllowNegativeStock_Settings.this).add(jsonObjectRequest);




    }

    private void getMenuItemArrayFromSharedPreferences() {
        final SharedPreferences sharedPreferencesMenuitem = getApplicationContext().getSharedPreferences("MenuList", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferencesMenuitem.getString("MenuList", "");
        if (json.isEmpty()) {
            Toast.makeText(getApplicationContext(),"There is something error",Toast.LENGTH_LONG).show();
        } else {
            Type type = new TypeToken<List<Modal_MenuItem_Settings>>() {
            }.getType();
            MenuItem  = gson.fromJson(json, type);
        }

    }
    private void getMarinadeMenuItem(String vendorkey) {
        loadingPanel.setVisibility(View.VISIBLE);
        loadingpanelmask.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofMarinadeMenuItems,
                null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {


                //Log.d(TAG, "response for addMenuListAdaptertoListView: " + response.length());


                loadingpanelmask.setVisibility(View.GONE);
                loadingPanel.setVisibility(View.GONE);

                JSONArray JArray = null;
                try {
                    JArray = response.getJSONArray("content");

                    int i1 = 0;
                    int arrayLength = JArray.length();
                    //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                    for (; i1 < (arrayLength); i1++) {

                        try {
                            JSONObject json = JArray.getJSONObject(i1);
                            Modal_MenuItem_Settings newOrdersPojoClass = new Modal_MenuItem_Settings();
                            newOrdersPojoClass.barcode = String.valueOf(json.get("barcode"));
                            newOrdersPojoClass.key = String.valueOf(json.get("key"));
                            newOrdersPojoClass.itemavailability = String.valueOf(json.get("itemavailability"));
                            newOrdersPojoClass.itemuniquecode = String.valueOf(json.get("itemuniquecode"));
                            newOrdersPojoClass.displayno =String.valueOf(json.get("displayno"));
                            marinadeMenuList.add(newOrdersPojoClass);

                            //Log.d(Constants.TAG, "convertingJsonStringintoArray menuListFull: " + marinadeMenuList);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                            //Log.d(Constants.TAG, "e: " + e.getMessage());
                            //Log.d(Constants.TAG, "e: " + e.toString());

                        }


                    }

                    AddMarinadeDetailsinMenuItem(marinadeMenuList);


                } catch (JSONException e) {
                    e.printStackTrace();
                }








            }


        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {

                //Log.d(TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(TAG, "Error: " + error.getMessage());
                //Log.d(TAG, "Error: " + error.toString());
                error.printStackTrace();
            }
        }) {


            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("storeid", vendorkey);

                return params;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(60000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(ChangeMenuItemStatus_AllowNegativeStock_Settings.this).add(jsonObjectRequest);




    }
    private void AddMarinadeDetailsinMenuItem(List<Modal_MenuItem_Settings> marinadeMenuList) {
        for(int menuLoopcount = 0 ; menuLoopcount<MenuItem.size();menuLoopcount++) {
            Modal_MenuItem_Settings modal_MenuItem_pojo_class = MenuItem.get(menuLoopcount);

            String menuItemkey = String.valueOf(modal_MenuItem_pojo_class.getKey());
            String itemuniquecode = String.valueOf(modal_MenuItem_pojo_class.getItemuniquecode());
            for (int marinadeLoopCount = 0; marinadeLoopCount < marinadeMenuList.size(); marinadeLoopCount++) {
                Modal_MenuItem_Settings modal_marinademenuItem_pojo_class = marinadeMenuList.get(marinadeLoopCount);
                String marinadeItem_itemuniquecode = modal_marinademenuItem_pojo_class.getItemuniquecode();
                if (marinadeItem_itemuniquecode.equals(itemuniquecode)) {
                    modal_marinademenuItem_pojo_class.setIsMarinadeItem(true);
                    modal_MenuItem_pojo_class.setMarinadeBarcode(String.valueOf(modal_marinademenuItem_pojo_class.getBarcode()));
                    modal_MenuItem_pojo_class.setMarinadeItemAvailability(String.valueOf(modal_marinademenuItem_pojo_class.getItemavailability()));
                    modal_MenuItem_pojo_class.setMarinadeItemUniqueCode(String.valueOf(modal_marinademenuItem_pojo_class.getItemuniquecode()));
                    String marinadeItemKey = modal_marinademenuItem_pojo_class.getKey();
                    modal_MenuItem_pojo_class.setMarinadeKey(String.valueOf(modal_marinademenuItem_pojo_class.getKey()));

                }

            }
        }
        getMenuItemsbasedOnSubCtgy(SubCtgyKey);


    }










    private void getMenuCategoryList() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetMenuCategory,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {
                    //Log.d(Constants.TAG, "response from subCtgy: " + response.get("content"));
                    result  = response.getJSONArray("content");

                    JSONArray content = (JSONArray) response.get("content");
                    JSONArray jArray = (JSONArray) content;
                    if (jArray != null) {
                        for (int i = 0; i < jArray.length(); i++) {
                            try {
                                JSONObject json = content.getJSONObject(i);
                                String ctgyname = String.valueOf(json.get("tmcctgyname"));

                                String key = String.valueOf(json.get("key"));
                                String subctgyname = String.valueOf(json.get("subctgyname"));
                                String displayNo = String.valueOf(json.get("displayno"));
                                //Log.d(Constants.TAG, "subctgyname from subCtgy: " + subctgyname);
                                Modal_SubCtgyList  modal_subCtgyList = new Modal_SubCtgyList();
                                modal_subCtgyList.setKey(key);
                                modal_subCtgyList.setSubCtgyName(subctgyname);

                                if (!subCtgyName_arrayList.contains(subctgyname)) {
                                    subCtgyName_arrayList.add(modal_subCtgyList);

                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter_subCtgy_spinner = new ArrayAdapter<Modal_SubCtgyList>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, subCtgyName_arrayList);
                subCtgyItem_spinner.setAdapter(adapter_subCtgy_spinner);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());

                error.printStackTrace();
            }
        }) {


            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("modulename", "SubCategory");

                return params;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(ChangeMenuItemStatus_AllowNegativeStock_Settings.this).add(jsonObjectRequest);

    }

    private String getVendorData(int position,String fieldName){
        String data="";
        try {
            JSONObject json = result.getJSONObject(position);
            data = json.getString(fieldName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }


    void ChangeMenuitemAvailabilityStatus(String menuItemkey, String availability, String barcode, String menuItemStockAvlDetailskey) {
        for (int i = 0; i < MenuItem.size(); i++) {
            Modal_MenuItem_Settings modal_menuItemSettings = MenuItem.get(i);
            String MenuItemkey = modal_menuItemSettings.getKey();
            String MenuItemName = modal_menuItemSettings.getItemname();
            String MenuItemSubCtgykey = modal_menuItemSettings.getTmcsubctgykey();
            if (MenuItemkey.equals(menuItemkey)) {
                modal_menuItemSettings.setItemavailability(availability);
                ChangeAvailabilityInMenuItemDB(MenuItemkey,availability,MenuItemName,MenuItemSubCtgykey,menuItemStockAvlDetailskey);
                savedMenuIteminSharedPrefrences(MenuItem);

            }


        }

    }

    private void savedMenuIteminSharedPrefrences(List<Modal_MenuItem_Settings> menuItem) {
        final SharedPreferences sharedPreferencesMenuitem = getApplicationContext().getSharedPreferences("MenuList", MODE_PRIVATE);


        Gson gson = new Gson();
        String json = gson.toJson(menuItem);
        SharedPreferences.Editor editor = sharedPreferencesMenuitem.edit();
        editor.putString("MenuList",json );
        editor.apply();
    }

    private void ChangeAvailabilityInMenuItemDB(String menuItemKey, String availability, String menuItemName, String menuItemSubCtgykey , String menuItemStockAvlDetailskey) {
        Adjusting_Widgets_Visibility(true);
        //Log.d(TAG, " uploaduserDatatoDB.");
        String dateandtime  = getDate_and_time();

        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("key", menuItemKey);
            jsonObject.put("itemavailability", availability);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_updateMenuItemDetails,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                //Log.d(Constants.TAG, "Response: " + response);
                String message ="";
                Log.d(TAG, "change menu Item " + response.length());
                try {
                    message = response.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(menuItemStockAvlDetailskey.equals("") || menuItemStockAvlDetailskey.equals("null") || menuItemStockAvlDetailskey.equals(null) || menuItemStockAvlDetailskey.equals("NULL")) {

                    uploadMenuAvailabilityStatusTranscationinDB(UserPhoneNumber, menuItemName, Boolean.parseBoolean(availability), menuItemSubCtgykey, vendorkey, dateandtime, menuItemKey, message, "", false, "",false);
                }
                Adjusting_Widgets_Visibility(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Adjusting_Widgets_Visibility(false);
                uploadMenuAvailabilityStatusTranscationinDB(UserPhoneNumber,menuItemName, Boolean.parseBoolean(availability),menuItemSubCtgykey,vendorkey,dateandtime,menuItemKey,"Api Call Failed", "", false, "",false);

                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());

                error.printStackTrace();
            }
        }) {
            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");

                return params;
            }
        };



        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(this).add(jsonObjectRequest);


    }
    private void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void closeSearchBarEditText() {
        subtitle_Textview.setVisibility(View.VISIBLE);
        searchButton.setVisibility(View.VISIBLE);
        searchBarCloseButton.setVisibility(View.GONE);
        searchbarEdit.setVisibility(View.GONE);
        subCtgyMenuSwitch_Layout.setVisibility(View.VISIBLE);
        subctgy_on_Off_Switch.setVisibility(View.VISIBLE);
    }

    private void showSearchBarEditText() {
        subtitle_Textview.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);
        searchBarCloseButton.setVisibility(View.VISIBLE);
        searchbarEdit.setVisibility(View.VISIBLE);
        subCtgyMenuSwitch_Layout.setVisibility(View.GONE);
        subctgy_on_Off_Switch.setVisibility(View.GONE);

    }
    private void showKeyboard(final EditText editText) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                editText.setSelection(editText.getText().length());
            }
        },0);
    }






    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    private void uploadMenuAvailabilityStatusTranscationinDB(String userPhoneNumber, String menuItemName, boolean availability, String menuItemSubCtgykey, String vendorkey, String dateandtime, String menuItemKey, String message, String menuItemStockAvlDetailskey, boolean allowNegative, String itemStockAvlDetailskey, boolean issubctgyavailabilitychanged) {


        Adjusting_Widgets_Visibility(true);
        //Log.d(TAG, " uploaduserDatatoDB.");
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("itemname", menuItemName);
            jsonObject.put("status", availability);
            jsonObject.put("tmcsubctgykey", menuItemSubCtgykey);
            jsonObject.put("transactiontime", dateandtime);
            jsonObject.put("mobileno", userPhoneNumber);
            jsonObject.put("vendorkey", vendorkey);
            jsonObject.put("menuitemkey", menuItemKey);
            jsonObject.put("transcationstatus", message);
            jsonObject.put("issubctgyavailabilitychanged",issubctgyavailabilitychanged);
            try {
                if (!menuItemStockAvlDetailskey.equals("")) {
                    jsonObject.put("menuitemstockavldetailskey", menuItemStockAvlDetailskey);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
            if ((!menuItemStockAvlDetailskey.equals("")) ) {
                jsonObject.put("allownegativestock", allowNegative);

            }

        }
            catch (JSONException e) {
                e.printStackTrace();
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addMenuavailabilityTransaction,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                //Log.d(Constants.TAG, "Response: " + response);
                Adjusting_Widgets_Visibility(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Adjusting_Widgets_Visibility(false);

                Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "Error: " + error.getMessage());
                Log.d(Constants.TAG, "Error: " + error.toString());

                error.printStackTrace();
            }
        }) {
            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");

                return params;
            }
        };



        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(this).add(jsonObjectRequest);


    }


    private void getMenuItemsbasedOnSubCtgy(String subCtgykey) {
        Adjusting_Widgets_Visibility(true);
        total_no_item_availability =0;
        total_no_of_item =0;
        displaying_menuItems.clear();
        for(int i=0;i<MenuItem.size();i++){
            Modal_MenuItem_Settings modal_menuItemSettings = MenuItem.get(i);
            String menuSubCtgy = modal_menuItemSettings.getTmcsubctgykey();
            if(menuSubCtgy.equals(subCtgykey)) {
                String inventoryDetails = modal_menuItemSettings.getInventorydetails().toString();
                Modal_MenuItem_Settings selected_CtgyItems = new Modal_MenuItem_Settings();

                String MenuItemName;

                if(!inventoryDetails.equals("") &&(! inventoryDetails.toString().equals("nil") )  ) {
                    //  recycler.setVisibility(View.VISIBLE);
                    //menuitem_InventoryDetailsJson.clear();
                    String menuItemKeyFromInventoryDetails="",itemnameFromInventoryDetails ="",relationtypeFromInventoryDetails ="";
                    double grossweightinGramsFromInventoryDetails=0, netweightingramsFromInventoryDetails = 0;
                    boolean isshowavailabilityFromInventoryDetails = false;

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


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                        if(!relationtypeFromInventoryDetails.toUpperCase().equals("PARENT")) {



                            selected_CtgyItems.key = String.valueOf(modal_menuItemSettings.getKey());
                            selected_CtgyItems.key_AvlDetails = String.valueOf(modal_menuItemSettings.getKey_AvlDetails());
                            selected_CtgyItems.itemname = String.valueOf(modal_menuItemSettings.getItemname());
                            selected_CtgyItems.menuItemId = String.valueOf(modal_menuItemSettings.getMenuItemId());
                            selected_CtgyItems.tmcsubctgykey = String.valueOf(modal_menuItemSettings.getTmcsubctgykey());
                            selected_CtgyItems.barcode = String.valueOf(modal_menuItemSettings.getBarcode());
                            selected_CtgyItems.itemuniquecode = String.valueOf(modal_menuItemSettings.getItemuniquecode());
                            selected_CtgyItems.displayno = String.valueOf(modal_menuItemSettings.getDisplayno());
                            selected_CtgyItems.itemavailability_AvlDetails = String.valueOf(modal_menuItemSettings.getItemavailability_AvlDetails());
                            selected_CtgyItems.allownegativestock = String.valueOf(modal_menuItemSettings.getAllownegativestock());
                            selected_CtgyItems.itemavailability = String.valueOf(modal_menuItemSettings.getItemavailability());
                            selected_CtgyItems.inventorydetails = String.valueOf(modal_menuItemSettings.getInventorydetails());
                            selected_CtgyItems.stockbalance_AvlDetails = String.valueOf(modal_menuItemSettings.getStockbalance_AvlDetails());

                            try {
                                selected_CtgyItems.marinadeKey = String.valueOf(modal_menuItemSettings.getMarinadeKey());
                                selected_CtgyItems.isMarinadeItem = true;
                                selected_CtgyItems.marinadeItemAvailability = String.valueOf(modal_menuItemSettings.getMarinadeItemAvailability());
                                selected_CtgyItems.marinadeBarcode = String.valueOf(modal_menuItemSettings.getMarinadeBarcode());
                                selected_CtgyItems.marinadeItemUniqueCode = String.valueOf(modal_menuItemSettings.getMarinadeItemUniqueCode());
                            } catch (Exception e) {
                                selected_CtgyItems.marinadeKey = "";
                                selected_CtgyItems.marinadeItemAvailability = "";
                                selected_CtgyItems.marinadeBarcode = "";
                                selected_CtgyItems.marinadeItemUniqueCode = "";
                                selected_CtgyItems.isMarinadeItem = false;
                            }
                            if (String.valueOf(modal_menuItemSettings.getItemavailability()).equals("TRUE")) {
                                total_no_item_availability = total_no_item_availability + 1;
                            }

                            displaying_menuItems.add(selected_CtgyItems);

                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }



                }
                else {




                    selected_CtgyItems.key = String.valueOf(modal_menuItemSettings.getKey());
                    selected_CtgyItems.key_AvlDetails = String.valueOf(modal_menuItemSettings.getKey_AvlDetails());
                    selected_CtgyItems.itemname = String.valueOf(modal_menuItemSettings.getItemname());
                    selected_CtgyItems.menuItemId = String.valueOf(modal_menuItemSettings.getMenuItemId());
                    selected_CtgyItems.tmcsubctgykey = String.valueOf(modal_menuItemSettings.getTmcsubctgykey());
                    selected_CtgyItems.barcode = String.valueOf(modal_menuItemSettings.getBarcode());
                    selected_CtgyItems.itemuniquecode = String.valueOf(modal_menuItemSettings.getItemuniquecode());
                    selected_CtgyItems.displayno = String.valueOf(modal_menuItemSettings.getDisplayno());
                    selected_CtgyItems.itemavailability_AvlDetails = String.valueOf(modal_menuItemSettings.getItemavailability_AvlDetails());
                    selected_CtgyItems.allownegativestock = String.valueOf(modal_menuItemSettings.getAllownegativestock());
                    selected_CtgyItems.itemavailability = String.valueOf(modal_menuItemSettings.getItemavailability());
                    selected_CtgyItems.inventorydetails = String.valueOf(modal_menuItemSettings.getInventorydetails());
                    selected_CtgyItems.stockbalance_AvlDetails = String.valueOf(modal_menuItemSettings.getStockbalance_AvlDetails());

                    try {
                        selected_CtgyItems.marinadeKey = String.valueOf(modal_menuItemSettings.getMarinadeKey());
                        selected_CtgyItems.isMarinadeItem = true;
                        selected_CtgyItems.marinadeItemAvailability = String.valueOf(modal_menuItemSettings.getMarinadeItemAvailability());
                        selected_CtgyItems.marinadeBarcode = String.valueOf(modal_menuItemSettings.getMarinadeBarcode());
                        selected_CtgyItems.marinadeItemUniqueCode = String.valueOf(modal_menuItemSettings.getMarinadeItemUniqueCode());
                    } catch (Exception e) {
                        selected_CtgyItems.marinadeKey = "";
                        selected_CtgyItems.marinadeItemAvailability = "";
                        selected_CtgyItems.marinadeBarcode = "";
                        selected_CtgyItems.marinadeItemUniqueCode = "";
                        selected_CtgyItems.isMarinadeItem = false;
                    }
                    if (String.valueOf(modal_menuItemSettings.getItemavailability()).equals("TRUE")) {
                        total_no_item_availability = total_no_item_availability + 1;
                    }

                    displaying_menuItems.add(selected_CtgyItems);
                }

                //Log.d(Constants.TAG, "displaying_menuItems: " + String.valueOf(modal_menuItemSettings.getItemname()));

            }
                //  itemAvailabilityCount_textWidget.setText("Out of "+String.valueOf(total_no_of_item)+" Items / "+String.valueOf(total_no_item_availability)+" Items Available");
             if(MenuItem.size()-1==i){
                 callAdapter(displaying_menuItems);
                  //hide progress bar process will be done in Adapter file last line.
            }



        }





    }

    private void callAdapter(List<Modal_MenuItem_Settings> displaying_menuItems) {
        try {

            Collections.sort(displaying_menuItems, new Comparator<Modal_MenuItem_Settings>() {
                public int compare(final Modal_MenuItem_Settings object1, final Modal_MenuItem_Settings object2) {
                    String i2 = String.valueOf(object2.getItemname());
                    String i1 = String.valueOf(object1.getItemname());
                    return i1.compareTo(i2);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        total_no_of_item = displaying_menuItems.size();
        adapter_Change_menutem_availabilityWithAllowNegativeStock_settings = new Adapter_ChangeMenutem_AvailabilityWithAllowNegativeStock_settings(ChangeMenuItemStatus_AllowNegativeStock_Settings.this, displaying_menuItems, ChangeMenuItemStatus_AllowNegativeStock_Settings.this,isinventorycheck);
        MenuItemsListView.setAdapter(adapter_Change_menutem_availabilityWithAllowNegativeStock_settings);
        Adjusting_Widgets_Visibility(false);

        if(displaying_menuItems.size()<=0){

          //  itemAvailabilityCount_textWidget.setText("There is no MenuItem Under this SubCtgy");

        }

    }


    void Adjusting_Widgets_Visibility(boolean show) {
        if (show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);

        } else {
            loadingPanel.setVisibility(View.GONE);
            loadingpanelmask.setVisibility(View.GONE);

        }
    }

    public void ChangeMarinadeMenuitemAvailabilityStatus(String marinadeItemKey, String availability, String barcode) {
        Adjusting_Widgets_Visibility(true);

        //Log.d(TAG, " uploaduserDatatoDB.");
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("key", marinadeItemKey);
            jsonObject.put("itemavailability", availability);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_updateMarinadeMenuItemDetails,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                //Log.d(Constants.TAG, "Response: " + response);
                Adjusting_Widgets_Visibility(false);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Adjusting_Widgets_Visibility(false);

                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());

                error.printStackTrace();
            }
        }) {
            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");

                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(this).add(jsonObjectRequest);





    }



    public String getDate_and_time()
    {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);

        SimpleDateFormat day = new SimpleDateFormat("EEE", Locale.ENGLISH);
        day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String  CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String CurrentDatee = df.format(c);
        String   CurrentDate = CurrentDay+", "+CurrentDatee;


        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
        dfTime.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String   FormattedTime = dfTime.format(c);
        String   formattedDate = CurrentDay+", "+CurrentDatee+" "+FormattedTime;
        return formattedDate;
    }

    public void ChangeMenuitemStockAvlDetailsData(String menuItemStockAvlDetailskey, boolean allowNegative, boolean itemAvailability, String menuItemname, String menuItemkey, String tmcsubCtgykey) {
        Adjusting_Widgets_Visibility(true);
        //Log.d(TAG, " uploaduserDatatoDB.");
        String dateandtime  = getDate_and_time();

        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("key", menuItemStockAvlDetailskey);
            jsonObject.put("allownegativestock", allowNegative);
            jsonObject.put("itemavailability", itemAvailability);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_Update_MenuItemStockAvlDetails,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                //Log.d(Constants.TAG, "Response: " + response);
                String message =""; String MenuItemName="";
                Log.d(TAG, "change menu Item " + response.length());
                try {
                    message = response.getString("message");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(message.toUpperCase().equals("SUCCESS")){
                    for (int i = 0; i < MenuItem.size(); i++) {
                        Modal_MenuItem_Settings modal_menuItemSettings = MenuItem.get(i);
                        String MenuItemkey = "";
                                try {
                                    MenuItemkey =       modal_menuItemSettings.getKey_AvlDetails().toString();
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }

                                try{
                                    MenuItemName = modal_menuItemSettings.getItemname();

                                }
                                catch (Exception e ){
                                    e.printStackTrace();
                                }
                        try{
                            String MenuItemSubCtgykey = modal_menuItemSettings.getTmcsubctgykey();

                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }

                        String MenuItemSubCtgykey = modal_menuItemSettings.getTmcsubctgykey();
                        if (MenuItemkey.equals(String.valueOf(menuItemStockAvlDetailskey)) ){
                            modal_menuItemSettings.setItemavailability_AvlDetails(String.valueOf(itemAvailability));
                            modal_menuItemSettings.setAllownegativestock(String.valueOf(allowNegative));
                            adapter_Change_menutem_availabilityWithAllowNegativeStock_settings.notifyDataSetChanged();
                            savedMenuIteminSharedPrefrences(MenuItem);

                        }


                    }
                }
                        if((!menuItemStockAvlDetailskey.equals(null)) && (!menuItemStockAvlDetailskey.equals("null")) ) {
                            uploadMenuAvailabilityStatusTranscationinDB(UserPhoneNumber, menuItemname, itemAvailability, tmcsubCtgykey, vendorkey, dateandtime, menuItemkey, message, menuItemStockAvlDetailskey, allowNegative, MenuItemName,false);
                        }
                Adjusting_Widgets_Visibility(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Adjusting_Widgets_Visibility(false);
                uploadMenuAvailabilityStatusTranscationinDB(UserPhoneNumber,menuItemname, itemAvailability,tmcsubCtgykey,vendorkey,dateandtime,menuItemkey,"Api Call Failed", menuItemStockAvlDetailskey, allowNegative, "",false);

                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());

                error.printStackTrace();
            }
        }) {
            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");

                return params;
            }
        };



        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(this).add(jsonObjectRequest);













    }
}



/*
  private void changeStatusintheDeliverySlot(String status) {
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("key", deliverySlotKey);
            jsonObject.put("status", status);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, api_Update_DeliverySlotDetails,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    String message =  response.getString("message");
                    if(message.equals("success")) {
                        //Log.d(Constants.TAG, "Express Slot has been succesfully turned Off: " );
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());

                error.printStackTrace();
            }
        }) {


            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");

                return params;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(ChangeMenuItemStatus_AllowNegativeStock_Settings.this).add(jsonObjectRequest);











    }

    private void checkforVendorSlotDetails() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, api_GetDeliverySlotDetails+"?storeid="+vendorkey,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    JSONArray content = (JSONArray) response.get("content");
                    JSONArray jArray = (JSONArray) content;
                    if (jArray != null) {
                        for (int i = 0; i < jArray.length(); i++) {
                            try {
                                JSONObject json = content.getJSONObject(i);
                                String slotName = String.valueOf(json.get("slotname"));
                                slotName = slotName.toUpperCase();
                                if(slotName.equals(Constants.EXPRESS_DELIVERY_SLOTNAME)){
                                    deliverySlotKey= String.valueOf(json.get("key"));
                                    String status =String.valueOf(json.get("status"));
                                    status = status.toUpperCase();

                                    if(status.equals("ACTIVE")){
                                        vendorSlotAvailabiltySwitch.setChecked(true);
                                    }
                                    if(status.equals("INACTIVE")){
                                        vendorSlotAvailabiltySwitch.setChecked(false);

                                    }
                                }




                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());

                error.printStackTrace();
            }
        }) {


            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");

                return params;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(ChangeMenuItemStatus_AllowNegativeStock_Settings.this).add(jsonObjectRequest);




    }



    private List<Modal_MenuItem_Settings> getMenuItemfromString(String menulist) {
        List<Modal_MenuItem_Settings>MenuList=new ArrayList<>();
        if(!menulist.isEmpty()) {

            try {
                //converting jsonSTRING into array
                JSONObject jsonObject = new JSONObject(menulist);
                JSONArray JArray = jsonObject.getJSONArray("content");
                //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                int i1 = 0;
                int arrayLength = JArray.length();
                //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                for (; i1 < (arrayLength); i1++) {

                    try {
                        JSONObject json = JArray.getJSONObject(i1);
                        Modal_MenuItem_Settings newOrdersPojoClass = new Modal_MenuItem_Settings();
                        newOrdersPojoClass.itemname = String.valueOf(json.get("itemname"));
                        newOrdersPojoClass.key = String.valueOf(json.get("key"));
                        //Log.d("Constants.TAG", "out If : " + String.valueOf(json.get("itemname")));

                        if(String.valueOf(json.get("key")).equals("a065b1ce-0c12-4359-a593-97e85ddbb552")){
                            //Log.d("Constants.TAG", "in If : " + String.valueOf(json.get("itemname")));

                        }
                        newOrdersPojoClass.tmcsubctgykey = String.valueOf(json.get("tmcsubctgykey"));
                        newOrdersPojoClass.itemavailability = String.valueOf(json.get("itemavailability"));
                        newOrdersPojoClass.barcode = String.valueOf(json.get("barcode"));
                        newOrdersPojoClass.itemuniquecode = String.valueOf(json.get("itemuniquecode"));

                        //Log.d(TAG, "itemname of addMenuListAdaptertoListView: " + newOrdersPojoClass.portionsize);
                        MenuList.add(newOrdersPojoClass);

                        //Log.d(Constants.TAG, "convertingJsonStringintoArray menuListFull: " + MenuList);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        //Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                        //Log.d(Constants.TAG, "e: " + e.getMessage());
                        //Log.d(Constants.TAG, "e: " + e.toString());

                    }


                }

                //Log.d(Constants.TAG, "convertingJsonStringintoArray menuListFull: " + MenuList);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return MenuList;
    }
 */