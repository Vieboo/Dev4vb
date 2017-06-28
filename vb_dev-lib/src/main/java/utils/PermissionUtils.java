package utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by Administrator on 2016/10/19 0019.
 */
public class PermissionUtils {

    public static final String CAMERA = Manifest.permission.CAMERA;
    public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String SEND_SMS = Manifest.permission.SEND_SMS;
    public static final String INTERNET = Manifest.permission.INTERNET;
    public static final String INSTALL_PACKAGES = Manifest.permission.INSTALL_PACKAGES;
    public static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String READ_CONTACTS = Manifest.permission.READ_CONTACTS;
    public static final String BLUETOOTH = Manifest.permission.BLUETOOTH;
    public static final String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;

    /**
     * 获取是否开启权限
     * @param context
     * @param permission
     * @return
     */
    public static boolean hasPermission(Context context, String permission) {
        PackageManager pm = context.getPackageManager();
        boolean result = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission(permission, context.getPackageName()));
        return result;
    }


    /**
     * 请求权限
     * @param activity
     */
    public static void requestPermission(Activity activity, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                int hasWriteContactsPermission = activity.checkSelfPermission(permission);//权限检查
                if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                    activity.requestPermissions(new String[]{Manifest.permission.CAMERA}, 0);
                    return ;//没有权限，结束
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}
