package com.finnzhanchen.songgo;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by chen on 05/11/2017.
 */

public class ExpandableListViewAdapter_Test extends BaseExpandableListAdapter {
    private String[] groupNames = {
            "Unclassified", "Very Interesting", "Interesting", "Not Boring", "Boring"};
    private String[][] childNames ={{"a","b"},{},{},{},{}};
    // index at 0 represents unclassified words .... index 4 represents boring words
    /*

    String[][] childNames = {{}, {}, {}, {}, {}};
*/
    Context context;

    public ExpandableListViewAdapter_Test(Context context, ArrayList<Placemark> collected_placemarks){
        Log.e("Stage", "ViewAdapted initialisitation reached");
        //Log.e("X placemarks collected", collected_placemarks.size() +"");
        this.context = context;
    }


    @Override
    public int getGroupCount() {
        return groupNames.length;
    }

    @Override
    public int getChildrenCount(int i) {
        return childNames[i].length;
    }

    @Override
    public Object getGroup(int i) {
        return groupNames[i];
    }

    @Override
    public Object getChild(int i, int i1) {
        return childNames[i][i1];
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
        txtView.setText(childNames[i][i1]);
        txtView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txtView.setTextSize(20);
        return txtView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
