package utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * 验证工具类
 * Created by Administrator on 2016/9/29 0029.
 */
public class CheckUtils {

    public static boolean emptyCheck(Context context, String content, CharSequence chars) {
        if(TextUtils.isEmpty(content)) {
            Toast.makeText(context, chars, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public static boolean phoneCheck(Context context, String phone) {
        if(TextUtils.isEmpty(phone)) {
            Toast.makeText(context, "手机号码不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        String telRegex = ConstUtils.REGEX_MOBILE_SIMPLE;
        if(!phone.matches(telRegex)) {
            Toast.makeText(context, "手机号码格式错误！", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public static boolean smsCodeCheck(Context context, String smdCode) {
        if(TextUtils.isEmpty(smdCode)) {
            Toast.makeText(context, "验证码不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public static boolean pwdCheck(Context context, String pwd) {
        if(TextUtils.isEmpty(pwd)) {
            Toast.makeText(context, "密码不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(pwd.length() < 6) {
            Toast.makeText(context, "密码不能少于6位！", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public static boolean pwdCheck(Context context, String pwd, String pwd2) {
        if(!pwd2.equals(pwd)) {
            Toast.makeText(context, "两次密码输入不一致！", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty(pwd)) {
            Toast.makeText(context, "密码不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(pwd.length() < 6) {
            Toast.makeText(context, "密码不能少于6位！", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    /**
     * 身份证验证
     * @param context
     * @param idcard
     * @return
     */
    public static boolean iDCardCheck(Context context, String idcard) {
        if(TextUtils.isEmpty(idcard)) {
            Toast.makeText(context, "身份证号码不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        String idRegex15 = ConstUtils.REGEX_IDCARD15;
        String idRegex18 = ConstUtils.REGEX_IDCARD18;
        if(!idcard.matches(idRegex15) && !idcard.matches(idRegex18)) {
            Toast.makeText(context, "身份证号码格式错误！", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    /**
     * Email验证
     * @param context
     * @param email
     * @return
     */
    public static boolean emailCheck(Context context, String email) {
        if(TextUtils.isEmpty(email)) {
            Toast.makeText(context, "邮箱地址不能为空！", Toast.LENGTH_SHORT).show();
            return false;
        }
        String emailRegex = ConstUtils.REGEX_EMAIL;
        if(!email.matches(emailRegex)) {
            Toast.makeText(context, "邮箱地址格式错误！", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
