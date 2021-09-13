package com.zebone.nhis.labor.nis.service;

import com.zebone.nhis.common.module.labor.nis.PiLabor;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.labor.pub.dao.PiLaborPubMapper;
import com.zebone.nhis.labor.pub.service.PiLaborPubService;
import com.zebone.nhis.labor.pub.vo.PiLaborVo;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class PiLaborOptService {
	
	@Resource
	private PiLaborPubService PiLaborPubService;
	
	@Resource
    private PiLaborPubMapper piLaborPubMapper;
	
	/**
     * 分情况查询孕妇档案列表
     * @param param
     * @param user
     * @return
     */
    public List<PiLabor> queryPiLaborByCon(String param,IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	if(CommonUtils.isNull(paramMap.get("pkOrg"))){
    		paramMap.put("pkOrg", ((User)user).getPkOrg());
    	}
    	
    	// 添加<10月的校验 ,过滤出本次孕妇档案
//    	Calendar c = Calendar.getInstance();
//    	c.add(Calendar.MONTH, -10); // 目前时间  - 10个月    
//    	paramMap.put("createTime", c.getTime());
    	String dateStr = DateUtils.addDate(new Date(), -10, 2, "yyyyMMddHHmmss"); 
		paramMap.put("createTime", dateStr);
    	
    	return piLaborPubMapper.queryPiLabor(paramMap);
    }
	
	/**
     * 根据患者或孕妇档案主键查询患者信息
     * @param param
     * @param user
     * @return
     */
    public List<PiLabor> queryPiLaborByPk(String param,IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	
    	// 添加<10月的校验 ,过滤出本次孕妇档案
//    	Calendar c = Calendar.getInstance();
//    	c.add(Calendar.MONTH, -10); // 目前时间  - 10个月    
//    	paramMap.put("createTime", c.getTime());
    	String dateStr = DateUtils.addDate(new Date(), -10, 2, "yyyyMMddHHmmss"); 
		paramMap.put("createTime", dateStr);
    	//如果不存在孕妇档案信息，则查询患者基本信息
    	List<PiLabor> lablist = PiLaborPubService.queryPiLaborByPk(paramMap,user);
    	if(lablist==null||lablist.size()<=0)
    		lablist = piLaborPubMapper.queryPiInfo(paramMap);
    	return lablist;
    }
    
    /**
	 * 保存孕妇档案
	 * @param param
	 * @param user
	 */
	public void savePiLabor(String param,IUser user){
		PiLaborVo piLabor = JsonUtil.readValue(param, PiLaborVo.class);
		if(piLabor == null){
			throw new BusException("未获取到要保存的内容！");
		}
		PiLabor labor = new PiLabor();
		ApplicationUtils.copyProperties(labor, piLabor);
		Map<String,Object> verfyResult = new HashMap<String,Object>();
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("datePre", DateUtils.dateToStr("yyyyMMddHHmmss",piLabor.getDatePrebirth()));
		paramMap.put("pkPi", piLabor.getPkPi());
		if (CommonUtils.isEmptyString(piLabor.getPkPilabor())) {//新增
			
			//校验预产期是否有重复
			if(Application.isSqlServer()){				
				verfyResult = piLaborPubMapper.verfyPreBirthSqlServer(paramMap);
			}else{					
				
				verfyResult = piLaborPubMapper.verfyPreBirthOracle(paramMap);
			}
			if(verfyResult!=null&&verfyResult.get("mxdate")!=null){
				String preDate = DateUtils.getDateStr((Date)verfyResult.get("mxdate"));
				throw new BusException("该患者已经存在一条预产期为"+preDate+"的档案信息，预产期距离此日期10个月以内不允许再添加孕妇档案！");
			}
			String pk_pilabor = "";
			pk_pilabor = NHISUUID.getKeyId();
			labor.setPkPilabor(pk_pilabor);
			DataBaseHelper.insertBean(labor);
			updatePvLabor(pk_pilabor,piLabor.getPkPv());
		}else{//修改
			//校验预产期是否有重复，更新无需

			DataBaseHelper.updateBeanByPk(labor,false);
			/*if(Application.isSqlServer()){
				verfyResult = piLaborPubMapper.modifyverfyPreBirthSqlServer(paramMap);
			}else{					
				
				verfyResult = piLaborPubMapper.modifyverfyPreBirthOracle(paramMap);
			}
			if(verfyResult!=null&&verfyResult.get("mxdate")!=null){
				String preDate = DateUtils.getDateStr((Date)verfyResult.get("mxdate"));
				throw new BusException("该患者已经存在一条预产期为"+preDate+"的档案信息，预产期距离此日期10个月以内不允许再添加孕妇档案！");
			}else{

			}	*/
		
			
		}
	}
	/**
	 * 若存在产房就诊记录，则更新产房就诊记录中的pk_pilabor
	 * @param pk_pilabor
	 * @param pk_pi
	 */
	private void updatePvLabor(String pk_pilabor,String pk_pv){
		 if(CommonUtils.isEmptyString(pk_pv)) return;
		 DataBaseHelper.update("update pv_labor set pk_pilabor = ? where pk_pv = ?", new Object[]{pk_pilabor,pk_pv});
	}
	/**
	 * 删除孕妇档案
	 * @param param
	 * @param user
	 */
	public String delPiLabor(String param,IUser user){
		List<String> list= JsonUtil.readValue(param, ArrayList.class);
		User user1 = (User)user;
		String inStr = "";
		String resultStr = "";
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				List<Map<String,Object>> listpi = DataBaseHelper.queryForList("select pi.name_pi from pi_master pi inner join pi_labor lab on "
						+ " lab.pk_pi = pi.pk_pi inner join pv_labor pvlab on pvlab.pk_pilabor = lab.pk_pilabor "
						+ " inner join PV_LABOR_REC rec on rec.pk_pv = pvlab.pk_pv left join cn_order cn on cn.pk_pi = lab.pk_pi and cn.pk_dept = ? and cn.del_flag = '0'"
						+ " where lab.pk_pilabor = ? ", new Object[]{user1.getPkDept(), list.get(i)});
				if(listpi != null && listpi.size()>0){
					resultStr = resultStr +CommonUtils.getString(listpi.get(0).get("namePi"))+"、";
					continue;
				}
				inStr += "'" + list.get(i) + "',";
			}
			if(inStr!=null&&inStr.length()>1)
			DataBaseHelper.execute("delete from pi_labor where pk_pilabor in ("
					+ inStr.substring(0, inStr.length() - 1) + ")", null);
		}
		if(!CommonUtils.isEmptyString(resultStr)){
			resultStr = resultStr+"已存在产房分娩记录或医嘱信息，不允许被删除！";
		}
		return resultStr;
	}
}
