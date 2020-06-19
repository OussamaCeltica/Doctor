package com.celtica.doctor.Diagnosis;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.celtica.doctor.BD.MyBD;
import com.celtica.doctor.GlobalVar;
import com.celtica.doctor.MyApplication;
import com.celtica.doctor.PostServerRequest5;
import com.celtica.doctor.R;

public class DiagnoResultActivity extends AppCompatActivity {
    public static  Diagnostique diagnostique;
    AlertDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagno_result);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.maladiesResult_aff);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(DiagnoResultActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)



        final DiagnoResultAdapter mAdapter = new DiagnoResultAdapter(diagnostique.maladies, DiagnoResultActivity.this);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);

        diagnostique.getResults(new PostServerRequest5.doBeforAndAfterGettingData() {
            @Override
            public void before() {
                progress=GlobalVar.WaitScreenAds(DiagnoResultActivity.this,"","Loading ..");
            }

            @Override
            public void echec(Exception e) {
                runOnUiThread(() ->{
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(),"No Internet problem .. \n"+e.getMessage(),Toast.LENGTH_LONG).show();


                });

            }

            @Override
            public void After(String result) {
                MyBD.getInstance(MyApplication.getAppContext()).db.getUserDAO().updateSyncQuota(1,0);
                runOnUiThread(()->{
                    progress.dismiss();
                    mAdapter.notifyDataSetChanged();
                });

            }
        });
    }
}
