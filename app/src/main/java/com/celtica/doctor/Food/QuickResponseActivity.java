package com.celtica.doctor.Food;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.celtica.doctor.GlobalVar;
import com.celtica.doctor.PostServerRequest5;
import com.celtica.doctor.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class QuickResponseActivity extends AppCompatActivity {
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_response);
        EditText questionInp=(EditText)findViewById(R.id.quickResp_inp);
        TextView resultat=(TextView)findViewById(R.id.quickResp_result);

        GlobalVar.setRewadedAd(this,"ca-app-pub-4807740938253496/9569996702");

        questionInp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                resultat.setText("");

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ((CardView)findViewById(R.id.quickResp_resultButt)).setOnClickListener((view)->{
            GlobalVar.getInstance().nutriAjax.setUrlRead("/recipes/quickAnswer?q="+questionInp.getText().toString()+"&apiKey="+GlobalVar.getInstance().nutriToken);
            GlobalVar.getInstance().nutriAjax.get(new HashMap<>(), new PostServerRequest5.doBeforAndAfterGettingData() {
                @Override
                public void before() {
                    progress=GlobalVar.OpenWaitScreen(QuickResponseActivity.this,"","");
                    progress.show();
                    Log.e("tttt","/recipes/quickAnswer?q="+questionInp.getText().toString());
                }

                @Override
                public void echec(Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress.dismiss();
                        }
                    });

                }

                @Override
                public void After(String result) {
                    progress.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject obj=new JSONObject(result);
                                resultat.setText(obj.getString("answer"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });
        });
    }
}
