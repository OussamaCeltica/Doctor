package com.celtica.doctor.Food;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.celtica.doctor.ExpandableItem;
import com.celtica.doctor.R;

import java.util.ArrayList;

public class NutritionActivity extends AppCompatActivity {
    ExpandableListView expandableListView;
    NutritionExpandableAdapter expandableListAdapter;
    ArrayList<ExpandableItem> items=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);
        expandableListView = (ExpandableListView) findViewById(R.id.nutrition_divOption);


        ArrayList<String> recipe=new ArrayList<>();
        recipe.add(getResources().getString(R.string.nutrition_recetteNutri));
        recipe.add(getResources().getString(R.string.nutrition_recetteIngr));
        items.add(new ExpandableItem(getResources().getString(R.string.nutrition_recetteLabel),recipe));

        ArrayList<String> ingredient=new ArrayList<>();
        ingredient.add(getResources().getString(R.string.nutrition_ingrNutri));


        items.add(new ExpandableItem(getResources().getString(R.string.nutrition_nutriLabel),ingredient));

        ArrayList<String> diverse=new ArrayList<>();
        diverse.add(getResources().getString(R.string.nutrition_quickAnswer));


        items.add(new ExpandableItem(getResources().getString(R.string.nutrition_diverLabel),diverse));


        expandableListAdapter = new NutritionExpandableAdapter(this,expandableListView, items );
        expandableListView.setAdapter(expandableListAdapter);

        expandableListAdapter.expandableListView=expandableListView;
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {


            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                if (items.get(groupPosition).liste.get(childPosition).equals(getResources().getString(R.string.nutrition_recetteIngr))){
                    startActivity(new Intent(NutritionActivity.this,RecetteIngredientActivity.class));

                }else if(items.get(groupPosition).liste.get(childPosition).equals(getResources().getString(R.string.nutrition_recetteNutri))){
                    startActivity(new Intent(NutritionActivity.this,RecetteParNutriActivity.class));

                }else if (items.get(groupPosition).liste.get(childPosition).equals(getResources().getString(R.string.nutrition_ingrNutri))){

                }else if (items.get(groupPosition).liste.get(childPosition).equals(getResources().getString(R.string.nutrition_quickAnswer))){
                    startActivity(new Intent(NutritionActivity.this,QuickResponseActivity.class));
                }

                return false;
            }
        });
    }

}