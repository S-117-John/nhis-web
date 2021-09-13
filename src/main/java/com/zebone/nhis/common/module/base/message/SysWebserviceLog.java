package com.zebone.nhis.common.module.base.message;
import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: SYS_WEBSERVICE_LOG - 博爱医院接口日志表 
 *
 * @since 2018-04-16 05:58:21
 */
@Table(value="SYS_WEBSERVICE_LOG")
public class SysWebserviceLog {

	/** PK_LOG - webService调用日志主键 */
	@PK
	@Field(value="PK_LOG",id=KeyId.UUID)
    private String pkLog;

	/** FUNC_ID - 交易码 */
	@Field(value="FUNC_ID")
    private String funcId;

	/** INPUT_INFO - 入参 */
	@Field(value="INPUT_INFO")
    private String inputInfo;

	/** OUTPUT_INFO - 出参 */
	@Field(value="OUTPUT_INFO")
    private String outputInfo;
	
	/** TS - 时间戳 */
	@Field(date=FieldType.ALL)
    private Date ts;
	
	/** FLAG_SUCCESS - 调用成功 */
	@Field(value="FLAG_SUCCESS")
    private String flagSuccess;

    public String getPkLog(){
        return this.pkLog;
    }
    public void setPkLog(String pkLog){
        this.pkLog = pkLog;
    }

    public String getFuncId(){
        return this.funcId;
    }
    public void setFuncId(String funcId){
        this.funcId = funcId;
    }

    public String getInputInfo(){
        return this.inputInfo;
    }
    public void setInputInfo(String inputInfo){
        this.inputInfo = inputInfo;
    }

    public String getOutputInfo(){
        return this.outputInfo;
    }
    public void setOutputInfo(String outputInfo){
        this.outputInfo = outputInfo;
    }

    public Date getTs(){
        return this.ts;
    }
    public void setTs(Date ts){
        this.ts = ts;
    }

    public String getFlagSuccess(){
        return this.flagSuccess;
    }
    public void setFlagSuccess(String flagSuccess){
        this.flagSuccess = flagSuccess;
    }
}