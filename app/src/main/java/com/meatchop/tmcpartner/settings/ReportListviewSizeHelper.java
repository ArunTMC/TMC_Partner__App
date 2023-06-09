package com.meatchop.tmcpartner.settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.meatchop.tmcpartner.Constants;

public class ReportListviewSizeHelper {
    public static void getListViewSize(ListView myListView, double screenInches) {
        ListAdapter myListAdapter=myListView.getAdapter();

        if (myListAdapter==null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight=0;
        for (int size=0; size < myListAdapter.getCount(); size++) {
            View listItem=myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
         //   //Log.i("height of listItem :", String.valueOf(totalHeight));
            if(screenInches< Constants.default_mobileScreenSize) {
                if (totalHeight < 500) {
                    totalHeight += listItem.getMeasuredHeight() + 55;
          //          //Log.i("height of listItem: 1  ", String.valueOf(totalHeight));
                }
                else {
                    totalHeight += listItem.getMeasuredHeight() + 20;
              //      //Log.i("height of listItem: 1  ", String.valueOf(totalHeight));
                }
            }
            if (screenInches>Constants.default_mobileScreenSize){
                totalHeight += listItem.getMeasuredHeight()+10;

            }


        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params=myListView.getLayoutParams();
        params.height=totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
        // print height of adapter on log
    }
}