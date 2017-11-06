package com.finnzhanchen.songgo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chen on 05/11/2017.
 */

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private String[] groupNames = {
            "Unclassified", "Very Interesting", "Interesting", "Not Boring", "Boring"};
    private ArrayList<ArrayList<String>> childNames =
            new ArrayList<ArrayList<String>>(5);
    // index at 0 represents unclassified words .... index 4 represents boring words
    /*

    String[][] childNames = {{}, {}, {}, {}, {}};
*/
    Context context;

    public ExpandableListViewAdapter(Context context, ArrayList<Placemark> collected_placemarks){
        Log.e("Stage", "ViewAdapted initialisitation reached");
        Log.e("X placemarks collected", collected_placemarks.size() +"");
        this.context = context;

        //Initialise childNames arraylist of arraylist
        for (int i = 0; i<groupNames.length; i++){
            childNames.add(new ArrayList<String>());
        }
        Log.e("Before FOR loop", "Reached");
        for (Placemark placemark : collected_placemarks){
            Log.e("Inside FOR loop", "Reached");
            switch (placemark.description){
                case "Unclassified":
                    childNames.get(0).add(placemark.position);
                    break;
                case "Very Interesting":
                    childNames.get(1).add(placemark.position);
                    break;
                case "Interesting":
                    childNames.get(2).add(placemark.position);
                    break;
                case "Not Boring":
                    childNames.get(3).add(placemark.position);
                    break;
                case "Boring":
                    childNames.get(4).add(placemark.position);
                    break;
            }
        }
    }

    @Override
    public int getGroupCount() {
        return groupNames.length;
    }

    @Override
    public int getChildrenCount(int i) {
        return childNames.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return groupNames[i];
    }

    @Override
    public Object getChild(int i, int i1) {
        return childNames.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean isExpanded, View view, ViewGroup viewGroup) {
        TextView txtView = new TextView(context);
        txtView.setText(groupNames[i]);
        //txtView.setBackgroundColor(Color.parseColor("#afb1b5"));
        txtView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txtView.setPadding(0,50,0,0);
        txtView.setTextSize(24);
        return txtView;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        TextView txtView = new TextView(context);
        txtView.setText(childNames.get(i).get(i1));
        txtView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txtView.setTextSize(20);
        return txtView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
