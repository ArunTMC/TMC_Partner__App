package com.meatchop.tmcpartner.Settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.meatchop.tmcpartner.R;

import java.util.ArrayList;

public class slotwiseitem_RecyclerviewAdapter extends RecyclerView.Adapter<slotwiseitem_RecyclerviewAdapter.ViewHolder> {

    private ArrayList<String> list = new ArrayList<>();
    private Context context;
    int blackgroundfinalposition=0, blackgroundstartposition=0;
    boolean startchangecolor = false;
    int numberX=0,numberY=0;
    public slotwiseitem_RecyclerviewAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_unit, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(list.get(position));
/*
        if((position)%8!=0){
            holder.textView.setBackground(context.getResources().getDrawable(R.drawable.orange_non_selected_button_background));

            if((position+1)%8==0){
                startchangecolor =true;
                blackgroundfinalposition = position+1+8+1;
                blackgroundstartposition = position+1;
            }
        }
        if(startchangecolor){
        if((position>=blackgroundstartposition)&&position<=blackgroundfinalposition){

                holder.textView.setBackground(context.getResources().getDrawable(R.drawable.button_background));
                if (position == (blackgroundfinalposition)) {
                    startchangecolor = false;
                    blackgroundfinalposition = 0;

            }

        }
        }

 */
    if(position!=0) {

        if(startchangecolor){
            if((position>=blackgroundstartposition)&&(position<=blackgroundfinalposition)) {
                holder.textView.setBackground(context.getResources().getDrawable(R.drawable.orange_border_button));
                if (position == blackgroundfinalposition - 1) {
                    startchangecolor = false;
                }
            }
        }
        else {


            if (position % 8 == 0) {



                if(position==8){
                holder.textView.setBackground(context.getResources().getDrawable(R.drawable.orange_border_button));
                    startchangecolor = true;

                }

                blackgroundstartposition = (blackgroundfinalposition+8);
                blackgroundfinalposition = blackgroundstartposition + 8;


            }
            else {

                    holder.textView.setBackground(context.getResources().getDrawable(R.drawable.orange_non_selected_button_background));



            }
        }
        if((blackgroundstartposition-1)==position){
            startchangecolor = true;

        }
    }
    else{
        holder.textView.setBackground(context.getResources().getDrawable(R.drawable.orange_non_selected_button_background));

    }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textview);
        }
    }
}