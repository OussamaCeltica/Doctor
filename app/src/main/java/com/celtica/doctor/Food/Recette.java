package com.celtica.doctor.Food;

import android.util.Log;

import com.celtica.doctor.GlobalVar;
import com.celtica.doctor.PostServerRequest5;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Recette {
    int id;
    String nom,imgUrl,resumeDescreption,ingredientCook="";
    ArrayList<NutritionInRecette> nutritionInRecettes;
    ArrayList<IngredientRecette> ingredients;
    Double healthscore,readyMin;

    //ingrediontCook est un string qui indique la préparation de recette avec les ingredient t, tari9at al tahdir ..

    public Recette(String nom) {
        this.nom = nom;
    }

    public Recette(String nom, ArrayList<NutritionInRecette> nutritionInRecettes) {
        this.nom = nom;
        this.nutritionInRecettes = nutritionInRecettes;
    }

    //pour les recette par ingredient ..
    public Recette(int id, String nom, String imgUrl) {
        this.id = id;
        this.nom = nom;
        this.imgUrl = imgUrl;
        this.nutritionInRecettes = nutritionInRecettes;

    }

    //pour les recette par nutrition
    public Recette(int id, String nom, String imgUrl, ArrayList<NutritionInRecette> nutritionInRecettes) {
        this.id = id;
        this.nom = nom;
        this.imgUrl = imgUrl;
        this.nutritionInRecettes = nutritionInRecettes;

    }

    public static void getRecetteByNutritions(ArrayList<NutritionScale> nutritionScales, PostServerRequest5.doBeforAndAfterGettingData callback){
        String request="/recipes/findByNutrients?";
        for (NutritionScale n:nutritionScales){
            request=request+"&min"+n.nom+"="+n.min+"&max"+n.nom+"="+n.max;
        }
        request=request+"&apiKey="+GlobalVar.getInstance().nutriToken+"&number=10";


        GlobalVar.getInstance().nutriAjax.setUrlRead(request);
        GlobalVar.getInstance().nutriAjax.get(new HashMap<>(), callback);

    }


    public static void getRecetteByIngredient(ArrayList<Ingredient> ingredients,ArrayList<Ingredient> excludeIngr,ArrayList<String> intolerances, PostServerRequest5.doBeforAndAfterGettingData callback){

        //region formating includeIngredient
        String request="/recipes/complexSearch?query=&includeIngredients=";
        for (Ingredient n:ingredients){
            request=request+n.nom+",+";
        }
        //endregion

        //region formating excludeingredient
        request=request+"&excludeIngredients=";

        for (Ingredient n:excludeIngr){
            request=request+n.nom+",+";
        }
        //endregion

        //region formating intolerance
        request=request+"&intolerances=";

        for (String s:intolerances){
            request=request+s+",+";
        }
        //endregion


        request=request+"&apiKey="+GlobalVar.getInstance().nutriToken+"&number=20";

        GlobalVar.getInstance().nutriAjax.setUrlRead(request);
        GlobalVar.getInstance().nutriAjax.get(new HashMap<>(), callback);

    }

    public void getRecetteInfo(PostServerRequest5.doBeforAndAfterGettingData callback){
        String request="/recipes/"+id+"/information?includeNutrition=true"+"&apiKey="+GlobalVar.getInstance().nutriToken;;

        GlobalVar.getInstance().nutriAjax.setUrlRead(request);
        GlobalVar.getInstance().nutriAjax.get(new HashMap<>(), new PostServerRequest5.doBeforAndAfterGettingData() {
            @Override
            public void before() {
                callback.before();
            }

            @Override
            public void echec(Exception e) {
                callback.echec(e);

            }

            @Override
            public void After(String result) {
                try {
                    JSONObject obj=new JSONObject(result);
                    resumeDescreption=obj.getString("summary");
                    healthscore=obj.getDouble("healthScore");
                    readyMin=obj.getDouble("readyInMinutes");
                    JSONArray r=new JSONArray(obj.getString("extendedIngredients"));
                    for (int i=0;i != r.length() ; i++){
                        JSONObject ing=r.getJSONObject(i);
                        ingredientCook=ingredientCook+"-"+ing.getString("original")+" \n";
                    }
                    callback.After(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
