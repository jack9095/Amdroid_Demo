package com.kuanquan.lyrics.utils;


import com.kuanquan.lyrics.formats.LyricsFileReader;
import com.kuanquan.lyrics.formats.LyricsFileWriter;
import com.kuanquan.lyrics.formats.hrc.HrcLyricsFileReader;
import com.kuanquan.lyrics.formats.hrc.HrcLyricsFileWriter;
import com.kuanquan.lyrics.formats.krc.KrcLyricsFileReader;
import com.kuanquan.lyrics.formats.krc.KrcLyricsFileWriter;
import com.kuanquan.lyrics.formats.ksc.KscLyricsFileReader;
import com.kuanquan.lyrics.formats.ksc.KscLyricsFileWriter;
import com.kuanquan.lyrics.formats.lrc.LrcLyricsFileReader;
import com.kuanquan.lyrics.formats.lrc.LrcLyricsFileWriter;
import com.kuanquan.lyrics.formats.lrcwy.WYLyricsFileReader;
import com.kuanquan.lyrics.formats.lrcwy.WYLyricsFileWriter;
import com.kuanquan.lyrics.formats.trc.TrcLyricsFileReader;
import com.kuanquan.lyrics.formats.trc.TrcLyricsFileWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 歌词io操作
 *
 */
public class LyricsIOUtils {
	private static ArrayList<LyricsFileReader> readers;
	private static ArrayList<LyricsFileWriter> writers;

	static {
		readers = new ArrayList<LyricsFileReader>();
		readers.add(new HrcLyricsFileReader());
		readers.add(new KscLyricsFileReader());
		readers.add(new KrcLyricsFileReader());
		readers.add(new TrcLyricsFileReader());
		readers.add(new WYLyricsFileReader());
		readers.add(new LrcLyricsFileReader());
		//
		writers = new ArrayList<LyricsFileWriter>();
		writers.add(new HrcLyricsFileWriter());
		writers.add(new KscLyricsFileWriter());
		writers.add(new KrcLyricsFileWriter());
		writers.add(new TrcLyricsFileWriter());
		writers.add(new WYLyricsFileWriter());
		writers.add(new LrcLyricsFileWriter());
	}

	/**
	 * 获取支持的歌词文件格式
	 * 
	 * @return
	 */
	public static List<String> getSupportLyricsExts() {
		List<String> lrcExts = new ArrayList<String>();
		for (LyricsFileReader lyricsFileReader : readers) {
			lrcExts.add(lyricsFileReader.getSupportFileExt());
		}
		return lrcExts;
	}

	/**
	 * 获取歌词文件读取器
	 * 
	 * @param file
	 * @return
	 */
	public static LyricsFileReader getLyricsFileReader(File file) {
		return getLyricsFileReader(file.getName());
	}

	/**
	 * 获取歌词文件读取器
	 * 
	 * @param fileName
	 * @return
	 */
	public static LyricsFileReader getLyricsFileReader(String fileName) {
		String ext = FileUtils.getFileExt(fileName);
		for (LyricsFileReader lyricsFileReader : readers) {
			if (lyricsFileReader.isFileSupported(ext)) {
				return lyricsFileReader;
			}
		}
		return null;
	}

	/**
	 * 获取歌词保存器
	 *
	 * @param file
	 * @return
	 */
	public static LyricsFileWriter getLyricsFileWriter(File file) {
		return getLyricsFileWriter(file.getName());
	}

	/**
	 * 获取歌词保存器
	 *
	 * @param fileName
	 * @return
	 */
	public static LyricsFileWriter getLyricsFileWriter(String fileName) {
		String ext = FileUtils.getFileExt(fileName);
		for (LyricsFileWriter lyricsFileWriter : writers) {
			if (lyricsFileWriter.isFileSupported(ext)) {
				return lyricsFileWriter;
			}
		}
		return null;
	}
}
