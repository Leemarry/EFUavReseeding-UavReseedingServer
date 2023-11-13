package com.bear.reseeding.utils;

import com.alibaba.fastjson.JSONObject;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.GpsDirectory;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ReadPictrueUtil {

    public static JSONObject readPicLatLng(File file) throws JpegProcessingException, IOException {
        Metadata metadata = JpegMetadataReader.readMetadata(file);
        GpsDirectory gpsDirectory = metadata.getDirectory(GpsDirectory.class);
        JSONObject object = new JSONObject();
        if (Objects.nonNull(gpsDirectory)) {
            GeoLocation geoLocation = gpsDirectory.getGeoLocation();
            object.put("lat", geoLocation.getLatitude());
            object.put("lng", geoLocation.getLongitude());
        }
        return object;
    }

    public static void readPic(String pathname) throws JpegProcessingException, IOException {
        File jpegFile = new File(pathname);
        Metadata metadata = JpegMetadataReader.readMetadata(jpegFile);
        //获取图片所有EXIF信息
        Iterable<Directory> directories = metadata.getDirectories();
        for (Directory directory : directories) {
            for (Tag tag : directory.getTags()) {
            }
        }
    }
}
