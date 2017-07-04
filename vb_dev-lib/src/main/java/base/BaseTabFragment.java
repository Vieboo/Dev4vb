package base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.vb.dev.lib.R;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import vieboo.net.okhttp.http.io.IDataLoadingEventListener;

/**
 *
 * Created by weibo.kang on 2016/3/31.
 */
public abstract class BaseTabFragment extends Fragment implements IDataLoadingEventListener {

    protected Activity mActivity;
    protected View contentView;

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 获取布局id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化界面view
     */
    protected abstract void initView();

    /**
     * 设置监听事件
     */
    protected abstract void initEvent();

    /**
     * Activity创建完毕
     */
    protected abstract void activityCreated();

    /**
     * Fragment被隐藏
     */
    protected abstract void onFragmentHidden();

    /**
     * Fragment被显示出来（初次创建显示时不会调用）
     */
    protected abstract void onFragmentShow();

    /**
     * 当Fragment被解绑销毁时
     */
    protected abstract void fragmentDetach();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity)context;
        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(getLayoutId(), null);
        ButterKnife.bind(this, contentView);
        initView();
        initEvent();
        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activityCreated();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden)
            onFragmentHidden();
        else
            onFragmentShow();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        onFragmentShow();
    }

    @Override
    public void onPause() {
        super.onPause();
        onFragmentHidden();
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

    public void startActivityNoAnim(Intent intent) {
        super.startActivity(intent);
    }

    public void startActivity4UpAndDown(Intent intent) {
        super.startActivity(intent);
        mActivity.overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out);
    }

    protected void showToast(String msg) {
        BaseApp.getInstance().showToast(msg);
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


    public String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }
}
