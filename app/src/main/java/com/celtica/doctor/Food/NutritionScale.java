package com.celtica.doctor.Food;

public class NutritionScale extends Nutrition {
   Double min,max;

    public NutritionScale(String nom, double min, double max) {
        super(nom);
        this.min = min;
        this.max = max;
    }
}
