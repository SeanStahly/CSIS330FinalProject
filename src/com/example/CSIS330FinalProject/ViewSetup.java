package com.example.CSIS330FinalProject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

/**
 * Created by sean on 11/10/14.
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

        String[] from =  new String[] { "name" };
//        int[] to = new int[] {R.id.assignmentSetupView};


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
