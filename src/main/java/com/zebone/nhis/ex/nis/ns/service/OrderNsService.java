package com.zebone.nhis.ex.nis.ns.service;

import com.zebone.nhis.cn.ipdw.vo.CnOrderVO;
import com.zebone.nhis.cn.pub.service.BdSnService;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.pv.PvAdt;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.nis.ns.dao.OrderMapper;
import com.zebone.nhis.ex.nis.ns.support.OrderCheckSortByOrdUtil;
import com.zebone.nhis.ex.nis.ns.vo.OrderCheckVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.DatabaseMetaData;
import java.text.ParseException;
import java.util.*;
/**
 * 护理医嘱处理服务
 * @author yangxue
 *
 */
@Service
public class OrderNsService {
	@Autowired
	private BdSnService snService ;
	@Resource
	private OrderMapper orderMapper;
	
	
	/**
     * 根据条件查询护理医嘱
     * @param param{pkPv,euAlways(0:长期，1：临时，9：全部),flagCk(0:未核对,1:已核对,9:全部),sortno（0：大人，其他数字为实际选择婴儿序号）}
     * @param user
     * @return
     */
    public List<OrderCheckVo> queryOrderNsByCon(String param,IUser user){
    	Map<String,Object> paramMap= JsonUtil.readValue(param, Map.class);
    	if(paramMap == null||paramMap.get("pkPv")==null||paramMap.get("pkPv").equals("")){
    		throw new BusException("请先选择患者！");
    	}
    	String pk_dept_cur = ((User)user).getPkDept();
    	String euAlways = CommonUtils.getString(paramMap.get("euAlways"));
    	String sortno = CommonUtils.getString(paramMap.get("sortno"));
    	String pkPv = CommonUtils.getString(paramMap.get("pkPv"));
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("pkPv",pkPv);
    	map.put("pkDeptNs", pk_dept_cur);
    	if(euAlways!=null&&!euAlways.equals("9")){//不是全部的情况
    		map.put("euAlways",euAlways);
    	}
    	map.put("flagCk", paramMap.get("flagCk"));
    	map.put("sortno",CommonUtils.getInteger(sortno));
    	List<OrderCheckVo> ordlist = new ArrayList<OrderCheckVo>();
  		ordlist =  orderMapper.queryOrderNsList(map);
  		if(ordlist!=null&&ordlist.size()>0){   		 
   		 new OrderCheckSortByOrdUtil().ordGroup(ordlist);
   	    } 
    	return ordlist;
    }
   
