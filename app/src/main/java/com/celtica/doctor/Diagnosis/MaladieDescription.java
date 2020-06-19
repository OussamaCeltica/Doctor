package com.celtica.doctor.Diagnosis;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.celtica.doctor.PostServerRequest5;
import com.celtica.doctor.R;
import com.celtica.doctor.SpinnerItem;

import java.util.ArrayList;

public class MaladieDescription extends AppCompatActivity {

    public  static  Maladie maladie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maladie_description);

        ((TextView)findViewById(R.id.malDesc_name)).setText(""+maladie.name);

        //region get maladies data ..
        maladie.getInfos(new PostServerRequest5.doBeforAndAfterGettingData() {
            @Override
            public void before() {

            }

            @Override
            public void echec(Exception e) {

            }

            @Override
            public void After(String result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //region configuration des données ..
                        ArrayList<SpinnerItem> datas=new ArrayList<>();
                        datas.add(new SpinnerItem(getResources().getString(R.string.malDesc_resume), maladie.ShortDescription+""));
                        datas.add(new SpinnerItem(getResources().getString(R.string.malDesc_desc), maladie.Description+""));
                        datas.add(new SpinnerItem(getResources().getString(R.string.malDesc_condition), maladie.conditionMedicale+""));

                        String symptomes="";
                        for (Symptome s : maladie.symptomes){
                            symptomes=symptomes+"-"+s.name+" \n\n";
                        }
                        datas.add(new SpinnerItem(getResources().getString(R.string.malDesc_symptomes),symptomes));
                        datas.add(new SpinnerItem(getResources().getString(R.string.malDesc_traitement), maladie.Treatment+""));
                        //endregion

                        //region affichage des information de maladie ..
                        LinearLayout listeInfos=(LinearLayout)findViewById(R.id.malDesc_infosListe);
                        View v;

                        for(SpinnerItem item : datas) {
                            Log.d("mmaa","key="+item.key+" / value=");

                            v=getLayoutInflater().inflate(R.layout.maladi_info,listeInfos,false);
                            ((TextView)v.findViewById(R.id.malInfo_titre)).setText(item.key);
                            ((TextView)v.findViewById(R.id.malInfo_detail)).setText(""+item.value);
                            listeInfos.addView(v);

                        }
                        //endregion
                    }
                });

            }
        });
        //endregion

    }
}
