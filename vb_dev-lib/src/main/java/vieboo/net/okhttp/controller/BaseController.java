package vieboo.net.okhttp.controller;


import android.os.Handler;
import android.os.Looper;

import com.squareup.okhttp.Request;

import org.json.JSONObject;

import vieboo.net.okhttp.http.GetDelegate;
import vieboo.net.okhttp.http.HttpConstant;
import vieboo.net.okhttp.http.Param;
import vieboo.net.okhttp.http.PostDelegate;
import vieboo.net.okhttp.http.io.IDataLoadingEventListener;
import vieboo.net.okhttp.http.io.ResultCallback;


/**
 * Created by weibo.kang on 2015/11/6.
 */
public abstract class BaseController implements ResultCallback {

    protected int taskId;
    protected IDataLoadingEventListener mLoadingIndicator = null; //回调接口
    protected String loadingMessage;  //加载动画提示语
    protected boolean isLoadingDialogCancel = true;  //加载动画框是否可以取消

    protected Handler uiHandler;

    public BaseController(
            int taskId, IDataLoadingEventListener mLoadingIndicator,
            String loadingMessage, boolean isLoadingDialogCancel) {

        this.taskId = taskId;
        this.mLoadingIndicator = mLoadingIndicator;
        this.loadingMessage = loadingMessage;
        this.isLoadingDialogCancel = isLoadingDialogCancel;

        uiHandler = new Handler(Looper.getMainLooper());
    }

    public abstract void loadOnSuccess(String response);   //请求成功
    public abstract void loadOnFaild(Request request, Exception e);     //请求失败

    @Override
    public void onBefore(Request request) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if(null != mLoadingIndicator) {
                    mLoadingIndicator.loadDataStarted(loadingMessage, isLoadingDialogCancel, taskId);
                }
            }
        });
    }

    @Override
    public void onAfter() {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (null != mLoadingIndicator) {
                    mLoadingIndicator.loadDataComplete(taskId);
                }
            }
        });
    }

    @Override
    public void onError(final Request request, final Exception e) {

        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (null != mLoadingIndicator) {
                    mLoadingIndicator.onError(request, e, taskId);
                }
            }
        });
        loadOnFaild(request, e);
    }

    @Override
    public void onResponse(String response) {
        loadOnSuccess(response);
    }

    /**
     *
     * @param method    请求方式 post get
     * @param url       请求地址
     * @param params    参数
     */
    protected void sendRequest(int method, String url, Param params[]) {
        switch (method) {
            //get
            case HttpConstant.GET:
                GetDelegate.getInstance().getAsyn(url, this);
                break;
            //post
            case HttpConstant.POST:
                PostDelegate.getInstance().postAsyn(url, params, this);
                break;
        }
    }

    /**
     *
     * @param url       请求地址
     * @param json    参数
     */
    protected void sendRequestJson(String url, JSONObject json) {
        PostDelegate.getInstance().postAsyn(url, json, this);
    }

}
