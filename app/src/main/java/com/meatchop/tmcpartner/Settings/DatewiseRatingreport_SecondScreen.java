package com.meatchop.tmcpartner.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.meatchop.tmcpartner.R;

import java.util.ArrayList;

public class DatewiseRatingreport_SecondScreen extends AppCompatActivity {
ListView  detailedRatinglistview;
TextView totalRating_headingText,vendorNameTextview;
String vendorName;
ArrayList<Modal_RatingOrderDetails> ratingList = new ArrayList<>();
    public static LinearLayout loadingpanelmask;
    public static LinearLayout loadingPanel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datewise_ratingreport__second_screen);
        detailedRatinglistview = findViewById(R.id.detailedRatingListview);
        vendorNameTextview = findViewById(R.id.vendorNameTextview);
        totalRating_headingText = findViewById(R.id.totalRating_headingText);
        loadingpanelmask = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel = findViewById(R.id.loadingPanel_dailyItemWisereport);

        try{
            Bundle b = getIntent().getExtras();
            ratingList = (ArrayList<Modal_RatingOrderDetails>) b.getSerializable("ratingList");


        }
        catch (Exception e){
            e.printStackTrace();
        }
       try{
            totalRating_headingText.setText(String.valueOf(ratingList.size()));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{

            SharedPreferences sharedPreferences
                    = getSharedPreferences("VendorLoginData",
                    MODE_PRIVATE);

            //  vendorKey = sharedPreferences.getString("VendorKey", "");
            vendorName = sharedPreferences.getString("VendorName", "");
            vendorNameTextview.setText(vendorName);


        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{



            Adapter_DatewiseRatingReport adapter_datewiseRatingReport = new Adapter_DatewiseRatingReport(DatewiseRatingreport_SecondScreen.this,ratingList,DatewiseRatingreport_SecondScreen.this);
            detailedRatinglistview.setAdapter(adapter_datewiseRatingReport);






        }
        catch (Exception e){
            e.printStackTrace();
        }




    }





    void showProgressBar(boolean show) {
        if(show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);
            // manageOrders_ListView.setVisibility(View.GONE);

        }
        else {
            loadingpanelmask.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.GONE);
            //  manageOrders_ListView.setVisibility(View.VISIBLE);
        }

    }



}