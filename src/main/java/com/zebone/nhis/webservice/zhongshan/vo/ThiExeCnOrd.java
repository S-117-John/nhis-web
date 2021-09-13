package com.zebone.nhis.webservice.zhongshan.vo;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @Classname ThiExeCnOrd
 * @Description 04号接口
 * @Date 2021-04-08 10:13
 * @Created by wuqiang
 */
public class ThiExeCnOrd {


    @NotBlank(message = "执行人不能为空")
    private String pkEmpOcc;

    @NotBlank(message = "医嘱主键不能为空")
    private String pkCnord;

    @NotBlank(message = "执行科室不能为空")
    private String pkDeptOcc;

    
    public String getPkCnord() {
		return pkCnord;
	}

	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}

	public String getPkDeptOcc() {
		return pkDeptOcc;
	}

	public void setPkDeptOcc(String pkDeptOcc) {
		this.pkDeptOcc = pkDeptOcc;
	}

	public String getPkEmpOcc() {
        return pkEmpOcc;
    }

    public void setPkEmpOcc(String pkEmpOcc) {
        this.pkEmpOcc = pkEmpOcc;
    }
}
