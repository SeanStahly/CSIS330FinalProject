package com.example.CSIS330FinalProject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class SetupList extends ListActivity {
    public static final String ROW_ID = "row_id";
    private ListView snakeSetupListView;
    private CursorAdapter snakeAdapter;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        snakeSetupListView = getListView();
        snakeSetupListView.setOnItemClickListener(viewSetupListener);

        String[] from = new String[] { "name" };
        int[] to = new int[] {R.id.snakeSetupView};
        snakeAdapter = new SimpleCursorAdapter(SetupList.this, R.layout.snake_setup_item, null, from, to);
        setListAdapter(snakeAdapter);

    }

    OnItemClickListener viewSetupListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent viewSetup = new Intent(SetupList.this, ViewSetup.class);

            viewSetup.putExtra(ROW_ID, l);
            startActivity(viewSetup);
        }
    };
}
