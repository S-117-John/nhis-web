package com.zebone.nhis.ex.nis.ns.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.transcode.SysServiceRegister;
import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOccPrt;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.nis.ns.dao.ExecListPrintMapper;
import com.zebone.nhis.ex.nis.ns.support.ExlistPrintSortByOrdUtil;
import com.zebone.nhis.ex.nis.ns.vo.OrdListVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 医嘱打印
 * @author yangxue
 *
 */
@Service
public class OrderExecListPrintService {
	@Resource
	private ExecListPrintMapper execListPrintMapper;
    /**
     * 
     * @param param
     * {
     * type:执行单类型（0：变更单，1：护理，2：口服，3：注射，4：输液，5：饮食，6：治疗，7：其他）
        date:查询日期
        time:查询班次
        euAlways:0长期，1临时（其他情况不传）
        flagPrint:0未打印，1已打印
        pkPvs:就诊主键
        pkDeptNs:病区主键
        }
     * @param user
     * @return
     */
	public List<Map<String,Object>> queryExlist(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		List<String> pvlist = (List)map.get("pkPvs");
		if(map == null || map.get("date")==null ||map.get("type")==null)
				throw new BusException("未获取到查询条件");
    	if(pvlist == null || pvlist.size()<=0)
    		 throw new BusException("未获取到患者就诊信息！");
		
		if(map.get("time")!=null){
			convertTime(CommonUtils.getString(map.get("date")),qryBanCiInfo(CommonUtils.getString(map.get("time"))),map);
		}
		String type = CommonUtils.getString(map.get("type"));
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		
		switch (type)
		{
		case "0":
			list = execListPrintMapper.queryChangedOrderList(map);
			break;
		case "1":
			list = execListPrintMapper.queryNsExlistList(map);
			break;
		case "2":
			list = execListPrintMapper.queryKFExlistList(map);
			break;
		case "3":
			list = execListPrintMapper.queryZSExlistList(map);
			break;
		case "4":
			list = execListPrintMapper.querySYExlistList(map);
			break;
		case "5":
			list = execListPrintMapper.queryYSExlistList(map);
			break;
		case "6":
			list = execListPrintMapper.queryZLExlistList(map);
			break;
/*原其他页签		case "7":
			list = execListPrintMapper.queryQTExlistList(map);
			break;*/
		case "7":
			list=execListPrintMapper.queryJCExlistList(map);
			break;
		case "8":
			list = execListPrintMapper.queryJYExlistList(map);
			break;
			/*	原 自定义模板打印  改为动态sql 以下暂不使用
			 * case "9001": 
		list=execListPrintMapper.queryCQYPExlistList(map);
			break;
		case "9002":
			list=execListPrintMapper.queryLSYPExlistList(map);
			break;
		case "9003":
			list=execListPrintMapper.queryPSExlistList(map);
			break;
		case "9004":
			list=execListPrintMapper.queryZSZExlistList(map);
			break;
		case "9006":
			list=execListPrintMapper.queryWHExlistList(map);
			break;
		case "9007":
			list=execListPrintMapper.queryHZExlistList(map);
			break;*/
		}
		if(list!=null&&list.size()>0){
			new ExlistPrintSortByOrdUtil().ordGroup(list);
		}
		return list;
	}
	//查询班次对应的时间区间 
	private Map<String,Object> qryBanCiInfo(String code_dateslot) {
		Map<String,Object> map=new HashMap<String,Object>();
		if("0200".equals(code_dateslot)){
			map.put("begint", "00:00:00");
			map.put("endt", "23:59:59");
			return map;
		}
		StringBuilder sql = new StringBuilder("select slot.time_begin as begint,slot.time_end as endt from bd_defdoc doc  inner join bd_defdoclist lst on lst.code=doc.code_defdoclist");
		sql.append(" inner join bd_code_dateslot slot on slot.dt_dateslottype=doc.code where lst.code ='020005' and doc.code ='02' and slot.code_dateslot = '"+code_dateslot+"' ");
		map = DataBaseHelper.queryForMap(sql.toString(), new HashMap<String,Object>());
		return map;
	}
	/**
	 * 转换查询日期
	 * @param date
	 * @param time
	 * @param paramMap
	 * @return
	 */
	private void convertTime(String date,Map<String,Object> time,Map<String,Object> paramMap){
		String begin = CommonUtils.getString(time.get("begint"));
		String beginTime = begin == null?"000000":begin.replaceAll(":", "");
		String end = CommonUtils.getString(time.get("endt"));
		String endTime = end == null?"595959":end.replaceAll(":", "");	
		paramMap.put("dateBegin", date+beginTime);
		paramMap.put("dateEnd", date+endTime);
	}
	
