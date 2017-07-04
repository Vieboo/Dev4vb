package base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.umeng.analytics.MobclickAgent;
import com.vb.dev.lib.R;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import vieboo.net.okhttp.http.io.IDataLoadingEventListener;

/**
 * Created by Vieboo on 2016/4/7.
 */
public abstract class BaseFragment extends Fragment implements IDataLoadingEventListener {

    protected FragmentActivity mActivity;
    protected View contentView;

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
    protected abstract void initView();

    /**
     * 初始化事件
     */
    protected abstract void initEvent();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (FragmentActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initData(savedInstanceState);
        if(getLayoutId() != 0) contentView = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, contentView);
        initView();
        initEvent();
        return contentView;
    }


    /**
     * 启动页面
     *
     * @param intent
     */
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.slide_in_from_right,
                R.anim.slide_out_to_left);
    }

    public void startActivity4UpAndDown(Intent intent) {
        super.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out);
    }

    public void startActivity4UpAndDownForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        mActivity.overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out);
    }

    /**
     * 关闭页面
     */
    public void finish() {
        mActivity.finish();
        mActivity.overridePendingTransition(R.anim.slide_in_from_left,
                R.anim.slide_out_to_right);
    }


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

    public String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("BaseFragment"); //统计页面，"BaseFragment"为页面名称，可自定义
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("BaseFragment");
    }
}
