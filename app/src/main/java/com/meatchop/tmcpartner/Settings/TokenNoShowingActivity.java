package com.meatchop.tmcpartner.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.R;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TokenNoShowingActivity extends AppCompatActivity {
String Itemname,Tokens,Quantity;
TextView itemname,itemquantity,ordercount;
ListView tokennoListview;
ArrayList<String> tokenNo;
    private ArrayAdapter<String> listAdapter ;




    private RecyclerView recycler;
    private RecyclerView.LayoutManager manager;
    private slotwiseitem_RecyclerviewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.token_no_showing_activity);
        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        itemname = findViewById(R.id.itemname);
        tokennoListview = findViewById(R.id.tokennoListview);
        itemquantity = findViewById(R.id.itemquantity);
        ordercount = findViewById(R.id.ordercount);
        tokenNo = new ArrayList<>();
        recycler = findViewById(R.id.recyclerView);
        recycler.setHasFixedSize(true);
        manager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(manager);

        try{
            Itemname = getIntent().getStringExtra("itemname");
            Tokens = getIntent().getStringExtra("tokenNo");
            Quantity = getIntent().getStringExtra("quantity");
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



    }

}