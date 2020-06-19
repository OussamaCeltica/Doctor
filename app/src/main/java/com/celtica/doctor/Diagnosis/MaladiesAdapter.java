package com.celtica.doctor.Diagnosis;

import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
       if(viewType == 1){
           View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.div_maladie,parent,false);

           MalasieView vh = new MalasieView(v);
           return vh;
       }else {
           View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.div_maladie_ads,parent,false);

           AdsView vh = new AdsView(v);
           return vh;
       }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(position % 12 == 0 && position != 0){

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
            ((MalasieView)holder).name.setText("-"+maladies.get(position-(position/12)).name);

            //region Voir le detail du maladie ..
            ((MalasieView)holder).body.setOnClickListener((view -> {

                MaladieDescription.maladie=maladies.get(position-(position/12));
                c.startActivity(new Intent(c,MaladieDescription.class));
            }));
            //endregion
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position % 12 == 0 && position != 0) return 2; //return la vue de ads ..
        else return 1;
    }

    @Override
    public int getItemCount() {
        return maladies.size()+(maladies.size()/12);
    }

}
