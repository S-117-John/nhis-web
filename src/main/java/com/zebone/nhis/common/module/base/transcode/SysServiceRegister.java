package com.zebone.nhis.common.module.base.transcode;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 *
 * @author 
 */
@Table("sys_service_register")
public class SysServiceRegister{
    /**
     * 交易码
     */
	@PK
	@Field("trans_code")
    private String transcode;
    /**
     * 名称
     */
	@Field("name")
    private String name;
    /**
     * 地址
     */
	@Field("address")
    private String address;
    /**
     * 备注
     */
	@Field("remark")
    private String remark;
    /**
     * 类型
     */
	@Field("type")
    private String type;
	/**
     * 条件
     */
	@Field("condition")
	private String condition;
	/**
     * 入参格式
     */
	@Field("inputformat")
	private String inputformat;
	/**
     * 出参格式
     */
	@Field("outputformat")
	private String outputformat;
	/**
     * 状态（0-待开发，1-开发中，2-完成，9-需求）
     */
	@Field("zt")
	private String zt;
	
    /**
     * 交易码
     */
    public String getTranscode(){
        return this.transcode;
    }

    /**
     * 交易码
     */
    public void setTranscode(String transcode){
        this.transcode = transcode;
    }    
    /**
     * 名称
     */
    public String getName(){
        return this.name;
    }

    /**
     * 名称
     */
    public void setName(String name){
        this.name = name;
    }    
    /**
     * 地址
     */
    public String getAddress(){
        return this.address;
    }

    /**
     * 地址
     */
    public void setAddress(String address){
        this.address = address;
    }    
    /**
     * 备注
     */
    public String getRemark(){
        return this.remark;
    }

    /**
     * 备注
     */
    public void setRemark(String remark){
        this.remark = remark;
    }    
    /**
     * 
     */
    public String getType(){
        return this.type;
    }

    /**
     * 
     */
    public void setType(String type){
        this.type = type;
    }

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getInputformat() {
		return inputformat;
	}

	public void setInputformat(String inputformat) {
		this.inputformat = inputformat;
	}

	public String getOutputformat() {
		return outputformat;
	}

	public void setOutputformat(String outputformat) {
		this.outputformat = outputformat;
	}

	public String getZt() {
		return zt;
	}

	public void setZt(String zt) {
		this.zt = zt;
	}

	@Override
	public String toString() {
		return "SysServiceRegister [transcode=" + transcode + ", name=" + name + ", address=" + address + ", remark="
				+ remark + ", type=" + type + "]";
	}    
}