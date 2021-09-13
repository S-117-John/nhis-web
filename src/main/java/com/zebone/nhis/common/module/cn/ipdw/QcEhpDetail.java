package com.zebone.nhis.common.module.cn.ipdw;




import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: QC_EHP_DETAIL - 病案首页质控明细 
 *
 * @since 2020-06-16 09:43:32
 */
@Table(value="QC_EHP_DETAIL")
public class QcEhpDetail extends BaseModule  {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** PK_QCDETAIL - 主键 */
	@PK
	@Field(value="PK_QCDETAIL",id=KeyId.UUID)
    private String pkQcdetail;

    /** PK_QCEHP - 关联质控 */
	@Field(value="PK_QCEHP")
    private String pkQcehp;

    /** PK_QCEHPREC - 关联质控记录 */
	@Field(value="PK_QCEHPREC")
    private String pkQcehprec;

    /** EU_TYPE - 质控类型 */
	@Field(value="EU_TYPE")
    private String euType;

    /** DT_EHPITEM - 质控项目编码 */
	@Field(value="DT_EHPITEM")
    private String dtEhpitem;

    /** PK_QCITEM - 评分项目编码 */
	@Field(value="PK_QCITEM")
    private String pkQcitem;

    /** NAME_ITEM - 项目名称 */
	@Field(value="NAME_ITEM")
    private String nameItem;

    /** DESC_DEFECT - 缺陷描述 */
	@Field(value="DESC_DEFECT")
    private String descDefect;

    /** EU_STATUS - 缺陷状态 */
	@Field(value="EU_STATUS")
    private String euStatus;

    /** SCORE - 分值 */
	@Field(value="SCORE")
    private String score;

    /** CNT_DED - 减分项 */
	@Field(value="CNT_DED")
    private Integer cntDed;

    /** VAL - 减分 */
	@Field(value="VAL")
    private Double val;

	//项目类别
	public String euItemcate;
	//项目数
	public String itemCount;
	//评分项
	public String scoreItem;
	
	public double valMax;
    public String getPkQcdetail(){
        return this.pkQcdetail;
    }
    public void setPkQcdetail(String pkQcdetail){
        this.pkQcdetail = pkQcdetail;
    }

    public String getPkQcehp(){
        return this.pkQcehp;
    }
    public void setPkQcehp(String pkQcehp){
        this.pkQcehp = pkQcehp;
    }

    public String getPkQcehprec(){
        return this.pkQcehprec;
    }
    public void setPkQcehprec(String pkQcehprec){
        this.pkQcehprec = pkQcehprec;
    }

    public String getEuType(){
        return this.euType;
    }
    public void setEuType(String euType){
        this.euType = euType;
    }

    public String getDtEhpitem(){
        return this.dtEhpitem;
    }
    public void setDtEhpitem(String dtEhpitem){
        this.dtEhpitem = dtEhpitem;
    }

    public double getValMax() {
		return valMax;
	}
	public void setValMax(double valMax) {
		this.valMax = valMax;
	}
	public String getPkQcitem(){
        return this.pkQcitem;
    }
    public void setPkQcitem(String pkQcitem){
        this.pkQcitem = pkQcitem;
    }

    public String getNameItem(){
        return this.nameItem;
    }
    public void setNameItem(String nameItem){
        this.nameItem = nameItem;
    }

    public String getDescDefect(){
        return this.descDefect;
    }
    public void setDescDefect(String descDefect){
        this.descDefect = descDefect;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getScore(){
        return this.score;
    }
    public void setScore(String score){
        this.score = score;
    }

    public Integer getCntDed(){
        return this.cntDed;
    }
    public void setCntDed(Integer cntDed){
        this.cntDed = cntDed;
    }

    public Double getVal(){
        return this.val;
    }
    public void setVal(Double val){
        this.val = val;
    }
	public String getEuItemcate() {
		return euItemcate;
	}
	public void setEuItemcate(String euItemcate) {
		this.euItemcate = euItemcate;
	}
	public String getItemCount() {
		return itemCount;
	}
	public void setItemCount(String itemCount) {
		this.itemCount = itemCount;
	}
	public String getScoreItem() {
		return scoreItem;
	}
	public void setScoreItem(String scoreItem) {
		this.scoreItem = scoreItem;
	}
}