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
     * 初始化标题栏
     */
    protected abstract void initTitleBar();

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
        initTitleBar();
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

//    /**
//     * 软键盘是否隐藏
//     */
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_UP) {
//            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
//            View v = getCurrentFocus();
//            if (isShouldHideInput(v, ev)) {
//                ((InputMethodManager) this
//                        .getSystemService(INPUT_METHOD_SERVICE))
//                        .hideSoftInputFromWindow(this.getCurrentFocus()
//                                        .getWindowToken(),
//                                InputMethodManager.HIDE_NOT_ALWAYS);
//
//                // return false;
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    public boolean dispatchTouchEvent2(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
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
        removeNightHover();
    }

    /**
     * isShouldHideInput
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return boolean
     * @throws
     * @since 1.0.0
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
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
        Toast.makeText(App.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(Object response, int taskId) {
        if (!(response instanceof BaseJson)) return;
        onSuccessJson((BaseJson) response, taskId);
    }

    public abstract void onSuccessJson(BaseJson response, int taskId);


    @Override
    public void onClick(View v) {

    }

    protected void showToast(String msg) {
        Toast.makeText(App.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev == null) {
            return true;
        }
        if(!finishFlag) return super.dispatchTouchEvent(ev);
//        if (this instanceof MainActivity) {
//            return super.dispatchTouchEvent(ev);
//        }
        // 过滤右划关闭监听时间
        boolean isGesture = false;
        // if (GlobalVars.IS_ENABLE_GESTURE) {
        if (slideDirection == null) {
            slideDirection = SlideDirection.RIGHT;
        }
        if (detector == null) {
            gestureListener = new SlideFinishOnGestureListener(this,
                    slideDirection);
            detector = new GestureDetector(this, gestureListener);
        }
        isGesture = detector.onTouchEvent(ev);
        // }
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

    protected void fullscreen(boolean enable) {
        if (enable) { //显示状态栏
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else { //隐藏状态栏
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(lp);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    protected String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    //设置屏幕亮度
    public void setScreenLight(int progress) {
        if (progress < 1) {
            progress = 1;
        } else if (progress > 255) {
            progress = 255;
        }
        final WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.screenBrightness = progress / 255f;
        getWindow().setAttributes(attrs);
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
