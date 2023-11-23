package com.bear.reseeding.eflink;

import com.alibaba.fastjson.JSONObject;
import com.bear.reseeding.utils.BytesUtil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 从无人机获取媒体文件信息回复(#3202)
 */
public class EFLINK_MSG_3202_2 implements Serializable {

    long fileTime;
    int fileSize;
    int fileType;
    int fileIndex;
    String mediaTypeDisplayName;
    String fileName;
    Boolean isExistOnServer;

    public EFLINK_MSG_3202_2() {
    }

    public EFLINK_MSG_3202_2(long fileTime, int fileSize, int fileType, int fileIndex, String mediaTypeDisplayName) {
        this.fileTime = fileTime;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.fileIndex = fileIndex;
        this.mediaTypeDisplayName = mediaTypeDisplayName;
    }

    public long getFileTime() {
        return fileTime;
    }

    public void setFileTime(long fileTime) {
        this.fileTime = fileTime;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public Boolean getExistOnServer() {
        return isExistOnServer;
    }

    public void setExistOnServer(Boolean existOnServer) {
        isExistOnServer = existOnServer;
    }

    public int getFileIndex() {
        return fileIndex;
    }

    public void setFileIndex(int fileIndex) {
        this.fileIndex = fileIndex;
    }

    public String getMediaTypeDisplayName() {
        return mediaTypeDisplayName;
    }

    public void setMediaTypeDisplayName(String mediaTypeDisplayName) {
        this.mediaTypeDisplayName = mediaTypeDisplayName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        String suffix = "";
        if (fileType == 0) {
            suffix = ".jpeg";
        } else if (fileType == 1) {
            suffix = ".raw_dng";
        } else if (fileType == 2) {
            suffix = ".mov";
        } else if (fileType == 3) {
            suffix = ".mp4";
        } else if (fileType == 4) {
            suffix = ".panorama";
        } else if (fileType == 5) {
            suffix = ".shallow_focus";
        } else if (fileType == 6) {
            suffix = ".tiff";
        } else if (fileType == 7) {
            suffix = ".seq";
        } else if (fileType == 8) {
            suffix = ".tiff_seq";
        } else if (fileType == 9) {
            suffix = ".json";
        } else if (fileType == 10) {
            suffix = ".photo_folder";
        } else if (fileType == 11) {
            suffix = ".video_folder";
        } else if (fileType == 12) {
            suffix = ".unknown";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = dateFormat.format(new Date(fileTime));
        fileName = "dji" + fileIndex + "_" + time +"."+ mediaTypeDisplayName;
        return fileName;
    }

    @Override
    public String toString() {
        return "EFLINK_MSG_3202_2{" +
                "fileTime=" + fileTime +
                ", fileSize=" + fileSize +
                ", fileType=" + fileType +
                ", fileIndex=" + fileIndex +
                ", mediaTypeDisplayName='" + mediaTypeDisplayName + '\'' +
                ", fileName='" + fileName + '\'' +
                ", isExistOnServer=" + isExistOnServer +
                '}';
    }
}
