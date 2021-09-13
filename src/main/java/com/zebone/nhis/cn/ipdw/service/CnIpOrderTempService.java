package com.zebone.nhis.cn.ipdw.service;

import com.zebone.nhis.cn.ipdw.vo.BdOrdSetDtVO;
import com.zebone.nhis.cn.ipdw.vo.BdTempVo;
import com.zebone.nhis.cn.pub.service.BdSnService;
import com.zebone.nhis.common.module.cn.ipdw.BdOrdSet;
import com.zebone.nhis.common.module.cn.ipdw.BdOrdSetDt;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Service
public class CnIpOrderTempService {

	@Autowired
	private BdSnService bdSnService;

	/**
	 * 查询医嘱模板 
	 * trans code 004004003020
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public List<BdOrdSet> getTempList(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		BdOrdSet bos = JsonUtil.readValue(param, BdOrdSet.class);		
		String sql = "select distinct ord.* from bd_ord_set ord where ord.code is not null and";
		
		if(!StringUtils.isEmpty(bos.getEuRight())) sql += " ord.eu_right='"+bos.getEuRight()+"' ";
		if("1".equals(bos.getEuRight())){
			if(!StringUtils.isEmpty(bos.getPkDept())) {
				sql += " and (ord.pk_dept='"+bos.getPkDept()+"' or ";
				sql += " exists(select 1 from BD_ORD_SET_SHARE where ord.PK_ORDSET = PK_ORDSET and ord.EU_RIGHT='"+bos.getEuRight()+"' and PK_DEPT_SHARE ='"+bos.getPkDept()+"')) ";
			}
		}else{
			if(!StringUtils.isEmpty(bos.getPkDept())) sql += " and ord.pk_dept='"+bos.getPkDept()+"' ";
		}

		if(!StringUtils.isEmpty(bos.getPkOrg())) sql += " and ord.pk_org='"+bos.getPkOrg()+"' ";
		if(!StringUtils.isEmpty(bos.getPkEmp())) sql += " and ord.pk_emp='"+bos.getPkEmp()+"' ";
		if(!StringUtils.isEmpty(bos.getFlagIp()) && "1".equals(bos.getFlagIp())) sql += " and ord.flag_ip='"+bos.getFlagIp()+"' ";
		if(!StringUtils.isEmpty(bos.getFlagOp()) && "1".equals(bos.getFlagOp())) sql += " and ord.flag_op='"+bos.getFlagOp()+"' ";
		if(!StringUtils.isEmpty(bos.getCode()) && !StringUtils.isEmpty(bos.getName())) sql += " and (ord.code like '%"+bos.getCode()+"%' or "
				+ " ord.name like '%"+bos.getName()+"%')";
		sql +=" order by SPCODE ";
		
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		List<BdOrdSet> ret = new ArrayList<BdOrdSet>();
		List<Map<String,Object>> ll = DataBaseHelper.queryForList(sql);
				
		if(null == ll || ll.size() <= 0) return null;
		
		for(Map<String,Object> map : ll){
			BdOrdSet bo = new BdOrdSet();
			BeanUtils.copyProperties(bo, map);				
			ret.add(bo);
		}	
		
		return ret;	
	}
	

	/**
	 * 查询医嘱模板明细
	 * trans code 004004003024
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public List<BdOrdSetDtVO> getTempDtList(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		String pid = JsonUtil.getFieldValue(param, "pid");
		String sql = "select dt.note,dt.flag_self,ord.spec,dt.pk_ordsetdt,dt.FLAG_PD,dt.quan,dt.PK_ORDSET, dt.sort_no, dt.pk_ord, ord.code ord_code,ord.name ord_name, dt.order_no,dt.parent_no, dt.dosage,null as unit, dt.code_freq,freq.name freq, "
				+ " dt.code_supply, null as supply, dt.days, dt.pk_dept_exec,dept.name_dept,dt.flag_def,dt.name_ord,type.name ordtype,p.price,ord.eu_ordtype,dt.dt_herbusage,ord.spec ,dt.dt_prestype,dt.pres_no,'' eu_usecate, dt.EU_BOIL "
				+ " from bd_ord_set_dt dt"
				+ " left outer join bd_ord ord on dt.pk_ord=ord.pk_ord and dt.flag_pd=0 "
				+ " left outer join bd_term_freq freq on dt.code_freq=freq.code "
				+ " left outer join bd_ou_dept dept on dt.pk_dept_exec=dept.pk_dept "
				+ " left outer join bd_ordtype type on type.code = ord.code_ordtype"
				+ " left outer join (select busi.pk_ord,sum((case when busi.quan=null then 0 else busi.quan end) * "
				+ " (case when item.price=null then 0 else item.price end)) as price "
				+ " from bd_ord_item busi, bd_item item "
				+ " where busi.pk_item = item.pk_item and busi.del_flag='0' and item.del_flag='0' "
				+ " group by busi.pk_ord) p on p.pk_ord = ord.pk_ord "
				+ " where dt.pk_ordset='"+pid+"' and dt.flag_pd=0 "
				+" order by dt.sort_no "; //非药品
		
		List<Map<String,Object>> ll = DataBaseHelper.queryForList(sql);
		ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
		List<BdOrdSetDtVO> ret = new ArrayList<BdOrdSetDtVO>();
		
		for(Map<String,Object> map : ll){
			BdOrdSetDtVO bo = new BdOrdSetDtVO();
			BeanUtils.copyProperties(bo, map);				
			ret.add(bo);
		}
		
		sql = "select dt.note,dt.flag_self,dt.pk_ordsetdt,dt.quan,dt.FLAG_PD,dt.PK_ORDSET,dt.sort_no,dt.pk_ord,ord.code ord_code,ord.name ord_name,dt.order_no,dt.parent_no,dt.dosage,unit.name as unit,dt.code_freq,freq.name freq,dt.code_supply,dt.pk_unit_dos,"
				+ " sup.name as supply,dt.days,dt.pk_dept_exec,dept.name_dept,dt.flag_def,dt.name_ord,ord.pk_unit_min,nvl(ord.weight,0) as weight,ord.pk_unit_wt,nvl(ord.vol,0) as vol,ord.pk_unit_vol,dt.pk_unit,null as eu_ordtype,dt.dt_herbusage,ord.spec,ord.price,dt.dt_prestype,dt.pres_no,ord.EU_USECATE, dt.EU_BOIL  "
				+ "  from bd_ord_set_dt dt"
				+ " inner join bd_pd ord on dt.pk_ord=ord.pk_pd and dt.flag_pd=1 "
				+ " left outer join bd_unit unit on dt.pk_unit_dos=unit.pk_unit "
				+ " left outer join bd_term_freq freq on dt.code_freq=freq.code "
				+ " left outer join bd_supply sup on dt.code_supply=sup.code "
				+ " left outer join bd_ou_dept dept on dt.pk_dept_exec=dept.pk_dept where dt.pk_ordset='"+pid+"'"
				+ " order by dt.sort_no "; //药品
		
		ll = DataBaseHelper.queryForList(sql);
		
		for(Map<String,Object> map : ll){
			BdOrdSetDtVO bo = new BdOrdSetDtVO();
			BeanUtils.copyProperties(bo, map);
			ret.add(bo);
		}
		
		return ret;		
	}
	
	/**
	 * 删除医嘱模板明细
	 * trans code 004004003025
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void delTempDtList(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		List<BdOrdSetDt> list = JsonUtil.readValue(param, new TypeReference<List<BdOrdSetDt>>() {});
		
		/*for(BdOrdSetDt dt : dts){
			if(StringUtils.isEmpty(dt.getPkOrdsetdt())) continue;
			DataBaseHelper.deleteBeanByPk(dt);
		}*/
		Iterator<BdOrdSetDt> it = list.iterator();
		while(it.hasNext()){
			BdOrdSetDt dt = it.next();
			if(StringUtils.isEmpty(dt.getPkOrdsetdt())){
				continue;
			}
			if("1".equals(dt.getChecked())){
				DataBaseHelper.deleteBeanByPk(dt);
				it.remove();
			}
		}
		for (int i = 0; i < list.size(); i++) {
			BdOrdSetDt dt = list.get(i);
			DataBaseHelper.update("update bd_ord_set_dt set sort_no = ? where pk_ordsetdt = ? "
					, new Object[]{i+1,dt.getPkOrdsetdt()});
		}
		
	}
	
	/**
	 * 保存医嘱模板明细
	 * trans code 004004003026
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void saveTempDtList(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		List<BdOrdSetDt> dts = JsonUtil.readValue(param, new TypeReference<List<BdOrdSetDt>>() {});
		User u = (User)user;
				
		for(BdOrdSetDt dt : dts){
			
			if("1".equals(dt.getFlagDef())){
				dt.setNameOrd(dt.getPkOrd());
				dt.setPkOrd(null);
			}
		    else{
				dt.setFlagDef("0");
//				dt.setNameOrd(null);
			}
			
			dt.setDelFlag("0");
			dt.setDosage(dt.getDosage());
			
			if(StringUtils.isEmpty(dt.getPkOrdsetdt())){
				dt.setCreator(u.getPkEmp());
				dt.setCreateTime(new Date());
				
				DataBaseHelper.insertBean(dt);
			}else{				
				dt.setModifier(u.getPkEmp());
				DataBaseHelper.updateBeanByPk(dt,false);

				//如果科室为null则要更新
				String sqlNu = " update BD_ORD_SET_DT set PK_DEPT_EXEC=:pkDeptExec where PK_ORDSETDT=:pkOrdsetdt ";
				DataBaseHelper.update(sqlNu, dt);
			}			
		}		
	}
	

	/**
	 * 删除模板
	 * trans code 004004003023
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void delOrdSet(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		String pid = JsonUtil.getFieldValue(param, "pid");	
		String sql ="select * from bd_ord_set ord where ord.PK_PARENT='"+pid+"'";
		
		List<Map<String,Object>> ll = DataBaseHelper.queryForList(sql);
		if(null != ll && ll.size() >0) throw new BusException("请先删除模板下的子模板");
		
		BdOrdSet bos = new BdOrdSet();
		bos.setPkOrdset(pid);
		DataBaseHelper.deleteBeanByPk(bos);
		
		sql = "select * from BD_ORD_SET_DT d where d.PK_ORDSET='"+pid+"'";
		DataBaseHelper.deleteBeanByWhere(new BdOrdSetDt(), " PK_ORDSET='"+pid+"'");
	}	
	

	/**
	 * 保存医嘱模板
	 * trans code 004004003021
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	public void saveBdordSet(String param,IUser user) {
		BdOrdSet bos = JsonUtil.readValue(param, BdOrdSet.class);
		User u = (User)user;
		
		String sql = "select * from BD_ORD_SET where (CODE='"+bos.getCode()+"' or NAME='"+bos.getName()+"') ";
		if("1".equals(bos.getEuRight())) sql += " and pk_dept='"+bos.getPkDept()+"' ";
		if("2".equals(bos.getEuRight())) sql += " and PK_EMP='"+bos.getPkEmp()+"' ";
		if(!StringUtils.isEmpty(bos.getPkOrdset())) sql +=" and pk_ordset!='"+bos.getPkOrdset()+"' ";
		sql += "and flag_ip = '1'";
		
		List ll = DataBaseHelper.queryForList(sql);
		if(ll.size() > 0)throw new BusException("编码或者名称重复");
		
		if(StringUtils.isEmpty(bos.getPkOrdset())){
			bos.setCreateTime(new Date());
			bos.setCreator(u.getPkEmp());
			//bos.setEuOrdType("1"); //0不限
			DataBaseHelper.insertBean(bos);
		}else{
			bos.setModifier(u.getPkEmp());	
			int cnt = DataBaseHelper.updateBeanByPk(bos);
			if(cnt != 1)throw new BusException("当前医嘱模板已经不是最新的数据，请刷新后再修改！！！");
		}		
	}


	public void copyTemp(String param,IUser user) throws InvocationTargetException, IllegalAccessException {
		BdTempVo vo = JsonUtil.readValue(param,BdTempVo.class);
		User u=(User) user;
		if(vo.getTmpSet()==null){
			throw new BusException("请选择当前要复制的模板！");
		}
		if(vo.getListRight()==null || vo.getListRight().size()==0){
			throw new BusException("请选择当前要复制成为的模板类型！");
		}
		List<BdOrdSet> ordlist=new ArrayList<BdOrdSet>();
		List<BdOrdSetDt> ordDtlist=new ArrayList<BdOrdSetDt>();
		/**查询模板明细**/
		List<BdOrdSetDt> tempDt = DataBaseHelper.queryForList(" select * from BD_ORD_SET_DT where PK_ORDSET='"+vo.getTmpSet().getPkOrdset()+"'", BdOrdSetDt.class);

		for(String item : vo.getListRight()){
			//重新给值
			BdOrdSet newVo=new BdOrdSet();
			BeanUtils.copyProperties(newVo,vo.getTmpSet());
			newVo.setPkOrdset(NHISUUID.getKeyId());
			newVo.setEuRight(item);
			newVo.setCreateTime(new Date());
			newVo.setCreator(u.getPkEmp());
			newVo.setPkEmp(u.getPkEmp());
			newVo.setPkDept(u.getPkDept());
			ordlist.add(newVo);

			if(tempDt!=null && tempDt.size()>0){
				for (BdOrdSetDt dt : tempDt){
					BdOrdSetDt newDt=new BdOrdSetDt();
					BeanUtils.copyProperties(newDt,dt);
					newDt.setPkOrdsetdt(NHISUUID.getKeyId());
					newDt.setPkOrdset(newVo.getPkOrdset());
					newDt.setCreateTime(new Date());
					newDt.setCreator(u.getPkEmp());
					ordDtlist.add(newDt);
				}
			}
		}

		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdOrdSet.class),ordlist);
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdOrdSetDt.class),ordDtlist);


	}

}
