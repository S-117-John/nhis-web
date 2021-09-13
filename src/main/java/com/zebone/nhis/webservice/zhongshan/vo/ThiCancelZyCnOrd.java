package com.zebone.nhis.webservice.zhongshan.vo;

import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * @Classname ThiCancelCnOrd
 * @Description 19号接口业务入参
 * @Date 2021-04-08 10:00
 * @Created by wuqiang
 */
public class ThiCancelZyCnOrd {

    //03号开始
    /**
     *作废人医生编码
     */
    @NotBlank(message = "作废人医生不能为空")
    private String pkEmpErase;
    /**
     *医嘱主键
     */
    @NotBlank(message = "医嘱主键不能为空")
    private List<String> pkCnord;
    /**
     *医嘱序号
     */
    @NotBlank(message = "作废时间不能为空")
    private  String dateErase;
   
    
    public String getPkEmpErase() {
        return pkEmpErase;
    }

    public void setPkEmpErase(String pkEmpErase) {
        this.pkEmpErase = pkEmpErase;
    }

	public List<String> getPkCnord() {
		return pkCnord;
	}

	public void setPkCnord(List<String> pkCnord) {
		this.pkCnord = pkCnord;
	}

	public String getDateErase() {
		return dateErase;
	}

	public void setDateErase(String dateErase) {
		this.dateErase = dateErase;
	}

}
