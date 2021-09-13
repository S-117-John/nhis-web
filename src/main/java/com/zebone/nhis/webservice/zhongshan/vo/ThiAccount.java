package com.zebone.nhis.webservice.zhongshan.vo;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @Classname ThiAccount
 * @Description 08 号接口入参
 * @Date 2021-04-08 11:34
 * @Created by wuqiang
 */
public class ThiAccount {

    /**
     * 操作员工号
     */
    @NotBlank(message = "操作员工号不能为空")
    private String logCode;
    /**
     * 操作员密码
     */
    @NotBlank(message = "操作员密码不能为空")
    private String logPwd;

    public String getLogCode() {
        return logCode;
    }

    public void setLogCode(String logCode) {
        this.logCode = logCode;
    }

    public String getLogPwd() {
        return logPwd;
    }

    public void setLogPwd(String logPwd) {
        this.logPwd = logPwd;
    }

    @Override
    public String toString() {
        return "ThiAccount{" +
                "logCode='" + logCode + '\'' +
                ", logPwd='" + logPwd + '\'' +
                '}';
    }
}
