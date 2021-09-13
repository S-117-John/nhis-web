package com.zebone.nhis.ex.nis.pi.service;

import com.zebone.nhis.common.module.base.bd.res.BdResBed;
import com.zebone.nhis.ex.nis.pi.vo.PvIpVo;
import com.zebone.nhis.ex.pub.dao.ExPubMapper;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 取消入科业务处理类
 * @author yangxue
 *
 */
@Service
public class CancelDeptInService {
	@Resource
	private ExPubMapper exPubMapper;
	/**
	 * 更新取消入科后的相关业务数据
	 * @param pk_pv
	 * @param pk_dept_ns
	 * @param user
	 */
    public void updateDeptInData(String pk_pv,String pk_dept_ns,IUser user){
      //先取入科床位，就诊科室，为了后面更新用
	  User u = (User)user;
      Map<String,Object> result = DataBaseHelper.queryForMap("select bed_no,pk_dept from pv_encounter where pk_pv = ? ", new Object[]{pk_pv});
      if(result == null) return;
      String bedno = result.get("bedNo").toString();
      String pk_dept = result.get("pkDept").toString();
      
      //更新就诊记录
      DataBaseHelper.update("update pv_encounter set eu_status='0', flag_in = '0', date_admit = null,pk_wg = '',bed_no = '',pk_emp_phy = '',name_emp_phy = '',pk_emp_ns='',name_emp_ns='' where pk_pv = ?", new Object[]{pk_pv});
      
      //删除就诊科室记录
      DataBaseHelper.execute("delete from pv_adt where pk_pv = ? and pk_dept = ? and pk_dept_ns = ? ", new Object[]{pk_pv,pk_dept,pk_dept_ns});
      
      //删除就诊床位记录
      DataBaseHelper.execute("delete from pv_bed where pk_pv = ? and pk_dept = ? and pk_dept_ns = ?  and bedno = ? ", new Object[]{pk_pv,pk_dept,pk_dept_ns,bedno});
      
      //删除就诊医疗小组
      DataBaseHelper.execute("delete from pv_clinic_group where pk_pv = ? and pk_dept = ? and pk_dept_ns = ? ", new Object[]{pk_pv,pk_dept,pk_dept_ns});
      
      //删除就诊医护人员
      DataBaseHelper.execute("delete from pv_staff where pk_pv = ? and pk_dept = ? and pk_dept_ns = ? ", new Object[]{pk_pv,pk_dept,pk_dept_ns});
      
      //更新婴儿信息
      DataBaseHelper.update("update pv_infant set eu_status_adt='0',reason_adt = null,date_adt=null  where pk_pv_infant = ?", new Object[]{pk_pv});

	  BdResBed bdResBed = DataBaseHelper.queryForBean("select * from bd_res_bed where code = ?  and pk_org = ? and pk_ward = ?",BdResBed.class,bedno,u.getPkOrg(),u.getPkDept());
      //更新床位信息
		StringBuilder  bed_update = new StringBuilder("update bd_res_bed set pk_pi = null,flag_ocupy='0',pk_dept_used = null ,");
	    if(bdResBed != null && bdResBed.getDtBedtype().equals("09")){
		               bed_update.append("del_flag = '1',");
	    }
		               bed_update .append("eu_status = '01' where code = ?  and pk_org = ? and pk_ward = ?");
      DataBaseHelper.update(bed_update.toString(), new Object[]{bedno,u.getPkOrg(),u.getPkDept()});
      
      //删除--固定费用
      DataBaseHelper.execute("delete from pv_daycg_detail where exists (select 1 from pv_daycg where pv_daycg_detail.pk_daycg=pv_daycg.pk_daycg and pv_daycg.pk_pv = ?)",pk_pv);
      DataBaseHelper.execute("delete from pv_daycg where pk_pv = ?",new Object[]{pk_pv});
      
      //删除 -- 信用担保
      DataBaseHelper.execute("delete from pv_ip_acc where pk_pv = ?",new Object[]{pk_pv});

      //更新PV_IP表中婴儿陪护床位主键
	  DataBaseHelper.update("update pv_ip set pk_bed_an = null  where pk_pv = ?", new Object[]{pk_pv});

	  //如果该患者在本病区（产科）存在婴儿，患者取消入科时该病区婴儿的PV_IP表中的占用床位主键设置为患者原床位
		String queSql = " select pi.*, pv.pk_dept, pv.PK_DEPT_NS" +
						" from  pv_ip pi " +
						" inner join PV_INFANT inf on pi.PK_PV = inf.PK_PV_INFANT " +
				        " inner join PV_ENCOUNTER pv on pi.PK_PV = pv.pk_pv " +
						" where inf.pk_pv = ?";
		List<PvIpVo> pkIpList = DataBaseHelper.queryForList(queSql,PvIpVo.class,pk_pv);
		if(pkIpList.size() > 0){
			for(PvIpVo pkIpVo : pkIpList){
				if(pkIpVo.getPkDeptNs().equals(pk_dept_ns)){
					//更新婴儿陪护床位信息
					DataBaseHelper.update("update pv_ip set pk_bed_an = ?  where pk_pvip = ?", new Object[]{bdResBed.getPkBed(),pkIpVo.getPkPvip()});
				}
			}
		}
    }
    
    public Map<String,Object> getCancelDataInfo(String pk_pv,String pk_dept_ns){
    	Map<String,Object> paramMap = new HashMap<String,Object>();
    	paramMap.put("pkPv", pk_pv);
    	//查询总费用
    	BigDecimal zfy = exPubMapper.getTotalFee(paramMap);
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("zfy", zfy==null?0:zfy.doubleValue());
    	
    	//查询有效医嘱
    	String ord_sql = "  select count(pk_cnord) as ordnum from cn_order ord where  ord.eu_status_ord in ('0','1','2','3') and ord.pk_pv = ? and ord.del_flag = '0' ";
    	Map<String,Object> ordnum= DataBaseHelper.queryForMap(ord_sql, new Object[]{pk_pv});
    	if(ordnum!=null){
    		map.put("ordnum", ordnum.get("ordnum"));
    	}
    	
    	//查询病历
    	String emrSql = "select COUNT(1) as emrnum from EMR_MED_REC where PK_PV = ? and DEL_FLAG='0'";
    	Map<String,Object> emrnum= DataBaseHelper.queryForMap(emrSql, new Object[]{pk_pv});
    	if(ordnum!=null){
    		map.put("emrnum", emrnum.get("emrnum"));
    	}
    	return map;
    }
    
}
