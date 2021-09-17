package com.kuanquan.doyincover.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Author:  Chenglong.Lu
 * Email:   1053998178@qq.com | w490576578@gmail.com
 * Date:    2019/01/28
 * Description:
 */
public class NetworkUtil {
  public static String GetNetworkType(Context context)
  {
    String strNetworkType = "";

    NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    if (networkInfo != null && networkInfo.isConnected())
    {
      if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
      {
        strNetworkType = "wifi";
      }
      else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
      {
        String _strSubTypeName = networkInfo.getSubtypeName();
        // TD-SCDMA   networkType is 17
        int networkType = networkInfo.getSubtype();
        switch (networkType) {
          case TelephonyManager.NETWORK_TYPE_GPRS:
          case TelephonyManager.NETWORK_TYPE_EDGE:
          case TelephonyManager.NETWORK_TYPE_CDMA:
          case TelephonyManager.NETWORK_TYPE_1xRTT:
          case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
            strNetworkType = "2G";
            break;
          case TelephonyManager.NETWORK_TYPE_UMTS:
          case TelephonyManager.NETWORK_TYPE_EVDO_0:
          case TelephonyManager.NETWORK_TYPE_EVDO_A:
          case TelephonyManager.NETWORK_TYPE_HSDPA:
          case TelephonyManager.NETWORK_TYPE_HSUPA:
          case TelephonyManager.NETWORK_TYPE_HSPA:
          case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
          case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
          case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
            strNetworkType = "3G";
            break;
          case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
            strNetworkType = "4G";
            break;
          default:
            // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
            if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA")
                || _strSubTypeName.equalsIgnoreCase("WCDMA")
                || _strSubTypeName.equalsIgnoreCase("CDMA2000"))
            {
              strNetworkType = "3G";
            }
            else
            {
              strNetworkType = _strSubTypeName;
            }
            break;
        }
      }
    }
    return strNetworkType;
  }
}
