package com.celtica.doctor.Food;



import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.celtica.doctor.Diagnosis.ExpandableItem;
import com.celtica.doctor.R;

public class NutritionExpandableAdapter extends BaseExpandableListAdapter {

    private AppCompatActivity context;
   ArrayList<ExpandableItem> expandableItems;
   public ExpandableListView expandableListView;

    public NutritionExpandableAdapter(AppCompatActivity context,ExpandableListView expandableListView,ArrayList<ExpandableItem> expandableItems) {
        this.context = context;
        this.expandableListView=expandableListView;
        this.expandableItems=expandableItems;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return expandableItems.get(listPosition).liste.get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {

             convertView = context.getLayoutInflater().inflate(R.layout.div_expandable_item, null);
        }
        TextView titre = (TextView) convertView.findViewById(R.id.divExpandItem_titre);

        titre.setText(expandedListText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return expandableItems.get(listPosition).liste.size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return expandableItems.get(listPosition).titre;
    }

    @Override
    public int getGroupCount() {
        return this.expandableItems.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {


        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.div_expandable_parent,parent,false);
        }
        TextView titre = (TextView) convertView
                .findViewById(R.id.divExpandable_titre);
        titre.setText(expandableItems.get(listPosition).titre);

        View v=convertView;
        ((CardView)convertView.findViewById(R.id.divExpandable_root)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImageView img=(ImageView)v.findViewById(R.id.expand_icon);

                if(img.getRotation() != 90.0){
                    expandableListView.expandGroup(listPosition);
                    img.setRotation(90);
                }else {
                    img.setRotation(0);
                    expandableListView.collapseGroup(listPosition);
                }
            }
        });

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }


}