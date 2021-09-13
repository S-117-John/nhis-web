package com.zebone.nhis.common.module.base.transcode;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 *
 * @author 
 */
@Table(value="sys_service_test_case")
public class SysTestCase extends BaseModule {
	
    /**
     * 主键id
     */
	@PK
	@Field(value="id",id=KeyId.UUID)
    private String id;
    
    /**
     * 关联交易号
     */
	@Field(value="trans_code")
    private String transCode;
    
    /**
     * 入参数据
     */
	@Field(value="rcsj")
    private String rcsj;
    
    /**
     * 用户名
     */
	@Field(value="yhm")
    private String yhm;
    
    /**
     * 用例名称
     */
	@Field(value="ylmc")
    private String ylmc;

    /**
     * 主键id
     */
    public String getId(){
        return this.id;
    }

    /**
     * 主键id
     */
    public void setId(String id){
        this.id = id;
    }    
    /**
     * 关联交易号
     */
    public String getTransCode(){
        return this.transCode;
    }

    /**
     * 关联交易号
     */
    public void setTransCode(String transCode){
        this.transCode = transCode;
    }    
    /**
     * 入参数据
     */
    public String getRcsj(){
        return this.rcsj;
    }

    /**
     * 入参数据
     */
    public void setRcsj(String rcsj){
        this.rcsj = rcsj;
    }    
    /**
     * 用户名
     */
    public String getYhm(){
        return this.yhm;
    }

    /**
     * 用户名
     */
    public void setYhm(String yhm){
        this.yhm = yhm;
    }    
    /**
     * 用例名称
     */
    public String getYlmc(){
        return this.ylmc;
    }

    /**
     * 用例名称
     */
    public void setYlmc(String ylmc){
        this.ylmc = ylmc;
    }    
}