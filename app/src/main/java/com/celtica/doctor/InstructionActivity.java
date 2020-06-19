package com.celtica.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.celtica.doctor.BD.MyBD;
import com.celtica.doctor.BD.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class InstructionActivity extends AppCompatActivity {
    AlertDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);




        ((Button)findViewById(R.id.b)).setOnClickListener((v)->{

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},1);
                return;
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
       if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
           TelephonyManager telephonyManager = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
           @SuppressLint("MissingPermission") String id=telephonyManager.getDeviceId()+"";
           User user=new User(id,0,0);
           user.FreeNutriQuota=5;
           MyBD.getInstance(InstructionActivity.this).db.getUserDAO().insert(user);

           GlobalVar.getInstance().MyServerAjax.setUrlRead("/write.php");
           HashMap<String,String> datas=new HashMap<>();
           datas.put("id",id);
           GlobalVar.getInstance().MyServerAjax.write("insert ignore into user(id,date_insc,nutri_quota,diagno_quota) values(?,NOW(),0,5)", datas, new PostServerRequest5.doBeforAndAfterGettingData() {
               @Override
               public void before() {
                   progress=GlobalVar.WaitScreenAds(InstructionActivity.this,"","Loading ...");

               }

               @Override
               public void echec(Exception e) {
                   runOnUiThread(()->{
                       progress.dismiss();
                   });

               }

               @Override
               public void After(String result) {
                   runOnUiThread(()->{
                       progress.dismiss();
                   });

                   if (result.trim().equals("succ")){
                       startActivity(new Intent(InstructionActivity.this,Accueil.class));
                       finish();
                   }else {
                       Log.e("innnsc",result);
                       runOnUiThread(()->{
                           Toast.makeText(getApplicationContext(),"ERROOOOOOOOOOR",Toast.LENGTH_LONG).show();
                       });
                   }

               }
           });


       }else {
           Toast.makeText(getApplicationContext(),"You Can't continue without granting the permission to accesss to the device id , we use a device id as user identifier.",Toast.LENGTH_LONG).show();
       }
    }
}
