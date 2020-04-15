package com.celtica.doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.celtica.doctor.Food.NutritionActivity;
import com.celtica.doctor.Food.NutritionScale;
import com.celtica.doctor.Food.Recette;

import java.util.ArrayList;
import java.util.HashMap;

public class Accueil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalVar.changeLang(this,"en");

        setContentView(R.layout.activity_accueil);





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
