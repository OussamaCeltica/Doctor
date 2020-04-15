package com.celtica.doctor;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Maladie extends Identifier {
    String name,ShortDescription,Description,Treatment,conditionMedicale;
    ArrayList<Symptome> symptomes=new ArrayList<>();

    //pour l affichage dans la liste des maladies ..
    public Maladie(int id, String name) {
        super(id);
        this.name = name;
    }

    public Maladie(int id, String name, String shortDescription, String description, String treatment,String conditionMedicale) {
        super(id);
        this.name=name;
        ShortDescription = shortDescription;
        Description = description;
        Treatment = treatment;

        this.conditionMedicale=conditionMedicale;

    }

    public void getInfos(PostServerRequest5.doBeforAndAfterGettingData callback){

        Log.d("iii",""+id);
        Log.d("iii","URL: "+"/issues/"+id+"/info?token="+GlobalVar.getInstance().token);
        GlobalVar.getInstance().ajax.setUrlRead("/issues/"+id+"/info?token="+GlobalVar.getInstance().token);
        GlobalVar.getInstance().ajax.get(new HashMap<>(), new PostServerRequest5.doBeforAndAfterGettingData() {
            @Override
            public void before() {
                callback.before();
            }

            @Override
            public void echec(Exception e) {
                callback.before();

            }

            @Override
            public void After(String result) {
                Log.d("iii",result);
                try {

                    JSONObject obj=new JSONObject(result);

                    ShortDescription = obj.getString("DescriptionShort");
                    Description = obj.getString("Description");
                    Treatment = obj.getString("TreatmentDescription");
                    conditionMedicale=obj.getString("MedicalCondition");
                    String[] symps = obj.getString("PossibleSymptoms").split(",", -1);
                    for(int i=0; i != symps.length;i++){
                        symptomes.add(new Symptome(i,symps[i]));
                    }

                    callback.After(result);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public void addSymtome(Symptome sym){
        symptomes.add(sym);
    }
}