	/**
	 * 保存执行单打印信息
	 * @param param
	 * @param user
	 */
	public void savePrtList(String param,IUser user){
		List<ExOrderOccPrt> list = JsonUtil.readValue(param,new TypeReference<List<ExOrderOccPrt>>(){});
		if(list == null || list.size() < 0)
			throw new BusException("未获取到打印内容");
		for(ExOrderOccPrt prtvo : list){
			ExOrderOccPrt vo = DataBaseHelper.queryForBean("select * from ex_order_occ_prt where pk_exocc = ? ", ExOrderOccPrt.class, new Object[]{prtvo.getPkExocc()});
			if(vo == null){
				ApplicationUtils.setBeanComProperty(prtvo, true);
				DataBaseHelper.insertBean(prtvo);
			}else{
				DataBaseHelper.update("update ex_order_occ_prt set pk_dept_prt = ?,pk_dept_ns_prt = ?,pk_emp_prt = ?,name_emp_prt = ?,eu_expttype = ?,ts = to_date('"+DateUtils.getDefaultDateFormat().format(new Date())+"','YYYYMMDDHH24MISS') where pk_exocc = ? ", new Object[]{prtvo.getPkDeptPrt(),
								prtvo.getPkDeptNsPrt(),prtvo.getPkEmpPrt(),prtvo.getNameEmpPrt(),prtvo.getEuExpttype(),prtvo.getPkExocc()});
			}
		}
	}
	
	public void updateOrdList(String param,IUser user){
		List<OrdListVo> list=JsonUtil.readValue(param,new TypeReference<List<OrdListVo>>(){});
		for (OrdListVo ordListVo : list) {
			if(!CommonUtils.isEmptyString(ordListVo.getPkExocc()))
			execListPrintMapper.updateOrdList(ordListVo);
		}
	}

	/**
	 * 保存执行单打印信息
	 * @param param
	 * @param user
	 */
	public void savePrtListBySyx(String param,IUser user){
		List<ExOrderOccPrt> list = JsonUtil.readValue(param,new TypeReference<List<ExOrderOccPrt>>(){});
		if(list == null || list.size() < 0)
			throw new BusException("未获取到打印内容");
		List<ExOrderOccPrt> addList = new ArrayList<ExOrderOccPrt>();

		Set<String> pkExoccs = new HashSet<>();
		Set<String> dtExcardtypes = new HashSet<>();
		for (ExOrderOccPrt prtvo : list) {
			if(!CommonUtils.isEmptyString(prtvo.getPkExocc())){
				pkExoccs.add(prtvo.getPkExocc());
			}
			dtExcardtypes.add(prtvo.getDtExcardtype());
		}
		String sql = "select pk_exocc from ex_order_occ_prt where ( pk_exocc  in ("
				+ CommonUtils.convertSetToSqlInPart(pkExoccs, "pk_exocc") + ") ) and dt_excardtype in ("
				+ CommonUtils.convertSetToSqlInPart(dtExcardtypes, "dt_excardtype") + ")";
		List<String> pkExoccList = DataBaseHelper.getJdbcTemplate().queryForList(sql, String.class);

		for(ExOrderOccPrt prtvo : list){
			if(pkExoccList != null && !pkExoccList.contains(prtvo.getPkExocc())){
				if(!CommonUtils.isEmptyString(prtvo.getPkExoccprt())){
					continue;
				}
				prtvo.setPkExoccprt(NHISUUID.getKeyId());
				ApplicationUtils.setBeanComProperty(prtvo, true);
				addList.add(prtvo);
			}

			//原逻辑：判断是否打印，未打印插入，已打印更新
//			ExOrderOccPrt vo = DataBaseHelper.queryForBean("select * from ex_order_occ_prt where pk_exocc = ? "
//				+ "and dt_excardtype = ?",ExOrderOccPrt.class, new Object[]{prtvo.getPkExocc(),prtvo.getDtExcardtype()});
//			if(vo == null){
//				ApplicationUtils.setBeanComProperty(prtvo, true);
//				DataBaseHelper.insertBean(prtvo);
//			}else{
//				prtvo.setTs(vo.getTs());
//				prtvo.setPkExoccprt(vo.getPkExoccprt());
//				DataBaseHelper.updateBeanByPk(prtvo);
//			}
		}
		//2019-04-24 现逻辑：新增的才作插入
		if(addList.size() > 0)
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExOrderOccPrt.class), addList);//批量插入
	}


	/**
	 * 获取科室自定义打印模板
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String,Object> queryPrintTemplate(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		Map<String,Object> rtnMap = execListPrintMapper.queryPrintTemplate(map);
		return rtnMap;
	}
}
