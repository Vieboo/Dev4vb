package vieboo.net.okhttp.http.io;

import com.squareup.okhttp.Request;

/**
 * Created by weibo.kang on 2015/11/6.
 */
public interface ResultCallback {

    public void onBefore(Request request);

    public void onAfter();

    public void onError(Request request, Exception e);

    public void onResponse(String response);
}
