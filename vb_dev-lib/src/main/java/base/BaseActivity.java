package base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.umeng.analytics.MobclickAgent;
import com.vb.dev.lib.R;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import slide.SlideDirection;
import slide.SlideFinishOnGestureListener;
import utils.KeyboardUtils;
import vieboo.net.okhttp.http.io.IDataLoadingEventListener;

/**
 * Created by Vieboo on 2016/3/31.
 */
public abstract class BaseActivity extends AppCompatActivity implements IDataLoadingEventListener {

    protected Activity mActivity;
    protected GestureDetector detector; // 触摸监听实例
    protected SlideFinishOnGestureListener gestureListener;
    protected SlideDirection slideDirection;

    protected boolean finishFlag=true;

    /**
     * 初始化数据
     */
    protected abstract void initData(Bundle savedInstanceState);

    /**
     * 初始化布局文件
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化UI
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 初始化事件
     */
    protected abstract void initEvent();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        initData(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initView(savedInstanceState);
        initEvent();

        //遮罩
//        boolean isNight = new SharedPreferenceUtil(mActivity, CACHE_SETTING).getBoolean(SETTING_NIGHT_THEME, false);
//        if(isNight){
//            addNightHover();
//        }
    }

    /**
     * 从右边进入动画
     *
     * @param intent
     */
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_right,
                R.anim.slide_out_to_left);
    }

    public void startActivityNoAnim(Intent intent) {
        super.startActivity(intent);
    }

    /**
     * 从右边出去动画
     */
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_left,
                R.anim.slide_out_to_right);
        KeyboardUtils.dismissKeyboard(this);
    }

    public void finishNoAnim() {
        super.finish();
        KeyboardUtils.dismissKeyboard(this);
    }

    public void startActivity4UpAndDown(Intent intent) {
        super.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out);
        KeyboardUtils.dismissKeyboard(this);
    }

    public void startActivity4UpAndDownForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        mActivity.overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.slide_in_from_right,
                R.anim.slide_out_to_left);
    }

    public void finish4UpAndDown() {
        super.finish();
        overridePendingTransition(R.anim.slide_out, R.anim.slide_out_to_bottom);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onStop();
//        removeNightHover();
    }

    /***
     *
     * *****网络层回调******
     *
     */
    @Override
    public void loadDataStarted(String msg, boolean isLoadingDialogCancel, int taskId) {

    }

    @Override
    public void loadDataComplete(int taskId) {

    }

    @Override
    public void onBefore(Request request, int taskId) {

    }

    @Override
    public void onAfter(int taskId) {

    }

    @Override
    public void onError(Request request, Exception e, int taskId) {
        String msg = "网络发生错误，请求失败";
        if(e instanceof SocketTimeoutException) {
            msg = "连接超时，请检查您的网络";
        }
        BaseApp.getInstance().showToast(msg);
    }

    @Override
    public void onSuccess(Object response, int taskId) {
        if (!(response instanceof BaseJson)) return;
        onSuccessJson((BaseJson) response, taskId);
    }

    public abstract void onSuccessJson(BaseJson response, int taskId);


    protected void showToast(String msg) {
        BaseApp.getInstance().showToast(msg);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev == null) {
            return true;
        }
        if(!finishFlag) return super.dispatchTouchEvent(ev);
        // 过滤右划关闭监听时间
        boolean isGesture = false;
        if (slideDirection == null) {
            slideDirection = SlideDirection.RIGHT;
        }
        if (detector == null) {
            gestureListener = new SlideFinishOnGestureListener(this,
                    slideDirection);
            detector = new GestureDetector(this, gestureListener);
        }
        isGesture = detector.onTouchEvent(ev);
        if (isGesture) {
            return isGesture;
        } else {
            try {
                return super.dispatchTouchEvent(ev);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    }


    protected String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }


    protected View mNightView;
    protected WindowManager mWindowManager;
    public void addNightHover() {
        if(null !=mNightView) return ;

        WindowManager.LayoutParams mNightViewParam = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);

        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mNightView = new View(this);
        mWindowManager.addView(mNightView, mNightViewParam);
        mNightView.setBackgroundResource(R.color.color_night_hover);
    }

    public void removeNightHover() {
        if(null  != mNightView && null != mWindowManager) {
            mWindowManager.removeViewImmediate(mNightView);
            mNightView = null;
        }
    }
}
