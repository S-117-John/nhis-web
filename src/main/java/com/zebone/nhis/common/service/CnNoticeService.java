package com.zebone.nhis.common.service;

import com.zebone.nhis.cn.ipdw.vo.ApplyCanlParam;
import com.zebone.nhis.cn.ipdw.vo.OrderParam;
import com.zebone.nhis.common.dao.CnNoticeMapper;
import com.zebone.nhis.common.handler.CnNoticeHandler;
import com.zebone.nhis.common.module.cn.ipdw.CnNotice;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.ex.vo.OrderCheckVO;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 临床消息提醒
 * @author yangxue
 *
 */
@Service
public class CnNoticeService {
   
	@Autowired
	private CnNoticeMapper cnNoticeMapper;
	@Autowired
	private CnNoticeHandler cnNoticeHandler;

	/**
	 * 查询未处理消息数量
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryCnNoticeCnt(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(cnNoticeHandler.useCache()){
			return cnNoticeHandler.queryCnNoticeCnt(paramMap);
		}
		return cnNoticeMapper.queryCnNoticeCnt(paramMap);
	}

	/**
	 * 将通知数据存入缓存中
	 * @param param
	 * @param user
	 */
	public void flushCnNoticeCnt(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		User u = (User)user;
		paramMap.put("pkOrg", u.getPkOrg());
		if(cnNoticeHandler.useCache()){
			cnNoticeHandler.flush(paramMap);
		}
	}
	/**
	 * 取消执行，待作废的消息
	 * @param pkCns
	 */
	public void saveCnNoticeToBe(Set<String> pkCns,IUser user){
		if(pkCns==null ||pkCns.size()==0) {
			return;
		}
		User users = (User)user;
		//查询需要发送的医嘱主键
		List<Map<String,Object>> map = DataBaseHelper.queryForList("select pk_cnord from ex_order_occ "
				+ "where pk_cnord  in ("+ CommonUtils.convertSetToSqlInPart(pkCns, "pk_cnord") +" ) and DEL_FLAG = '0'  and eu_status != '9' group by pk_cnord ");
		if(map != null && map.size()>0){
			for(Map<String,Object> ma : map){
				pkCns.remove(ma.get("pkCnord"));
			}
		}
		if(pkCns==null ||pkCns.size()==0) {
			return;
		}
		List<OrderCheckVO> cnOrds = DataBaseHelper.queryForList("select ord.*,pv.name_pi from cn_order ord left join PV_ENCOUNTER pv on pv.pk_pv = ord.pk_pv  where ord.pk_cnord in ("+ CommonUtils.convertSetToSqlInPart(pkCns, "pk_cnord") +" ) ",OrderCheckVO.class);
		if(cnOrds==null ||cnOrds.size()==0) {
			return;
		}
		List<CnNotice> cnNoticeList = new ArrayList<>();
		for(OrderCheckVO cn : cnOrds){
			if(!"9".equals(cn.getEuStatusOrd())){
				CnNotice cnNotice = new CnNotice();
				cnNotice.setPkCnnotice(NHISUUID.getKeyId());
				cnNotice.setNamePi(cn.getNamePi());
				cnNotice.setPkOrg(cn.getPkOrg());
				cnNotice.setPkPv(cn.getPkPv());
				cnNotice.setNote(cn.getNameOrd());
				cnNotice.setPkDeptSend(users.getPkDept());
				cnNotice.setDeptSend(getDeptName(users.getPkDept()));
				cnNotice.setEmpSend(users.getNameEmp());
				cnNotice.setDateSend(new Date());
				cnNotice.setPkDeptRecp(cn.getPkDept());
				cnNotice.setDeptRecp(getDeptName(cn.getPkDept()));
				//新消息
				cnNotice.setEuStatus("0");
				//待作废
				cnNotice.setEuType("4");
				cnNotice.setPkCnorder(cn.getPkCnord());
				cnNotice.setCreateTime(new Date());
				cnNotice.setCreator(users.getPkEmp());
				cnNotice.setTs(new Date());
				cnNotice.setDelFlag("0");
				cnNoticeList.add(cnNotice);
			}
		}
		if(cnNoticeList.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnNotice.class), cnNoticeList);
		}
	}
	/**
	 * 检验作废通知
	 * @param param
	 */
	public CnNotice saveCnNoticeVoidForLis(String param){
		CnNotice cnNoticeDO = new CnNotice();
		try{
			ApplyCanlParam rcp = JsonUtil.readValue(param, ApplyCanlParam.class);
			List<String> pkCnOrds = rcp.getPkCnOrds();
			if(pkCnOrds.size()>0){
				List<CnOrder> cnOrds = DataBaseHelper.queryForList("select name_ord,pk_pv,pk_dept_ns,pk_cnord,ordsn from cn_order where pk_cnord=?",
						CnOrder.class, new Object[]{pkCnOrds.get(0)});
				if(cnOrds.size()>0){
					cnNoticeDO.setPkDeptRecp(cnOrds.get(0).getPkDeptNs());
                    cnNoticeDO.setPkCnorder(cnOrds.get(0).getPkCnord());
					cnNoticeDO.setEuStatus("0");
                    cnNoticeDO.setEuType("3");
					cnNoticeDO.setDelFlag("0");
					cnNoticeDO.setCntVoid(pkCnOrds.size());
					DataBaseHelper.insertBean(cnNoticeDO);
				}
			}
		}
		catch (Exception e){
			cnNoticeDO =null;
		}
		return cnNoticeDO;
	}

	/**
	 * 作废医嘱通知
	 * @param param
	 */
	public CnNotice saveCnNoticeVoid(String param){
		CnNotice cnNoticeDO = new CnNotice();
		try {
			OrderParam list = JsonUtil.readValue(param,OrderParam.class);
			List<CnOrder> changeOrdList = list.getChangeOrdList();
			if(changeOrdList.size()>0){
				cnNoticeDO.setPkPv(changeOrdList.get(0).getPkPv());
				//当前科室编码
				cnNoticeDO.setPkDeptSend(changeOrdList.get(0).getPkDept());
				//患者所在护理单元
				cnNoticeDO.setPkDeptRecp(changeOrdList.get(0).getPkDeptNs());
				//当前医生姓名
				cnNoticeDO.setEmpSend(changeOrdList.get(0).getNameEmpInput());
				//当前科室名称
				cnNoticeDO.setDeptSend(getDeptName(cnNoticeDO.getPkDeptSend()));
				//患者所在护理单元名称
				cnNoticeDO.setDeptRecp(getDeptName(cnNoticeDO.getPkDeptRecp()));
				//患者name_pi
				cnNoticeDO.setNamePi(getNamePi(changeOrdList.get(0).getPkPi()));
                cnNoticeDO.setPkCnorder(getNamePi(changeOrdList.get(0).getPkCnord()));
				//查询患者所在护理单元名称
				cnNoticeDO.setEuStatus("0");
				cnNoticeDO.setDelFlag("0");
				cnNoticeDO.setCntVoid(changeOrdList.size());
				cnNoticeDO.setEuType("3");
				DataBaseHelper.insertBean(cnNoticeDO);
			}

			for (CnOrder cnOrder : changeOrdList) {
				//删除对应医嘱提示类型为1、2、4的提示信息
				String sql = "delete from CN_NOTICE where pk_cnorder = ? and eu_type in ('1','2','4')";
				DataBaseHelper.execute(sql,new Object[]{cnOrder.getPkCnord()});
			}
		}
		catch (Exception e){
			cnNoticeDO =null;
		}

		return cnNoticeDO;
	}

	/**
	 * 保存临床提醒消息
	 */
	public void saveCnNotice(String param,boolean isStop){

		try{
			OrderParam list = JsonUtil.readValue(param,OrderParam.class);
			List<CnOrder> changeOrdList = list.getChangeOrdList();
			if("1".equals(list.getFlagCancleStop())){
				//	取消提交：删除对应医嘱提示类型为1的提示信息
				for (CnOrder cnOrder : changeOrdList) {
					String sql = "delete from CN_NOTICE where pk_cnorder = ? and eu_type = '1'";
					DataBaseHelper.execute(sql,new Object[]{cnOrder.getPkCnord()});
				}
				return;
			}
			//新停医嘱
			for (int i = 0; i < changeOrdList.size(); i++) {
				CnNotice cnNoticeDO = new CnNotice();
				cnNoticeDO.setPkPv(changeOrdList.get(i).getPkPv());
				//当前科室编码
				cnNoticeDO.setPkDeptSend(changeOrdList.get(i).getPkDept());
				//患者所在护理单元
				cnNoticeDO.setPkDeptRecp(changeOrdList.get(i).getPkDeptNs());
				//当前医生姓名
				cnNoticeDO.setEmpSend(changeOrdList.get(i).getNameEmpInput());
				//当前科室名称
				cnNoticeDO.setDeptSend(getDeptName(cnNoticeDO.getPkDeptSend()));
				//患者所在护理单元名称
				cnNoticeDO.setDeptRecp(getDeptName(cnNoticeDO.getPkDeptRecp()));
				//患者name_pi
				cnNoticeDO.setNamePi(getNamePi(changeOrdList.get(i).getPkPi()));
				//查询患者所在护理单元名称
				cnNoticeDO.setEuStatus("0");
				cnNoticeDO.setDelFlag("0");
				cnNoticeDO.setPkCnorder(changeOrdList.get(i).getPkCnord());

				//签署过的
				if("1".equals(changeOrdList.get(i).getEuStatusOrd())||"3".equals(changeOrdList.get(i).getEuStatusOrd())){

					if("1".equals(changeOrdList.get(i).getFlagStop())){
						cnNoticeDO.setCntEnd(1);
						cnNoticeDO.setEuType("2");
					}
					if("1".equals(changeOrdList.get(i).getFlagEmer())){
						cnNoticeDO.setCntEmer(1);
					}

				}else{//没有签署过的
					if(!isStop){
						cnNoticeDO.setCntNew(1);
						//记录新开立医嘱
						cnNoticeDO.setEuType("1");
						if("1".equals(changeOrdList.get(i).getFlagStop())){
							cnNoticeDO.setCntEnd(1);
							cnNoticeDO.setEuType("2");
						}
						if("1".equals(changeOrdList.get(i).getFlagEmer())){
							cnNoticeDO.setCntEmer(1);
						}
					}else{

					}
				}
				DataBaseHelper.insertBean(cnNoticeDO);
			}
		}
		catch (Exception e){

		}

	}


	/**
	 * 非医嘱签署
	 * @param
	 * @param
	 */
	public CnNotice saveCnNotice(List<CnOrder> ordList){
		try{
			//急医嘱数
			int flagEmerCount = 0;
			//停医嘱数
			int stopCount = 0;
			//新医嘱数
			int newCount = 0;
			if(ordList.size()>0&&ordList!=null){
				for (CnOrder item:ordList){
					CnNotice cnNoticeDO = new CnNotice();
					//医嘱主键
					String pkCnOrd = item.getPkCnord();
					Map<String,Object> result = getCnOrder(pkCnOrd);
					cnNoticeDO.setPkCnorder(pkCnOrd);
					if(result!=null){
						if(result.containsKey("pkPv")&&result.get("pkPv")!=null){
							cnNoticeDO.setPkPv(result.get("pkPv").toString());
						}
						if(result.containsKey("pkPi")&&result.get("pkPi")!=null){
							cnNoticeDO.setNamePi(getNamePi(result.get("pkPi").toString()));
						}
					}
					Map<String,Object> resultDept = getDept(cnNoticeDO.getPkPv());
					if(resultDept!=null){
						if(resultDept.containsKey("pkDept")&&resultDept.get("pkDept")!=null){
							cnNoticeDO.setPkDeptSend(resultDept.get("pkDept").toString());
						}
						if(resultDept.containsKey("pkDeptNs")&&resultDept.get("pkDeptNs")!=null){
							cnNoticeDO.setPkDeptRecp(resultDept.get("pkDeptNs").toString());
						}
						//当前科室名称
						cnNoticeDO.setDeptSend(getDeptName(cnNoticeDO.getPkDeptSend()));
						//患者所在护理单元名称
						cnNoticeDO.setDeptRecp(getDeptName(cnNoticeDO.getPkDeptRecp()));

					}
					//当前医生姓名
					cnNoticeDO.setEmpSend(item.getNameEmpInput());
					//新停医嘱
					//签署过的
					if("1".equals(item.getEuStatusOrd())){
						newCount++;
						if("1".equals(item.getFlagStop())){
							stopCount++;
						}
						if("1".equals(item.getFlagEmer())){
							flagEmerCount++;
						}
					}else{//没有签署过的
						newCount++;
						if("1".equals(item.getFlagStop())){
							stopCount++;
						}
						if("1".equals(item.getFlagEmer())){
							flagEmerCount++;
						}
					}
					//查询患者所在护理单元名称
					cnNoticeDO.setCntNew(newCount);
					cnNoticeDO.setCntEmer(flagEmerCount);
					cnNoticeDO.setCntEnd(stopCount);
					cnNoticeDO.setEuStatus("0");
					cnNoticeDO.setEuType("1");
					cnNoticeDO.setDelFlag("0");
					DataBaseHelper.insertBean(cnNoticeDO);
				}
			}
		}
		catch (Exception e){

		}
		return null;
	}

	/**
	 * 查询患者namePi
	 * @param pkPi
	 * @return
	 */
	private String getNamePi(String pkPi) {
		String result = "";
		if(StringUtils.isEmpty(pkPi)){
			return result;
		}
		StringBuffer stringBuffer = new StringBuffer();
		//查询患者name_pi
		stringBuffer.append("select NAME_PI from PI_MASTER where PK_PI =");
		stringBuffer.append("'");
		stringBuffer.append(pkPi);
		stringBuffer.append("'");
		Map<String,Object> resultMap = DataBaseHelper.queryForMap(stringBuffer.toString());
		if(resultMap.containsKey("namePi")&&resultMap.get("namePi")!=null){
			//患者name_pi
			result = resultMap.get("namePi").toString();
		}
		return result;
	}

	/**
	 * 获取科室名称
	 * @param
	 * @return
	 */
	private String getDeptName(String pkDept) {
		String result = "";
		if(StringUtils.isEmpty(pkDept)){
			return result;
		}
		StringBuffer stringBuffer = new StringBuffer();
		//查询当前科室名称
		stringBuffer.append("select NAME_DEPT from BD_OU_DEPT where PK_DEPT =");
		stringBuffer.append("'");
		stringBuffer.append(pkDept);
		stringBuffer.append("'");
		Map<String,Object> resultMap = DataBaseHelper.queryForMap(stringBuffer.toString());
		if(resultMap.containsKey("nameDept")&&resultMap.get("nameDept")!=null){
			//当前科室名称
			result = resultMap.get("nameDept").toString();
		}
		return result;
	}

	/**
	 * 获取当前科室与病区pk
	 * @param pkPv
	 * @return
	 */
	public Map<String,Object> getDept(String pkPv){
		Map<String,Object> result = new HashMap<>(16);
		if(StringUtils.isEmpty(pkPv)){
			return result;
		}
		StringBuffer stringBuffer = new StringBuffer();
		//查询当前科室名称
		stringBuffer.append("select PK_DEPT,PK_DEPT_NS,PK_PI from PV_ENCOUNTER where PK_PV =");
		stringBuffer.append("'");
		stringBuffer.append(pkPv);
		stringBuffer.append("'");
		Map<String,Object> resultMap = DataBaseHelper.queryForMap(stringBuffer.toString());

		return resultMap;
	}

	/**
	 * 查询医嘱信息表
	 * @param pkCnOrd
	 * @return
	 */
	public Map<String,Object> getCnOrder(String pkCnOrd){
		Map<String,Object> result = new HashMap<>(16);
		if(StringUtils.isEmpty(pkCnOrd)){
			return result;
		}
		StringBuffer stringBuffer = new StringBuffer();
		//查询当前科室名称
		stringBuffer.append("select PK_PV,PK_PI from CN_ORDER where PK_CNORD =");
		stringBuffer.append("'");
		stringBuffer.append(pkCnOrd);
		stringBuffer.append("'");
		Map<String,Object> resultMap = DataBaseHelper.queryForMap(stringBuffer.toString());

		return resultMap;
	}

	/**
	 * 更新读取消息数量
	 * 001003001016
	 * @param param
	 * @param user
	 */
	public void updateCnNotice(String param,IUser user){
		
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		paramMap.put("curDate", DateUtils.getDateTimeStr(new Date()));
		paramMap.put("nameEmp", ((User)user).getNameEmp());
		paramMap.put("euStatus", "1");
		paramMap.put("flagRead", "1");
		cnNoticeMapper.updateCnNotice(paramMap);
	}
	/**
	 * 更新执行消息数量
	 * @param
	 * @param
	 */
	public Map<String,Object> updateChkCnNotice(Map<String,Object> paramMap){
		paramMap.put("euStatus", "2");
		paramMap.put("flagRead", "0");
		paramMap.put("dateChk", DateUtils.getDateTimeStr(new Date()));
		cnNoticeMapper.updateCnNotice(paramMap);
		return paramMap;
	}
	
	/**
	 * 查询未处理消息所对应就诊信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<String> queryUnHandleCnNoticePvs(String param,IUser user){
		Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
		if(paramMap==null||CommonUtils.isNull(paramMap.get("pkDept"))) {
			return null;
		}
		List<CnNotice> noticeList = cnNoticeMapper.queryCnNotice(paramMap);
		if(noticeList==null||noticeList.size()<=0) {
			return null;
		}
		List<String> pvlist = new ArrayList<String>();
		for(CnNotice notice:noticeList){
			if(CommonUtils.isNotNull(notice.getPkPv())){
				 pvlist.add(notice.getPkPv());
			}
		}
		return pvlist;
	}

	public void cancelStop(List<CnOrder> list){
		try{
			for (CnOrder cnOrder : list) {
				String sql = "delete from CN_NOTICE where pk_cnorder = ? and eu_type = '2'";
				DataBaseHelper.execute(sql,new Object[]{cnOrder.getPkCnord()});
			}
		}catch (Exception e){

		}
	}


	/**
	 * 医嘱消息详情
	 * 004001005049
	 * @param param
	 * @param user
	 */
	public List<Map<String,Object>> noticeDetail(String param,IUser user){
		String pkDept = ((User)user).getPkDept();
		List<Map<String,Object>> result = cnNoticeMapper.noticeDetail(pkDept);
		return result;
	}

 }
