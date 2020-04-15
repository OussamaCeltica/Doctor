package com.celtica.doctor.Food;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.celtica.doctor.Food.NutritionScale;
import com.celtica.doctor.R;

import java.util.ArrayList;

/**
 * Created by celtica on 15/08/18.
 */

public class ResultRecetteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    AppCompatActivity c;
    ArrayList<Recette> recettes;
    RecetteQueryType recetteQueryType;

    public ResultRecetteAdapter(ArrayList<Recette> recettes, AppCompatActivity c, RecetteQueryType recetteQueryType) {
        this.c = c;
        this.recettes = recettes;
        this.recetteQueryType=recetteQueryType;
    }

    public static class RecetteView extends RecyclerView.ViewHolder  {
        public TextView titre;
        public TextView calorie;
        public TextView fat;
        public TextView carbs;
        public TextView protein;
        ImageView img;
        LinearLayout detailButt;

        public RecetteView(View v) {
            super(v);
            titre=(TextView)v.findViewById(R.id.divRecetteNutri_titre);
            calorie=(TextView)v.findViewById(R.id.divRecetteNutri_calorie);
            fat=(TextView) v.findViewById(R.id.divRecetteNutri_fat);
            carbs=(TextView) v.findViewById(R.id.divRecetteNutri_carb);
            protein=(TextView) v.findViewById(R.id.divRecetteNutri_protein);
            img=(ImageView) v.findViewById(R.id.divRecetteNutri_img);
            detailButt=(LinearLayout) v.findViewById(R.id.divRecetteNutri_butt);
        }
    }

    public static class RecetteIngrView extends RecyclerView.ViewHolder  {
        public TextView titre;

        ImageView img;
        LinearLayout detailButt;

        public RecetteIngrView(View v) {
            super(v);
            titre=(TextView)v.findViewById(R.id.divRecetteNutri_titre);

            img=(ImageView) v.findViewById(R.id.divRecetteNutri_img);
            detailButt=(LinearLayout) v.findViewById(R.id.divRecetteNutri_butt);
        }
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (recetteQueryType == RecetteQueryType.PAR_NUTRITION){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.div_recette,parent,false);
            RecetteView vh = new RecetteView(v);
            return vh;
        }else {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.div_recette_ingr,parent,false);
            RecetteIngrView vh = new RecetteIngrView(v);
            return vh;
        }



    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if(recetteQueryType == RecetteQueryType.PAR_NUTRITION){
            ((RecetteView)holder).titre.setText(recettes.get(position).nom+"");
            //il faut garder l ordre lors d ajout des ingredient au recette ..
            ((RecetteView)holder).calorie.setText(recettes.get(position).nutritionInRecettes.get(0).value+"");
            ((RecetteView)holder).carbs.setText(recettes.get(position).nutritionInRecettes.get(1).value+"g");
            ((RecetteView)holder).fat.setText(recettes.get(position).nutritionInRecettes.get(2).value+"g");
            ((RecetteView)holder).protein.setText(recettes.get(position).nutritionInRecettes.get(3).value+"g");

            //region config recette image
            Glide.with(c)
                    .load(recettes.get(position).imgUrl)
                    .thumbnail(Glide.with(c).load(R.drawable.wait))
                    .apply(new RequestOptions().override(600, 400))
                    .error(Glide.with(c).load(R.drawable.err_img))
                    .into(((RecetteView)holder).img);
            //endregion

            ((RecetteView)holder).detailButt.setOnClickListener((view -> {
                DetailRecetteActivity.recette=recettes.get(position);
                c.startActivity(new Intent(c,DetailRecetteActivity.class));
            }));
        }else {
            ((RecetteIngrView)holder).titre.setText(recettes.get(position).nom+"");

            //region config recette image
            Glide.with(c)
                    .load(recettes.get(position).imgUrl)
                    .thumbnail(Glide.with(c).load(R.drawable.wait))
                    .apply(new RequestOptions().override(600, 400))
                    .error(Glide.with(c).load(R.drawable.err_img))
                    .into(((RecetteIngrView)holder).img);
            //endregion

            ((RecetteIngrView)holder).detailButt.setOnClickListener((view -> {
                DetailRecetteActivity.recette=recettes.get(position);
                c.startActivity(new Intent(c,DetailRecetteActivity.class));
            }));

        }

    }



    @Override
    public int getItemCount() {
        return recettes.size();
    }

}
