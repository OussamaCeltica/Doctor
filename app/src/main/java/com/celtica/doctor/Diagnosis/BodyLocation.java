package com.celtica.doctor.Diagnosis;

import android.util.Log;

import com.celtica.doctor.GlobalVar;
import com.celtica.doctor.PostServerRequest5;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BodyLocation extends Identifier {
    public ArrayList<Symptome> symptomes=new ArrayList<>();

    public BodyLocation(int id) {
        super(id);
    }

    public  void getSymptomes(PostServerRequest5.doBeforAndAfterGettingData callback){

        GlobalVar.getInstance().ajax.setUrlRead("/symptoms/"+id+"/0?token="+GlobalVar.getInstance().token);
        GlobalVar.getInstance().ajax.get(new HashMap<>(), new PostServerRequest5.doBeforAndAfterGettingData() {
            @Override
            public void before() {
                Log.d("uurl","/symptoms/"+id+"/0?token="+GlobalVar.getInstance().token);
                callback.before();
            }

            @Override
            public void echec(Exception e) {
                callback.echec(e);

            }

            @Override
            public void After(String result) {
                try {
                    JSONArray r=new JSONArray(result);
                    for (int i=0;i != r.length(); i++){
                        JSONObject obj=r.getJSONObject(i);
                        symptomes.add(new Symptome(obj.getInt("ID"),obj.getString("Name")));
                    }
                    callback.After(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public static void getBodyLocation(PostServerRequest5.doBeforAndAfterGettingData callback){

        GlobalVar.getInstance().ajax.setUrlRead("/body/locations?token="+GlobalVar.getInstance().token);
        GlobalVar.getInstance().ajax.get(new HashMap<>(),callback);
    }
}
