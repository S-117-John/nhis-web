package com.zebone.nhis.pi.pub.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Select;

import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.platform.modules.mybatis.Mapper;
/**
 * 孙逸仙纪念医院-empi相关处理
 * @author jd_em
 *
 */
@Mapper
public interface EmpiClientSyxMapper {
	/**获取患者详细信息*/
	public Map<String, Object> getPiMasterInfo(String pkPi);
	
	/**获取患者医保类型编码 */
	public Map<String, Object> getHpType (String pkPi);
	
	/**获取创建人信息*/
	public Map<String, Object> getCreator(String creator);
	
	/**获取患者省市区4级地址**/
	public Map<String, Object> getAddr(String addrcodeCur);
	
	/**
	 * 获取患者创建信息
	 * @param pkPi
	 * @return
	 */
	public Map<String, Object> getCreatInfo(String pkPi);
	
	public String getHicNo(String pkPi);
	
	int qryRepetitionPi(String pkPi);

	public String getpkPiByMpi(String oldMpi);
	
	public PiMaster qryPiMasterById(String idNo);
	
	@Select("select pk_picate from pi_cate where flag_def = '1'")
	public String qryCate();
	
	public Map<String, Object> qryPiMap(String pkPi);
}
