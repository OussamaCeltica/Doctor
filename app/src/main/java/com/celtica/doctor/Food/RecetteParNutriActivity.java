package com.celtica.doctor.Food;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.celtica.doctor.MySpinnerSearchable;
import com.celtica.doctor.R;
import com.celtica.doctor.SpinnerItem;

import java.util.ArrayList;

public class RecetteParNutriActivity extends AppCompatActivity {
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recette_par_nutri);


        ArrayList<NutritionScale> nutriValues=new ArrayList<>();



        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recetteNutri_divNutri);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(RecetteParNutriActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)




        final RecetteParNutriAdapter mAdapter = new RecetteParNutriAdapter(nutriValues, RecetteParNutriActivity.this);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);

        //Region configuration de la liste des nutrtion
        String[] nutri=getResources().getStringArray(R.array.nutriments_liste);
        ArrayList<SpinnerItem> nutritions=new ArrayList<>();
        for (String n:nutri){
            nutritions.add(new SpinnerItem("",n));
        }


        MySpinnerSearchable nutriSpinner=new MySpinnerSearchable(this, nutritions, "", new MySpinnerSearchable.SpinnerConfig() {
            @Override
            public void onChooseItem(int pos, SpinnerItem item) {
                NutritionScale n=new NutritionScale(item.value+"",-1,-1);
                if(!nutriValues.contains(n)){
                    nutriValues.add(0,n);
                    mRecyclerView.scrollToPosition(0);
                    mAdapter.notifyItemInserted(0);
                }

            }
        });

        ((CardView)findViewById(R.id.recetteNutri_listButt)).setOnClickListener(view -> {
            nutriSpinner.openSpinner();

        });
        //endregion

    }
}
