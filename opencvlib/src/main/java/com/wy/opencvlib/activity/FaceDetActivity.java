package com.wy.opencvlib.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.wy.opencvlib.R;

import java.io.IOException;
import java.util.List;

import static android.graphics.Color.GREEN;

@SuppressLint("StaticFieldLeak")
public class FaceDetActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback, Camera.PreviewCallback {

    private Button bt_det;
    private ImageView iv;
    private Bitmap bitmap;
    private ProgressDialog pd;
    private int FACE_MAX_NUM = 10;
    private int realFaceNum = 0;
    private Paint paint;
    private boolean hasDetected = false;


    private SurfaceView sf_view;
    private SurfaceHolder holder;
    private Camera camera;
    private int mCameraId = 1;
    private Camera.Size mPreviewSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facedet);


        init();

        initData();
    }

    private void init() {
        bt_det = findViewById(R.id.bt_det);
        iv = findViewById(R.id.iv);

        bt_det.setOnClickListener(this);

        pd = new ProgressDialog(this);
        pd.setTitle("温馨提示");
        pd.setMessage("正在检测人脸,请稍后...");


        paint = new Paint();
        paint.setColor(GREEN);
        paint.setStrokeWidth(3.0f);
        paint.setStyle(Paint.Style.STROKE);


        sf_view = findViewById(R.id.sf_view);
        holder = sf_view.getHolder();
        holder.addCallback(this);

    }

    private void initData() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test1);
        bitmap = bitmap.copy(Bitmap.Config.RGB_565, true);
        iv.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_det) {
            if (hasDetected) {
                Toast.makeText(this, "已经检测过了", Toast.LENGTH_SHORT).show();
            } else {
                new FindFaceTask().execute();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startCamera();
        camera.setPreviewCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

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


    private void startCamera() {
        camera = getCamera(mCameraId);
        if (camera != null && holder != null) {
            startPreview(camera, holder);
        }
    }


    private Camera getCamera(int id) {
        Camera camera = null;
        try {
            camera = Camera.open(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return camera;
    }


    /**
     * 预览相机
     */
    private void startPreview(Camera camera, SurfaceHolder holder) {
        try {
            setupCamera(camera);
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

    }


    private void setupCamera(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        camera.setDisplayOrientation(90);
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        if (mPreviewSize == null) {
            mPreviewSize = getOptimalPreviewSize(previewSizes, sf_view.getWidth(), sf_view.getHeight());
        }
        if (mPreviewSize != null) {
            parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
        }
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        camera.setParameters(parameters);
    }


    //根据横竖屏自动调节preview方向，Starting from API level 14, this method can be called when preview is active.
    private static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }


    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int width, int height) {
        Camera.Size result = null;
        //特别注意此处需要规定rate的比是大的比小的，不然有可能出现rate = height/width，但是后面遍历的时候，current_rate = width/height,所以我们限定都为大的比小的。
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


    private class FindFaceTask extends AsyncTask<Void, Void, FaceDetector.Face[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
        }


        @Override
        protected FaceDetector.Face[] doInBackground(Void... voids) {
            FaceDetector faceDetector = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), FACE_MAX_NUM);
            FaceDetector.Face[] faces = new FaceDetector.Face[FACE_MAX_NUM];
            realFaceNum = faceDetector.findFaces(bitmap, faces);
            if (realFaceNum > 0) {
                return faces;
            }
            return null;
        }


        @Override
        protected void onPostExecute(FaceDetector.Face[] faces) {
            super.onPostExecute(faces);
            pd.dismiss();
            if (faces == null) {
                Toast.makeText(FaceDetActivity.this, "抱歉，图片中未检测到人脸", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FaceDetActivity.this, "图片中检测到" + realFaceNum + "张人脸", Toast.LENGTH_SHORT).show();
                drawFacesArea(faces);
            }
        }
    }

    private void drawFacesArea(FaceDetector.Face[] faces) {
        float eyesDistance = 0f;//两眼间距
        Canvas canvas = new Canvas(bitmap);
        for (int i = 0; i < faces.length; i++) {
            FaceDetector.Face face = faces[i];
            if (face != null) {
                PointF pointF = new PointF();
                face.getMidPoint(pointF);//获取人脸中心点
                eyesDistance = face.eyesDistance();//获取人脸两眼的间距
                //画出人脸的区域
                canvas.drawRect(pointF.x - eyesDistance, pointF.y - eyesDistance / 2, pointF.x + eyesDistance, pointF.y + eyesDistance * 1.5f, paint);
                hasDetected = true;
            }
        }
        //画出人脸区域后要刷新ImageView
        iv.invalidate();
    }


}
