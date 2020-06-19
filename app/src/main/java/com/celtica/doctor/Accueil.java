package com.celtica.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.celtica.doctor.Diagnosis.DiagnostiqueActivtity;
import com.celtica.doctor.Diagnosis.MaladiesActivity;
import com.celtica.doctor.Food.Ingredient;
import com.celtica.doctor.Food.NutritionActivity;
import com.celtica.doctor.Food.Recette;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Accueil extends AppCompatActivity {

    private AdView mAdView;
    private AdView mAdView2;
    private DrawerLayout mDrawerLayout;
    private AlertDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalVar.changeLang(Accueil.this,"en");
        setContentView(R.layout.activity_accueil);

        //startActivity(new Intent(Accueil.this,TestActivity.class));



        //region SyncQuota et  get user FreeQuota Nutri ..
        GlobalVar.getInstance().user.syncQuotas();
        GlobalVar.getInstance().user.getFreeNutriQuota();
        //endregion

        //region get user quotas ..
        GlobalVar.getInstance().user.getUserQuotas(new PostServerRequest5.doBeforAndAfterGettingData() {
            @Override
            public void before() {
                progress=GlobalVar.WaitScreenAds(Accueil.this,"","Loading ..");
            }

            @Override
            public void echec(Exception e) {
                GlobalVar.getInstance().user.setQuotas( 0,0);

                e.printStackTrace();
                runOnUiThread(()->{
                    progress.dismiss();
                        Toast.makeText(getApplicationContext(),"Check Internet ..",Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void After(String result) {
                runOnUiThread(()->{
                    new Handler().postDelayed(()->{ progress.dismiss(); },3000);
                });
                try {
                    JSONObject r=new JSONArray(result).getJSONObject(0);
                    GlobalVar.getInstance().user.setQuotas(r.getInt("diagno_quota")-GlobalVar.getInstance().user.diagnoQuotas,r.getInt("nutri_quota")-GlobalVar.getInstance().user.nutriQuotas);
                } catch (JSONException e) {
                    GlobalVar.getInstance().user.setQuotas( 0,0);
                    e.printStackTrace();
                }

            }
        });
        //endregion

        //region congig admob
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView2 = findViewById(R.id.adView2);
        AdRequest adRequest2 = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest2);

        GlobalVar.changeLang(Accueil.this,"en");
        //Log.e("addd",initializationStatus.)
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.e("addd","Loaded");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Log.e("addd","err= "+errorCode);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
        //endregion

        //region configurer drawer layout ..
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);

        ((ImageView)findViewById(R.id.drawerButt)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.LEFT);

            }
        });


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        //menuItem.setChecked(true);
                        /*
                        if(menuItem.getItemId()== R.id.menu_parametrage){
                            startActivity(new Intent(Accueil.this,LoginActivity.class));

                        }else if(menuItem.getItemId()== R.id.menu_about) {
                            // startActivity(new Intent(Accueil.this,InterviewVideo.class));


                        }

                         */
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
        //endregion

        //region start maladies activity ..
        ((CardView)findViewById(R.id.maladiesButt)).setOnClickListener((view)->{
            startActivity(new Intent(Accueil.this, MaladiesActivity.class));
        });
        //endregion

        //region start diagnostique activity ..
        ((CardView)findViewById(R.id.diagButt)).setOnClickListener((view)->{

            startActivity(new Intent(Accueil.this, DiagnostiqueActivtity.class));
        });
        //endregion

        //region start nutrition activity ..
        ((CardView)findViewById(R.id.nutritionButt)).setOnClickListener((view)->{
            startActivity(new Intent(Accueil.this, NutritionActivity.class));
        });
        //endregion
    }


}
