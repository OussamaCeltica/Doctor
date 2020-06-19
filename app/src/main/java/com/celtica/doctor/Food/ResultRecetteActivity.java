package com.celtica.doctor.Food;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.celtica.doctor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResultRecetteActivity extends AppCompatActivity {

    public static String result="";

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

                    ArrayList<NutritionInRecette> nutritions=new ArrayList<>();
                    JSONArray nutriArr=new JSONArray(obj.getJSONObject("nutrition").getString("nutrients"));


                    for (int i2=0;i2 != nutriArr.length();i2++){
                        JSONObject nutriObj=nutriArr.getJSONObject(i2);


                        try {
                            if(nutriObj.getString("title").equals("Calories"))
                                nutritions.add(new NutritionInRecette(nutriObj.getDouble("amount"),new Nutrition("Calories")));
                            else if (nutriObj.getString("title").equals("Carbohydrates"))
                                nutritions.add(new NutritionInRecette(nutriObj.getDouble("amount"),new Nutrition("Carbs")));
                            else if (nutriObj.getString("title").equals("Fat"))
                                nutritions.add(1,new NutritionInRecette(nutriObj.getDouble("amount"),new Nutrition("Fat")));
                            else if (nutriObj.getString("title").equals("Protein"))
                                nutritions.add(new NutritionInRecette(nutriObj.getDouble("amount"),new Nutrition("Protein")));

                        }catch (JSONException ee){
                            ee.printStackTrace();
                        }


                    }

                    recettes.add(new Recette(obj.getInt("id"),obj.getString("title"),obj.getString("image"),nutritions));


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        result="";
    }
}
