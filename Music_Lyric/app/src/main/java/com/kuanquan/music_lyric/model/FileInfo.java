package com.kuanquan.music_lyric.model;


/**
 * @Description: 文件信息
 **/
public class FileInfo {
    /**
     * 是否是文件
     */
    private boolean isFile;
    /**
     * 路径
     */
    private String filePath;
    /**
     * 文件名
     */
    private String fileName;

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
