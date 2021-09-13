package com.zebone.nhis.base.bd.vo;

import java.util.Date;

public class DictattrVo {

	// 主键
	private String PkDictattr;
	// 所属机构
	private String PkOrg;
	// 关联模板主键
	private String PkDictattrtemp;
	// 关联字典项目主键
	private String PkDict;
	// 属性编码
	private String Code;

	private String CodeAttr;
	private String NameAttr;
	// 属性名称
	private String Name;
	// 规格
	private String Spec;
	// 属性值
	private String ValAttr;
	// 字典码表编码
	public String Decode;

	

	// 创建人
	private String Creator;
	// 创建时间
	private Date CreateTime;
	// 修改人
	private String Modifier;
	// 修改时间
	private Date ModityTime;
	// 删除标志
	private String DelFlag;
	// 时间戳
	private Date Ts;
	
	public String getDecode() {
		return Decode;
	}

	public void setDecode(String decode) {
		Decode = decode;
	}

	public String getCodeAttr() {
		return CodeAttr;
	}

	public void setCodeAttr(String codeAttr) {
		this.CodeAttr = codeAttr;
	}

	public String getNameAttr() {
		return NameAttr;
	}

	public void setNameAttr(String nameAttr) {
		this.NameAttr = nameAttr;
	}

	public String getPkDictattr() {
		return PkDictattr;
	}

	public void setPkDictattr(String pkDictattr) {
		PkDictattr = pkDictattr;
	}

	public String getPkOrg() {
		return PkOrg;
	}

	public void setPkOrg(String pkOrg) {
		PkOrg = pkOrg;
	}

	public String getPkDictattrtemp() {
		return PkDictattrtemp;
	}

	public void setPkDictattrtemp(String pkDictattrtemp) {
		PkDictattrtemp = pkDictattrtemp;
	}

	public String getPkDict() {
		return PkDict;
	}

	public void setPkDict(String pkDict) {
		PkDict = pkDict;
	}

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getSpec() {
		return Spec;
	}

	public void setSpec(String spec) {
		Spec = spec;
	}

	public String getValAttr() {
		return ValAttr;
	}

	public void setValAttr(String valAttr) {
		ValAttr = valAttr;
	}

	public String getCreator() {
		return Creator;
	}

	public void setCreator(String creator) {
		Creator = creator;
	}

	public Date getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Date createTime) {
		CreateTime = createTime;
	}

	public String getModifier() {
		return Modifier;
	}

	public void setModifier(String modifier) {
		Modifier = modifier;
	}

	public Date getModityTime() {
		return ModityTime;
	}

	public void setModityTime(Date modityTime) {
		ModityTime = modityTime;
	}

	public String getDelFlag() {
		return DelFlag;
	}

	public void setDelFlag(String delFlag) {
		DelFlag = delFlag;
	}

	public Date getTs() {
		return Ts;
	}

	public void setTs(Date ts) {
		Ts = ts;
	}
}
