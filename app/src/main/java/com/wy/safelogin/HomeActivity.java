package com.wy.safelogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wy.facelib.FaceDetectActivity;
import com.wy.fingerlib.FingerActivity;
import com.wy.lockpattern.GestureActivity;
import com.wy.opencvlib.activity.FaceDetActivity;
import com.wy.opencvlib.activity.FaceSimilarActivity;
import com.wy.ywkeyboard.YWKeyboardActivity;


public class HomeActivity extends Activity implements View.OnClickListener {

    private Button bt_det_face,bt_similar,bt_safe_keyboard,bt_finger,bt_gesture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();


    }

    private void init() {
        bt_det_face=findViewById(R.id.bt_det_face);
        bt_similar=findViewById(R.id.bt_similar);
        bt_safe_keyboard=findViewById(R.id.bt_safe_keyboard);
        bt_finger=findViewById(R.id.bt_finger);
        bt_gesture=findViewById(R.id.bt_gesture);
        bt_det_face.setOnClickListener(this);
        bt_similar.setOnClickListener(this);
        bt_safe_keyboard.setOnClickListener(this);
        bt_finger.setOnClickListener(this);
        bt_gesture.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_det_face) {
            startActivity(FaceDetectActivity.class);
        }
        if (v.getId() == R.id.bt_similar) {
            startActivity(FaceSimilarActivity.class);
        }
        if (v.getId() == R.id.bt_safe_keyboard) {
            startActivity(YWKeyboardActivity.class);
        }
        if (v.getId() == R.id.bt_finger) {
            startActivity(FingerActivity.class);
        }
        if (v.getId() == R.id.bt_gesture) {
            startActivity(GestureActivity.class);
        }
    }

    private void startActivity(Class cl){
        Intent intent=new Intent(this,cl);
        startActivity(intent);
    }
}
