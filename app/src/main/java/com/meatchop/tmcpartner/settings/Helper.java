package com.meatchop.tmcpartner.settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.meatchop.tmcpartner.Constants;

public class Helper {
    public static void getListViewSize(ListView myListView, double screenInches, int extraSizeBasedOnListview) {
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
                    totalHeight += listItem.getMeasuredHeight() + 130;
          //          //Log.i("height of listItem: 1  ", String.valueOf(totalHeight));
                }
                else {
                    totalHeight += listItem.getMeasuredHeight() ;
              //      //Log.i("height of listItem: 1  ", String.valueOf(totalHeight));
                }
            }
            if (screenInches>Constants.default_mobileScreenSize){
                totalHeight += listItem.getMeasuredHeight();
                if(totalHeight>100){
                    totalHeight = totalHeight+2;

                }

            }


        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params=myListView.getLayoutParams();
        params.height=extraSizeBasedOnListview + totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount()));
        myListView.setLayoutParams(params);
        // print height of adapter on log
    }
}