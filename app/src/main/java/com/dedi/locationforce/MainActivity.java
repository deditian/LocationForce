package com.dedi.locationforce;

import androidx.appcompat.app.AppCompatActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";

    TextView showLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showLocation = (TextView) findViewById(R.id.txtStartForce);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(view -> {
            showLocation.setText("location");
            Intent intent = new Intent(MainActivity.this, MyServices.class);
            startService(intent);
        });


    }

    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String str = (String) intent.getExtras().get("DATA").toString();
            showLocation.setText(str);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filt = new IntentFilter("FILTER");
        this.registerReceiver(br, filt);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);
    }

}