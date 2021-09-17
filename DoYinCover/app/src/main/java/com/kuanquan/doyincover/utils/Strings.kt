@file:JvmName("Strings")

package com.kuanquan.doyincover.utils

import okio.ByteString
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset
import kotlin.text.Charsets.UTF_8


fun String?.valueOrDefault(
    defaultValue: String): String = if (isNullOrBlank()) defaultValue else this!!

inline fun String?.valueOrDefault(
    lazy: () -> String): String = if (isNullOrBlank()) lazy() else this!!

fun String.md5(charset: Charset = UTF_8): String = ByteString.encodeString(this,
    charset).md5().hex()

/**
 * 字符串 Url Encode
 * 在 JDK 里默认：
 * 1. `+` -> `%2B`
 * 2. ` ` -> `+`
 * 比如：hello world ------> hello+world, hello+world ------> hello%2Bworld
 * @param spaceCharToPercentEncode 为 true 话，会把空格转义为 `%20`，否则会转义为 `+`, 默认不处理为 %20
 * @param keepPlusChar 为 true 话，不转义 `+`，否则会把 `+` 转义为 `%2B`，默认不转义
 */
fun String.urlEncode(spaceCharToPercentEncode: Boolean = false,
    keepPlusChar: Boolean = false): String {
  var encoded = URLEncoder.encode(this, "UTF-8")

  if (contains(' ') && spaceCharToPercentEncode) {
    encoded = encoded.replace("+", "%20")
  }
  if (contains('+') && keepPlusChar) {
    encoded = encoded.replace("%2B", "+")
  }
  return encoded
}

fun String.urlDecode(): String = URLDecoder.decode(this, "UTF-8")
