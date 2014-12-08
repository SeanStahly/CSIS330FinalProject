package com.example.CSIS330FinalProject;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class SetupList extends ListActivity {
    public static final String ROW_ID = "row_id";
    private ListView snakeSetupListView;
    private CursorAdapter snakeAdapter;
//    public Button sendBtn;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        snakeSetupListView = getListView();
        snakeSetupListView.setOnItemClickListener(viewSetupListener);

        String[] from = new String[] { "name" };
        int[] to = new int[] {R.id.snakeSetupView};
        snakeAdapter = new SimpleCursorAdapter(SetupList.this, R.layout.snake_setup_item, null, from, to);
        setListAdapter(snakeAdapter);

//        sendBtn = (Button) findViewById(R.id.btnSendSMS);
//
//        sendBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                sendSMSMessage();
//            }
//        });

    }

    OnItemClickListener viewSetupListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent viewSetup = new Intent(SetupList.this, ViewSetup.class);

            viewSetup.putExtra(ROW_ID, l);
            startActivity(viewSetup);
        }
    };

//    protected void sendSMSMessage() {
//        SmsManager smsManager = SmsManager.getDefault();
//        smsManager.sendTextMessage("4024179736", null, "did this work?", null, null);
//
//    }
}
