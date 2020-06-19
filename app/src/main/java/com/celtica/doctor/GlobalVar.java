package com.celtica.doctor;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.celtica.doctor.BD.MyBD;
import com.celtica.doctor.BD.User;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.Locale;

public class GlobalVar {
    public PostServerRequest5 ajax;
    public String token;
    public PostServerRequest5 nutriAjax;
    public String nutriToken;
    public User user;

    public PostServerRequest5 MyServerAjax;
    private static GlobalVar ourInstance=null;

    public static GlobalVar getInstance() {
       if(ourInstance == null){
           ourInstance=new GlobalVar();
       }
       return ourInstance;
    }

    private GlobalVar() {
        ajax=new PostServerRequest5("https://healthservice.priaid.ch");
        token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Im91c3NhbWFjZWx0aWNhQGdtYWlsLmNvbSIsInJvbGUiOiJVc2VyIiwiaHR0cDovL3NjaGVtYXMueG1sc29hcC5vcmcvd3MvMjAwNS8wNS9pZGVudGl0eS9jbGFpbXMvc2lkIjoiNDAwMiIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvdmVyc2lvbiI6IjEwOSIsImh0dHA6Ly9leGFtcGxlLm9yZy9jbGFpbXMvbGltaXQiOiIxMDAiLCJodHRwOi8vZXhhbXBsZS5vcmcvY2xhaW1zL21lbWJlcnNoaXAiOiJCYXNpYyIsImh0dHA6Ly9leGFtcGxlLm9yZy9jbGFpbXMvbGFuZ3VhZ2UiOiJlbi1nYiIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvZXhwaXJhdGlvbiI6IjIwOTktMTItMzEiLCJodHRwOi8vZXhhbXBsZS5vcmcvY2xhaW1zL21lbWJlcnNoaXBzdGFydCI6IjIwMjAtMDMtMTkiLCJpc3MiOiJodHRwczovL2F1dGhzZXJ2aWNlLnByaWFpZC5jaCIsImF1ZCI6Imh0dHBzOi8vaGVhbHRoc2VydmljZS5wcmlhaWQuY2giLCJleHAiOjE1OTIxNTkyNjcsIm5iZiI6MTU5MjE1MjA2N30.kZKRkrSZGvZOuAjKwo6eqTW962SDfWO0nlBGm6CX4CM&format=json&language=en-gb";
        nutriAjax=new PostServerRequest5("https://api.spoonacular.com");
        nutriToken="2a2810d757284d0d92c7994126fb3f53";
        MyServerAjax=new PostServerRequest5("https://testingebook.000webhostapp.com");
        user= MyBD.getInstance(MyApplication.getAppContext()).db.getUserDAO().getUsers().get(0);
    }

    public static ProgressDialog OpenWaitScreen(AppCompatActivity c,String titre,String msg){
        ProgressDialog progress = new ProgressDialog(c); // activité non context ..
        progress.setTitle(titre);
        progress.setMessage(msg);
        progress.setCanceledOnTouchOutside(false);


        return progress;

    }

    public static AlertDialog WaitScreenAds(AppCompatActivity c, String titre, String msg){
        AlertDialog.Builder mb = new AlertDialog.Builder(c); //c est l activity non le context ..

        View v= c.getLayoutInflater().inflate(R.layout.div_waiting_with_ads,null);
        TextView msgView=(TextView) v.findViewById(R.id.divWaitAds_msg);
        final AdView mAdView=(AdView) v.findViewById(R.id.divWaitAds_ad);
        msgView.setText(msg+"");

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mb.setView(v);
        final AlertDialog ad=mb.create();
        ad.show();
        ad.setCanceledOnTouchOutside(false); //ne pas fermer on click en dehors ..
        ad.setCancelable(false); //désactiver le button de retour ..


        return ad;

    }

    public static void changeLang(AppCompatActivity context, String lang) {
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public static void setRewadedAd(AppCompatActivity c,String adId){
        //region config rewarded add ...
        RewardedAd rewardedAd = new RewardedAd(c, adId);
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                Log.e("addd","Tt est ok");
                RewardedAdCallback adCallback = new RewardedAdCallback() {
                    @Override
                    public void onRewardedAdOpened() {
                        // Ad opened.

                    }

                    @Override
                    public void onRewardedAdClosed() {
                        // Ad closed.
                    }

                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem reward) {
                        // User earned reward.
                    }

                    @Override
                    public void onRewardedAdFailedToShow(int errorCode) {
                        Log.e("addd","mahabch yenhal haho le code="+errorCode);                    }
                };
                rewardedAd.show(c, adCallback);
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                Log.e("addd","Mafihach le code err="+errorCode);
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        //endregion
    }
}
