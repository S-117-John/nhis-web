package com.zebone.nhis.webservice.syx.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.XmlUtil;
import com.zebone.nhis.webservice.support.RespJson;
import com.zebone.nhis.webservice.syx.vo.BlRequest;
import com.zebone.nhis.webservice.syx.vo.BlResponse;
import com.zebone.nhis.webservice.syx.vo.bl.BlCgVo;
import com.zebone.nhis.webservice.syx.vo.bl.BlIpCgVo;
import com.zebone.nhis.webservice.syx.vo.bl.BlPubParamVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;

/**
 * 住院记费WebService接口
 * @author c
 *
 */
@Service
public class SyxBlWsService {
	
	private User u  = new User();
	
	/**
	 * 住院记费接口
	 * @param param
	 * @return
	 */
	public String chargeIpBatch(String param) throws Exception{
		List<BlCgVo> cgList = JsonUtil.readValue(
				JsonUtil.getJsonNode(param, "datalist"),
				new TypeReference<List<BlCgVo>>() {
				});
		String result = CommonUtils.getString(new RespJson("0|调用成功！", false));	//返回结果
		
		if(cgList==null || cgList.size()<=0)
			return CommonUtils.getString(new RespJson("99|未传入记费数据，请检查！", false));; 
		
		Set<String> codePiList = new HashSet<String>();//患者id集合
		Set<String> codePvList = new HashSet<String>();//就诊id集合
		Set<String> codeItemList = new HashSet<String>();//项目编码集合
		Set<String> codeDeptList = new HashSet<String>();//科室集合
		Set<String> codeEmpList = new HashSet<String>();//人员集合
		
		for(BlCgVo cgVo : cgList){
			codePiList.add(cgVo.getCode_pi());
			codePvList.add(cgVo.getCode_pv());
			codeItemList.add(cgVo.getCode_item());
			codeDeptList.add(cgVo.getCode_dept());
			codeDeptList.add(cgVo.getCode_dept_ns());
			codeDeptList.add(cgVo.getCode_dept_ex());
			codeEmpList.add(cgVo.getCode_emp_cg());
			codeEmpList.add(cgVo.getCode_emp_phy());
		}
		
		//患者信息集合
		List<Map<String,Object>> piList = new ArrayList<>();
		//就诊信息集合
		List<Map<String,Object>> pvList = new ArrayList<>();
		//收费项目集合
		List<Map<String,Object>> itemList=  new ArrayList<>();
		//执行科室、住院科室集合
		List<Map<String,Object>> deptList = new ArrayList<>();
		//人员集合
		List<Map<String,Object>> empList=  new ArrayList<>();
		
		//根据患者codePi获取患者信息
		String piListSql = "select pi.PK_PI,pi.CODE_IP,pi.CODE_PI from PI_MASTER pi where pi.code_pi in ("
				+ CommonUtils.convertSetToSqlInPart(codePiList, "pi.code_pi")+")";
		piList = DataBaseHelper.queryForList(piListSql, new Object[]{});
		//根据患者code_pv获取患者就诊信息
		String pvListSql = "select pv.pk_org,pv.PK_PV,pv.PK_PI,pv.EU_STATUS,pv.CODE_PV,pv.eu_pvtype,pv.pk_dept,pv.pk_dept_ns from PV_ENCOUNTER pv where pv.code_pv in ("
				+ CommonUtils.convertSetToSqlInPart(codePvList, "pv.code_pv")+")";
		pvList = DataBaseHelper.queryForList(pvListSql, new Object[]{});
		//根据code_item获取收费项目信息
		String itemListSql = "select code as code_item,pk_item,pk_itemcate from bd_item where code in ("+CommonUtils.convertSetToSqlInPart(codeItemList, "code")+")";
		itemList = DataBaseHelper.queryForList(itemListSql, new Object[]{});
		//根据执行科室id，住院科室ID获取科室信息
		String deptListSql = "select pk_dept,pk_org,code_dept from bd_ou_dept where code_dept in ("
				+ CommonUtils.convertSetToSqlInPart(codeDeptList, "code_dept")+")";
		deptList = DataBaseHelper.queryForList(deptListSql, new Object[]{});
		//根据人员编码获取人员信息集合
		String empListSql = "select emp.pk_org,emp.code_emp,emp.pk_emp,emp.name_emp,empjob.pk_dept from bd_ou_employee emp "+
				" inner join bd_ou_empjob empjob on emp.pk_emp = empjob.pk_emp where emp.code_emp in ("+CommonUtils.convertSetToSqlInPart(codeEmpList, "emp.code_emp")+")";
		empList = DataBaseHelper.queryForList(empListSql, new Object[]{});
		
		//组装记费数据
		List<BlPubParamVo> blCgVos = new ArrayList<>();
		for(BlCgVo cgVo : cgList){
			Map<String,Object> piMap = getMap(piList,"codePi",cgVo.getCode_pi());
			Map<String,Object> pvMap = getMap(pvList,"codePv",cgVo.getCode_pv());
			Map<String,Object> itemMap = getMap(itemList,"codeItem",cgVo.getCode_item());
			Map<String,Object> deptMap = getMap(deptList,"codeDept",cgVo.getCode_dept());
			Map<String,Object> deptNsMap = getMap(deptList,"codeDept",cgVo.getCode_dept_ns());
			Map<String,Object> deptExMap = getMap(deptList,"codeDept",cgVo.getCode_dept_ex());
			Map<String,Object> empPhyMap = getMap(empList,"codeEmp",cgVo.getCode_emp_phy());
			Map<String,Object> empCgMap = getMap(empList,"codeEmp",cgVo.getCode_emp_cg());
			
			BlPubParamVo vo = new BlPubParamVo();
			vo.setPkOrg(CommonUtils.getString(pvMap.get("pkOrg")));
			vo.setPkPi(CommonUtils.getString(piMap.get("pkPi")));
			vo.setPkPv(CommonUtils.getString(pvMap.get("pkPv")));
			vo.setEuPvType(CommonUtils.getString(pvMap.get("euPvtype")));
			vo.setFlagPd("0");
			vo.setPkItem(CommonUtils.getString(itemMap.get("pkItem")));
			vo.setQuanCg(cgVo.getQuan());
			vo.setPkOrgApp(CommonUtils.getString(deptMap.get("pkOrg")));
			vo.setPkDeptApp(CommonUtils.getString(deptMap.get("pkDept")));
			vo.setPkDeptNsApp(CommonUtils.getString(deptNsMap.get("pkDept")));
			vo.setPkEmpApp(CommonUtils.getString(empPhyMap.get("pkEmp")));
			vo.setNameEmpApp(CommonUtils.getString(empPhyMap.get("nameEmp")));
			vo.setPkOrgEx(CommonUtils.getString(deptExMap.get("pkOrg")));
			vo.setPkDeptEx(CommonUtils.getString(deptExMap.get("pkDept")));
			vo.setDateHap(DateUtils.strToDate(cgVo.getDate_hap(), "yyyy-MM-dd"));
			vo.setPkDeptCg(CommonUtils.getString(empCgMap.get("pkOrg")));
			vo.setPkEmpCg(CommonUtils.getString(empCgMap.get("pkEmp")));
			vo.setNameEmpCg(CommonUtils.getString(empCgMap.get("nameEmp")));
			
			blCgVos.add(vo);
		}
		
		if(blCgVos!=null && blCgVos.size()>0){
			ApplicationUtils apputil = new ApplicationUtils();
			//组装用户信息
			u.setPkOrg(blCgVos.get(0).getPkOrg());
			u.setPkEmp(blCgVos.get(0).getPkEmpEx());
			u.setNameEmp(blCgVos.get(0).getNameEmpEx());
			u.setPkDept(blCgVos.get(0).getPkDeptEx());
			
			UserContext.setUser(u);
			
			//组装记费参数
			BlIpCgVo cgVo = new BlIpCgVo();
			cgVo.setAllowQF(true);
			cgVo.setBlCgPubParamVos(blCgVos);
			
			ResponseJson  rs =  apputil.execService("BL", "IpCgPubService", "chargeIpBatch", cgVo,u);
			if(!CommonUtils.isEmptyString(rs.getErrorMessage()))
				result = CommonUtils.getString(new RespJson("99|"+rs.getErrorMessage(), false));
			
		}
		else
			result = CommonUtils.getString(new RespJson("99|组装记费数据有问题，请联系管理员！", false));
		
		return result;
	}
	
