package utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import java.util.ArrayList;
import java.util.List;

/**
 * 为TextView填充多元化内容（多颜色、分段点击等）
 * Created by Administrator on 2016/9/29 0029.
 */
public class CharSequenceUtils {

    /**
     * 分段颜色
     * @param values
     * @param colors
     * @return
     */
    private CharSequence getColorsText(List<String> values, List<String> colors) {
        return getColorsText(values, colors, "");
    }


    /**
     * 分段颜色
     * @param values
     * @param colors
     * @param space     间隔符
     * @return
     */
    private CharSequence getColorsText(List<String> values, List<String> colors, String space) {
        if(null == values || values.size() == 0) {
            return "";
        }

        String value = "";
        for(String str : values) {
            value += (str + space);
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(value);
        //默认字体颜色
        ForegroundColorSpan defaultFCS = new ForegroundColorSpan(Color.GRAY);
        List<ForegroundColorSpan> fcsList = new ArrayList<>();
        if(null != colors && colors.size() > 0) {
            for (String str : colors) {
                fcsList.add(new ForegroundColorSpan(Color.parseColor(str)));
            }
        }
        int cursor = 0;
        for(int i=0; i<values.size(); i++) {
            if(fcsList.size() > i) {
                builder.setSpan(fcsList.get(i), cursor, cursor+values.get(i).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else {
                builder.setSpan(defaultFCS, cursor, cursor+values.get(i).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            cursor += (values.get(i).length() + space.length());
        }
        return builder;
    }

}
