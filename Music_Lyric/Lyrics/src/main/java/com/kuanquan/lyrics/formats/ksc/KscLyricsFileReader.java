package com.kuanquan.lyrics.formats.ksc;

import android.text.TextUtils;

import com.kuanquan.lyrics.formats.LyricsFileReader;
import com.kuanquan.lyrics.model.LyricsInfo;
import com.kuanquan.lyrics.model.LyricsLineInfo;
import com.kuanquan.lyrics.model.LyricsTag;
import com.kuanquan.lyrics.utils.CharUtils;
import com.kuanquan.lyrics.utils.StringUtils;
import com.kuanquan.lyrics.utils.TimeUtils;
import com.kuanquan.lyrics.utils.UnicodeInputStream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Description: ksc歌词解析器
 */
public class KscLyricsFileReader extends LyricsFileReader {
    /**
     * 歌曲名 字符串
     */
    private final static String LEGAL_SONGNAME_PREFIX = "karaoke.songname";
    /**
     * 歌手名 字符串
     */
    private final static String LEGAL_SINGERNAME_PREFIX = "karaoke.singer";
    /**
     * 时间补偿值 字符串
     */
    private final static String LEGAL_OFFSET_PREFIX = "karaoke.offset";
    /**
     * 歌词 字符串
     */
    public final static String LEGAL_LYRICS_LINE_PREFIX = "karaoke.add";

    /**
     * 歌词Tag
     */
    public final static String LEGAL_TAG_PREFIX = "karaoke.tag";

    /**
     * 读取歌词文件
     *
     * @param file
     * @return
     */
    @Override
    public LyricsInfo readFile(File file) throws Exception {
        if (file != null) {
            String charsetName = getCharsetName(file);
            setDefaultCharset(Charset.forName(charsetName));
            InputStream inputStream = new FileInputStream(file);
            if (charsetName.toLowerCase().equals("utf-8")) {
                inputStream = new UnicodeInputStream(inputStream, charsetName);
            }
            return readInputStream(inputStream);
        }
        return null;
    }

    @Override
    public LyricsInfo readInputStream(InputStream in) throws Exception {
        LyricsInfo lyricsIfno = new LyricsInfo();
        lyricsIfno.setLyricsFileExt(getSupportFileExt());
        if (in != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in,
                    getDefaultCharset()));

