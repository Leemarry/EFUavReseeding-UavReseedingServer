package com.bear.reseeding.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 实时拍摄照片表(EfMediaPhoto)实体类
 *
 * @author makejava
 * @since 2023-11-23 18:57:32
 */
public class EfMediaPhoto implements Serializable {
    private static final long serialVersionUID = -30842740478476987L;
    /**
     * 自增主键
     */
    private Integer id;
    /**
     * 拍照时间
     */
    private Date createDate;
    /**
     * 设备编号，无人机编号或停机坪编号
     */
    private String deviceid;
    /**
     * 图片名称标识
     */
    private String imageTag;
    /**
     * 照片组，一组包含多种类型照片
     */
    private String imageGroup;
    /**
     * 类型：0无人机照片，1停机坪照片
     */
    private Integer type;
    /**
     * 照片类型，0默认
     */
    private Integer sourceId;
    /**
     * DEFAULT,WIDE,ZOOM,INFRARED_THERMAL,UNKNOWN
     */
    private String cameraVideoStreamSource;
    /**
     * 图片路径
     */
    private String pathImage;
    /**
     * 图片大小，单位 byte
     */
    private double sizeImage;
    /**
     * 缩略图片路径
     */
    private String pathThumbnail;
    /**
     * 缩略图图片大小，单位 byte
     */
    private double sizeThumbnail;
    /**
     * 分析后的图片
     */
    private String pathImageAnalysis;
    /**
     * 分析后的图片大小，单位 byte
     */
    private double sizeImageAnalysis;
    /**
     * 拍摄所在纬度
     */
    private Double lat;
    /**
     * 拍摄所在经度
     */
    private Double lng;
    /**
     * 拍摄所在时的相对高度
     */
    private Float alt;
    /**
     * 拍摄所在的海拔高度
     */
    private Float altabs;
    /**
     * 拍摄时的无人机横滚值
     */
    private Object roll;
    /**
     * 拍摄时的无人机俯仰值
     */
    private Object pitch;
    /**
     * 拍摄时的无人机朝向值
     */
    private Object yaw;
    /**
     * 拍摄时的焦距，mm，为空时从相机参数表获取
     */
    private double focalLength;
    /**
     * 拍摄时的云台横滚值
     */
    private Object gimbalRoll;
    /**
     * 拍摄时的云台俯仰值
     */
    private Object gimbalPitch;
    /**
     * 拍摄时的云台朝向值
     */
    private Object gimbalYaw;
    /**
     * 照片拍摄地址，通过经纬度获取地理位置
     */
    private String place;
    /**
     * 当前照片空洞数量
     */
    private Integer cavityCount;
    /**
     * 外键：飞行架次ID
     */
    private Integer eachsortieId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getImageTag() {
        return imageTag;
    }

    public void setImageTag(String imageTag) {
        this.imageTag = imageTag;
    }

    public String getImageGroup() {
        return imageGroup;
    }

    public void setImageGroup(String imageGroup) {
        this.imageGroup = imageGroup;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public String getCameraVideoStreamSource() {
        return cameraVideoStreamSource;
    }

    public void setCameraVideoStreamSource(String cameraVideoStreamSource) {
        this.cameraVideoStreamSource = cameraVideoStreamSource;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public double getSizeImage() {
        return sizeImage;
    }

    public void setSizeImage(double sizeImage) {
        this.sizeImage = sizeImage;
    }

    public String getPathThumbnail() {
        return pathThumbnail;
    }

    public void setPathThumbnail(String pathThumbnail) {
        this.pathThumbnail = pathThumbnail;
    }

    public double getSizeThumbnail() {
        return sizeThumbnail;
    }

    public void setSizeThumbnail(double sizeThumbnail) {
        this.sizeThumbnail = sizeThumbnail;
    }

    public String getPathImageAnalysis() {
        return pathImageAnalysis;
    }

    public void setPathImageAnalysis(String pathImageAnalysis) {
        this.pathImageAnalysis = pathImageAnalysis;
    }

    public double getSizeImageAnalysis() {
        return sizeImageAnalysis;
    }

    public void setSizeImageAnalysis(double sizeImageAnalysis) {
        this.sizeImageAnalysis = sizeImageAnalysis;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Float getAlt() {
        return alt;
    }

    public void setAlt(Float alt) {
        this.alt = alt;
    }

    public Float getAltabs() {
        return altabs;
    }

    public void setAltabs(Float altabs) {
        this.altabs = altabs;
    }

    public Object getRoll() {
        return roll;
    }

    public void setRoll(Object roll) {
        this.roll = roll;
    }

    public Object getPitch() {
        return pitch;
    }

    public void setPitch(Object pitch) {
        this.pitch = pitch;
    }

    public Object getYaw() {
        return yaw;
    }

    public void setYaw(Object yaw) {
        this.yaw = yaw;
    }

    public double getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(double focalLength) {
        this.focalLength = focalLength;
    }

    public Object getGimbalRoll() {
        return gimbalRoll;
    }

    public void setGimbalRoll(Object gimbalRoll) {
        this.gimbalRoll = gimbalRoll;
    }

    public Object getGimbalPitch() {
        return gimbalPitch;
    }

    public void setGimbalPitch(Object gimbalPitch) {
        this.gimbalPitch = gimbalPitch;
    }

    public Object getGimbalYaw() {
        return gimbalYaw;
    }

    public void setGimbalYaw(Object gimbalYaw) {
        this.gimbalYaw = gimbalYaw;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getCavityCount() {
        return cavityCount;
    }

    public void setCavityCount(Integer cavityCount) {
        this.cavityCount = cavityCount;
    }

    public Integer getEachsortieId() {
        return eachsortieId;
    }

    public void setEachsortieId(Integer eachsortieId) {
        this.eachsortieId = eachsortieId;
    }

}
