package com.meatchop.tmcpartner.settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TokenNoShowingActivity extends AppCompatActivity {
String Itemname ="",Tokens,Quantity,cutname;
TextView itemname,itemquantity,ordercount,cutnameTextWidget,wholeItemName;
ListView tokennoListview;
ArrayList<String> tokenNo;
    private ArrayAdapter<String> listAdapter ;

    private RecyclerView recycler;
    private RecyclerView.LayoutManager manager;
    private slotwiseitem_RecyclerviewAdapter adapter;

    Adapter_filteredSlotWiseAppOrders_List adapter_filteredSlotWiseAppOrders_list;

    public  List<Modal_ManageOrders_Pojo_Class> filteredArray_menuItemKey_CutWeightdetails = new ArrayList<>();
    String filteredArray_menuItemKey_CutWeightdetailsString ="";
    ListView itemwithCutnameListview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.token_no_showing_activity);
        itemwithCutnameListview = findViewById(R.id.itemwithCutnameListview);
        wholeItemName = findViewById(R.id.wholeItemName);

        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        try {
            filteredArray_menuItemKey_CutWeightdetailsString = getIntent().getStringExtra("menuItemArray");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        JSONArray JArray = null;
        try {
            JArray = new JSONArray(filteredArray_menuItemKey_CutWeightdetailsString);

        //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
        int i1=0;
        int arrayLength = JArray.length();
        //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


        for(;i1<(arrayLength);i1++) {

            try {
                JSONObject json = JArray.getJSONObject(i1);
                Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();

                if(json.has("itemName")){
                    modal_manageOrders_pojo_class.itemName = json.getString("itemName");
                    Itemname = json.getString("itemName");
                }
                else{
                    modal_manageOrders_pojo_class.itemName = "";
                    Itemname = "";
                }

                if(json.has("tmcSubCtgyKey")){
                    modal_manageOrders_pojo_class.tmcSubCtgyKey = json.getString("tmcSubCtgyKey");;

                }
                else{
                    modal_manageOrders_pojo_class.tmcSubCtgyKey = "";
                }

                if(json.has("tmcSubCtgyName")){
                    modal_manageOrders_pojo_class.tmcSubCtgyName = json.getString("tmcSubCtgyName");;

                }
                else{
                    modal_manageOrders_pojo_class.tmcSubCtgyName = "";
                }

                if(json.has("quantity")){
                    modal_manageOrders_pojo_class.quantity = json.getString("quantity");

                }
                else{
                    modal_manageOrders_pojo_class.quantity = "";
                }

                if(json.has("tokenno")){
                    modal_manageOrders_pojo_class.tokenno = json.getString("tokenno");

                }
                else{
                    modal_manageOrders_pojo_class.tokenno = "";
                }

                if(json.has("ItemFinalWeight")){
                    modal_manageOrders_pojo_class.ItemFinalWeight = json.getString("ItemFinalWeight");
                }
                else{
                    modal_manageOrders_pojo_class.ItemFinalWeight = "";
                }

                if(json.has("menuitemkey_weight_cut")){
                    modal_manageOrders_pojo_class.menuitemkey_weight_cut = json.getString("menuitemkey_weight_cut");
                }
                else{
                    modal_manageOrders_pojo_class.menuitemkey_weight_cut = "";
                }

                if(json.has("cutname")){
                    modal_manageOrders_pojo_class.cutname = json.getString("cutname");
                }
                else{
                    modal_manageOrders_pojo_class.cutname = "";
                }

                if(json.has("menuItemKey")){
                    modal_manageOrders_pojo_class.menuItemKey = json.getString("menuItemKey");
                }
                else{
                    modal_manageOrders_pojo_class.menuItemKey = "";
                }
                filteredArray_menuItemKey_CutWeightdetails.add(modal_manageOrders_pojo_class);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
            wholeItemName .setText(String.valueOf(Itemname));
            adapter_filteredSlotWiseAppOrders_list  = new Adapter_filteredSlotWiseAppOrders_List(TokenNoShowingActivity.this,filteredArray_menuItemKey_CutWeightdetails);
            itemwithCutnameListview.setAdapter(adapter_filteredSlotWiseAppOrders_list);

        } catch (JSONException e) {
            e.printStackTrace();
        }




        /*


        itemname = findViewById(R.id.itemname);
        tokennoListview = findViewById(R.id.tokennoListview);
        itemquantity = findViewById(R.id.itemquantity);
        ordercount = findViewById(R.id.ordercount);
        cutnameTextWidget = findViewById(R.id.cutnameTextWidget);
        tokenNo = new ArrayList<>();
        recycler = findViewById(R.id.recyclerView);
        recycler.setHasFixedSize(true);
        manager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(manager);

        try{
            Itemname = getIntent().getStringExtra("itemname");
            Tokens = getIntent().getStringExtra("tokenNo");
            Quantity = getIntent().getStringExtra("quantity");
            cutname = getIntent().getStringExtra("cutname");
        }
        catch (Exception e){

            e.printStackTrace();
        }




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
                itemname.setText(Itemname);
                itemquantity.setText(MessageFormat.format("{0} ", Quantity));
            }
            catch (Exception e){
                e.printStackTrace();
            }


            try{
                cutnameTextWidget.setText(cutname);
            }
            catch (Exception e){
                e.printStackTrace();
            }




            try {
                ordercount.setText(MessageFormat.format("{0} Tokens", String.valueOf(tokenNo.size())));
            }
            catch(Exception e){
                e.printStackTrace();
            }


         //   listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tokenNo);
         //   tokennoListview.setAdapter(listAdapter);
            adapter = new slotwiseitem_RecyclerviewAdapter(tokenNo,this);
            recycler.setAdapter(adapter);

        }
        catch (Exception e){
            e.printStackTrace();
        }


         */


    }




}