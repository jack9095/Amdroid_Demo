package com.kuanquan.doyincover.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;

/**
 * Author:  liJie
 * Date:   2019/1/26
 * Email:  2607401801@qq.com
 */
public class SizeUtils {

  public static CharSequence tvFirstBig(String str,int size){
    SpannableString spannableString=new SpannableString(str);
    spannableString.setSpan(new AbsoluteSizeSpan(size,true),0,1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    return spannableString;
  }
}
