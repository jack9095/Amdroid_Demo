package com.kuanquan.lyrics.formats;

import android.util.Base64;

import com.kuanquan.lyrics.model.LyricsInfo;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * @Description: 歌词文件读取器
 */
public abstract class LyricsFileReader {
    /**
     * 默认编码
     */
    private Charset defaultCharset = Charset.forName("utf-8");

    /**
     * 读取歌词文件
     *
     * @param file
     * @return
     */
    public LyricsInfo readFile(File file) throws Exception {
        if (file != null) {
            return readInputStream(new FileInputStream(file));
        }
        return null;
    }

    /**
     * 读取歌词文本内容
     *
     * @param base64FileContentString base64位文件内容
     * @param saveLrcFile             要保存的歌词文件
     * @return
     * @throws Exception
     */
    public LyricsInfo readLrcText(String base64FileContentString,
                                  File saveLrcFile) throws Exception {
        byte[] fileContent = Base64.decode(base64FileContentString, Base64.NO_WRAP);

        if (saveLrcFile != null) {
            // 生成歌词文件
            FileOutputStream os = new FileOutputStream(saveLrcFile);
            os.write(fileContent);
            os.close();

            os = null;
        }

        return readInputStream(new ByteArrayInputStream(fileContent));
    }

    /**
     * 读取歌词文本内容
     *
     * @param base64ByteArray base64内容数组
     * @param saveLrcFile
     * @return
     * @throws Exception
     */
    public LyricsInfo readLrcText(byte[] base64ByteArray,
                                  File saveLrcFile) throws Exception {
        if (saveLrcFile != null) {
            // 生成歌词文件
            FileOutputStream os = new FileOutputStream(saveLrcFile);
            os.write(base64ByteArray);
            os.close();

            os = null;
        }

        return readInputStream(new ByteArrayInputStream(base64ByteArray));
    }

    /**
     * @param dynamicContent  动感歌词内容
     * @param lrcContent      lrc歌词内容
     * @param extraLrcContent 额外歌词内容（翻译歌词、音译歌词）
     * @param lyricsFilePath  歌词文件保存路径
     * @return
     * @throws Exception
     */
    public abstract LyricsInfo readLrcText(String dynamicContent, String lrcContent, String extraLrcContent, String lyricsFilePath) throws Exception;


    /**
     * 读取歌词文件
     *
     * @param in
     * @return
     */
    public abstract LyricsInfo readInputStream(InputStream in) throws Exception;

    /**
     * 支持文件格式
     *
     * @param ext 文件后缀名
     * @return
     */
    public abstract boolean isFileSupported(String ext);

    /**
     * 获取支持的文件后缀名
     *
     * @return
     */
    public abstract String getSupportFileExt();

    public void setDefaultCharset(Charset charset) {
        defaultCharset = charset;
    }

    public Charset getDefaultCharset() {
        return defaultCharset;
    }

    /**
     * 判断文件的编码格式
     *
     * @param file
     * @return
     */
    public String getCharsetName(File file) {
        String code = "GBK";
        BufferedInputStream bin = null;
        try {
            bin = new BufferedInputStream(new FileInputStream(file));
            int p = (bin.read() << 8) + bin.read();

            switch (p) {
                case 0xefbb:
                    code = "UTF-8";
                    break;
                case 0xfffe:
                    code = "Unicode";
                    break;
                case 0xfeff:
                    code = "UTF-16BE";
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bin != null) {
                try {
                    bin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return code;
    }
}
