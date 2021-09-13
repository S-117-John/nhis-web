package com.zebone.nhis.webservice.zhongshan.vo;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @Classname ThiCancelExeCnord
 * @Description 05 号接口
 * @Date 2021-04-08 10:22
 * @Created by wuqiang
 */
public class ThiCancelExeCnord {

    @NotBlank(message = "取消人不能为空")
    private String pkEmpCanc;
    @NotBlank(message = "医嘱主键不能为空")
    private String pkCnord;
    @NotBlank(message = "取消部门不能为空")
    private String pkDeptCanc;

    public String getPkDeptCanc() {
        return pkDeptCanc;
    }

    public void setPkDeptCanc(String pkDeptCanc) {
        this.pkDeptCanc = pkDeptCanc;
    }

    public String getPkEmpCanc() {
        return pkEmpCanc;
    }

    public void setPkEmpCanc(String pkEmpCanc) {
        this.pkEmpCanc = pkEmpCanc;
    }

	public String getPkCnord() {
		return pkCnord;
	}

	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}
}
