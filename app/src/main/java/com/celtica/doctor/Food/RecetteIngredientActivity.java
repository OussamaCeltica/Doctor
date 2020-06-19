package com.celtica.doctor.Food;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.celtica.doctor.GlobalVar;
import com.celtica.doctor.MySpinnerSearchable;
import com.celtica.doctor.PostServerRequest5;
import com.celtica.doctor.R;
import com.celtica.doctor.SpinnerItem;
import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecetteIngredientActivity extends AppCompatActivity {
    EditText ingredientInp,excludeIngrInp;
    FlexboxLayout listIntoleranceView;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recette_ingredient);

        GlobalVar.setRewadedAd(this,"ca-app-pub-4807740938253496/9569996702");

        ingredientInp=(EditText)findViewById(R.id.recetteIng_inp);
        excludeIngrInp=(EditText)findViewById(R.id.recetteIngr_excludeIngrHint);
        listIntoleranceView=(FlexboxLayout)findViewById(R.id.recetteIngr_divIntolerance);

        //region config intolerance liste ..
        ArrayList<SpinnerItem> intolerances=new ArrayList<>();
        ArrayList<String> intolerancesSelectioné=new ArrayList<>();
        for (String s:getResources().getStringArray(R.array.intolerance_liste)){
            intolerances.add(new SpinnerItem("",s));
        }
        MySpinnerSearchable intoleranceSpinner=new MySpinnerSearchable(this, intolerances, "", new MySpinnerSearchable.SpinnerConfig() {
            @Override
            public void onChooseItem(int pos, SpinnerItem item) {

                if(!intolerancesSelectioné.contains(item.value)){
                    intolerancesSelectioné.add(item.value);
                    View v=getLayoutInflater().inflate(R.layout.symptome_item,null);
                    ((TextView)v.findViewById(R.id.item_name)).setText(item.value);
                    ((ImageView)v.findViewById(R.id.item_delete)).setOnClickListener((view -> {
                        listIntoleranceView.removeView(v);
                    }));

                    listIntoleranceView.addView(v);
                }

            }
        });

        ((CardView)findViewById(R.id.recetteIngr_intoleranceButt)).setOnClickListener(view -> {
            intoleranceSpinner.openSpinner();
        });
        //endregion

        //region get result ..
        ((CardView)findViewById(R.id.recetteIng_testButt)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String [] ingrSplit= ingredientInp.getText().toString().split(",");
                ArrayList<Ingredient> ingrs=new ArrayList<>();

                for(String ing:ingrSplit){
                    ingrs.add(new Ingredient(ing));
                }

                String [] ingrExcludeSplit= excludeIngrInp.getText().toString().split(",");
                ArrayList<Ingredient> ingrsExclude=new ArrayList<>();

                for(String ing:ingrExcludeSplit){
                    ingrsExclude.add(new Ingredient(ing));
                }

                Recette.getRecetteByIngredient(ingrs,ingrsExclude,intolerancesSelectioné, new PostServerRequest5.doBeforAndAfterGettingData() {
                    @Override
                    public void before() {
                        progress= GlobalVar.OpenWaitScreen(RecetteIngredientActivity.this,"","");
                        progress.show();
                    }

                    @Override
                    public void echec(Exception e) {
                        runOnUiThread(()-> {
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(),"No Internet ...",Toast.LENGTH_LONG).show();

                        });
                    }

                    @Override
                    public void After(String result) {
                        runOnUiThread(()-> {
                            progress.dismiss();

                        });

                        try {
                            JSONObject obj=new JSONObject(result);
                            Intent i=new Intent(RecetteIngredientActivity.this, ResultRecetteActivity.class);

                            ResultRecetteActivity.result=obj.getString("results");
                            i.putExtra("query","ingredient");
                            startActivity(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                });
            }
        });
        //endregion
    }
}
