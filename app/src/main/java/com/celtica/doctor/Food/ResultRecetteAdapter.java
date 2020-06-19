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
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

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
        public TextView calorie;
        public TextView fat;
        public TextView carbs;
        public TextView protein;

        ImageView img;
        LinearLayout detailButt;

        public RecetteIngrView(View v) {
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


    public static class AdsView extends RecyclerView.ViewHolder  {
        public TemplateView adsView;
        LinearLayout body;

        public AdsView(View v) {
            super(v);
            adsView=(TemplateView) v.findViewById(R.id.adView);
            // body=(LinearLayout)v.findViewById(R.id.DivMaladie_body);



        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 1){
            if (recetteQueryType == RecetteQueryType.PAR_NUTRITION){
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.div_recette,parent,false);
                RecetteView vh = new RecetteView(v);
                return vh;
            }else {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.div_recette_ingr,parent,false);
                RecetteIngrView vh = new RecetteIngrView(v);
                return vh;
            }
        }else {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.div_recette_ads,parent,false);
            AdsView vh = new AdsView(v);
            return vh;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if(position % 6 == 0 && position != 0){
            AdLoader adLoader = new AdLoader.Builder(c, "ca-app-pub-4807740938253496/5604801156")
                    .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                        @Override
                        public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                            NativeTemplateStyle styles = new
                                    NativeTemplateStyle.Builder().build();


                            ((AdsView)holder).adsView.setStyles(styles);
                            ((AdsView)holder).adsView.setNativeAd(unifiedNativeAd);

                        }
                    })
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());

        }else {
            if(recetteQueryType == RecetteQueryType.PAR_NUTRITION){
                ((RecetteView)holder).titre.setText(recettes.get(position-(position/6)).nom+"");
                //il faut garder l ordre lors d ajout des ingredient au recette ..
                ((RecetteView)holder).calorie.setText(recettes.get(position-(position/6)).nutritionInRecettes.get(0).value+"");
                ((RecetteView)holder).carbs.setText(recettes.get(position-(position/6)).nutritionInRecettes.get(1).value+"g");
                ((RecetteView)holder).fat.setText(recettes.get(position-(position/6)).nutritionInRecettes.get(2).value+"g");
                ((RecetteView)holder).protein.setText(recettes.get(position-(position/6)).nutritionInRecettes.get(3).value+"g");

                //region config recette image
                Glide.with(c)
                        .load(recettes.get(position-(position/6)).imgUrl)
                        .thumbnail(Glide.with(c).load(R.drawable.wait4))
                        .apply(new RequestOptions().override(600, 400))
                        .error(Glide.with(c).load(R.drawable.err_img))
                        .into(((RecetteView)holder).img);
                //endregion

                ((RecetteView)holder).detailButt.setOnClickListener((view -> {
                    DetailRecetteActivity.recette=recettes.get(position-(position/6));
                    c.startActivity(new Intent(c,DetailRecetteActivity.class));
                }));
            }else {
                ((RecetteIngrView)holder).titre.setText(recettes.get(position-(position/6)).nom+"");
                ((RecetteIngrView)holder).calorie.setText(recettes.get(position-(position/6)).nutritionInRecettes.get(0).value+"");
                ((RecetteIngrView)holder).carbs.setText(recettes.get(position-(position/6)).nutritionInRecettes.get(1).value+"g");
                ((RecetteIngrView)holder).fat.setText(recettes.get(position-(position/6)).nutritionInRecettes.get(2).value+"g");
                ((RecetteIngrView)holder).protein.setText(recettes.get(position-(position/6)).nutritionInRecettes.get(3).value+"g");

                //region config recette image
                Glide.with(c)
                        .load(recettes.get(position-(position/6)).imgUrl)
                        .thumbnail(Glide.with(c).load(R.drawable.wait4))
                        .apply(new RequestOptions().override(600, 400))
                        .error(Glide.with(c).load(R.drawable.err_img))
                        .into(((RecetteIngrView)holder).img);
                //endregion

                ((RecetteIngrView)holder).detailButt.setOnClickListener((view -> {
                    DetailRecetteActivity.recette=recettes.get(position-(position/6));
                    c.startActivity(new Intent(c,DetailRecetteActivity.class));
                }));

            }
        }

    }


    @Override
    public int getItemViewType(int position) {
        if(position % 6 == 0 && position !=0) return 2;
        else return 1;
    }

    @Override
    public int getItemCount() {
        return recettes.size()+(recettes.size()/6);
    }

}
