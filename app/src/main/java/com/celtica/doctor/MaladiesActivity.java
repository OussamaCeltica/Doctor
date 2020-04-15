package com.celtica.doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MaladiesActivity extends AppCompatActivity {
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maladies);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.maladies_aff);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(MaladiesActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        ArrayList<Maladie> maladies=new ArrayList<>();
        ArrayList<Maladie> filtredM=new ArrayList<>();

        final MaladiesAdapter mAdapter = new MaladiesAdapter(maladies, MaladiesActivity.this);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);

        //region récupéré les maladies ..
        GlobalVar.getInstance().ajax.setUrlRead("/issues?token="+GlobalVar.getInstance().token);
        GlobalVar.getInstance().ajax.get(new HashMap<>(), new PostServerRequest5.doBeforAndAfterGettingData() {
            @Override
            public void before() {
                progress=GlobalVar.OpenWaitScreen(MaladiesActivity.this,"","");
                progress.show();
            }

            @Override
            public void echec(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(),"No Internet ...",Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void After(String result) {
                progress.dismiss();

                try {
                    JSONArray r=new JSONArray(result);
                    for (int i=0; i != r.length();i++){
                        JSONObject obj=r.getJSONObject(i);
                        maladies.add(new Maladie(obj.getInt("ID"),obj.getString("Name")));
                    }
                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           mAdapter.notifyDataSetChanged();
                       }
                   });
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Data Problem ...",Toast.LENGTH_LONG).show();
                        }
                    });
                }


            }
        });
        //endregion

        //region filtrage des MaladiesActivity ..
        ((EditText)findViewById(R.id.maladies_searchInp)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                filtredM.clear();
                if(s.toString().equals("")) {
                    mAdapter.maladies=maladies;
                }
                else {
                    for(Maladie maladie : maladies){
                        if(maladie.name.toLowerCase().indexOf(s.toString().toLowerCase()) != -1){
                            filtredM.add(maladie);
                        }

                    }
                    mAdapter.maladies=filtredM;
                }
                mAdapter.notifyDataSetChanged();


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //endregion
    }
}
