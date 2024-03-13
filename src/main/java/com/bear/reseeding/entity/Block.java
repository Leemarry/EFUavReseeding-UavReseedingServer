package com.bear.reseeding.entity;


public class Block {
    private int id;
    private double gapSquare;
    private int reseedAreaNum;
    private double reseedSquare;
    private double centreLatitude;
    private double centreLongitude;
    private int seedNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public double getCentreLatitude() {
        return centreLatitude;
    }

    public void setCentreLatitude(double centreLatitude) {
        this.centreLatitude = centreLatitude;
    }

    public double getCentreLongitude() {
        return centreLongitude;
    }

    public void setCentreLongitude(double centreLongitude) {
        this.centreLongitude = centreLongitude;
    }

    public int getSeedNum() {
        return seedNum;
    }

    public void setSeedNum(int seedNum) {
        this.seedNum = seedNum;
    }


}
