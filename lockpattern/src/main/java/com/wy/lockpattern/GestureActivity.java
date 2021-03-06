package com.wy.lockpattern;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class GestureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture);
        findViewById(R.id.patternlock_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestureActivity.this, PatternLockActivity.class);
                intent.putExtra("type", "setting");
                startActivity(intent);
            }
        });
        findViewById(R.id.patternlock_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestureActivity.this, PatternLockActivity.class);
                intent.putExtra("type", "open");
                startActivity(intent);
            }
        });
    }
}
