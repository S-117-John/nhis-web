package com.zebone.nhis.webservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.base.ou.BdOuUser;
import com.zebone.nhis.common.module.emr.rec.rec.EmrMedRec;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.webservice.dao.EmrPubRecMapper;
import com.zebone.nhis.webservice.support.RespJson;
import com.zebone.nhis.webservice.vo.EmrAppLoginVo;
import com.zebone.nhis.webservice.vo.EmrEmpDeptVo;
import com.zebone.nhis.webservice.vo.EmrMedRecVo;
import com.zebone.nhis.webservice.vo.EmrOrdListVo;
import com.zebone.nhis.webservice.vo.EmrPatListVo;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 病历书写-公共
 * 
 * @author chengjia
 *
 */
@Service
public class EmrPubRecService {

	@Resource
	private EmrPubRecMapper recMapper;

	/**
	 * 查询病历文档记录
	 * @param codeIp住院号/codePi就诊ID/codePv就诊流水号/ipTimes住院次数
	 * @param 
	 * @return
	 */
	public String queryPatMedRecList(Map map) {
		//Map map = JsonUtil.readValue(param, Map.class);
		List<EmrMedRec> list = recMapper.queryPatMedRecList(map);
		String rtnStr="";
		String dataList="";
		List<EmrMedRecVo> rtnList=new ArrayList<EmrMedRecVo>();
		if(list!=null&&list.size()>0){
			for (EmrMedRec item : list) {
				EmrMedRecVo vo = new EmrMedRecVo();
				vo.setPkRec(item.getPkRec());
				vo.setCode(item.getSeqNo()==null?"":item.getSeqNo().toString());
				vo.setName(item.getName());
				vo.setRecDate(item.getRecDate());
				vo.setTypeCode(item.getTypeCode());
				if(item.getDocType()!=null){
					vo.setTypeName(item.getDocType().getName());
				}
				if(item.getMedDoc()!=null){
					vo.setPathName(item.getMedDoc().getFilePath());
					vo.setFileName(item.getMedDoc().getFileName()+"."+item.getMedDoc().getFileType());
				}
				rtnList.add(vo);
			}
			dataList=JsonUtil.writeValueAsString(rtnList);
		}
		//CommonUtils.getString(new RespJson("0|成功|" + rslt, true));
		rtnStr = CommonUtils.getString(new RespJson("0|成功|" + dataList, true));
		return rtnStr;
	}
	
	/**
	 * 查询病历患者列表
	 * @param deptCode科室编码/deptId科室ID
	 * @param 
	 * @return
	 */
	public String queryPatList(Map map) {
		List<EmrPatListVo> list = recMapper.queryPatList(map);
		String rtnStr="";
		String dataList="";
		if(list!=null&&list.size()>0){
			
			dataList=JsonUtil.writeValueAsString(list);
		}
		rtnStr = CommonUtils.getString(new RespJson("0|成功|" + dataList, true));
		return rtnStr;
	}	
	
	/**
	 * 查询病历患者列表
	 * @param deptCode科室编码/deptId科室ID
	 * @param 
	 * @return
	 */
	public String queryOrdList(Map map) {
		List<EmrOrdListVo> list = recMapper.queryOrdList(map);
		String rtnStr="";
		String dataList="";
		if(list!=null&&list.size()>0){
			
			dataList=JsonUtil.writeValueAsString(list);
		}
		rtnStr = CommonUtils.getString(new RespJson("0|成功|" + dataList, true));
		return rtnStr;
	}
	
	
	/**
	  * 登录接口 - 并返回工号、登录名、科室权限列表
	  * @param param{code,pwd}
	  * @param user
	  * @return
	  */
	 @SuppressWarnings("unchecked")
	 public String userLogin(Map map){
		 String rtnStr="";
		 if(map == null)
			 return CommonUtils.getString(new RespJson("-1|未获取到待验证的用户信息！", false));
		 if(null == map.get("code") || CommonUtils.isEmptyString(map.get("code").toString()))
			 return CommonUtils.getString(new RespJson("-1|未获取到登录名！", false));
		 if(null == map.get("pwd") || CommonUtils.isEmptyString(map.get("pwd").toString()))
			 return CommonUtils.getString(new RespJson("-1|未获取到密码！", false));
		 List<BdOuUser> listUser = DataBaseHelper.queryForList("select * from bd_ou_user where code_user = ? ", BdOuUser.class, new Object[]{map.get("code")});
		 if( listUser==null||listUser.size()==0){
			 return CommonUtils.getString(new RespJson("-1|用户不存在！", false));
		 }else{
			 if(listUser.size()>1){
				 return CommonUtils.getString(new RespJson("-1|用户名所对用户不唯一！", false));
			 }
		 }
		 BdOuUser user= listUser.get(0);
		 List<Map<String,Object>> pkEmps = DataBaseHelper.queryForList("select pk_emp from bd_ou_user where code_user = ? and pwd = ? " ,new Object[]{map.get("code"),new SimpleHash("md5",map.get("pwd")).toHex()});
		 if(null == pkEmps || pkEmps.size() < 1){
			 return CommonUtils.getString(new RespJson("-1|密码输入有误！", false));
		 }

		 BdOuEmployee emp = DataBaseHelper.queryForBean("select name_emp,pk_emp,code_emp from bd_ou_employee where pk_emp = ?", BdOuEmployee.class, new Object[]{pkEmps.get(0).get("pkEmp")});
		 if(null == emp){
			 return CommonUtils.getString(new RespJson("-1|该用户未维护相关人员信息！", false));
		 }
		 
		 String deptSql = "select DISTINCT t.* from ("
	                + "select a.pk_dept,b.code_dept dept_code,b.name_dept dept_name,a.seqno "
	                + " from bd_ou_usrgrp_dept a "
	                + "left join bd_ou_dept b on a.pk_dept = b.pk_dept "
	                + "where  a.pk_usrgrp = ? "
	                + "union "
	                + "select ugpdept.pk_dept, dept.code_dept dept_code,dept.name_dept dept_name,ugpdept.seqno "
	                + "  from bd_ou_user_usrgrp ugp  "
	                + "LEFT OUTER JOIN BD_OU_USRGRP_DEPT ugpdept on ugp.pk_usrgrp = ugpdept.pk_usrgrp "
	                + "left join bd_ou_dept dept on ugpdept.pk_dept = dept.pk_dept "
	                + "where ugp.pk_user = ?  and ugp.del_flag = '0') t "
	                + "ORDER BY seqno";

		 List<EmrEmpDeptVo> deptList = DataBaseHelper.queryForList(deptSql, EmrEmpDeptVo.class, user.getPkUsrgrp(), user.getPkUser());

		 EmrAppLoginVo vo=new EmrAppLoginVo();
		 vo.setRet_code("0");
		 vo.setRet_msg("成功");
		 vo.setEmpCode(emp.getCodeEmp());
		 vo.setEmpName(emp.getNameEmp());
		 vo.setDatalist(deptList);
		 
		 rtnStr=JsonUtil.writeValueAsString(vo);
		 
	   	 return rtnStr;
	 }

}
