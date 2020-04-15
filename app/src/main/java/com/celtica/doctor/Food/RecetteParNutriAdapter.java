package com.celtica.doctor.Food;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.celtica.doctor.GlobalVar;
import com.celtica.doctor.PostServerRequest5;
import com.celtica.doctor.R;

import java.util.ArrayList;

/**
 * Created by celtica on 15/08/18.
 */

public class RecetteParNutriAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    AppCompatActivity c;
    ArrayList<NutritionScale> nutritions;
    private ProgressDialog progress;

    public RecetteParNutriAdapter(ArrayList<NutritionScale> nutritions, AppCompatActivity c) {
        this.c = c;
        this.nutritions = nutritions;
    }

    public static class NutriScaleView extends RecyclerView.ViewHolder  {
        public TextView name;
        public EditText min;
        public EditText max;
        ImageView suppButt;

        public NutriScaleView(View v) {
            super(v);
            name=(TextView)v.findViewById(R.id.divNutriVal_name);
            min=(EditText)v.findViewById(R.id.divNutriVal_min);
            max=(EditText)v.findViewById(R.id.divNutriVal_max);
            suppButt=(ImageView) v.findViewById(R.id.divNutriVal_close);
        }
    }
    public static class NutriResutlButtView extends RecyclerView.ViewHolder  {
        CardView resultButt;

        public NutriResutlButtView(View v) {
            super(v);
            resultButt=(CardView) v.findViewById(R.id.resultButt);

        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case 1: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.div_nutrition_values,parent,false);

                NutriScaleView vh = new NutriScaleView(v);
                return vh;

            }
            case 2: {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.div_result_butt,parent,false);

                NutriResutlButtView vh = new NutriResutlButtView(v);
                return vh;
            }
            default: return null;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        if(position == nutritions.size()){


            //region click the buttopn for result ..
            ((NutriResutlButtView)holder).resultButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Recette.getRecetteByNutritions(nutritions, new PostServerRequest5.doBeforAndAfterGettingData() {
                        @Override
                        public void before() {
                            progress=GlobalVar.OpenWaitScreen(c,"","");
                            progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    GlobalVar.getInstance().nutriAjax.getDispatcher().cancelAll();
                                }
                            });
                            progress.show();


                        }

                        @Override
                        public void echec(Exception e) {
                            c.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progress.dismiss();
                                    Toast.makeText(c,"No Internet ..",Toast.LENGTH_LONG).show();
                                }
                            });

                        }

                        @Override
                        public void After(String result) {
                            progress.dismiss();
                            Intent i=new Intent(c, ResultRecetteActivity.class);
                            i.putExtra("result",result+"");
                            i.putExtra("query","nutrition");
                            c.startActivity(i);

                        }
                    });
                }
            });
            //endregion

        }else {
            ((NutriScaleView)holder).name.setText(nutritions.get(position).nom+"");
            ((NutriScaleView)holder).min.setText((nutritions.get(position).min == -1 ? "" : nutritions.get(position).min+""));
            ((NutriScaleView)holder).max.setText((nutritions.get(position).max == -1 ? "" : nutritions.get(position).max+""));

            //region config min max changed value ...
            ((NutriScaleView)holder).min.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence value, int i, int i1, int i2) {
                    nutritions.get(position).min=value.toString().equals("") ? -1 : Double.parseDouble(value.toString());

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            ((NutriScaleView)holder).max.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence value, int i, int i1, int i2) {
                    nutritions.get(position).max=value.toString().equals("") ? -1 : Double.parseDouble(value.toString());

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            //endregion

            //region supp nutriment item .. ..
            ((NutriScaleView)holder).suppButt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nutritions.remove(position);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                }
            });
            //endregion
        }



    }

    @Override
    public int getItemViewType(int position) {
        if(position == nutritions.size()){
            return 2;
        }else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        if(nutritions.size() !=0){
            return nutritions.size()+1;
        }else {
            return 0;
        }
    }

}
