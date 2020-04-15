package com.celtica.doctor;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Diagnostique {
    public SEXE sexe;
    public String age;
    public ArrayList<Symptome> symptomes;
    public ArrayList<DiagnoResult> maladies=new ArrayList<>();

    public Diagnostique(SEXE sexe, String age, ArrayList<Symptome> symptomes) {
        this.sexe = sexe;
        this.age = age;
        this.symptomes = symptomes;
    }

    public String serializeSymptome(){
        String str="";
        for (Symptome s: symptomes) {
            str=str+s.id+",";
        }

        return str.substring(0,str.length()-1);
    }

    public void getResults(PostServerRequest5.doBeforAndAfterGettingData callback){

        String url="/diagnosis?&gender="+(sexe == SEXE.MALE ? "male" : "female")+"&year_of_birth="+age+"&symptoms=["+serializeSymptome()+"]&token="+GlobalVar.getInstance().token;


        GlobalVar.getInstance().ajax.setUrlRead(url);
        GlobalVar.getInstance().ajax.get(new HashMap<>(), new PostServerRequest5.doBeforAndAfterGettingData() {
            @Override
            public void before() {
                callback.before();
            }

            @Override
            public void echec(Exception e) {
                callback.echec(e);

            }

            @Override
            public void After(String result) {
                Log.d("rrr",result+" / url= "+url);
                try {
                    JSONArray r=new JSONArray(result);
                    for (int i=0;i != r.length();i++){
                        JSONObject maladie=r.getJSONObject(i).getJSONObject("Issue");
                        JSONArray specialArray=r.getJSONObject(i).getJSONArray("Specialisation");
                        String specialisation="";
                        for (int ii=0; ii != specialArray.length(); ii++){
                            specialisation=specialisation+specialArray.getJSONObject(ii).getString("Name")+"/";
                        }
                        maladies.add(new DiagnoResult(new Maladie(maladie.getInt("ID"),maladie.getString("Name")),maladie.getDouble("Accuracy"),specialisation.substring(0,specialisation.length()-1)));
                    }

                    callback.After(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
