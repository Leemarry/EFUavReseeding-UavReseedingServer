package com.bear.reseeding.entity;


public class BlockAll {
    private String id;
    private double gapSquare;
    private int reseedAreaNum;

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

    private double reseedSquare;
    private int seedNum;
}
