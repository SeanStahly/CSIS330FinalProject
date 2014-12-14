package com.example.CSIS330FinalProject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by sean
 * on 11/10/14.
 */
public class ViewSetup extends Activity {
    public long rowID;
    private ListView snakeViewList;
    private CursorAdapter snakeAdapter;
    private Bundle extras;
    ArrayList<TextView> labels = new ArrayList<TextView>();
    ArrayList<TextView> people = new ArrayList<TextView>();
    ArrayList<String> peopleNames = new ArrayList<String>();
    ArrayList<TextView> instruments = new ArrayList<TextView>();
    ArrayList<String> instumentTypes = new ArrayList<String>();
    ArrayList<String> phoneNumbers = new ArrayList<String>();

    private  int channelCount;

    private String toDBPeopleString;
    private String toDBInstrumentString;
    private String toDBPhoneNumberString;


    ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInsanceState) {
        super.onCreate(savedInsanceState);


//        snakeViewList = getListView();
        extras = getIntent().getExtras();

        if (extras.getBoolean("new")) {
            getNewConstants();
        } else {
            getOldConstants();
        }

        initLayout();

//        snakeViewList.setOnItemClickListener(viewAssignmentListner);
//
//
//
//        Long i = extras.getLong(ROW_ID);
//        Log.d("test", i.toString());
//
//        if (extras.getLong(ROW_ID) > 0) {
//
//        } else {
//
//        }
//
//        String[] from =  new String[] { "name" };
//        int[] to = new int[] {R.id.assignmentItem};
//        snakeAdapter = new SimpleCursorAdapter(ViewSetup.this, R.layout.assignment_item, null, from, to);

//        setListAdapter(snakeAdapter);
        rowID = extras.getLong("row_id");
    }

    @Override
    public void onResume() {
        super.onResume();

        new LoadSetupTask().execute(rowID);

    }

    protected void initLayout() {
        LinearLayout viewGroup = new LinearLayout(this);
        viewGroup.setOrientation(LinearLayout.VERTICAL);



        for (int i = 0; i < extras.getInt("channel_count"); i++) {
            Log.i("Yo Over Here", ""+i);
            LinearLayout assignmentLayout = new LinearLayout(this);
            assignmentLayout.setOrientation(LinearLayout.HORIZONTAL);
            assignmentLayout.setMinimumHeight(50);

            labels.add(i, labelTextView(i+1));
            assignmentLayout.addView(labels.get(i));

            people.add(i, assignmentTextView());
            assignmentLayout.addView(people.get(i));
            Log.i("YEAH! YEAH!", people.get(i).getText().toString());
            peopleNames.add(i, people.get(i).getText().toString());

            instruments.add(i, assignmentTextView());
            assignmentLayout.addView(instruments.get(i));

            phoneNumbers.add(i, "");

            viewGroup.addView(assignmentLayout);

            viewGroup.addView(line());
        }

//        snakeViewList = new ListView(this);



//        snakeViewList.setAdapter(snakeAdapter);

//        LayoutInflater vi = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = vi.inflate(R.layout.assignment_item, null);
////
////
//        for (int i = 0; i < extras.getInt("channel_count"); i++) {
//            Log.i("Yo this is the test", "It's over HERE " + i);
//            TextView tv = (TextView) v.findViewById(R.id.assignmentItem);
//
//            tv.setText("blarg");
//            snakeViewList.addView(tv);
//            viewGroup.addView(v, i, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
//
//        }

//        viewGroup.addView(tv);
//        viewGroup.addView(snakeViewList);
        setContentView(viewGroup);
    }

    private void createJSONs()  {
        JSONObject peopleJson = new JSONObject();
        JSONObject instrumentJson = new JSONObject();
        JSONObject phoneNumberJson = new JSONObject();

        try {
            peopleJson.put("peopleArray", new JSONArray(peopleNames));
            instrumentJson.put("instrumentArray", new JSONArray(instumentTypes));
            phoneNumberJson.put("phoneNumberArray", new JSONArray(peopleNames));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        toDBPeopleString = peopleJson.toString();
        toDBInstrumentString = instrumentJson.toString();
        toDBPhoneNumberString = phoneNumberJson.toString();

//        try {
//            JSONObject json = new JSONObject(peopleArrayList);
//            JSONArray stuff = json.optJSONArray("peopleArray");
//            ArrayList<String> things = new ArrayList<String>();
//            for (int i =0; i<stuff.length(); i++) {
//                things.add(i, stuff.get(i).toString());
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

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
        tv.setText("empty");
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(20);
        tv.setPadding(8,8,8,8);
        return tv;
    }

    public void onClose() {

    }


//    AdapterView.OnItemClickListener viewAssignmentListner = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            Intent viewAssignment = new Intent(ViewSetup.this, ViewAssignment.class);
//
//            viewAssignment.putExtra(ROW_ID, l);
//            startActivity(viewAssignment);
//        }
//    };

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


            String name = result.getString(nameIndex);

            String peopleString = result.getString(peopleIndex);
            Log.i("YOYO BOI!!!", name);
            Log.i("This is a test", peopleString);
//            nameTextView.setText(result.getString(nameIndex));
//            phoneTextView.setText(result.getString(phoneIndex));
//            emailTextView.setText(result.getString(emailIndex));
//            streetTextView.setText(result.getString(streetIndex));
//            cityTextView.setText(result.getString(cityIndex));


            try {
                JSONObject json = new JSONObject(peopleString);
                JSONArray stuff = json.optJSONArray("peopleArray");
                peopleNames = new ArrayList<String>();
                for (int i =0; i<stuff.length(); i++) {
                    peopleNames.add(i, stuff.get(i).toString());
                    Log.i("Another Test!!!", peopleNames.get(i));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            result.close();
            databaseConnector.close();
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

    private void getOldConstants() {

    }

    private void getNewConstants() {
        channelCount = extras.getInt("channel_count");
    }


}
