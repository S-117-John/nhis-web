package com.zebone.nhis.pro.zsba.th.service.vo;

/**
 * @Classname PrescriptionQu
 * @Description 处方审核结果
 * @Date 2021-02-20 16:32
 * @Created by wuqiang
 */
public class PrescriptionQu {

    private String id;

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
