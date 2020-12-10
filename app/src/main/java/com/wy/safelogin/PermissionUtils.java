package com.wy.safelogin;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;


/**
 * @author wy
 * @desc 权限申请工具类
 */
public class PermissionUtils {

    private String[] permissions = {
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
    };

    private int perCode[] = new int[]{
            0x001, 0x002, 0x003, 0x004, 0x005
    };

    private Activity activity;
    private AlertDialog.Builder builder;
    private int index = 0;

    public PermissionUtils(Activity activity) {
        this.activity = activity;
        builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
    }

    /**
     * 检测一组权限
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkPermission(Activity activity) {
        if (index < permissions.length) {
            int checkSelfPermission = activity.checkSelfPermission(permissions[index]);
            if (checkSelfPermission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{permissions[index]}, perCode[index]);
            }
        }
    }

    /**
     * 打开拨打电话权限
     */
    public boolean requestCameraPer() {
        if (ContextCompat.checkSelfPermission(activity, permissions[4]) !=
                PackageManager.PERMISSION_GRANTED) {
            showToast("请开启拨打电话权限！");
            goSetting();
            return false;
        } else {
            return true;
        }
    }


    /**
     * 打开拨打电话权限
     */
    public boolean requestCallPhonePer() {
        if (ContextCompat.checkSelfPermission(activity, permissions[0]) !=
                PackageManager.PERMISSION_GRANTED) {
            showToast("请开启拨打电话权限！");
            goSetting();
            return false;
        } else {
            return true;
        }
    }

    /**
     * 打开发送短信权限
     */
    public boolean requstSendSmsPer() {
        if (ContextCompat.checkSelfPermission(activity, permissions[1]) !=
                PackageManager.PERMISSION_GRANTED) {
            showToast("请开启发送短信权限");
            goSetting();
            return false;
        } else {
            return true;
        }
    }

    /**
     * 打开读写存储卡权限
     */
    public boolean requestWriteExStroagePer() {
        if (ContextCompat.checkSelfPermission(activity, permissions[2]) !=
                PackageManager.PERMISSION_GRANTED) {
            showToast("请开启读写存储卡权限");
            goSetting();
            return false;
        } else {
            return true;
        }
    }


    /**
     * 打开定位权限
     */
    public boolean requestLocation() {
        if (ContextCompat.checkSelfPermission(activity, permissions[3]) !=
                PackageManager.PERMISSION_GRANTED) {
            showDialog("请开启定位权限");
//            showToast("请开启定位权限");
//            goSetting();
            return false;
        } else {
            return true;
        }
    }

    private void showToast(String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    private void goSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        activity.startActivity(intent);
    }

    public void showDialog(String msg) {
        builder.setIcon(activity.getResources().getDrawable(R.mipmap.ic_launcher));
        builder.setTitle("温馨提示");
        builder.setMessage(msg);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


}
