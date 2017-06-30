package utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.security.Permission;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 常用功能
 * Created by Vieboo on 2017/6/30 0030.
 */

public class VBUsedUtil {

    /**
     * 跳转应用市场（APP评分功能）
     * @param context
     */
    public static void goMarketScore(Context context) {
        try {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "没有找到应用市场", Toast.LENGTH_SHORT).show();
        }
    }


//    public void phoneCall(Context context, String phone) {
//        try {
//            if (EasyPermissions.hasPermissions(context, Manifest.permission.CALL_PHONE)) {
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
//                context.startActivity(intent);
//            }
//            else {
//                Toast.makeText(context, "请在系统设置中开启拨打电话的权限", Toast.LENGTH_SHORT).show();
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(context, "请在系统设置中开启拨打电话的权限", Toast.LENGTH_SHORT).show();
//        }
//    }

}
