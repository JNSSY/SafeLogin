package com.wy.facelib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class FaceDetectActivity extends AppCompatActivity implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private SurfaceView sv;
    private SurfaceHolder holder;
    private Camera camera;
    private int cameraID = 0;
    private Camera.Size mPreviewSize;
    private static final int FACE_MAX_NUM = 10;
    private int realFaceNum = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_detect);

        init();
    }

    private void init() {
        sv = findViewById(R.id.sv);
        holder = sv.getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initCamera();
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        camera.setPreviewCallback(this);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            camera.autoFocus(null);
            camera.setPreviewCallback(null);
            camera.setOneShotPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }


    private void initCamera() {
        camera = getCamera();
        if (camera != null && holder != null) {
            startPreview();
        }
    }

    private Camera getCamera() {
        if (camera == null) {
            camera = Camera.open(cameraID);
        }
        return camera;
    }

    private void startPreview() {
        setupCamera();
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupCamera() {
        Camera.Parameters parameters = camera.getParameters();
        camera.setDisplayOrientation(90);
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        if (mPreviewSize == null) {
            mPreviewSize = getOptimalPreviewSize(previewSizes, sv.getWidth(), sv.getHeight());
        }
        if (mPreviewSize != null) {
            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
        }
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        camera.setParameters(parameters);
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int width, int height) {
        Camera.Size result = null;
        float rate = (float) Math.max(width, height) / (float) Math.min(width, height);
        float tmp_diff;
        float min_diff = -1f;
        for (Camera.Size size : sizes) {
            float current_rate = (float) Math.max(size.width, size.height) / (float) Math.min(size.width, size.height);
            tmp_diff = Math.abs(current_rate - rate);
            if (min_diff < 0) {
                min_diff = tmp_diff;
                result = size;
            }
            if (tmp_diff < min_diff) {
                min_diff = tmp_diff;
                result = size;
            }
        }
        return result;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        mPreviewSize = camera.getParameters().getPreviewSize();//获取尺寸,格式转换的时候要用到
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        YuvImage yuvimage = new YuvImage(
                data,
                ImageFormat.NV21,
                mPreviewSize.width,
                mPreviewSize.height,
                null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(0, 0, mPreviewSize.width, mPreviewSize.height), 100, baos);// 80--JPG图片的质量[0-100],100最高
        byte[] rawImage = baos.toByteArray();
        //将rawImage转换成bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length, options);


//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.RGB_565;
//        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        FaceDetector faceDetector = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), FACE_MAX_NUM);
        FaceDetector.Face[] faces = new FaceDetector.Face[FACE_MAX_NUM];
        realFaceNum = faceDetector.findFaces(bitmap, faces);
        bitmap.recycle();
        if (realFaceNum > 0) {
            Log.e("wy", "检测到的人脸数 " + realFaceNum);
        }
    }

//    private class FindFaceTask extends AsyncTask<Void, Void, FaceDetector.Face[]> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//
//        @Override
//        protected FaceDetector.Face[] doInBackground(Void... voids) {
//            FaceDetector faceDetector = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), FACE_MAX_NUM);
//            FaceDetector.Face[] faces = new FaceDetector.Face[FACE_MAX_NUM];
//            realFaceNum = faceDetector.findFaces(bitmap, faces);
//            return faces;
//        }
//
//
//        @Override
//        protected void onPostExecute(FaceDetector.Face[] faces) {
//            super.onPostExecute(faces);
//            if (faces == null) {
//                Toast.makeText(FaceDetectActivity.this, "抱歉，图片中未检测到人脸", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(FaceDetectActivity.this, "图片中检测到" + realFaceNum + "张人脸", Toast.LENGTH_SHORT).show();
////                drawFacesArea(faces);
//            }
//        }
//    }
}
