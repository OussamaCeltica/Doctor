package com.celtica.doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        PostServerRequest5 p=new PostServerRequest5("https://api.spoonacular.com/recipes/complexSearch?query=&addRecipeInformation=true&includeIngredients=meat,+&excludeIngredients=,+&intolerances=&apiKey=2a2810d757284d0d92c7994126fb3f53&number=20");
        p.setUrlRead("");
        p.get(new HashMap<>(), new PostServerRequest5.doBeforAndAfterGettingData() {
            @Override
            public void before() {

            }

            @Override
            public void echec(Exception e) {

            }

            @Override
            public void After(String result) {
                runOnUiThread(()->((TextView)findViewById(R.id.tst)).setText(result+""));


            }
        });
    }
}
