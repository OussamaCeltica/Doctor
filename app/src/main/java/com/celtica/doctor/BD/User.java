package com.celtica.doctor.BD;

import android.content.ClipData;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Update;

import com.celtica.doctor.GlobalVar;
import com.celtica.doctor.MyApplication;
import com.celtica.doctor.PostServerRequest5;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Entity(tableName = "user")
public class User {
    @PrimaryKey
    @NonNull
    public String id_device;

    @ColumnInfo(defaultValue = "0")
    public int diagnoSync;
    @ColumnInfo(defaultValue = "0")
    public int nutriSync;
    @ColumnInfo(name = "date_last_use")
    public String dateLastUse;
    public int FreeNutriQuota;


    @Ignore
    public int diagnoQuotas;

    @Ignore
    public int nutriQuotas;

    public User() {
    }

    public User(@NonNull String id_device, int diagnoQuotas, int nutriQuotas) {
        this.id_device = id_device;
        this.diagnoQuotas = diagnoQuotas;
        this.nutriQuotas = nutriQuotas;
    }

    public void getUserQuotas(PostServerRequest5.doBeforAndAfterGettingData callback){
        GlobalVar.getInstance().MyServerAjax.setUrlRead("/read.php");
        HashMap<String,String> datas=new HashMap<>();
        datas.put("id",id_device+"");
        GlobalVar.getInstance().MyServerAjax.read("select * from user where id=?",datas,callback);
    }

    public void setQuotas(int diagnoQuotas,int nutriQuotas){
        this.diagnoQuotas=diagnoQuotas;
        this.nutriQuotas=nutriQuotas;
    }

    public boolean diagnoValideQuotas(){
        return diagnoQuotas-diagnoSync > 0;
    }


    public boolean nutriValideQuotas(){
        return nutriQuotas-nutriSync > 0;
    }

    public void getFreeNutriQuota(){
        PostServerRequest5 ajax=new PostServerRequest5("http://worldtimeapi.org");
        ajax.setUrlRead("/api/timezone/Africa/Algiers");
        ajax.get(new HashMap<>(), new PostServerRequest5.doBeforAndAfterGettingData() {
            @Override
            public void before() {

            }

            @Override
            public void echec(Exception e) {
                e.printStackTrace();

            }

            @Override
            public void After(String result) {
                try {
                    JSONObject obj=new JSONObject(result);
                    String datetime=obj.getString("datetime");
                    String dateStr=datetime.substring(0,datetime.indexOf("T"));
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                    if(dateLastUse == null) dateLastUse=dateStr;
                    Date d2=new SimpleDateFormat("yyyy-MM-dd").parse(dateLastUse);
                    if (date.getDay()-d2.getDay() >=1) {
                        //update dateLast Use
                        FreeNutriQuota=5;
                        MyBD.getInstance(MyApplication.getAppContext()).db.getUserDAO().updateLastUseDate(dateStr);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public void syncQuotas(){
        if (nutriSync > 0 || diagnoSync >0){
            GlobalVar.getInstance().MyServerAjax.setUrlWrite("/write.php");
            HashMap<String,String> datas=new HashMap<>();
            datas.put("1",nutriSync+"");
            datas.put("2",diagnoSync+"");
            datas.put("3",id_device+"");
            GlobalVar.getInstance().MyServerAjax.write("update user set nutri_quota=nutri_quota-? , diagno_quota=diagno_quota-? where id=? ", datas, new PostServerRequest5.doBeforAndAfterGettingData() {
                @Override
                public void before() {

                }

                @Override
                public void echec(Exception e) {
                    e.printStackTrace();

                }

                @Override
                public void After(String result) {
                    if (result.trim().equals("succ")) MyBD.getInstance(MyApplication.getAppContext()).db.getUserDAO().resetSyncQuotas();

                }
            });
        }


    }


    @Dao
    public interface UserDAO {
        @Insert
        public void insert(User ...users);
        @Update
        public void update(User ...users);
        @Delete
        public void delete(User ...users);

        @Query("SELECT * FROM user")
        public List<User> getUsers();

        @Query("SELECT date_last_use FROM user")
        public String getLastUse();

        @Query("update user set date_last_use=:newDate")
        public void updateLastUseDate(String newDate);

        @Query("update user set diagnoSync=0 , nutriSync=0")
        public void resetSyncQuotas();

        @Query("update user set diagnoSync=diagnoSync+:diagno, nutriSync=nutriSync+:nutri")
        public void updateSyncQuota(int diagno,int nutri);







    }

}

