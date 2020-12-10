package com.wy.opencvlib.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

public class CircleSurfaceView extends SurfaceView {

    public CircleSurfaceView(Context context) {
        super(context);
    }

    public CircleSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void draw(Canvas canvas) {
        Log.e("wy", "draw: height:" + this.getHeight() + ",width:" + this.getWidth() + ",x:" + this.getX() + ",y:" + this.getY() + ",left:" + getLeft() + ",top:" + getTop());
//        Path path = new Path();
//        //用矩形表示SurfaceView宽高
//        RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
//        //15.0f即是圆角半径
//        path.addRoundRect(rect, 15.0f, 15.0f, Path.Direction.CCW);
//        //裁剪画布，并设置其填充方式
//        canvas.clipPath(path, Region.Op.REPLACE);
        Path path = new Path();
        int r = this.getWidth() > this.getHeight() ? this.getHeight() / 2 : this.getWidth() / 2;

        float dx = (float) getWidth() / 2;

        //设置裁剪的圆心，半径
        path.addCircle(dx , getTop() + r, r, Path.Direction.CCW);
        //裁剪画布，并设置其填充方式
        canvas.clipPath(path, Region.Op.REPLACE);
        super.draw(canvas);
    }
}
