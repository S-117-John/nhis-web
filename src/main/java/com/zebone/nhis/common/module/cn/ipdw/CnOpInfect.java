package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="CN_OP_INFECT")
public class CnOpInfect extends BaseModule{
	
	@PK
	@Field(value="PK_OPINFECT",id=KeyId.UUID)
	public String pkOpinfect;         //主键；
	@Field(value="PK_CNORD")
	public String pkCnord;         //关联手术申医嘱请主键；
	@Field(value="DT_INFECT")
	public String dtInfect;         //感染类疾病码表060606；
	@Field(value="DT_INFRESULT")
	public String dtInfresult;         //检测结果码表060607；
	@Field(value="MODIFIER")
	public String modifier;         //修改人；
	@Field(value="MODITY_TIME")
	public Date modityTime;         //修改时间；
	public String getPkOpinfect() {
		return pkOpinfect;
	}
	public void setPkOpinfect(String pkOpinfect) {
		this.pkOpinfect = pkOpinfect;
	}
	public String getDtInfect() {
		return dtInfect;
	}
	public void setDtInfect(String dtInfect) {
		this.dtInfect = dtInfect;
	}
	public String getDtInfresult() {
		return dtInfresult;
	}
	public void setDtInfresult(String dtInfresult) {
		this.dtInfresult = dtInfresult;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public Date getModityTime() {
		return modityTime;
	}
	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}
	public String getPkCnord() {
		return pkCnord;
	}
	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}
	
}
