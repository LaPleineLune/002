package com.android.linglan.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import com.android.linglan.LinglanApplication;
import com.android.linglan.ui.R;

public class TextUtilTools {

    /**
     * 关键字高亮显示
     * 
     * @param text	     需要显示的文字
     * @param target  需要高亮的关键字
     * @return spannable 处理完后的结果，记得不要toString()，否则没有效果
     */
    public static SpannableStringBuilder highlight(String text, String target) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        CharacterStyle span = null;

        Pattern p = Pattern.compile(target);
        Matcher m = p.matcher(text);
        while (m.find()) {
//            span = new ForegroundColorSpan(Color.RED);// 需要重复！
            span = new ForegroundColorSpan(ContextCompat.getColor(LinglanApplication.getsApplicationContext(), R.color.carminum));// 需要重复！
            spannable.setSpan(span, m.start(), m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }
    
    // 调用
    // SpannableStringBuilder textString = TextUtilTools.highlight(item.getItemName(), KnowledgeActivity.searchKey);
    // vHolder.tv_itemName_search.setText(textString);
}