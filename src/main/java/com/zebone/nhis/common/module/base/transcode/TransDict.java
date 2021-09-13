package com.zebone.nhis.common.module.base.transcode;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 *
 * @author 
 */
@Table(value="sys_service_trans_dict")
public class TransDict{
    /**
     * 
     */
	@PK
	@Field(value="id")
    private String id;
    /**
     * 类别名称
     */
	@Field(value="typename")
    private String typename;
    /**
     * 类别所属组
     */
	@Field(value="typegroup")
    private String typegroup;

    /**
     * 
     */
    public String getId(){
        return this.id;
    }

    /**
     * 
     */
    public void setId(String id){
        this.id = id;
    }    
    /**
     * 类别名称
     */
    public String getTypename(){
        return this.typename;
    }

    /**
     * 类别名称
     */
    public void setTypename(String typename){
        this.typename = typename;
    }    
    /**
     * 类别所属组
     */
    public String getTypegroup(){
        return this.typegroup;
    }

    /**
     * 类别所属组
     */
    public void setTypegroup(String typegroup){
        this.typegroup = typegroup;
    }    
}