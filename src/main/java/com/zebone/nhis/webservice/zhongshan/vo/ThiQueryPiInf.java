package com.zebone.nhis.webservice.zhongshan.vo;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @Classname ThiQueryPiInf
 * @Description 09号接口业务入参
 * @Date 2021-04-08 11:38
 * @Created by wuqiang
 */
public class ThiQueryPiInf {

    /**
     * 查询类型
     */
    @NotBlank(message = "查询类型不能为空")
    private String type;
    /**
     * 就诊类型
     */
    @NotBlank(message = "就诊类型不能为空")
    private String range;
    /**
     * 编码
     */
    @NotBlank(message = "编码不能为空")
    private String code;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ThiQueryPiInf{" +
                "type='" + type + '\'' +
                ", range='" + range + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
