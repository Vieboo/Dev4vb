package vieboo.net.okhttp.http.io;

import com.squareup.okhttp.Request;

/**
 * 加载数据时加载动画的工具接口
 *
 * Created by vieboo on 15/11/13.
 */
public interface IDataLoadingEventListener {

    void loadDataStarted(String msg, boolean isLoadingDialogCancel, int taskId);

    void loadDataComplete(int taskId);

    void onBefore(Request request, int taskId);

    void onAfter(int taskId);

    void onError(Request request, Exception e, int taskId);

    void onSuccess(Object response, int taskId);

}
