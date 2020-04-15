package com.celtica.doctor.Food;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.celtica.doctor.GlobalVar;
import com.celtica.doctor.PostServerRequest5;
import com.celtica.doctor.R;

import org.w3c.dom.Text;

public class DetailRecetteActivity extends AppCompatActivity {
    public static Recette recette;
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recette);
        if (savedInstanceState != null) {

        }else {
            progress= GlobalVar.OpenWaitScreen(this,"","");

            recette.getRecetteInfo(new PostServerRequest5.doBeforAndAfterGettingData() {
                @Override
                public void before() {
                    progress.show();
                    progress.setCanceledOnTouchOutside(false);
                    progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            finish();
                            GlobalVar.getInstance().nutriAjax.getDispatcher().cancelAll();
                        }
                    });
                }

                @Override
                public void echec(Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            e.printStackTrace();
                            progress.dismiss();
                            Toast.makeText(getApplicationContext(),"No Internet ..",Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });


                }

                @Override
                public void After(String result) {
                    progress.dismiss();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            ((TextView)findViewById(R.id.detailRecette_titre)).setText(recette.nom+"");
                            ((TextView)findViewById(R.id.detailRecette_ready)).setText(recette.readyMin+" Min");
                            ((TextView)findViewById(R.id.detailRecette_ingr)).setText(recette.ingredientCook+"");
                            ((TextView)findViewById(R.id.detailRecette_resume)).setText(recette.resumeDescreption+"");

                            //region config recette image
                            Glide.with(DetailRecetteActivity.this)
                                    .load(recette.imgUrl)
                                    .thumbnail(Glide.with(DetailRecetteActivity.this).load(R.drawable.wait))
                                    .apply(new RequestOptions().override(600, 400))
                                    .error(Glide.with(DetailRecetteActivity.this).load(R.drawable.err_img))
                                    .into((ImageView) findViewById(R.id.detailRecette_img));
                            //endregion

                        }
                    });
                }
            });


        }
    }
}
