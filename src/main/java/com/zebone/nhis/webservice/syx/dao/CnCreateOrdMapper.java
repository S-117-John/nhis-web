package com.zebone.nhis.webservice.syx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CnCreateOrdMapper {
	public String qryPkPi(String codePi);
	
	public Map<String, Object> qryPkPv(String codePv,String euPvtype);
	
	public String qryPkDept(String codeDept);
	
	public Map<String, Object> qryEmp(String codeDept);
	
	public Integer qryOrdtype(String codeDr);
	
	public String getPkOrg();
	
	public Map<String, Object> qryBdOrd(String code);
	
	public String qryDocCode(@Param("codeList")String codeList,@Param("name")String name);
	
	public String qryUnit(String nameUnit);

	public List<Map<String, Object>> qryOrderInfo(String pkCnord);

	/**
	 * 查询医嘱信息发送者IP
	 * @return
	 */
	@Select("select t.attr_desc from bd_defdoc t where code_defdoclist ='800004'and code = #{action}")
	public String qryIpSend(@Param(value = "action")String action);
	
	@Select("select val from bd_sysparam where code = #{code} and del_flag = '0' and pk_org !='~'")
	public String qrySysCode(@Param("code")String code);
	
	@Select("select code from bd_ord where name = #{name}")
	public List<String> qryOrdByName(@Param("name")String name);
}