	/**
     * 根据传入的key,value对比ListMap里是否有相同的value，如果有返回此map
     * @param listMap
     * @param key
     * @return
     */
    private Map<String,Object> getMap(List<Map<String,Object>> listMap,String key,String value){
    	Map<String,Object> rtnMap = new HashMap<String, Object>();
    	if(listMap!=null && listMap.size()>0){
	    	for(Map<String,Object> map : listMap){
	    		if(CommonUtils.getString(map.get(key)).equals(value)){
	    			rtnMap = map;
	    			break;
	    		}
	    	}
    	}
    	return rtnMap;
    }
    
    public String analysisXml(String input_info) throws Exception {
    	BlRequest req = (BlRequest)XmlUtil.XmlToBean(input_info, BlRequest.class);
		return XmlUtil.beanToXml(chargeIpBatch(req), BlResponse.class);
				
	}
	
    /**
	 * 重载住院记费接口:xml格式
	 * @param param
	 * @return
	 */
	public BlResponse chargeIpBatch(BlRequest req) throws Exception{
		BlResponse rsp = new BlResponse();//返回结果
		try {
			List<BlCgVo> cgList = req.getDatalist();
			
			if(cgList==null || cgList.size()<=0)
				return setErr(rsp, "99", "未传入记费数据，请检查！");
			
			Set<String> codePiList = new HashSet<String>();//患者id集合
			Set<String> codePvList = new HashSet<String>();//就诊id集合
			Set<String> codeItemList = new HashSet<String>();//项目编码集合
			Set<String> codeDeptList = new HashSet<String>();//科室集合
			Set<String> codeEmpList = new HashSet<String>();//人员集合
			
			for(BlCgVo cgVo : cgList){
				codePiList.add(cgVo.getCode_pi());
				codePvList.add(cgVo.getCode_pv());
				codeItemList.add(cgVo.getCode_item());
				codeDeptList.add(cgVo.getCode_dept());
				codeDeptList.add(cgVo.getCode_dept_ns());
				codeDeptList.add(cgVo.getCode_dept_ex());
				codeEmpList.add(cgVo.getCode_emp_cg());
				codeEmpList.add(cgVo.getCode_emp_phy());
			}
			
			//患者信息集合
			List<Map<String,Object>> piList = new ArrayList<>();
			//就诊信息集合
			List<Map<String,Object>> pvList = new ArrayList<>();
			//收费项目集合
			List<Map<String,Object>> itemList=  new ArrayList<>();
			//执行科室、住院科室集合
			List<Map<String,Object>> deptList = new ArrayList<>();
			//人员集合
			List<Map<String,Object>> empList=  new ArrayList<>();
			
			//根据患者codePi获取患者信息
			String piListSql = "select pi.PK_PI,pi.CODE_IP,pi.CODE_PI from PI_MASTER pi where pi.code_ip in ("
					+ CommonUtils.convertSetToSqlInPart(codePiList, "pi.code_ip")+")";
			piList = DataBaseHelper.queryForList(piListSql, new Object[]{});
			if(piList == null || piList.size()<codePiList.size())
				return setErr(rsp, "1", "请求失败，未查询到相应患者！");
			//根据患者code_pv获取患者就诊信息
			String pvListSql = "select pv.pk_org,pv.PK_PV,pv.PK_PI,pv.EU_STATUS,pv.CODE_PV,pv.eu_pvtype,pv.pk_dept,pv.pk_dept_ns from PV_ENCOUNTER pv where pv.code_pv in ("
					+ CommonUtils.convertSetToSqlInPart(codePvList, "pv.code_pv")+")";
			pvList = DataBaseHelper.queryForList(pvListSql, new Object[]{});
			if(pvList == null || pvList.size()<codePvList.size())
				return setErr(rsp, "1", "请求失败，未查询到相应就诊！");
			//根据code_item获取收费项目信息
			String itemListSql = "select code as code_item,pk_item,pk_itemcate from bd_item where code in ("+CommonUtils.convertSetToSqlInPart(codeItemList, "code")+")";
			itemList = DataBaseHelper.queryForList(itemListSql, new Object[]{});
			if(itemList == null || itemList.size()<codeItemList.size())
				return setErr(rsp, "1", "请求失败，未查询到相应的收费项目！");
			//根据执行科室id，住院科室ID获取科室信息
			String deptListSql = "select pk_dept,pk_org,code_dept from bd_ou_dept where code_dept in ("
					+ CommonUtils.convertSetToSqlInPart(codeDeptList, "code_dept")+")";
			deptList = DataBaseHelper.queryForList(deptListSql, new Object[]{});
			if(deptList == null || deptList.size()<codeDeptList.size())
				return setErr(rsp, "1", "请求失败，未查询到相应的科室或病区！");
			//根据人员编码获取人员信息集合
			String empListSql = "select emp.pk_org,emp.code_emp,emp.pk_emp,emp.name_emp,empjob.pk_dept from bd_ou_employee emp "+
					" inner join bd_ou_empjob empjob on emp.pk_emp = empjob.pk_emp where emp.code_emp in ("+CommonUtils.convertSetToSqlInPart(codeEmpList, "emp.code_emp")+")";
			empList = DataBaseHelper.queryForList(empListSql, new Object[]{});
			if(empList == null || empList.size()<codeEmpList.size())
				return setErr(rsp, "1", "请求失败，未查询到相应的人员！");
			//组装记费数据
			List<BlPubParamVo> blCgVos = new ArrayList<>();
			for(BlCgVo cgVo : cgList){
				Map<String,Object> piMap = getMap(piList,"codePi",cgVo.getCode_pi());
				Map<String,Object> pvMap = getMap(pvList,"codePv",cgVo.getCode_pv());
				Map<String,Object> itemMap = getMap(itemList,"codeItem",cgVo.getCode_item());
				Map<String,Object> deptMap = getMap(deptList,"codeDept",cgVo.getCode_dept());
				Map<String,Object> deptNsMap = getMap(deptList,"codeDept",cgVo.getCode_dept_ns());
				Map<String,Object> deptExMap = getMap(deptList,"codeDept",cgVo.getCode_dept_ex());
				Map<String,Object> empPhyMap = getMap(empList,"codeEmp",cgVo.getCode_emp_phy());
				Map<String,Object> empCgMap = getMap(empList,"codeEmp",cgVo.getCode_emp_cg());
				
				BlPubParamVo vo = new BlPubParamVo();
				vo.setPkOrg(CommonUtils.getString(pvMap.get("pkOrg")));
				vo.setPkPi(CommonUtils.getString(pvMap.get("pkPi")));
				vo.setPkPv(CommonUtils.getString(pvMap.get("pkPv")));
				vo.setEuPvType(CommonUtils.getString(pvMap.get("euPvtype")));
				vo.setFlagPd("0");
				vo.setPkItem(CommonUtils.getString(itemMap.get("pkItem")));
				vo.setQuanCg(cgVo.getQuan());
				vo.setPkOrgApp(CommonUtils.getString(deptMap.get("pkOrg")));
				vo.setPkDeptApp(CommonUtils.getString(deptMap.get("pkDept")));
				vo.setPkDeptNsApp(CommonUtils.getString(deptNsMap.get("pkDept")));
				vo.setPkEmpApp(CommonUtils.getString(empPhyMap.get("pkEmp")));
				vo.setNameEmpApp(CommonUtils.getString(empPhyMap.get("nameEmp")));
				vo.setPkOrgEx(CommonUtils.getString(deptExMap.get("pkOrg")));
				vo.setPkDeptEx(CommonUtils.getString(deptExMap.get("pkDept")));
				vo.setDateHap(DateUtils.strToDate(cgVo.getDate_hap(), "yyyy-MM-dd"));
				vo.setPkDeptCg(CommonUtils.getString(empCgMap.get("pkOrg")));
				vo.setPkEmpCg(CommonUtils.getString(empCgMap.get("pkEmp")));
				vo.setNameEmpCg(CommonUtils.getString(empCgMap.get("nameEmp")));
				vo.setEuBltype("99");
				
				blCgVos.add(vo);
			}
			
			if(blCgVos!=null && blCgVos.size()>0){
				ApplicationUtils apputil = new ApplicationUtils();
				//组装用户信息
				u.setPkOrg(blCgVos.get(0).getPkOrg());
				u.setPkEmp(blCgVos.get(0).getPkEmpEx());
				u.setNameEmp(blCgVos.get(0).getNameEmpEx());
				u.setPkDept(blCgVos.get(0).getPkDeptEx());
				
				UserContext.setUser(u);
				
				//组装记费参数
				BlIpCgVo cgVo = new BlIpCgVo();
				cgVo.setAllowQF(true);
				cgVo.setBlCgPubParamVos(blCgVos);
				
				ResponseJson  rs =  apputil.execService("BL", "IpCgPubService", "chargeIpBatch", cgVo,u);
				if(!CommonUtils.isEmptyString(rs.getErrorMessage()))
					rsp = setErr(rsp, "99", rs.getErrorMessage());
				
			}
			else
				rsp = setErr(rsp, "99","组装记费数据有问题，请联系管理员！");
			
			return rsp;
		} catch (Exception e) {
			e.printStackTrace();
			return setErr(rsp, "1", "调用失败，请重试！");
		}
	}
	
	private BlResponse setErr(BlResponse rsp,String code,String err){
		if(StringUtils.isNotBlank(code))
			rsp.setRetCode(code);
		rsp.setRetMsg(err);
		return rsp;
	}
    
}
