package com.celtica.doctor.Food;

import androidx.annotation.Nullable;

import com.celtica.doctor.PostServerRequest5;

public class Nutrition {
    String nom;

    public Nutrition(String nom) {
        this.nom = nom;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(!(obj instanceof Nutrition)){
            return false;
        }else {
            Nutrition s=(Nutrition)obj;
            return (s.nom.equals(nom));
        }
    }

    @Override
    public int hashCode() {
        return nom.length()/2;
    }

    public static void getAllNutritions(PostServerRequest5.doBeforAndAfterGettingData callback){

    }
}