    /**
     * 删除护嘱信息（只有开立状态的允许删除）
     * @param paramMap
     */
    public void deleteOrderNsByPK(String param,IUser user){
    	List<String> list= JsonUtil.readValue(param, ArrayList.class);
    	String inStr = "";
    	if(list!=null&&list.size()>0){
    		for(int i=0;i<list.size();i++){
    			inStr += "'"+list.get(i)+"',";
    		}
			DataBaseHelper.execute("delete from bl_ip_dt where pk_cnord in ("+inStr.substring(0, inStr.length()-1)+")",null);
    		DataBaseHelper.execute("delete from cn_order where pk_cnord in ("+inStr.substring(0, inStr.length()-1)+")",null);
    	}
    }
    /**
     * 保存护嘱信息
     * @param param{保存实体数组}
     * @param user
     * @throws ParseException 
     */
    public void saveOrderNsVo(String param, IUser user){
    	List<CnOrderVO> paramMap = JsonUtil.readValue(param,new TypeReference<List<CnOrderVO>>(){});
    	if(paramMap == null ||paramMap.size()<=0) return ;
		if("0".equals(paramMap.get(0).getFlagFunDept())){
			List<PvAdt> pvAdts = DataBaseHelper.queryForList("select * from pv_adt where pk_pv=? and (date_end is null or flag_dis='1')", PvAdt.class, new Object[]{paramMap.get(0).getPkPv()});
			// 由于病区转产房不算转科，所以产房需要单独判断
			Map<String, Object> map = DataBaseHelper.queryForMap("select dt_depttype from bd_ou_dept where pk_dept =?", UserContext.getUser().getPkDept());
			if (pvAdts != null && pvAdts.size() > 0) {
				if ("1".equals(pvAdts.get(0).getFlagDis())) {
					throw new BusException("该患者已批出院，无法保存护嘱！请确认患者状态！");
				} else if (!map.get("dtDepttype").equals("0310") && !UserContext.getUser().getPkDept().equals(pvAdts.get(0).getPkDeptNs())) {
					throw new BusException("该患者已批转科，无法保存护嘱！请确认患者状态！");
				}
			}
		}

		List<CnOrder> cnOrderNs = new ArrayList<>();
		for(CnOrderVO cnOrd : paramMap){
			CnOrder ord = new CnOrder();
			ApplicationUtils.copyProperties(ord, cnOrd);
			cnOrderNs.add(ord);
		}
		Map<String,List<CnOrder>> ordMap = convertCheckOrd(cnOrderNs,(User)user);
    	if(ordMap.get("insert")!=null){
    		//保存护嘱
        	DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnOrder.class), ordMap.get("insert"));
    	}
    	if(ordMap.get("update")!=null){
    		//循环更新护嘱，批量的方式调用太难用
        	for(CnOrder ord : ordMap.get("update")){
        		DataBaseHelper.updateBeanByPk(ord,false);
        	}
    	}
    }
    public CnOrder saveCheckOrdNsVo(OrderCheckVo chkvo,User u){
    	CnOrder ord = new CnOrder();
		ApplicationUtils.copyProperties(ord, chkvo);
		ord.setPkEmpInput(u.getPkEmp());
		ord.setPkEmpOrd(u.getPkEmp());
		ord.setNameEmpInput(u.getNameEmp());
		ord.setNameEmpOrd(u.getNameEmp());
		ord.setOrdsn(snService.getSerialNo("cn_order","ordsn", 1, u));
		if(ord.getOrdsnParent() == null||ord.getOrdsnParent() == 0){
			ord.setOrdsnParent(ord.getOrdsn());
		}
		DataBaseHelper.insertBean(ord);
		return ord;
    }
    /**
     * 转换前台传过来的护嘱为医嘱及频次计划实体
     * @param param
     * @return
     */
    private Map<String,List<CnOrder>> convertCheckOrd(List<CnOrder> param,User u){
    	List<CnOrder> insertList = new ArrayList<CnOrder>();
    	List<CnOrder> updateList = new ArrayList<CnOrder>();
		PvEncounter pvvo = DataBaseHelper.queryForBean(
				"select * from pv_encounter where pk_pv = ?",
				PvEncounter.class, param.get(0).getPkPv());
    	////校验主医嘱是否被作废或取消签署
    	Set<Integer> parentOrdSet = new HashSet<Integer>();
    	for(CnOrder ord : param){
    		if(ord.getPkCnord()!=null&&!"".equals(ord.getPkCnord())){
    			//更新
    			updateList.add(ord);
    		}else{
	    		String pk_cnord = NHISUUID.getKeyId();
	    		ord.setPkCnord(pk_cnord);
	    		ord.setDateSign(new Date());
	    		ord.setEuPvtype("3");
	    		ord.setEuStatusOrd(CommonUtils.isEmptyString(ord.getEuStatusOrd()) ? "1" : ord.getEuStatusOrd());
	    		ord.setFlagBase("0");
//	    		ord.setFlagBl("1");
	    		ord.setFlagCp("0");
	    		ord.setFlagDoctor("0");
	    		ord.setFlagDurg("0");
	    		ord.setFlagEmer("0");
	    		ord.setFlagSign("1");
	    		ord.setDateSign(new Date());    		
	    		ord.setNameEmpInput(u.getNameEmp());
	    		ord.setNameEmpChk(u.getNameEmp());
	    		ord.setNameEmpOrd(pvvo.getNameEmpPhy());
	    		ord.setDateEffe(new Date());
	    		ord.setDateEntry(new Date());
	    		ord.setNoteOrd(ord.getNoteOrd());
//	    		if(CommonUtils.isEmptyString(ord.getDescOrd()))
//	    		   ord.setDescOrd(ord.getNameOrd());
	    		if(CommonUtils.isEmptyString(ord.getPkUnitCg()))
	    		   ord.setPkUnitCg(ord.getPkUnit());
	    		if(CommonUtils.isEmptyString(ord.getPkUnitDos()))
	    		   ord.setPkUnitDos(ord.getPkUnitDos());
	    		ord.setQuanCg(ord.getQuan());
	    		ord.setDays(1L);
	    		ord.setOrds(1L);
	    		ord.setOrdsn(snService.getSerialNo("cn_order","ordsn", 1, u));
	    		if(ord.getOrdsnParent() == null||ord.getOrdsnParent() == 0){
	    			ord.setOrdsnParent(ord.getOrdsn());
	    		}else{
	    			parentOrdSet.add(ord.getOrdsnParent());
	    		}
	    		//开立科室与开立病区，前台传入
	    		//if(CommonUtils.isEmptyString(ord.getPkDept())){
	    		//	ord.setPkDept(ord.getPkDeptExec());//患者当前主医嘱对应的科室
	    		//}
	    		//ord.setPkDeptNs(ord.getPkDeptExec());
	    		//ord.setPriceCg(ord.getPriceCg());
	    		ord.setPkEmpInput(u.getPkEmp());
	    		ord.setPkEmpChk(u.getPkEmp());
	    		ord.setPkEmpOrd(pvvo.getPkEmpPhy());
	    		ord.setPkOrg(u.getPkOrg());
	    		ord.setPkOrgExec(u.getPkOrg());
	    		ord.setCreateTime(new Date());
	    		ord.setCreator(u.getPkEmp());
	    		ord.setTs(new Date());
				ord.setPkWg(pvvo.getPkWg());
				ord.setPkWgOrg(pvvo.getPkWgOrg());
	    		insertList.add(ord);
    		}
    	}
    	if(parentOrdSet!=null&&parentOrdSet.size()>0){
    		Map<String,Object> paramMap = new HashMap<String,Object>();
    		paramMap.put("parentList", parentOrdSet);
    		paramMap.put("flagDoctor", "1");
    		paramMap.put("euStatusOrd", "'1','2','3'");
    		//校验医嘱是否被取消签署或作废
    		List<CnOrder> ordlist = orderMapper.queryOrdByParent(paramMap);
    		if(ordlist!=null&&ordlist.size()<parentOrdSet.size())
    			throw new BusException("待核对列表中的医嘱可能已被取消签署，请刷新后重新添加！");
    	}
    	Map<String,List<CnOrder>> map = new HashMap<String,List<CnOrder>>();
    	if(insertList.size()>0){
    		map.put("insert", insertList);
    	}
    	if(updateList.size()>0){
    		map.put("update", updateList);
    	}
    	return map;
    }
    
    /**
   	 * 根据医嘱主键更新医嘱状态（作废、停止、核对护嘱）
   	 * @param param
   	 * @param user
   	 * @return
   	 */
       public void updateOrdNsByPk(Map<String,Object> paramMap, User user){
       	List<String> pkOrdsList = (List<String>)paramMap.get("pkOrds");
       	if(pkOrdsList==null||pkOrdsList.size()<=0) return;
       	//根据医嘱主键更新医嘱信息及状态
       	Date now = new Date();
       	String nameEmp = user.getNameEmp();
       	String pkEmp = user.getPkEmp();
       	//根据医嘱操作类型区分是哪一种操作
       	String operType = paramMap.get("operType").toString();
       	if(operType.equals("1")){//核对
       		paramMap.put("pkEmpChk", pkEmp);
       		paramMap.put("nameEmpChk",nameEmp);
       		paramMap.put("dateChk", now);
       		paramMap.put("euStatus", "2");
       	}else if(operType.equals("2")){//停止
       		paramMap.put("dateStop", now);
       		paramMap.put("pkEmpStop",pkEmp);
       		paramMap.put("nameEmpStop",nameEmp);
       		paramMap.put("flagStop", "1");
       		paramMap.put("dateStopChk",now);
       		paramMap.put("pkEmpStopChk", pkEmp);
       		paramMap.put("nameEmpStopChk", nameEmp);
       		paramMap.put("flagStopChk","1");
       		paramMap.put("euStatus", "4");
       	}else if(operType.equals("3")){//作废
       		paramMap.put("dateErase", now);
       		paramMap.put("pkEmpErase",pkEmp);
       		paramMap.put("nameEmpErase",nameEmp);
       		paramMap.put("flagErase", "1");
       		paramMap.put("dateEraseChk",now);
       		paramMap.put("pkEmpEraseChk", pkEmp);
       		paramMap.put("nameEraseChk", nameEmp);
       		paramMap.put("flagEraseChk","1");
       		paramMap.put("euStatus", "9");
       	}
       	orderMapper.updateOrdNsInfo(paramMap);
       }
       
       /**
        * 交易号：005002002103
        * 保存患者床位备注信息
        *
        * @param param
        * @param user
        * @return
        */
       public int saveBedMemo(String param, IUser user) {
           String pkBed = JsonUtil.getFieldValue(param, "pkBed");
           String memo = JsonUtil.getFieldValue(param, "memo");

           return DataBaseHelper.update("UPDATE bd_res_bed SET name_place = ? where pk_bed = ?", new Object[] {memo, pkBed}); 
       }		 
      
}
