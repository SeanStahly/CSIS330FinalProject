package com.example.CSIS330FinalProject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.telephony.SmsManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sean
 * on 11/10/14.
 */
public class ViewSetup extends Activity {
    public long rowID;
    private Bundle extras;
    ArrayList<TextView> labels = new ArrayList<TextView>();
    ArrayList<TextView> people = new ArrayList<TextView>();
    ArrayList<String> peopleNames = new ArrayList<String>();
    ArrayList<TextView> instruments = new ArrayList<TextView>();
    ArrayList<String> instumentTypes = new ArrayList<String>();
    ArrayList<String> phoneNumbers = new ArrayList<String>();
    ArrayList<LinearLayout> linearLayouts = new ArrayList<LinearLayout>();

    private int currentDialog;

    private  int channelCount;

    private String toDBPeopleString;
    private String toDBInstrumentString;
    private String toDBPhoneNumberString;

    private String nameOfSetup;


    @Override
    public void onCreate(Bundle savedInsanceState) {
        super.onCreate(savedInsanceState);

        extras = getIntent().getExtras();
        if (extras.getBoolean("new")) {
            getNewConstants();
            initLayout();
        }
        rowID = extras.getLong("row_id");
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!extras.getBoolean("new")) {
            new LoadSetupTask().execute(rowID);
        }

    }

    protected void initLayout() {
        setTitle(nameOfSetup);
        LinearLayout viewGroup = new LinearLayout(this);
        viewGroup.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < channelCount; i++) {

            linearLayouts.add(i, new LinearLayout(this));
            linearLayouts.get(i).setOrientation(LinearLayout.HORIZONTAL);
            linearLayouts.get(i).setMinimumHeight(50);
            linearLayouts.get(i).addView(labels.get(i));
            linearLayouts.get(i).addView(people.get(i));
            linearLayouts.get(i).addView(instruments.get(i));
            currentDialog = (i+1);
            linearLayouts.get(i).setOnClickListener(new MyLovelyOnClickListener(currentDialog));

            linearLayouts.get(i).setId(i);

            viewGroup.addView(linearLayouts.get(i));

            viewGroup.addView(line());
        }

        ScrollView sV = new ScrollView(this);
        sV.addView(viewGroup);
        setContentView(sV);
    }

    private void createJSONs()  {
        JSONObject peopleJson = new JSONObject();
        JSONObject instrumentJson = new JSONObject();
        JSONObject phoneNumberJson = new JSONObject();

        try {
            peopleJson.put("peopleArray", new JSONArray(peopleNames));
            instrumentJson.put("instrumentArray", new JSONArray(instumentTypes));
            phoneNumberJson.put("phoneNumberArray", new JSONArray(phoneNumbers));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        toDBPeopleString = peopleJson.toString();
        toDBInstrumentString = instrumentJson.toString();
        toDBPhoneNumberString = phoneNumberJson.toString();

    }

    private TextView line() {
        TextView tv = new TextView(this);
        tv.setHeight(3);
        tv.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        tv.setBackgroundColor(Color.GRAY);
        return tv;
    }

    private TextView labelTextView(int i) {
        TextView tv = new TextView(this);
        tv.setText(""+i+":");
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(20);
        tv.setPadding(8,8,8,8);
        return tv;
    }

    private TextView assignmentTextView() {
        TextView tv = new TextView(this);
        tv.setText("");
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(20);
        tv.setPadding(8,8,8,8);
        return tv;
    }

    private class LoadSetupTask extends AsyncTask<Long, Object, Cursor> {
        SetupDatabaseConnector databaseConnector = new SetupDatabaseConnector(ViewSetup.this);

        @Override
        protected Cursor doInBackground(Long... params) {
            databaseConnector.open();

            return databaseConnector.getOneSetup(params[0]);
        }

        @Override
        protected void onPostExecute(Cursor result) {
            super.onPostExecute(result);
            result.moveToFirst();

            int nameIndex = result.getColumnIndex("name");
            int channelsIndex = result.getColumnIndex("channels");
            int peopleIndex = result.getColumnIndex("people");
            int instrumentsIndex = result.getColumnIndex("instruments");
            int phonesIndex = result.getColumnIndex("phones");

            nameOfSetup = result.getString(nameIndex);
            channelCount = result.getInt(channelsIndex);
            String peopleString = result.getString(peopleIndex);
            String instrumnetsString = result.getString(instrumentsIndex);
            String phonesString = result.getString(phonesIndex);

            try {
                JSONObject peopleJson = new JSONObject(peopleString);
                JSONArray peopleJsonArray = peopleJson.optJSONArray("peopleArray");
                peopleNames = new ArrayList<String>();
                JSONObject instrumentJson = new JSONObject(instrumnetsString);
                JSONArray instrumentJsonArray = instrumentJson.optJSONArray("instrumentArray");
                instumentTypes = new ArrayList<String>();
                JSONObject phoneJson = new JSONObject(phonesString);
                JSONArray phoneJsonArray = phoneJson.optJSONArray("phoneNumberArray");
                phoneNumbers = new ArrayList<String>();


                for (int i =0; i<channelCount; i++) {
                    peopleNames.add(i, peopleJsonArray.get(i).toString());
                    instumentTypes.add(i, instrumentJsonArray.get(i).toString());
                    phoneNumbers.add(i, phoneJsonArray.get(i).toString());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            result.close();
            databaseConnector.close();
            generateTextViews();
            setTextViews();
            initLayout();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_setup_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveSetup:
                AsyncTask<Object, Object, Object> saveSetupTask = new AsyncTask<Object, Object, Object>() {
                    @Override
                    protected Object doInBackground(Object... objects) {
                        saveSetup();
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Object result) {
                        finish();
                    }
                };
                saveSetupTask.execute((Object[]) null);
                return true;
            case R.id.sendText:
                final String message = createTextMessage();

                LayoutInflater inflater = LayoutInflater.from(ViewSetup.this);
                View textMessageDialog = inflater.inflate(R.layout.dialog_send_text, null);

                final TextView messageEntry = (TextView) textMessageDialog.findViewById(R.id.dialogMessageToBeSent);
                messageEntry.setText(message);

                AlertDialog dialog = new AlertDialog.Builder(ViewSetup.this)
                        .setView(textMessageDialog)
                        .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sendSMSMessage(message);
                            }
                        })
                        .setNegativeButton("Cancel", null).create();
                dialog.show();
                return true;
            case R.id.exit:
                finish();
                return true;
            case R.id.delete:
                deleteSetup();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveSetup() {
        SetupDatabaseConnector databaseConnector = new SetupDatabaseConnector(this);
        if (extras.getBoolean("new")) {
            String name = extras.getString("name_id");
            int channels = extras.getInt("channel_count");
            createJSONs();
            databaseConnector.insertSetup(name, channels, toDBPeopleString, toDBInstrumentString, toDBPhoneNumberString);
        } else {
            createJSONs();
            databaseConnector.updateSetup(rowID, toDBPeopleString, toDBInstrumentString, toDBPhoneNumberString);

        }
    }

    private void deleteSetup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewSetup.this);

        builder.setTitle(R.string.confirmDeleteTitle);
        builder.setMessage(R.string.confirmDeleteMessage);

        builder.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int button) {
                final SetupDatabaseConnector databaseConnector = new SetupDatabaseConnector(ViewSetup.this);

                AsyncTask<Long, Object, Object> deleteTask = new AsyncTask<Long, Object, Object>() {
                    @Override
                    protected Object doInBackground(Long... params) {
                        databaseConnector.deleteSetup(params[0]);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object result) {
                        finish();
                    }
                };
                deleteTask.execute(new Long[] { rowID});
            }
        });

        builder.setNegativeButton(R.string.button_cancel, null);
        builder.show();
    }

    private void getNewConstants() {
        nameOfSetup = extras.getString("name_id");
        channelCount = extras.getInt("channel_count");
        generateTextViews();
        for (int i = 0; i < channelCount; i++) {

            peopleNames.add(i, people.get(i).getText().toString());
            instumentTypes.add(i, instruments.get(i).getText().toString());
            phoneNumbers.add(i, "");
        }
    }

    private void generateTextViews() {

        for (int i = 0; i < channelCount; i++) {
            labels.add(i, labelTextView(i + 1));
            people.add(i, assignmentTextView());
            instruments.add(i, assignmentTextView());
        }
    }

    private void setTextViews() {
        for (int i =0; i < channelCount; i++) {
            people.get(i).setText(
                    peopleNames.get(i));

            instruments.get(i).setText(
                    instumentTypes.get(i));
        }
    }

    private class MyLovelyOnClickListener implements View.OnClickListener {
        int myVariable;
        public MyLovelyOnClickListener(int v) {
            this.myVariable = v;
        }

        @Override
        public void onClick(View v) {
            LayoutInflater inflater = LayoutInflater.from(ViewSetup.this);
            View customAssingmentDialog = inflater.inflate(R.layout.dialog_assignment, null);

            final TextView nameEntry = (EditText) customAssingmentDialog.findViewById(R.id.dialogPersonNameEdit);
            nameEntry.setText(peopleNames.get(myVariable-1));
            final TextView numberEntry = (EditText) customAssingmentDialog.findViewById(R.id.dialogPhoneNumberEditText);
            numberEntry.setText(phoneNumbers.get(myVariable-1));
            final TextView instrumentEntry = (EditText) customAssingmentDialog.findViewById(R.id.dialogInstrumentEditText);
            instrumentEntry.setText(instumentTypes.get(myVariable-1));

            AlertDialog  dialog = new AlertDialog.Builder(ViewSetup.this)
                    .setView(customAssingmentDialog)
                    .setTitle("Channel "+myVariable+":")
                    .setPositiveButton("Assign", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (nameEntry != null && instrumentEntry != null) {
                                int index = myVariable -1;
                                peopleNames.set(index, nameEntry.getText().toString());
                                phoneNumbers.set(index, numberEntry.getText().toString());
                                instumentTypes.set(index, instrumentEntry.getText().toString());

                                generateTextViews();
                                setTextViews();
                                initLayout();

                            }

                        }
                    })
                    .setNegativeButton("Cancel", null).create();
            dialog.show();

        }
    };

    protected void sendSMSMessage(String message) {
        ArrayList<String> recipients = new ArrayList<String>();
        for (int i =0; i < channelCount; i++) {
            String test = phoneNumbers.get(i);
            if (!test.equals("")) {
                boolean add = true;
                for (String s : recipients) {
                    if (test.equals(s)) {
                        add = false;
                    }
                }
                if (add) {
                    recipients.add(test);
                }
            }
        }
        SmsManager smsManager = SmsManager.getDefault();
        for (String s : recipients) {
            smsManager.sendTextMessage(s, null, message, null, null);
        }
    }

    private String createTextMessage() {
        String message = "Snake List for "+ nameOfSetup+":\n";
        for (int i =0; i < channelCount; i++) {
            if ((!peopleNames.get(i).equals("")) || (!instumentTypes.get(i).equals(""))) {
                message += ""+(i+1)+": "+peopleNames.get(i)+" - "+instumentTypes.get(i) + "\n";
            }
        }
        return message;
    }

}
