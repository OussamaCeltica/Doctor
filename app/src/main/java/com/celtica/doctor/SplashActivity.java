package com.celtica.doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.celtica.doctor.BD.MyBD;
import com.celtica.doctor.BD.User;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.List;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //region congig admob
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                GlobalVar.changeLang(SplashActivity.this,"en");

            }

        });
        //endregion

        new Handler().postDelayed(()->{
            List<User> users= MyBD.getInstance(SplashActivity.this).db.getUserDAO().getUsers();
            if (users.size() == 0) startActivity(new Intent(SplashActivity.this,InstructionActivity.class));
            else startActivity(new Intent(SplashActivity.this,Accueil.class));

            finish();
        },2000);
    }
}
