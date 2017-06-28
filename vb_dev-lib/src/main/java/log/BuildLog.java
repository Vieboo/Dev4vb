package log;

import android.util.Log;


public class BuildLog {

	private static boolean IS_DEBUG_LOG = false;

	public static void init(boolean isBuildDebug) {
		IS_DEBUG_LOG = isBuildDebug;
	}

	public static void i(String tag, String str){
		if(tag==null||str==null)return;
		if(IS_DEBUG_LOG)Log.i(tag, str);
	}
	
	public static void i(String tag, Object... obj){
		if(tag==null||obj==null)return;
		if(IS_DEBUG_LOG) ObjectLog.i(tag, obj);
	}
	
	public static void d(String tag, String str){
		if(tag==null||str==null)return;
		if(IS_DEBUG_LOG)Log.d(tag, str);
	}
	
	public static void d(String tag, Object... obj){
		if(tag==null||obj==null)return;
		if(IS_DEBUG_LOG) ObjectLog.d(tag, obj);
	}
	
	public static void w(String tag, String str){
		if(tag==null||str==null)return;
		if(IS_DEBUG_LOG)Log.w(tag, str);
	}
	
	public static void w(String tag, Object... obj){
		if(tag==null||obj==null)return;
		if(IS_DEBUG_LOG) ObjectLog.w(tag, obj);
	}
	
	public static void e(String tag, String str){
		if(tag==null||str==null)return;
		if(IS_DEBUG_LOG)Log.e(tag, str);
	}
	
	public static void e(String tag, Object... obj){
		if(tag==null||obj==null)return;
		if(IS_DEBUG_LOG) ObjectLog.e(tag, obj);
	}
	
	
	public static void i(String tag){
		if(tag==null)return;
		if(IS_DEBUG_LOG) ObjectLog.i(tag);
	}
	
	public static void d(String tag){
		if(tag==null)return;
		if(IS_DEBUG_LOG) ObjectLog.d(tag);
	}
	
	public static void w(String tag){
		if(tag==null)return;
		if(IS_DEBUG_LOG) ObjectLog.w(tag);
	}
	
	public static void e(String tag){
		if(tag==null)return;
		if(IS_DEBUG_LOG) ObjectLog.e(tag);
	}
	
	
	public static void v(String tag, String str){
		if(tag==null||str==null)return;
		if(IS_DEBUG_LOG)Log.v(tag, str);
	}
	
	public static void v(String tag, Object... obj){
		if(tag==null||obj==null)return;
		if(IS_DEBUG_LOG) ObjectLog.v(tag, obj);
	}
	
	
	public static void v(String tag){
		if(tag==null)return;
		if(IS_DEBUG_LOG) ObjectLog.v(tag);
	}
	
	public static void out(String mes){
		if(mes==null)return;
		System.out.println(mes);
	}
}
