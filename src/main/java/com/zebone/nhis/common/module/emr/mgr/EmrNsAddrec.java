package com.zebone.nhis.common.module.emr.mgr;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EMR_NS_ADDREC 
 *
 * @since 2017-12-27 09:31:23
 */
@Table(value="EMR_NS_ADDREC")
public class EmrNsAddrec extends BaseModule {

	@Field(value="PK_ADDREC")
    private String pkAddrec;

	@Field(value="GROUP_NAME")
    private String groupName;

	@Field(value="DEL_FLAG")
    private String delFlag;
	
    public String getPkAddrec(){
        return this.pkAddrec;
    }
    public void setPkAddrec(String pkAddrec){
        this.pkAddrec = pkAddrec;
    }

    public String getGroupName(){
        return this.groupName;
    }
    public void setGroupName(String groupName){
        this.groupName = groupName;
    }
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
    
}