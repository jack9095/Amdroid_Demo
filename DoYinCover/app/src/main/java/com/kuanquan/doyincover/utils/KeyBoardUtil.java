package com.kuanquan.doyincover.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2018/08/13
 * Description:
 */
public class KeyBoardUtil {
  /**
   * 隐藏键盘
   * ：强制隐藏
   *
   * @param context
   */
  public static void hideInputSoftFromWindowMethod(Context context, View view) {
    try {
      InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 显示输入法
   *
   * @param context
   */
  public static void showInputSoftFromWindowMethod(Context context) {
    try {
      InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
