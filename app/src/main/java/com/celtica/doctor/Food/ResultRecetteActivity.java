package com.celtica.doctor.Food;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.celtica.doctor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResultRecetteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_recette);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recetteNutriResult_divAff);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ResultRecetteActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        ArrayList<Recette> recettes=new ArrayList<>();

        String result=getIntent().getExtras().getString("result");
        final ResultRecetteAdapter mAdapter;

        //region config result pour search by nutritions ..
        if(getIntent().getExtras().getString("query").equals("nutrition")){
            try {
                JSONArray r=new JSONArray(result);
                for (int i=0;i != r.length();i++){
                    JSONObject obj=r.getJSONObject(i);
                    ArrayList<NutritionInRecette> nutritions=new ArrayList<>();
                    nutritions.add(new NutritionInRecette(obj.getDouble("calories"),new Nutrition("calories")));
                    nutritions.add(new NutritionInRecette(Double.parseDouble(obj.getString("carbs").replace("g","")),new Nutrition("carbs")));
                    nutritions.add(new NutritionInRecette(Double.parseDouble(obj.getString("fat").replace("g","")),new Nutrition("fat")));
                    nutritions.add(new NutritionInRecette(Double.parseDouble(obj.getString("protein").replace("g","")),new Nutrition("protein")));

                    recettes.add(new Recette(obj.getInt("id"),obj.getString("title"),obj.getString("image"),nutritions));

                }
                mAdapter = new ResultRecetteAdapter(recettes, ResultRecetteActivity.this,RecetteQueryType.PAR_NUTRITION);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //endregion

        //region config result pour search by ingredient
        else {
            try {
                JSONArray r=new JSONArray(result);
                for (int i=0;i != r.length();i++){
                    JSONObject obj=r.getJSONObject(i);
                    recettes.add(new Recette(obj.getInt("id"),obj.getString("title"),obj.getString("image")));

                }
                mAdapter = new ResultRecetteAdapter(recettes, ResultRecetteActivity.this,RecetteQueryType.PAR_INREDIENT);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //endregion

    }
}
