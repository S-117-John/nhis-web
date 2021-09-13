package com.zebone.nhis.common.module.base.bd.mk;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_TERM_FREQ_TIME  - 医嘱频次时刻 
 *
 * @since 2016-10-09 03:46:49
 */
@Table(value="BD_TERM_FREQ_TIME")
public class BdTermFreqTime extends BaseModule  {

	private static final long serialVersionUID = 1L;

	/** PK_FREQTIME - 频次时刻主键 */
	@PK
	@Field(value="PK_FREQTIME",id=KeyId.UUID)
    private String pkFreqtime;

    /** PK_FREQ - 频次主键 */
	@Field(value="PK_FREQ")
    private String pkFreq;

    /** SORT_NO - 频次执行序号 */
	@Field(value="SORT_NO")
    private Long sortNo;

    /** WEEK_NO - 对应星期或日：0表示按天 1表示周1 7表示周日 */
	@Field(value="WEEK_NO")
    private Long weekNo;

    /** TIME_OCC - 执行时刻：例如：08:00 23:30 */
	@Field(value="TIME_OCC")
    private String timeOcc;

    /** MODIFIER - 修改人 */
	@Field(value="MODIFIER",userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;


    public String getPkFreqtime(){
        return this.pkFreqtime;
    }
    public void setPkFreqtime(String pkFreqtime){
        this.pkFreqtime = pkFreqtime;
    }

    public String getPkFreq(){
        return this.pkFreq;
    }
    public void setPkFreq(String pkFreq){
        this.pkFreq = pkFreq;
    }

    public Long getSortNo(){
        return this.sortNo;
    }
    public void setSortNo(Long sortNo){
        this.sortNo = sortNo;
    }

    public Long getWeekNo(){
        return this.weekNo;
    }
    public void setWeekNo(Long weekNo){
        this.weekNo = weekNo;
    }

    public String getTimeOcc(){
        return this.timeOcc;
    }
    public void setTimeOcc(String timeOcc){
        this.timeOcc = timeOcc;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }
}