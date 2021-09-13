package com.zebone.nhis.ex.pub.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.pub.support.wf.GetOrdWfExWeekHandler;
import com.zebone.nhis.ex.pub.vo.BdWfOrdArguDeptVo;
import com.zebone.nhis.ex.pub.vo.BdWfOrdArguVo;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 计算医嘱流向服务
 * @author yangxue
 *
 */
@Service
public class QueryOrdWfService {

	/**
	 * 确定医嘱流向前台对外服务方法
	 * @param param{pkOrg,euPvtype,ordrecur,pkSupplycate,orderType,weeknos}
	 * @param user
	 * @return
	 */
	public BdWfOrdArguDeptVo getExDept(String param,IUser user){
		BdWfOrdArguVo vo = JsonUtil.readValue(param, BdWfOrdArguVo.class);
		if(vo == null) throw new BusException("未获取到确定流向需要的参数！");
		return this.exce(vo);
	}
	/**
	 * 后台根据流向参数计算医嘱服务执行科室，(医嘱流向算法)
	 * @param vo 传入属性{pkOrg,euPvtype,ordrecur,pkSupplycate,orderType,weeknos}
	 * @return 无值，返回null
	 * @throws BusException
	 */
	public BdWfOrdArguDeptVo exce(BdWfOrdArguVo vo)throws BusException{
		String sql = "select argu.pk_org_exec, argu.pk_dept, argu.pk_supplycate , argu.weeknos, argu.order_type,argu.pk_wfargu "+ 
          "  from bd_wf wf  inner join bd_wf_ord_argu argu   on wf.pk_wf = argu.pk_wf "+
          " where  argu.pk_org = ? and argu.eu_pvtype = ?  and (argu.ordrecur = ? or argu.ordrecur is null) ";
		
		String recur = vo.getOrdrecur();
		if(null == recur || "".equals(recur)){
			recur = "1";//临时医嘱
		}
		List<String> param = new ArrayList<String>();
		param.add(vo.getPkOrg());
		param.add(vo.getEuPvtype());
		param.add(recur);
		if(!CommonUtils.isEmptyString(vo.getOrderType())){
			sql = sql +" and (argu.order_type = ? or argu.order_type is null) ";
			param.add(vo.getOrderType());
		}
		List<Map<String, Object>> list = DataBaseHelper.queryForList(sql, param.toArray(new String[0]));
		if(list == null){
			return null;
		}
		//先根据星期过滤
		Map<String,Object> map = new GetOrdWfExWeekHandler().exce(vo, list);
		if(null == map)
			return null;
		String pk_wfargu = CommonUtils.getString(map.get("pkWfargu"));
		return getExecDept(pk_wfargu);
	}
	
	/**
	 * 根据医嘱流向主表，获取字表记录
	 * @param pk_wfargu
	 * @return 无值，返回null
	 * @throws BusException
	 */
	private BdWfOrdArguDeptVo getExecDept(String pk_wfargu) {
		if(null == pk_wfargu || "".equals(pk_wfargu))
			return null;
		String sql = " select argu.* ,org.name_org as name_org_exec,dept.name_dept,org.code_org as code_org_exec,"
				+ "dept.code_dept  from bd_wf_ord_argu_dept argu "
				+" inner join bd_ou_org org on org.pk_org = argu.pk_org_exec "
				+" inner join bd_ou_dept dept on dept.pk_dept = argu.pk_dept "
				+" where argu.pk_wfargu = ?  and argu.flag_maj='1' ";
		List<BdWfOrdArguDeptVo> list = (List<BdWfOrdArguDeptVo>) DataBaseHelper.queryForList(sql, BdWfOrdArguDeptVo.class, pk_wfargu);
		if(null == list || list.size() < 1)
			return null;
		return list.get(0);
	}
	/**
	 * 过滤星期值，规则：
	 * 1.如果医嘱流向VO维护为空，表示表示全部支持，
	 * 2.如果传入参数为空，默认当天星期		
	 * @param week
	 * @param weeknos
	 * @return
	 */
	private boolean isWeekInclude(String week,String weeknos){
		if(weeknos == null || "".equals(weeknos))
			return true;
		if(null == week || "".equals(week)){
			week = DateUtils.getDayNumOfWeek(new Date())+"";
		}
		if(weeknos.contains(week))
			return true;
		return false;
	}
}