            TreeMap<Integer, LyricsLineInfo> lyricsLineInfos = new TreeMap<Integer, LyricsLineInfo>();
            Map<String, Object> lyricsTags = new HashMap<String, Object>();
            int index = 0;
            String lineInfo = "";
            while ((lineInfo = br.readLine()) != null) {

                // 行读取，并解析每行歌词的内容
                LyricsLineInfo lyricsLineInfo = parserLineInfos(lyricsTags,
                        lineInfo);
                if (lyricsLineInfo != null) {
                    lyricsLineInfos.put(index, lyricsLineInfo);
                    index++;
                }
            }
            in.close();
            in = null;
            // 设置歌词的标签类
            lyricsIfno.setLyricsTags(lyricsTags);
            //
            lyricsIfno.setLyricsLineInfoTreeMap(lyricsLineInfos);
        }
        return lyricsIfno;
    }

    @Override
    public LyricsInfo readLrcText(String dynamicContent, String lrcContent, String extraLrcContent, String lyricsFilePath) throws Exception {
        LyricsInfo lyricsIfno = new LyricsInfo();
        lyricsIfno.setLyricsFileExt(getSupportFileExt());
        if (!TextUtils.isEmpty(lrcContent)) {

            TreeMap<Integer, LyricsLineInfo> lyricsLineInfos = new TreeMap<Integer, LyricsLineInfo>();
            Map<String, Object> lyricsTags = new HashMap<String, Object>();
            int index = 0;

            // 获取歌词内容
            String lrcContents[] = lrcContent.split("\n");
            for (int i = 0; i < lrcContents.length; i++) {
                String lineInfo = lrcContents[i];

                // 行读取，并解析每行歌词的内容
                LyricsLineInfo lyricsLineInfo = parserLineInfos(lyricsTags,
                        lineInfo);
                if (lyricsLineInfo != null) {
                    lyricsLineInfos.put(index, lyricsLineInfo);
                    index++;
                }
            }

            // 设置歌词的标签类
            lyricsIfno.setLyricsTags(lyricsTags);
            lyricsIfno.setLyricsLineInfoTreeMap(lyricsLineInfos);
        }
        return lyricsIfno;
    }

    /**
     * 解析每行的歌词内容
     * <p>
     * 歌词列表
     *
     * @param lyricsTags 歌词标签
     * @param lineInfo   行歌词内容
     * @return
     */
    private LyricsLineInfo parserLineInfos(Map<String, Object> lyricsTags,
                                           String lineInfo) throws Exception {
        LyricsLineInfo lyricsLineInfo = null;
        if (lineInfo.startsWith(LEGAL_SONGNAME_PREFIX)) {
            String temp[] = lineInfo.split("\'");
            if (temp.length > 1) {
                lyricsTags.put(LyricsTag.TAG_TITLE, temp[1]);
            }
        } else if (lineInfo.startsWith(LEGAL_SINGERNAME_PREFIX)) {
            String temp[] = lineInfo.split("\'");
            if (temp.length > 1) {
                lyricsTags.put(LyricsTag.TAG_ARTIST, temp[1]);
            }
        } else if (lineInfo.startsWith(LEGAL_OFFSET_PREFIX)) {
            String temp[] = lineInfo.split("\'");
            if (temp.length > 1) {
                lyricsTags.put(LyricsTag.TAG_OFFSET, temp[1]);
            }
        } else if (lineInfo.startsWith(LEGAL_TAG_PREFIX)) {
            // 自定义标签
            if (lineInfo.contains(":")) {
                String temp1[] = lineInfo.split("\'");
                if (temp1.length > 1) {
                    String temp2[] = temp1[1].split(":");
                    if (temp2.length > 1) {
                        lyricsTags.put(temp2[0], temp2[1]);
                    }
                }
            }
        } else if (lineInfo.startsWith(LEGAL_LYRICS_LINE_PREFIX)) {
            lyricsLineInfo = new LyricsLineInfo();

            int leftIndex = lineInfo.indexOf('\'');
            int rightIndex = lineInfo.lastIndexOf('\'');

            String[] lineComments = lineInfo.substring(leftIndex + 1, rightIndex)
                    .split("'\\s*,\\s*'", -1);
            // 开始时间
            String startTimeStr = lineComments[0];
            int startTime = TimeUtils.parseInteger(startTimeStr);
            lyricsLineInfo.setStartTime(startTime);

            // 结束时间
            String endTimeStr = lineComments[1];
            int endTime = TimeUtils.parseInteger(endTimeStr);
            lyricsLineInfo.setEndTime(endTime);

            // 歌词
            String lineLyricsStr = lineComments[2];
            List<String> lineLyricsList = getLyricsWords(lineLyricsStr);

            // 歌词分隔
            String[] lyricsWords = lineLyricsList
                    .toArray(new String[lineLyricsList.size()]);
            lyricsLineInfo.setLyricsWords(lyricsWords);

            // 获取当行歌词
            String lineLyrics = getLineLyrics(lineLyricsStr);
            lyricsLineInfo.setLineLyrics(lineLyrics);

            // 获取每个歌词的时间
            int wordsDisInterval[] = getWordsDisIntervalString(lineComments[3]);
            lyricsLineInfo.setWordsDisInterval(wordsDisInterval);

            //验证
            if (lyricsWords.length != wordsDisInterval.length) {
                throw new Exception("字标签个数与字时间标签个数不相符");
            }
        }
        return lyricsLineInfo;
    }

    /**
     * 获取每个歌词的时间
     *
     * @param wordsDisIntervalString
     * @return
     */
    private int[] getWordsDisIntervalString(String wordsDisIntervalString) throws Exception {
        String[] wordsDisIntervalStr = wordsDisIntervalString.split(",");
        int wordsDisInterval[] = new int[wordsDisIntervalStr.length];
        for (int i = 0; i < wordsDisIntervalStr.length; i++) {
            String wordDisIntervalStr = wordsDisIntervalStr[i];
            if (StringUtils.isNumeric(wordDisIntervalStr))
                wordsDisInterval[i] = Integer.parseInt(wordDisIntervalStr);
            else throw new Exception("字时间标签不能含有非数字字符串");
        }
        return wordsDisInterval;
    }

    /**
     * 获取当前行歌词，去掉中括号
     *
     * @param lineLyricsStr
     * @return
     */
    private String getLineLyrics(String lineLyricsStr) throws Exception {
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < lineLyricsStr.length(); i++) {
            char c = lineLyricsStr.charAt(i);
            switch (c) {
                case '[':
                    break;
                case ']':
                    break;
                default:
                    temp.append(c);
                    break;
            }
        }
        return temp.toString();
    }

    /**
     * 分隔每个歌词
     *
     * @param lineLyricsStr
     * @return
     */
    private List<String> getLyricsWords(String lineLyricsStr) throws Exception {
        List<String> lineLyricsList = new ArrayList<String>();
        StringBuilder temp = new StringBuilder();
        boolean isEnterFlag = false;
        for (int i = 0; i < lineLyricsStr.length(); i++) {
            char c = lineLyricsStr.charAt(i);
            if (CharUtils.isChinese(c) || CharUtils.isHangulSyllables(c)
                    || CharUtils.isHiragana(c)
                    || (!CharUtils.isWord(c) && c != '[' && c != ']')) {
                if (isEnterFlag) {
                    temp.append(lineLyricsStr.charAt(i));
                } else {
                    lineLyricsList.add(String.valueOf(lineLyricsStr.charAt(i)));
                }
            } else if (c == '[') {
                isEnterFlag = true;
            } else if (c == ']') {
                isEnterFlag = false;
                lineLyricsList.add(temp.toString());

                //清空
                temp.delete(0, temp.length());
            } else {
                temp.append(lineLyricsStr.charAt(i));
            }
        }
        return lineLyricsList;
    }

    @Override
    public boolean isFileSupported(String ext) {
        return ext.equalsIgnoreCase("ksc");
    }

    @Override
    public String getSupportFileExt() {
        return "ksc";
    }
}
