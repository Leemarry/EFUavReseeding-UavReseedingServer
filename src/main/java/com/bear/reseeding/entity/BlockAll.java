package com.bear.reseeding.entity;


public class BlockAll {
    private String id;
    private double gapSquare;
    private int reseedAreaNum;
    private double reseedSquare;
    private int seedNum;
    private int handleId;
    private String handleUuid;

    public String getHandleUuid() {
        return handleUuid;
    }

    public void setHandleUuid(String handleUuid) {
        this.handleUuid = handleUuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getGapSquare() {
        return gapSquare;
    }

    public void setGapSquare(double gapSquare) {
        this.gapSquare = gapSquare;
    }

    public int getReseedAreaNum() {
        return reseedAreaNum;
    }

    public void setReseedAreaNum(int reseedAreaNum) {
        this.reseedAreaNum = reseedAreaNum;
    }

    public double getReseedSquare() {
        return reseedSquare;
    }

    public void setReseedSquare(double reseedSquare) {
        this.reseedSquare = reseedSquare;
    }

    public int getSeedNum() {
        return seedNum;
    }

    public void setSeedNum(int seedNum) {
        this.seedNum = seedNum;
    }

    public int getHandleId() {
        return handleId;
    }

    public void setHandleId(int handleId) {
        this.handleId = handleId;
    }
}
