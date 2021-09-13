package com.zebone.nhis.ex.oi.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.opdw.vo.CnOpEmrRecordVo;
import com.zebone.nhis.common.module.ex.oi.BdDeptIv;
import com.zebone.nhis.common.module.ex.oi.BdPlaceIv;
import com.zebone.nhis.ex.oi.vo.BdDeptIvVO;
import com.zebone.nhis.ex.oi.vo.BdPacleIvVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 门诊输液字典
 * @author gejianwen
 *
 */
@Service
public class OiDictService {
	
	/**
	 * 查询输液科室字典列表
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public List<BdDeptIvVO> getBdDeptIvVO(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		User u = (User)user;
		String sql = "select v.*,d.name_dept from bd_dept_iv v inner join bd_ou_dept d on v.pk_dept=d.pk_dept where v.pk_org='"+u.getPkOrg()+"' ";
		
		String code =  JsonUtil.getFieldValue(param, "code");
		if(StringUtils.isNotBlank(code) && !code.equalsIgnoreCase("null")) sql += " and code like '%"+code+"%'";
		
		code =  JsonUtil.getFieldValue(param, "name");
		if(StringUtils.isNotBlank(code) && !code.equalsIgnoreCase("null")) sql += " and name like '%"+code+"%'";
		
		code =  JsonUtil.getFieldValue(param, "dept");
		if(StringUtils.isNotBlank(code) && !code.equalsIgnoreCase("null")) sql += " and v.pk_dept='"+code+"'";
		
		List<Map<String,Object>> db = DataBaseHelper.queryForList(sql); 
		if(null == db || db.size() <= 0) return null;
		
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		List<BdDeptIvVO> ret = new ArrayList<BdDeptIvVO>();
		
		for(Map<String,Object> map : db){
			BdDeptIvVO bdi = new BdDeptIvVO();
			BeanUtils.copyProperties(bdi, map);
			
			if("1".equals(bdi.getFlagAlert())) bdi.setFlagAlertView("是");
			else bdi.setFlagAlertView("否");
			
			if("1".equals(bdi.getFlagPro())) bdi.setFlagProView("是");
			else bdi.setFlagProView("否");
			
			ret.add(bdi);
		 }		 
		
		return ret;		
	}
	
	/**
	 * 保存输液科室字典
	 * @param param
	 * @param user
	 */
	public void saveDeptVi(String param, IUser user){
		BdDeptIv bdi = JsonUtil.readValue(param, BdDeptIv.class);
		if(null == bdi) throw new BusException("保存失败,没有获取到前台对象!");
		
		if(!checkDuplicateDept(bdi)) throw new BusException("保存失败,编码或者名称重复");
		
		if(bdi.getFlagAlert().equalsIgnoreCase("true")) bdi.setFlagAlert("1");
		else bdi.setFlagAlert("0");
		
		if(bdi.getFlagPro().equalsIgnoreCase("true")) bdi.setFlagPro("1");
		else bdi.setFlagPro("0");
		
		User u = (User)user;		
		bdi.setDelFlag("0");
		bdi.setPkOrg(u.getPkOrg());
		
		if(StringUtils.isEmpty(bdi.getPkDeptiv())){
			bdi.setCreateTime(new Date());
			bdi.setCreator(u.getPkEmp());
			DataBaseHelper.insertBean(bdi);
		}
		else{
			bdi.setModifier(u.getPkEmp());
			bdi.setModityTime(new Date());
			DataBaseHelper.updateBeanByPk(bdi,false);
		}
	}
	
