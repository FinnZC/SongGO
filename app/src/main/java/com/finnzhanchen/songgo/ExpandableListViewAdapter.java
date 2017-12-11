package com.finnzhanchen.songgo;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;


// WRITTEN BY ME: FINN ZHAN CHEN

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private String[] groupNames = {
            "Unclassified", "Very Interesting", "Interesting", "Not Boring", "Boring"};
    private ArrayList<ArrayList<String>> childNames =
            new ArrayList<ArrayList<String>>(groupNames.length);
    // index at 0 represents unclassified words .... index 4 represents boring words

    private Context context;

    public ExpandableListViewAdapter(Context context,
                                     ArrayList<String> unclassified,
                                     ArrayList<String> veryinteresting,
                                     ArrayList<String> interesting,
                                     ArrayList<String> notboring,
                                     ArrayList<String> boring){

        this.context = context;

        //Initialise childNames arraylist of arraylist to the appropiate list of placemarks
        childNames.add(unclassified);
        childNames.add(veryinteresting);
        childNames.add(interesting);
        childNames.add(notboring);
        childNames.add(boring);
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
