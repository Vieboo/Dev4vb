package base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import log.BuildLog;
import vieboo.imageloader.ImageLoaderHelper;

/**
 * Created by Administrator on 2017/7/4 0004.
 */

public abstract class BaseApp extends MultiDexApplication implements Handler.Callback,
        Application.ActivityLifecycleCallbacks {

    private static BaseApp instance;
    Handler mHandler = new Handler(this);
    private List<String> runActivityArray = new ArrayList<>();
    private boolean isBack = false;


    public static BaseApp getInstance() {
        return instance;
    }

    protected abstract String getImageCacheDir();

    /**
     * 分割 Dex 支持
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

        //延迟初始化三方库，防止应用启动太慢
        mHandler.sendEmptyMessageDelayed(0, 200);
    }

    @Override
    public boolean handleMessage(Message message) {
        try {
            ImageLoaderHelper.init(instance, getImageCacheDir() + "/cache");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        try {
            ImageLoaderHelper.clearMemoryCache();
        } catch (Exception e) {
        }
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if(TRIM_MEMORY_UI_HIDDEN == level) {
            //程序到后台
        }
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        if(!runActivityArray.contains(activity.getClass().getSimpleName()))
            runActivityArray.add(activity.getClass().getSimpleName());
        BuildLog.e(activity.getClass().getSimpleName() + "-----onActivityStarted---->" + runActivityArray.size());
        if(isBack) {
            //程序回到前台
        }
        isBack = false;
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        if(runActivityArray.contains(activity.getClass().getSimpleName()))
            runActivityArray.remove(activity.getClass().getSimpleName());
        BuildLog.e(activity.getClass().getSimpleName() + "-----onActivityStopped---->" + runActivityArray.size());
        if(runActivityArray.size() <= 0) {
            isBack = true;
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }


    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * 生成设备唯一标识
     * @return
     */
    private String makeAndroidID() {
        String time = String.valueOf(System.currentTimeMillis());
        String englist[] = new String[]{"A", "B", "C", "D", "E", "F", "G",
                "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
                "U", "V", "W", "X", "Y", "Z"};
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        sb.append(englist[random.nextInt(englist.length)]);
        sb.append(englist[random.nextInt(englist.length)]);
        sb.append(time.substring(0, time.length() / 2));
        sb.append(englist[random.nextInt(englist.length)]);
        sb.append(englist[random.nextInt(englist.length)]);
        sb.append(time.substring(0, time.length() / 2));
        sb.append(englist[random.nextInt(englist.length)]);
        sb.append(englist[random.nextInt(englist.length)]);
        sb.append(englist[random.nextInt(englist.length)]);
        sb.append(englist[random.nextInt(englist.length)]);
        return sb.toString();
    }
}