	/**
	 * 检查科室的名称或者code是否重复
	 * @param iv
	 * @return
	 */
	private boolean checkDuplicateDept(BdDeptIv iv){
		String sql = "select * from bd_dept_iv v where v.code=? ";
		List<BdDeptIv> ll = DataBaseHelper.queryForList(sql, BdDeptIv.class, new Object[]{iv.getCode()});
		
		if(StringUtils.isEmpty(iv.getPkDeptiv()) && ll.size() > 0) return false;
		if(!StringUtils.isEmpty(iv.getPkDeptiv())){
			for(BdDeptIv vo : ll){
				if(!vo.getPkDeptiv().equals(iv.getPkDeptiv())) return false;
			}			
		}
		
		sql = "select * from bd_dept_iv v where v.name=?";
		ll = DataBaseHelper.queryForList(sql, BdDeptIv.class, new Object[]{iv.getName()});
		
		if(StringUtils.isEmpty(iv.getPkDeptiv()) && ll.size() > 0) return false;
		if(!StringUtils.isEmpty(iv.getPkDeptiv())){
			for(BdDeptIv vo : ll){
				if(!vo.getPkDeptiv().equals(iv.getPkDeptiv())) return false;
			}
		}
		
		return true;
		
		
	}
	
	/**
	 * 删除输液大厅字典
	 * @param param
	 * @param user
	 */
	public void delDeptVi(String param, IUser user){
		String pk = JsonUtil.getFieldValue(param, "pk");
		if(StringUtils.isEmpty(pk)) return;
		
		String sql = "select * from bd_place_iv p where p.pk_deptiv=?";
		List l = DataBaseHelper.queryForList(sql, pk);		
		
		if(null != l && l.size() > 0) throw new BusException("输液大厅字典下已经有床位字典,请先删除床位字典!");
		
		BdDeptIv bdi = new BdDeptIv();
		bdi.setPkDeptiv(pk);
		
		DataBaseHelper.deleteBeanByPk(bdi);		
	}
	

	/**
	 * 保存输液椅位字典
	 * @param param
	 * @param user
	 */
	public void savePacleVi(String param, IUser user){
		BdPlaceIv bdi = JsonUtil.readValue(param, BdPlaceIv.class);
		if(null == bdi) throw new BusException("保存失败,没有获取到前台对象!");
		
		if(!checkDuplicatePlace(bdi)) throw new BusException("保存失败,编码或者名称重复");
		
		if(null != bdi.getFlagActive() && "true".equalsIgnoreCase(bdi.getFlagActive())) bdi.setFlagActive("1");
		else bdi.setFlagActive("0");
		
		User u = (User)user;		
		bdi.setDelFlag("0");
		bdi.setPkOrg(u.getPkOrg());
		
		if(StringUtils.isEmpty(bdi.getPkPlaceiv())){
			bdi.setCreateTime(new Date());
			bdi.setCreator(u.getPkEmp());
			bdi.setEuStatus("0");
			DataBaseHelper.insertBean(bdi);
		}
		else{
			bdi.setModifier(u.getPkEmp());
			bdi.setModityTime(new Date());
			bdi.setEuStatus("0");
			DataBaseHelper.updateBeanByPk(bdi,false);
		}		
	}
	
	/**
	 * 检查椅位床位的名称或者code是否重复
	 * @param iv
	 * @return
	 */
	private boolean checkDuplicatePlace(BdPlaceIv iv){
		String sql = "select * from bd_place_iv v where v.code=?";
		
        List<BdPlaceIv> ll = DataBaseHelper.queryForList(sql, BdPlaceIv.class, new Object[]{iv.getCode()});
		
		if(StringUtils.isEmpty(iv.getPkPlaceiv()) && ll.size() > 0) return false;
		if(!StringUtils.isEmpty(iv.getPkPlaceiv())){
			for(BdPlaceIv vo : ll){
				if(!vo.getPkPlaceiv().equals(iv.getPkPlaceiv())) return false;
			}
			
		}
		
		sql = "select * from bd_place_iv v where v.name=?";
		ll = DataBaseHelper.queryForList(sql, BdPlaceIv.class, new Object[]{iv.getName()});
		
		if(StringUtils.isEmpty(iv.getPkPlaceiv()) && ll.size() > 0) return false;
		if(!StringUtils.isEmpty(iv.getPkPlaceiv())){
			for(BdPlaceIv vo : ll){
				if(!vo.getPkPlaceiv().equals(iv.getPkPlaceiv())) return false;
			}			
		}
		
		return true;
		
	}
	
