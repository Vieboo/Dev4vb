package vieboo.net.okhttp.http;

/**
 * Created by vieboo on 15/11/13.
 */
public class HttpConstant {

    public static final boolean IS_DEBUG = true;   //debug

    public static final int CONNECTION_TIME_OUT = 15;    //超时时间(秒)

    //GET
    public static final int GET = 1;
    //POST
    public static final int POST = 2;

    //charset
    public static final String CHARSET = HttpConstant.CHARSET_UTF8;
    //utf-8
    public static final String CHARSET_UTF8 = "utf-8";

}
