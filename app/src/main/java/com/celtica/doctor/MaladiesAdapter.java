package com.celtica.doctor;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by celtica on 15/08/18.
 */

public class MaladiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    AppCompatActivity c;
    ArrayList<Maladie> maladies;

    public MaladiesAdapter(ArrayList<Maladie> maladies, AppCompatActivity c) {
        this.c = c;
        this.maladies = maladies;
    }

    public static class MalasieView extends RecyclerView.ViewHolder  {
        public TextView name;
        LinearLayout body;

        public MalasieView(View v) {
            super(v);
            name=(TextView)v.findViewById(R.id.DivMaladie_name);
            body=(LinearLayout)v.findViewById(R.id.DivMaladie_body);



        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.div_maladie,parent,false);

        MalasieView vh = new MalasieView(v);
        return vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((MalasieView)holder).name.setText("-"+maladies.get(position).name);

        //region Voir le detail du maladie ..
        ((MalasieView)holder).body.setOnClickListener((view -> {

             MaladieDescription.maladie=maladies.get(position);
            c.startActivity(new Intent(c,MaladieDescription.class));
        }));
        //endregion

    }

    @Override
    public int getItemCount() {
        return maladies.size();
    }

    public void filterData(String filter){

    }
}