	/**
	 * 查询输液椅位列表
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public List<BdPacleIvVo> getBdPlaceVO(String param, IUser user) throws IllegalAccessException, InvocationTargetException{
		BdPlaceIv bdi = JsonUtil.readValue(param, BdPlaceIv.class);
		User u = (User)user;
		
		
		String sql = "select case p.eu_seattype when '0' then '椅位' when '1' then '床位' end as eu_seattype, "
                   +" case p.eu_placeattr when '0' then '普通' when '1' then '临时' when '9' then '虚拟' end as eu_placeattr, "
                   +" p.code,p.pk_item,p.name,p.room_no,p.pk_deptiv,d.name as sex_name, p.dt_sex,p.xpos,p.ypos,p.note,v.name as dept_name,p.price,bi.name as item_name, "
                   +" case p.eu_status when '0' then '未用' when '1' then '在用' when '2' then '锁定' end eu_status_name,p.pk_placeiv,eu_status,p.flag_active "
                   +" from bd_place_iv p inner join bd_dept_iv v on v.pk_deptiv = p.pk_deptiv left join bd_defdoc d on d.code = p.dt_sex and d.code_defdoclist = '000000'"
                   +" left join BD_ITEM bi on bi.pk_item = p.pk_item where p.pk_org='"+u.getPkOrg()+"' ";
		if(null == bdi){
			List<BdPacleIvVo> ret = DataBaseHelper.queryForList(sql,BdPacleIvVo.class);			
			return ret;
		}
		
		if(StringUtils.isNotBlank(bdi.getPkDeptiv()) && !"null".equalsIgnoreCase(bdi.getPkDeptiv())) sql += " and p.pk_deptiv='"+bdi.getPkDeptiv()+"' ";
		if(StringUtils.isNotBlank(bdi.getEuSeattype()) && !"null".equalsIgnoreCase(bdi.getEuSeattype())) sql += " and p.eu_seattype='"+bdi.getEuSeattype()+"' ";
		if(StringUtils.isNotBlank(bdi.getEuPlaceattr()) && !"null".equalsIgnoreCase(bdi.getEuPlaceattr())) sql += " and p.eu_placeattr='"+bdi.getEuPlaceattr()+"' ";
		if(StringUtils.isNotBlank(bdi.getCode()) && !"null".equalsIgnoreCase(bdi.getCode())) sql += " and p.code like '%"+bdi.getCode()+"%' ";
		if(StringUtils.isNotBlank(bdi.getName()) && !"null".equalsIgnoreCase(bdi.getName())) sql += " and p.name like '%"+bdi.getName()+"%' ";
		if(StringUtils.isNotBlank(bdi.getRoomNo()) && !"null".equalsIgnoreCase(bdi.getRoomNo())) sql += " and p.room_no like '%"+bdi.getRoomNo()+"%' ";
		if(StringUtils.isNotBlank(bdi.getDtSex()) && !"null".equalsIgnoreCase(bdi.getDtSex())) sql += " and p.dt_sex='"+bdi.getDtSex()+"' ";
		if(StringUtils.isNotBlank(bdi.getFlagActive()) && !"null".equalsIgnoreCase(bdi.getDtSex())){
			if("0".equals(bdi.getFlagActive())) sql += " and (p.flag_active=null or p.flag_active='0') ";
			else if("1".equals(bdi.getFlagActive())) sql += " and p.flag_active='1'";
		}
		
		
		List<BdPacleIvVo> ret = DataBaseHelper.queryForList(sql,BdPacleIvVo.class);
		
		return ret;
	}
	
	/**
	 * transcode 005004001006
	 * 删除输液椅位字典
	 * @param param
	 * @param user
	 */
	public void delPlaceIv(String param, IUser user){
		String pk = JsonUtil.getFieldValue(param, "pk");
		if(StringUtils.isEmpty(pk)) return;
		
		String sql = "select * from EX_INFUSION_OCC occ where occ.pk_bed=?";
		List l = DataBaseHelper.queryForList(sql, pk);
		
		if(null != l && l.size() >= 1)  throw new BusException("床位或者以为已经被占用，不能删除");
		
		
		BdPlaceIv bdi = new BdPlaceIv();
		bdi.setPkPlaceiv(pk);
		
		DataBaseHelper.deleteBeanByPk(bdi);
		
	}

}
