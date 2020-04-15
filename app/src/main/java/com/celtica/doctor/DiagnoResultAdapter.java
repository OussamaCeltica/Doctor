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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by celtica on 15/08/18.
 */

public class DiagnoResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    AppCompatActivity c;
    ArrayList<DiagnoResult> DiagnoResults;

    public DiagnoResultAdapter(ArrayList<DiagnoResult> DiagnoResults, AppCompatActivity c) {
        this.c = c;
        this.DiagnoResults = DiagnoResults;
    }

    public static class DiagnoResultView extends RecyclerView.ViewHolder  {
        public TextView name;
        public TextView proba;
        public TextView special;
        CardView detailButt;

        public DiagnoResultView(View v) {
            super(v);
            name=(TextView)v.findViewById(R.id.diagnoResult_maladie);
            proba=(TextView)v.findViewById(R.id.diagnoResult_proba);
            special=(TextView)v.findViewById(R.id.diagnoResult_spec);
            detailButt=(CardView)v.findViewById(R.id.diagnoResult_deatailButt);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.div_diagnoresult,parent,false);

        DiagnoResultView vh = new DiagnoResultView(v);
        return vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        ((DiagnoResultView)holder).name.setText(DiagnoResults.get(position).maladie.name+"");
        ((DiagnoResultView)holder).proba.setText(DiagnoResults.get(position).probabilité+" %");
        ((DiagnoResultView)holder).special.setText(DiagnoResults.get(position).specialisation+"");

        //region Voir le detail du maladie ..
        ((DiagnoResultView)holder).detailButt.setOnClickListener((view -> {
            MaladieDescription.maladie=DiagnoResults.get(position).maladie;
            c.startActivity(new Intent(c,MaladieDescription.class));

        }));
        //endregion

    }

    @Override
    public int getItemCount() {
        return DiagnoResults.size();
    }

    public void filterData(String filter){

    }
}
