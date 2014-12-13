package com.example.CSIS330FinalProject;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class SetupList extends Activity {
    public static final String ROW_ID = "row_id";
    public static final String NAME_ID = "name_id";
    public static final String CHANNEL_COUNT = "channel_count";
    private ListView snakeSetupListView;
    private CursorAdapter snakeAdapter;
    private Button createSnakeListBtn;

    protected String setupName;
    protected int numberOfChannels;

//    public Button sendBtn;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_list_layout);


        String[] from = new String[]{"name"};
        int[] to = new int[]{R.id.snakeSetupView};
        snakeAdapter = new SimpleCursorAdapter(SetupList.this, R.layout.snake_setup_item, null, from, to);

        snakeSetupListView = (ListView) findViewById(R.id.setupList);
        snakeSetupListView.setOnItemClickListener(viewSetupListener);
        snakeSetupListView.setAdapter(snakeAdapter);

        createSnakeListBtn = (Button) findViewById(R.id.createNewSetupList);
        createSnakeListBtn.setOnClickListener(createNewSetup);


//        sendBtn = (Button) findViewById(R.id.btnSendSMS);
//
//        sendBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                sendSMSMessage();
//            }
//        });

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        Cursor cursor = snakeAdapter.getCursor();
        if (cursor != null)
            cursor.deactivate();

        snakeAdapter.changeCursor(null);
        super.onStop();
    }

    private class getSetupsTask extends AsyncTask<Object, Object, Cursor> {
        SetupDatabaseConnector databaseConnector = new SetupDatabaseConnector(SetupList.this);

        @Override
        protected Cursor doInBackground(Object... params) {
            databaseConnector.open();
            return databaseConnector.getAllSetups();
        }

        @Override
        protected void onPostExecute(Cursor result) {
            snakeAdapter.changeCursor(result);
            databaseConnector.close();
        }
    }

    OnItemClickListener viewSetupListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent viewSetup = new Intent(SetupList.this, ViewSetup.class);

            viewSetup.putExtra(ROW_ID, l);
            startActivity(viewSetup);
        }
    };

    View.OnClickListener createNewSetup = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            LayoutInflater inflater = LayoutInflater.from(SetupList.this);
            View customDialogView = inflater.inflate(R.layout.dialog_new_setup, null);

            final TextView nameEntry = (EditText) customDialogView.findViewById(R.id.dialogSetupName);
            final TextView numberOfChannelsEntry = (EditText) customDialogView.findViewById(R.id.dialogChannelNumber);
            final boolean confirm = false;

            AlertDialog dialog = new AlertDialog.Builder(SetupList.this)
                    .setView(customDialogView)
                    .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (nameEntry != null && numberOfChannelsEntry != null) {
                                setupName = nameEntry.getText().toString();
                                numberOfChannels = Integer.parseInt(numberOfChannelsEntry.getText().toString());
                                successfullyCreateNewSetup();

                            }
                        }
                    })
                    .setNegativeButton("Cancel", null).create();
            dialog.show();


            }

//        }
    };

    public void successfullyCreateNewSetup() {
        Intent newSetup = new Intent(SetupList.this, ViewSetup.class);
        newSetup.putExtra(NAME_ID, setupName);
        newSetup.putExtra(CHANNEL_COUNT, numberOfChannels);

        startActivity(newSetup);
    }


//    protected void sendSMSMessage() {
//        SmsManager smsManager = SmsManager.getDefault();
//        smsManager.sendTextMessage("4024179736", null, "did this work?", null, null);
//
//    }
}
