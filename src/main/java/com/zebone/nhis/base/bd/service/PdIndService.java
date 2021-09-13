package com.zebone.nhis.base.bd.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.bd.dao.PdindMapper;
import com.zebone.nhis.base.bd.vo.BdPdIndVo;
import com.zebone.nhis.base.bd.vo.PdIndtypeVo;
import com.zebone.nhis.common.module.scm.pub.BdIndtype;
import com.zebone.nhis.common.module.scm.pub.BdPdInd;
import com.zebone.nhis.common.module.scm.pub.BdPdIndhp;
import com.zebone.nhis.common.module.scm.pub.BdPdIndpd;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class PdIndService {

	@Resource
	private PdindMapper pdindMapper;
	
	/**
	 * 001002007068
	 * 查询目录类别
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdIndtype> qryIndtype(String param, IUser user) {
		return pdindMapper.qryCatalog();
	}
	
	/**
	 * 保存 适应症及相关药品
	 * @param param
	 * @param user
	 * @return
	 */
	public BdPdIndVo savePdindAndPds(String param, IUser user){
		//1.1获取适应症用药信息和药品关联信息
		BdPdIndVo pdind = JsonUtil.readValue(param, BdPdIndVo.class);
		if(pdind == null) throw new BusException("未获取到待保存的 适应症用药信息！");
		User newUser=(User)user;
		
		//1.2分割数据，获取对应数据
		BdPdInd bdPdind = pdind.getBdPdInd();
		//List<BdPdIndpd> pdList = pdind.getPdList();//相应药品VO
		
		//2.1判断是否已存在相同的编码或者名称
		int count = DataBaseHelper.queryForScalar("select count(1) from bd_pd_ind where del_flag = '0' and (code_ind = ? or name_ind = ?)", Integer.class,new Object[]{ pdind.getChkCodeInd(),pdind.getChkNameInd()});
		if(count > 0) {
			throw new BusException("输入的编码或名称已存在，请重新输入!");
		}
		
		if(CommonUtils.isEmptyString(bdPdind.getPkPdind())){
			//2.2插入适应症用药信息
			String pkPdind = NHISUUID.getKeyId();
			bdPdind.setPkPdind(pkPdind);
			bdPdind.setPkOrg("~                               ");
			bdPdind.setCreator(newUser.getId());
			bdPdind.setCreateTime(new Date());
			bdPdind.setTs(new Date());
			bdPdind.setDelFlag("0");
			DataBaseHelper.insertBean(bdPdind);
		}else{
			//2.3更新适应症用药信息
			bdPdind.setModifier(newUser.getId());
			bdPdind.setModityTime(new Date());
			DataBaseHelper.updateBeanByPk(bdPdind,false);
		}
		
		DataBaseHelper.execute("delete from bd_pd_indpd where pk_pdind=?", new Object[]{bdPdind.getPkPdind()});
		//2.4更新相关药品的数据
		List<BdPdIndpd> pdList = pdind.getBdpds();
		if (pdList!=null&&pdList.size()>0) {
			for (BdPdIndpd bdPd : pdList){ 
				bdPd.setPkOrg("~                               ");
				bdPd.setPkPdindpd(NHISUUID.getKeyId());
				bdPd.setPkPdind(bdPdind.getPkPdind());
				bdPd.setCreator(newUser.getId());
				bdPd.setCreateTime(new Date());
				bdPd.setTs(new Date());
				bdPd.setDelFlag("0");
				DataBaseHelper.insertBean(bdPd);
			}
		}
		//12.13新增写表Bd_Pd_Indhp
		/*pdindMapper.delHp(bdPdind.getPkPdind());
		List<BdPdIndhp> hpList = pdind.getHpList();
		if (hpList!=null&&hpList.size()>0) {
			for (BdPdIndhp pdIndhp : hpList) {
				pdIndhp.setPkPdind(bdPdind.getPkPdind());
				pdIndhp.setPkOrg("~");
				//pdIndhp.setPkHp(bdPdind.getPkHp());
				DataBaseHelper.insertBean(pdIndhp);
			}
		}*/
		
		return pdind;
	}
	
	/**
	 * 删除 适应症 ,更新相关药品
	 * @param param
	 * @param user
	 */
	public void delPdindAndPds(String param, IUser user){
		String pKPdind = JsonUtil.readValue(param, String.class);
		if(CommonUtils.isEmptyString(pKPdind)){
			throw new BusException("未获取到待删除的 适应症用药主键！");
		}
		//12.13新增删除
		DataBaseHelper.execute("delete from bd_pd_indhp where pk_pdind=?", new Object[]{pKPdind});
		
		DataBaseHelper.execute("delete from bd_pd_ind where pk_pdind = ? ", new Object[]{pKPdind});
		DataBaseHelper.execute("delete from bd_pd_indpd where pk_pdind=?", new Object[]{pKPdind});
		//DataBaseHelper.execute("update bd_pd set pk_pdind = null ,flag_ped = '0' where pk_pdind = ? ", new Object[]{pKPdind});
	}
	
	/**
	 * 001002007048
	 * 查询机构下面的适应症用药和关联医保
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryPdInds(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		Map<String,Object> map = new HashMap<String, Object>();
		List<Map<String,Object>> PdInds = pdindMapper.qryPdInds(paramMap);
		map.put("PdList", PdInds);
		List<BdPdIndhp> hps = pdindMapper.qryHp((String)paramMap.get("pkIndtype"));
		map.put("HpList", hps);
		return map;
	}
	
	/**
	 * 001002007049
	 * 根据适应症用药主键查询相关药品
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryPds(String param,IUser user){
		String pkPdind=JsonUtil.getFieldValue(param, "pkPdind");
		if(pkPdind==null || "".equals(pkPdind)){
			return null;
		}
		List<BdPdIndhp> qryHps = pdindMapper.qryHp(pkPdind);
		List<Map<String,Object>> qryPds = pdindMapper.qryPds(pkPdind);
		if (qryPds!=null&&qryPds.size()>0) {
			for (Map<String, Object> map : qryPds) {
				map.put("hps", qryHps);
			}
		}
		return qryPds;
	}
	
	/**
	 * 001002007070
	 * 删除适应症目录类别
	 * @param param
	 * @param user
	 */
	public void delIndtype(String param,IUser user) {
		BdIndtype bdIndtype = JsonUtil.readValue(param, BdIndtype.class);
		if (bdIndtype==null) return;
		
		//删除目录关联的医保
		pdindMapper.delHp(bdIndtype.getPkIndtype());
		pdindMapper.delPdByType(bdIndtype.getCodeType());
		pdindMapper.delPdIndByType(bdIndtype.getCodeType());
		pdindMapper.delIndtype(bdIndtype.getCodeType());
	}
	
	/**
	 * 001002007071
	 * 根据适应症主键删除适应症和明细
	 * @param param
	 * @param user
	 */
	public void delPdind(String param,IUser user) {
		Map map = JsonUtil.readValue(param, Map.class);
		if(map==null)return;
		
		pdindMapper.delPd((String)map.get("pkPdind"));
		pdindMapper.delPdInd((String)map.get("pkPdind"));
	}
	
	/**
	 * 001002007069
	 * 保存适应症目录类别
	 * @param param
	 * @param user
	 */
	public void savePdIndtype(String param,IUser user) {
		PdIndtypeVo pdIndtypeVo = JsonUtil.readValue(param, PdIndtypeVo.class);
		if (pdIndtypeVo==null)return;
		BdIndtype pdIndtype = pdIndtypeVo.getPdIndtype();
		if (StringUtils.isNotBlank(pdIndtypeVo.getPdIndtype().getPkIndtype())) {
			DataBaseHelper.updateBeanByPk(pdIndtype,false);
			pdindMapper.delHp(pdIndtype.getPkIndtype());
		}else {
			if(pdindMapper.countCode(pdIndtype.getCodeType())>0){
				throw new BusException("编码不唯一！");
			}
			if(pdindMapper.countName(pdIndtype.getNameType())>0){
				throw new BusException("名称不唯一！");
			}
			pdIndtype.setPkOrg("~                               ");
			DataBaseHelper.insertBean(pdIndtype);
		}
		List<BdPdIndhp> hps = pdIndtypeVo.getHps();
		if (hps==null||hps.size()<1) return;
		
		for (BdPdIndhp bdPdIndhp : hps) {
			bdPdIndhp.setPkIndtype(pdIndtype.getPkIndtype());
			bdPdIndhp.setPkOrg("~                               ");
			DataBaseHelper.insertBean(bdPdIndhp);
		}
	}
	
	/**
	 * 001002007072
	 * 查询末级医保
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> qryHps(String param,IUser user) {
		
		return pdindMapper.qryHps();
	}
	
	/**
	 * 保存关联医保
	 * @param param
	 * @param user
	 */
	public void savePdIndHp(String param,IUser user) {
		PdIndtypeVo pdIndtypeVo = JsonUtil.readValue(param, PdIndtypeVo.class);
		if (pdIndtypeVo==null)return;
		BdIndtype pdIndtype = pdIndtypeVo.getPdIndtype();
		pdindMapper.delHp(pdIndtype.getPkIndtype());
		List<BdPdIndhp> hps = pdIndtypeVo.getHps();
		if (hps==null||hps.size()<1) return;
		
		for (BdPdIndhp bdPdIndhp : hps) {
			bdPdIndhp.setPkIndtype(pdIndtype.getPkIndtype());
			bdPdIndhp.setPkOrg("~                               ");
			DataBaseHelper.insertBean(bdPdIndhp);
		}
	}
}
