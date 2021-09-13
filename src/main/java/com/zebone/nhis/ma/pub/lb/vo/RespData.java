package com.zebone.nhis.ma.pub.lb.vo;

import java.util.List;

public class RespData {
    private int count ;
    private List<ResDetail> list;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ResDetail> getList() {
        return list;
    }

    public void setList(List<ResDetail> list) {
        this.list = list;
    }
}
