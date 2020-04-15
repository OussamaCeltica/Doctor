package com.celtica.doctor;

import android.app.ProgressDialog;
import android.content.DialogInterface;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class GlobalVar {
    public PostServerRequest5 ajax;
    public String token;
    public PostServerRequest5 nutriAjax;
    public String nutriToken;
    private static GlobalVar ourInstance=null;

    public static GlobalVar getInstance() {
       if(ourInstance == null){
           ourInstance=new GlobalVar();
       }
       return ourInstance;
    }

    private GlobalVar() {
        ajax=new PostServerRequest5("https://healthservice.priaid.ch");
        token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Im91c3NhbWFjZWx0aWNhQGdtYWlsLmNvbSIsInJvbGUiOiJVc2VyIiwiaHR0cDovL3NjaGVtYXMueG1sc29hcC5vcmcvd3MvMjAwNS8wNS9pZGVudGl0eS9jbGFpbXMvc2lkIjoiNDAwMiIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvdmVyc2lvbiI6IjEwOSIsImh0dHA6Ly9leGFtcGxlLm9yZy9jbGFpbXMvbGltaXQiOiIxMDAiLCJodHRwOi8vZXhhbXBsZS5vcmcvY2xhaW1zL21lbWJlcnNoaXAiOiJCYXNpYyIsImh0dHA6Ly9leGFtcGxlLm9yZy9jbGFpbXMvbGFuZ3VhZ2UiOiJlbi1nYiIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvZXhwaXJhdGlvbiI6IjIwOTktMTItMzEiLCJodHRwOi8vZXhhbXBsZS5vcmcvY2xhaW1zL21lbWJlcnNoaXBzdGFydCI6IjIwMjAtMDMtMTkiLCJpc3MiOiJodHRwczovL2F1dGhzZXJ2aWNlLnByaWFpZC5jaCIsImF1ZCI6Imh0dHBzOi8vaGVhbHRoc2VydmljZS5wcmlhaWQuY2giLCJleHAiOjE1ODUwNTYzNzYsIm5iZiI6MTU4NTA0OTE3Nn0.DBXIJMKTe0PLOJwXyEe8aO4HVeDILuJqpSG635D6diA&format=json&language=en-gb";

        nutriAjax=new PostServerRequest5("https://api.spoonacular.com");
        nutriToken="2a2810d757284d0d92c7994126fb3f53";
    }

    public static ProgressDialog OpenWaitScreen(AppCompatActivity c,String titre,String msg){
        ProgressDialog progress = new ProgressDialog(c); // activité non context ..
        progress.setTitle(titre);
        progress.setMessage(msg);
        progress.setCanceledOnTouchOutside(false);


        return progress;

    }

    public static void changeLang(AppCompatActivity context, String lang) {
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}
