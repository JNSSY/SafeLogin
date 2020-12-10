package com.wy.ywkeyboard;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class YWKeyboardActivity extends AppCompatActivity {
    private LinearLayout rootView;
    private ScrollView scrollView;
    private Button bt_get;
    private EditText specialEd;
    private KeyboardUtil keyboardUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_safe_keyboard);
        rootView = (LinearLayout) findViewById(R.id.root_view);
        scrollView = (ScrollView) findViewById(R.id.sv_main);

        bt_get = (Button) findViewById(R.id.bt_get);
        specialEd = (EditText) findViewById(R.id.special_ed);

        initMoveKeyBoard();

        bt_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(YWKeyboardActivity.this, specialEd.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });



//        tv.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
//        tv.playSoundEffect(SoundEffectConstants.CLICK);


    }

    private void initMoveKeyBoard() {
        keyboardUtil = new KeyboardUtil(this, rootView, scrollView, "安全键盘",true);
//        keyboardUtil.setOtherEdittext(normalEd);
        // monitor the KeyBarod state
        keyboardUtil.setKeyBoardStateChangeListener(new KeyBoardStateListener());
        // monitor the finish or next Key
//        keyboardUtil.setInputOverListener(new inputOverListener());
        specialEd.setOnTouchListener(new KeyboardTouchListener(keyboardUtil, KeyboardUtil.INPUTTYPE_ABC, -1));
    }

    class KeyBoardStateListener implements KeyboardUtil.KeyBoardStateChangeListener {

        @Override
        public void KeyBoardStateChange(int state, EditText editText) {
//            System.out.println("state" + state);
//            System.out.println("editText" + editText.getText().toString());
        }
    }

    class inputOverListener implements KeyboardUtil.InputFinishListener {

        @Override
        public void inputHasOver(int onclickType, EditText editText) {
//            System.out.println("onclickType" + onclickType);
            Log.e("wy", editText.getText().toString());
        }
    }


}
