package com.celtica.doctor.Food;

public class NutritionInRecette    {
    public Double value;
    public Recette recette;
    public Nutrition nutrition;

    public NutritionInRecette(Double value, Recette recette, Nutrition nutrition) {
        this.value = value;
        this.recette = recette;
        this.nutrition = nutrition;
    }

    public NutritionInRecette(Double value, Nutrition nutrition) {
        this.value = value;
        this.nutrition = nutrition;
    }
}
