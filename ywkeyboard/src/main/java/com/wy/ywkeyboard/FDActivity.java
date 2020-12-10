package com.wy.ywkeyboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;


@SuppressWarnings("all")
public class FDActivity extends AppCompatActivity {
    private EditText et_pwd;
    private LinearLayout rootView;
    private KeyboardUtil keyboardUtil;
    private ScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fd);

        sv = findViewById(R.id.sv);
        rootView = findViewById(R.id.rootView);
        et_pwd = findViewById(R.id.et_pwd);


        keyboardUtil = new KeyboardUtil(this, rootView, sv, "运维作业安全键盘",false);
        et_pwd.setOnTouchListener(new KeyboardTouchListener(keyboardUtil, KeyboardUtil.INPUTTYPE_ABC, -1));
    }
}
