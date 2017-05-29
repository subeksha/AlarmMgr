package com.example.subeksha.alarmmgr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("GHT_LOCATION")) {
                String lat = intent.getStringExtra("latitude");
                String lon = intent.getStringExtra("longitude");

                Log.e("MAIN_ACTIVITY", "Latitude:" + lat);
                Log.e("MAIN_ACTIVITY", "Longitude:" + lon);
            }
        }
    };
    private AlarmReceiver receiver = new AlarmReceiver();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter("GHT_LOCATION"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_start:
                receiver.setAlarm(this);
                break;

            case R.id.action_stop:
                receiver.cancelAlarm(this);
                break;

        }

        return super.onOptionsItemSelected(item);

    }
}
