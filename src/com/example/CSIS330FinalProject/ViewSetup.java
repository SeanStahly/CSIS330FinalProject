package com.example.CSIS330FinalProject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
* Created by sean
* on 11/10/14.
*/
public class ViewSetup extends ListActivity {
    public static final String ROW_ID= "row_id";
    private ListView snakeViewList;
    private CursorAdapter snakeAdapter;

    @Override
    public void onCreate(Bundle savedInsanceState) {
        super.onCreate(savedInsanceState);
        snakeViewList = getListView();
        snakeViewList.setOnItemClickListener(viewAssignmentListner);

        Bundle extras = getIntent().getExtras();

        Long i = extras.getLong(ROW_ID);
        Log.d("test", i.toString());

        if (extras.getLong(ROW_ID) > 0) {

        } else {

        }

        String[] from =  new String[] { "name" };
        int[] to = new int[] {R.id.assignmentTextView};
        snakeAdapter = new SimpleCursorAdapter(ViewSetup.this, R.layout.assignment_item, null, from, to);
        setListAdapter(snakeAdapter);
    }

    AdapterView.OnItemClickListener viewAssignmentListner = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent viewAssignment = new Intent(ViewSetup.this, ViewAssignment.class);

            viewAssignment.putExtra(ROW_ID, l);
            startActivity(viewAssignment);
        }
    };
}
