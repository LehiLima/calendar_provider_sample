package com.example.myapplication;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Eventdto> listoEvents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_CALENDAR,Manifest.permission.WRITE_CALENDAR},
                1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // Run query
                    Cursor cur = null;
                    final ContentResolver cr = getContentResolver();

                    cr.registerContentObserver(Uri.parse("content://com.android.calendar/events"), false,  new ContentObserver(null) {
                        @Override
                        public void onChange(boolean selfChange, Uri uri) {
                            super.onChange(selfChange, uri);
                            getEvents(cr);
                        }
                    });
                    getEvents(cr);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void getEvents(ContentResolver cr) {
        Cursor cur;
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        Calendar c_start= Calendar.getInstance();
        c_start.set(2019,0,1,0,0); //Note that months start from 0 (January)
        Calendar c_end= Calendar.getInstance();
        c_end.set(2019,0,30,0,0); //Note that months start from 0 (January)
        String selection = "((dtstart >= "+c_start.getTimeInMillis()+") AND (dtend <= "+c_end.getTimeInMillis()+"))";
        //              String[] selectionArgs = new String[]{"lehilima@gmail.com", "com.google", "lehilima@gmail.com"};
        // Submit the query and get a Cursor object back.

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        values.put(CalendarContract.Calendars.VISIBLE, 1);
        cur = cr.query(Uri.parse("content://com.android.calendar/events"), EVENT_PROJECTION, selection, null, null);
        listoEvents.clear();

        // Use the cursor to step through the returned records
        while (cur.moveToNext()) {
            String calID;
            String organizer;
            String title;
            String description;
            String dtstart;
            String dtsFinish;
            String eventLocationm;

            // Get the field values
            calID = cur.getString(PROJECTION_ID_INDEX);
            organizer = cur.getString(PROJECTION_ORGANIZER_INDEX);
            title = cur.getString(PROJECTION_TITLE_INDEX);
            description = cur.getString(PROJECTION_DESCRIPTION_INDEX);
            dtstart = cur.getString(PROJECTION_DTSTART_INDEX);
            dtsFinish = cur.getString(PROJECTION_DTEND_INDEX);
            eventLocationm = cur.getString(PROJECTION_EVENT_LOCATION_INDEX);

            listoEvents.add(new Eventdto(calID,organizer,title,description,dtstart,dtsFinish,eventLocationm));

        }
        populateList();
    }

    public void populateList(){
        mRecyclerView = findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(this, listoEvents);
        mRecyclerView.setAdapter(mAdapter);
    }

// dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.EventsEntity.CALENDAR_ID,                           // 0
            CalendarContract.EventsEntity.ORGANIZER,                     // 1
            CalendarContract.EventsEntity.TITLE,                         // 2
            CalendarContract.EventsEntity.DESCRIPTION,                   // 3
            CalendarContract.EventsEntity.DTSTART,                       // 4
            CalendarContract.EventsEntity.DTEND,                         // 5
            CalendarContract.EventsEntity.EVENT_LOCATION                 // 6
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ORGANIZER_INDEX = 1;
    private static final int PROJECTION_TITLE_INDEX = 2;
    private static final int PROJECTION_DESCRIPTION_INDEX = 3;
    private static final int PROJECTION_DTSTART_INDEX = 4;
    private static final int PROJECTION_DTEND_INDEX = 5;
    private static final int PROJECTION_EVENT_LOCATION_INDEX = 6;

}



