package com.zebone.nhis.pro.zsba.bl.vo;

public class WinoConfVo {
    private Integer cnt;
    private String wino;
    private String winoPrep;
    private String euUsecate;
    private Integer levelNum;

    private boolean flagCate;

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public String getWino() {
        return wino;
    }

    public void setWino(String wino) {
        this.wino = wino;
    }

    public String getWinoPrep() {
        return winoPrep;
    }

    public void setWinoPrep(String winoPrep) {
        this.winoPrep = winoPrep;
    }

    public String getEuUsecate() {
        return euUsecate;
    }

    public void setEuUsecate(String euUsecate) {
        this.euUsecate = euUsecate;
    }

    public Integer getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(Integer levelNum) {
        this.levelNum = levelNum;
    }

    public boolean isFlagCate() {
        return flagCate;
    }

    public void setFlagCate(boolean flagCate) {
        this.flagCate = flagCate;
    }
}
