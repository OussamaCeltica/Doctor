package com.celtica.doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DiagnostiqueActivtity extends AppCompatActivity {
    public BodyLocation bodyLocation;
    ArrayList<Symptome> symptomesSelectionné=new ArrayList<>();
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostique);

        //region configurer les location de body ..
        ArrayList<SpinnerItem> locations=new ArrayList<>();
        BodyLocation.getBodyLocation(new PostServerRequest5.doBeforAndAfterGettingData() {
            @Override
            public void before() {
                progress=GlobalVar.OpenWaitScreen(DiagnostiqueActivtity.this,"","");
                progress.show();
            }

            @Override
            public void echec(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        e.printStackTrace();
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(),"No Internet ... \n "+e.getMessage(),Toast.LENGTH_LONG).show();

                    }
                });
            }

            @Override
            public void After(String result) {
                progress.dismiss();
                try {
                    JSONArray r=new JSONArray (result);
                    for (int i=0;i != r.length(); i++){
                        JSONObject obj=r.getJSONObject(i);
                        locations.add(new SpinnerItem(obj.getString("ID"),obj.getString("Name")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        MySpinnerSearchable locationsSpinner=new MySpinnerSearchable(this, locations, getResources().getString(R.string.diag_BodyLocationHint), new MySpinnerSearchable.SpinnerConfig() {
            @Override
            public void onChooseItem(int pos, SpinnerItem item) {
                ((TextView)findViewById(R.id.diag_locationName)).setText(item.value+"");
                bodyLocation=new BodyLocation(Integer.parseInt(item.key));
            }
        });

        ((CardView)findViewById(R.id.diag_locationButt)).setOnClickListener((view -> {
            locationsSpinner.openSpinner();

        }));
        //endregion

        //region configurer des symptomes ..
        FlexboxLayout listSympt=(FlexboxLayout) findViewById(R.id.diag_ListSymp);

        ArrayList<SpinnerItem> symptomes=new ArrayList<>();


        MySpinnerSearchable symptomesSpinner=new MySpinnerSearchable(this, symptomes, getResources().getString(R.string.diag_SymptomesHint), new MySpinnerSearchable.SpinnerConfig() {
            @Override
            public void onChooseItem(int pos, SpinnerItem item) {
                Symptome s=new Symptome(Integer.parseInt(item.key),item.value);

                if(!symptomesSelectionné.contains(s)){
                    symptomesSelectionné.add(s);
                    View v=getLayoutInflater().inflate(R.layout.symptome_item,null);
                    ((TextView)v.findViewById(R.id.item_name)).setText(item.value);
                    ((ImageView)v.findViewById(R.id.item_delete)).setOnClickListener((view -> {
                        listSympt.removeView(v);
                    }));

                    listSympt.addView(v);
                }

            }
        });


        ((CardView)findViewById(R.id.diag_SympButt)).setOnClickListener((view -> {
            if (bodyLocation == null){
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.diag_NoLocation),Toast.LENGTH_LONG).show();

            }else {
                if (bodyLocation.symptomes.isEmpty()){
                    bodyLocation.getSymptomes(new PostServerRequest5.doBeforAndAfterGettingData() {
                        @Override
                        public void before() {
                            progress=GlobalVar.OpenWaitScreen(DiagnostiqueActivtity.this,"","");
                            progress.show();

                        }

                        @Override
                        public void echec(Exception e) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    e.printStackTrace();
                                    progress.dismiss();
                                    Toast.makeText(getApplicationContext(),"No Internet ... \n "+e.getMessage(),Toast.LENGTH_LONG).show();

                                }
                            });

                        }

                        @Override
                        public void After(String result) {
                            progress.dismiss();
                            symptomes.clear();
                            for (Symptome s : bodyLocation.symptomes){
                                symptomes.add(new SpinnerItem(s.id+"",s.name));
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    symptomesSpinner.openSpinner();
                                }
                            });


                        }
                    });
                }
                else {
                    symptomesSpinner.openSpinner();
                }
            }
        }));
        //endregion

        //region configurer radio button ..
        RadioButton male,female;
        male=(RadioButton)findViewById(R.id.diag_male);
        female=(RadioButton)findViewById(R.id.diag_female);

        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    female.setChecked(false);
                }
            }
        });

        female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    male.setChecked(false);
                }
            }
        });

        //endregion

        //region faire le test ..
        ((CardView)findViewById(R.id.diag_testButt)).setOnClickListener((view -> {
            EditText age=((EditText)findViewById(R.id.diag_SympAge));
            if(age.getText().toString().equals("")){

            }else {

            }


            Diagnostique diagnostique=new Diagnostique(((male.isChecked() ? SEXE.MALE : SEXE.FEMALE)),age.getText().toString(),symptomesSelectionné);
            DiagnoResultActivity.diagnostique=diagnostique;
            startActivity(new Intent(DiagnostiqueActivtity.this,DiagnoResultActivity.class));


        }));
        //endregion
    }
}
