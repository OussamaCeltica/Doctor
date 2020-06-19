package com.celtica.doctor.Diagnosis;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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

        if (viewType == 1) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.div_diagnoresult, parent, false);

            DiagnoResultView vh = new DiagnoResultView(v);
            return vh;
        }else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.div_maladie_ads, parent, false);

            AdsView vh = new AdsView(v);
            return vh;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        if (position % 4  ==  0 && position != 0){
            AdLoader adLoader = new AdLoader.Builder(c, "ca-app-pub-4807740938253496/3908576106")
                    .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                        @Override
                        public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                            NativeTemplateStyle styles = new
                                    NativeTemplateStyle.Builder().withMainBackgroundColor(new ColorDrawable(c.getResources().getColor(R.color.VertFance))).build();


                            ((AdsView)holder).adsView.setStyles(styles);
                            ((AdsView)holder).adsView.setNativeAd(unifiedNativeAd);

                        }
                    })
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());
        }else {
            ((DiagnoResultView)holder).name.setText(DiagnoResults.get(position-(position/4)).maladie.name+"");
            ((DiagnoResultView)holder).proba.setText(DiagnoResults.get(position-(position/4)).probabilité+" %");
            ((DiagnoResultView)holder).special.setText(DiagnoResults.get(position-(position/4)).specialisation+"");

            //region Voir le detail du maladie ..
            ((DiagnoResultView)holder).detailButt.setOnClickListener((view -> {
                MaladieDescription.maladie=DiagnoResults.get(position-(position/4)).maladie;
                c.startActivity(new Intent(c,MaladieDescription.class));

            }));
            //endregion
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position % 4 == 0 && position != 0) return 2; //return la vue de ads ..
        else return 1;
    }

    @Override
    public int getItemCount() {
        return DiagnoResults.size()+(DiagnoResults.size()/4);
    }


}
