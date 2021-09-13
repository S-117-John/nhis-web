package com.zebone.nhis.cn.pub.vo;

import java.util.List;

public class BdPdVo {

    private String code;//药品分类编码

    private String name;//药品分类名称

    private String fCode;//父编码

    private List<BdPdVo> children;//子节点

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getfCode() {
        return fCode;
    }

    public void setfCode(String fCode) {
        this.fCode = fCode;
    }

    public List<BdPdVo> getChildren() {
        return children;
    }

    public void setChildren(List<BdPdVo> children) {
        this.children = children;
    }
}
