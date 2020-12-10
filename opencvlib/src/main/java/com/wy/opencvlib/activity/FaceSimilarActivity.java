package com.wy.opencvlib.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.wy.opencvlib.R;

import org.bytedeco.javacpp.opencv_core;

public class FaceSimilarActivity extends AppCompatActivity {

    static {
        System.loadLibrary("detection_based_tracker");
    }

    private ImageView iv1, iv2;
    private Bitmap b1, b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar);

        init();

    }

    private void init() {
        b1 = BitmapFactory.decodeResource(getResources(), R.mipmap.test1);
        b2 = BitmapFactory.decodeResource(getResources(), R.mipmap.test3);

        iv1 = findViewById(R.id.iv1);
        iv2 = findViewById(R.id.iv2);

        iv1.setImageBitmap(b1);
        iv2.setImageBitmap(b2);

    }

    public void doCheck(View view) {
        compare();
    }

    private void compare() {
        opencv_core.Mat mat1 = new opencv_core.Mat();
        opencv_core.Mat mat2 = new opencv_core.Mat();

        mat1 = bitmapToMat(b1);
        mat2 = bitmapToMat(b2);


    }


    //Bitmap转Mat
    public static opencv_core.Mat bitmapToMat(Bitmap bm) {
//        Bitmap bmp32 = bm.copy(Bitmap.Config.RGB_565, true);
//        opencv_core.Mat imgMat = new opencv_core.Mat(bm.getHeight(), bm.getWidth(), 8, new opencv_core.Scalar(0));
//        Utils.bitmapToMat(bmp32, imgMat);
//        return imgMat;
        return null;
    }


}
