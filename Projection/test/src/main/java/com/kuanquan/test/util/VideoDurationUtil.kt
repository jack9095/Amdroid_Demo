package com.kuanquan.test.util

import android.media.MediaMetadataRetriever
import android.util.Log
import it.sauronsoftware.jave.Encoder
import it.sauronsoftware.jave.EncoderException
import java.io.File
import java.io.IOException
import java.net.UnknownHostException


object VideoDurationUtil {

    /**
     * 获取网络文件，暂存为临时文件
     * @param url
     * @return
     * @throws UnknownHostException
     * @throws IOException
     */
    @Throws(UnknownHostException::class, IOException::class)
    fun getFileByUrl(url: String?): File {
        val tmpFile = File.createTempFile("temp", ".tmp")//创建临时文件
        Image2Binary.toBDFile(url, tmpFile.canonicalPath)
        return tmpFile
    }

    /**
     * 获取时长
     * @param
     * @return
     * @throws IOException
     * @throws
     * @throws EncoderException
     */
    @Throws(EncoderException::class)
    fun getDuration(file: File?): Long {
        val m = Encoder().getInfo(file)
        return m.duration
    }

    /**
     * 根据视频 url 获取视频时长
     */
    fun getRingDuring(mUri: String?): String? {
        var duration: String? = null
        val mmr = MediaMetadataRetriever()
        try {
            if (mUri != null) {
                var headers: HashMap<String?, String?>? = null
                if (headers == null) {
                    headers = HashMap()
                    headers["User-Agent"] = "Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; MW-KW-001 Build/JRO03C) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/1.0.0.001 U4/0.8.0 Mobile Safari/533.1"
                }
                mmr.setDataSource(mUri, headers)
            }
            duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        } catch (ex: Exception) {
        } finally {
            mmr.release()
        }
        Log.e("VideoDurationUtil", "视频时长 -> duration $duration")
        return duration
    }
}
