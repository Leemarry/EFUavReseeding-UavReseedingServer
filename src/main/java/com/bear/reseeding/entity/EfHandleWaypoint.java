package com.bear.reseeding.entity;

public class EfHandleWaypoint {
    private static final long serialVersionUID = 347945384347757625L;
    /**
     * 纬度
     */
    private double lat;
    /**
     * 经度
     */
    private double lng;
    /**
     * 高度
     */
    private double alt;
    /**
     *补播草种数量
     */
    private Integer seedNum;
    /**
     *是否飞过这个航点，飞一次+1
     */
    private  Integer flyTimes =0;
    /**
     *
     */
    private  Integer id;
    /**
     * 外键：关联处理ID
     */
    private Integer handleId;

    public EfHandleWaypoint( double lat, double lng, double alt, Integer seedNum,Integer id, Integer handleId, Integer flyTimes) {
        this.id = id;
        this.handleId = handleId;
        this.lat = lat;
        this.lng = lng;
        this.alt = alt;
        this.seedNum = seedNum;
        this.flyTimes = flyTimes;
    }

    /**
     *
     * @param prop1 经度
     * @param prop2 纬度
     * @param prop3 大地高
     * @param prop4 路径播种草数
     */
    public EfHandleWaypoint(double prop1, double prop2, double prop3, Integer prop4) {
        this.lng = prop1;
        this.lat = prop2;
        this.alt = prop3;
        this.seedNum = prop4;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHandleId() {
        return handleId;
    }

    public void setHandleId(Integer handleId) {
        this.handleId = handleId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getAlt() {
        return alt;
    }

    public void setAlt(double alt) {
        this.alt = alt;
    }

    public Integer getSeedNum() {
        return seedNum;
    }

    public void setSeedNum(Integer seedNum) {
        this.seedNum = seedNum;
    }

    public Integer getFlyTimes() {
        return flyTimes;
    }

    public void setFlyTimes(Integer flyTimes) {
        this.flyTimes = flyTimes;
    }
}
