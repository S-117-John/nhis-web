package com.zebone.nhis.labor.pub.service;

import com.zebone.nhis.common.module.ex.nis.ns.ExPdApply;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ex.pub.support.ExSysParamUtil;
import com.zebone.nhis.ex.pub.vo.GeneratePdApExListVo;
import com.zebone.nhis.labor.pub.dao.DeptPdApplyPubMapper;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门（医技）领药--以部门为单位领药，忽略患者
 * @author yangxue
 *
 */
@Service
public class DeptPdApplyPubService {
   @Resource
   private DeptPdApplyPubMapper deptPdApplyPubMapper;
   /**
    * 查询已计费未退费，可生成请领的执行单列表
    * @param param{pkDept--开立科室,codeIp,dateBegin,dateEnd,pdname}
    * @return
    */
   public List<GeneratePdApExListVo> queryExCgList(String param,IUser user){
	   Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
	   if(paramMap == null||CommonUtils.isNull(paramMap.get("pkDept")))
		   throw new BusException("未获取到要生成请领单的当前科室！");
	   //之前处理
	   List<GeneratePdApExListVo> resultData=deptPdApplyPubMapper.qryUnPdApExList(paramMap);
	   if (CollectionUtils.isEmpty(resultData)){
	   	return resultData;
	   }
	   return  resultData.stream().filter(u->u.getQuanOcc()>0 && u.getAmount()>0).collect(Collectors.toList());
   }
	/**
	 * 查询退费，可生成请领的执行单列表
	 * @param param{pkDept--开立科室,codeIp,dateBegin,dateEnd,pdname}
	 * @return
	 */
	public List<GeneratePdApExListVo> queryExBackCgList(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		if(paramMap == null||CommonUtils.isNull(paramMap.get("pkDept")))
			throw new BusException("未获取到要生成请领单的当前科室！");
		return deptPdApplyPubMapper.qryUnPdApExBackList(paramMap);
	}
   /**
    * 生成请领
    *
   public void generatePdAp(String param,IUser user){
	   List<GeneratePdApExListVo> exlist = JsonUtil.readValue(param,new TypeReference<List<GeneratePdApExListVo>>(){});
	   if(exlist == null || exlist.size()<=0)
		   throw new BusException("未获取到需要生成请领单的物品信息！");
	   User u = (User)user;
	   //生成请领单
	   ExPdApply pdap = createPdAp(exlist.get(0).getPkOrgOcc(),exlist.get(0).getPkDeptOcc(),u);
	   DataBaseHelper.insertBean(pdap);
	   //生成明细
	   Map<String,List> resultmap = createApDt(exlist,u,pdap);
	   if(resultmap == null)
		   return;
	   List<ExPdApplyDetail> dtlist = resultmap.get("dtlist");
	   if(dtlist!=null&&dtlist.size()>0)
		   DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExPdApplyDetail.class),dtlist);
	   //更新执行单的请领明细
	   List<String> sqllist = resultmap.get("sqllist");
	   if(sqllist!=null&&sqllist.size()>0)
		   DataBaseHelper.batchUpdate(sqllist.toArray(new String[0]));
   }
   */
   
   public void generatePdAp(String param,IUser user){
	   Map<String,Object> paramMap=JsonUtil.readValue(param, Map.class);
	   List<GeneratePdApExListVo> exlist = JsonUtil.readValue(JsonUtil.getJsonNode(param, "exlist"),new TypeReference<List<GeneratePdApExListVo>>(){});
	   if(exlist == null || exlist.size()<=0)
		   throw new BusException("未获取到需要生成请领单的物品信息！");
	   User u = (User)user;
	   List<GeneratePdApExListVo> list = new ArrayList<>();
	   List<GeneratePdApExListVo> listBack = new ArrayList<>();
	   for(GeneratePdApExListVo vo: exlist){
			if(vo.getQuanOcc()>0){
				list.add(vo);
			}else{
				listBack.add(vo);
			}
	   }
	   //针对毒麻药品添加该字段设置备注区分
	   String pdType = null != paramMap.get("pdType") ? paramMap.get("pdType").toString() : "";
	   //生成请领单
	   if(list.size()>0){
		   ExPdApply pdap = createPdAp(list.get(0).getPkOrgOcc(),list.get(0).getPkDeptOcc(),u,pdType,"1");
		   DataBaseHelper.insertBean(pdap);
		   //创建请领明细并执行更新操作，回写bl_ip_dt中得数据
		   createApplyDt(list,u,pdap,paramMap,1);
	   }
	   //生成退请领单
	   if(listBack.size()>0){
		   ExPdApply pdap = createPdAp(listBack.get(0).getPkOrgOcc(),listBack.get(0).getPkDeptOcc(),u,pdType,"-1");
		   DataBaseHelper.insertBean(pdap);
		   //创建请领明细并执行更新操作，回写bl_ip_dt中得数据
		   createApplyDt(listBack,u,pdap,paramMap,-1);
	   }

   }
   
   /**
    * 根据病区查询请领单列表
    * @param param{pkDeptNs,dateEnd,dateBegin,pkDeptDe}
    * @param user
    * @return
    */
	public List<Map<String,Object>> queryPdAppByDept(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		String dateEnd = CommonUtils.getString(map.get("dateEnd"));
		if(dateEnd!=null&&!dateEnd.equals("")){
			map.put("dateEnd", dateEnd.substring(0,8)+"235959");
		}
		return deptPdApplyPubMapper.queryPdApply(map);
	}
	/**
	 * 根据请领单查询请领明细
	 * @param param{pkPdap}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryPdApDetail(String param,IUser user){
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		return deptPdApplyPubMapper.queryPdApDetail(map);
	}
	
	/**
	 * 拼接请领单要导出的数据
	 * @param param{pkPdapdts}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> queryExportPdAppDt(String param,IUser user){
		List<String> pkPdapdts = JsonUtil.readValue(param, ArrayList.class);
		Set<String> pkPdapdtSet = new HashSet<String>();		
		for (String str : pkPdapdts) {
			pkPdapdtSet.add(str);
		}
		
		StringBuffer sqlPdStr = new StringBuffer("select pd.name as name_pd,pd.pk_pd,defdoc.name as dt_dosage,pd.spec,unit.name name_unit");
		sqlPdStr.append(" from ex_pd_apply_detail detail ");
		sqlPdStr.append(" inner join bd_pd pd on pd.pk_pd = detail.pk_pd  inner join bd_unit unit on unit.pk_unit = detail.pk_unit ");
		sqlPdStr.append(" left join bd_defdoc defdoc on defdoc.code=pd.dt_dosage and defdoc.code_defdoclist='030400' ");
		sqlPdStr.append(" where  detail.pk_pdapdt in ("+ CommonUtils.convertSetToSqlInPart(pkPdapdtSet, "detail.pk_pdapdt") +") ");
		sqlPdStr.append(" group by pd.name,pd.pk_pd,defdoc.name ,pd.spec,unit.name ");
		List<Map<String, Object>> queryPdList = DataBaseHelper.queryForList(sqlPdStr.toString());
		
		StringBuffer sqlStr = new StringBuffer("select to_char(cn.date_pres,'yyyy-mm-dd') as date_pres,pv.pk_dept,pv.pk_dept_ns,pv.name_pi,");
		sqlStr.append("pv.dt_sex,pv.age_pv as age,pi.id_no,pi.code_ip,pd.name,pd.pk_pd,");
		sqlStr.append("diag.name_diag,detail.batch_no,cn.pk_emp_ord,sum(detail.quan_min) as quan_min ");
		sqlStr.append(" from ex_pd_apply_detail detail inner join pv_encounter pv on detail.pk_pv = pv.pk_pv ");
		sqlStr.append(" inner join pi_master pi on pi.pk_pi = pv.pk_pi inner join bd_pd pd on pd.pk_pd = detail.pk_pd ");
		sqlStr.append(" inner join bd_unit unit on unit.pk_unit = detail.pk_unit  ");
		sqlStr.append(" left join cn_prescription cn on detail.pk_pres=cn.pk_pres ");
		sqlStr.append(" left join pv_diag diag on diag.pk_pv=pv.pk_pv and diag.del_flag='0' and diag.flag_maj='1' ");
		sqlStr.append(" where  detail.pk_pdapdt in ("+ CommonUtils.convertSetToSqlInPart(pkPdapdtSet, "detail.pk_pdapdt") +")");
		sqlStr.append(" group by to_char(cn.date_pres,'yyyy-mm-dd'),pv.pk_dept,pv.pk_dept_ns,pv.name_pi, ");
		sqlStr.append(" pv.dt_sex,pv.age_pv ,pi.id_no,pi.code_ip,pd.name,pd.pk_pd,diag.name_diag,detail.batch_no,cn.pk_emp_ord ");
		sqlStr.append(" order by  pi.code_ip asc,to_char(cn.date_pres,'yyyy-mm-dd') desc ");
		List<Map<String, Object>> queryPdDtList = DataBaseHelper.queryForList(sqlStr.toString());
		
		for (Map<String, Object> pdMap : queryPdList) {
			List<Map<String, Object>> pdDtList = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> pdDtMap : queryPdDtList) {
				if(pdMap.get("pkPd").equals(pdDtMap.get("pkPd"))){
					pdDtList.add(pdDtMap);
				}
			}
			pdMap.put("expPdAppDtList", pdDtList);
		}
		
		return queryPdList;
	}
	
	
	/**
	 * 取消请领
	 * @param param{选中行所有数据}
	 * @param user
	 */
	public void cancelApply(String param,IUser user){
		Map<String,Object> params= JsonUtil.readValue(param, Map.class);
        Map<String,Object> map = (Map<String, Object>) params.get("dr");
        List<String> pkPvs = (List<String>) params.get("pkPvs");
        String paramOfPkpv =null;
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(pkPvs)){
            StringBuffer stringBuffer = new StringBuffer();
            for (String pkPv : pkPvs) {
                stringBuffer.append("'").append(pkPv).append("',");

            }
            if (StringUtils.isNotBlank(stringBuffer)){
                paramOfPkpv= stringBuffer.substring(0, stringBuffer.length() - 1);
            }

        }

        if(map == null) throw new BusException("未获取到需要取消的请领单信息！");
		String direct = CommonUtils.getString(map.get("euDirect"));
		User u = (User)user;
        Date date = new Date();
        map.put("dateCanc", date);
        map.put("strDate",DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", date));
        //if("1".equals(direct)){//请领
			//校验请领单是否发药
			String sql = "select pk_pdap from ex_pd_apply where (flag_cancel = '1' or date_de is not null ) and pk_pdap = :pkPdap  ";
			Map<String,Object> result = DataBaseHelper.queryForMap(sql, map);
			if(result!=null&&!CommonUtils.isEmptyString(CommonUtils.getString(result.get("pkPdap")))){
				throw new BusException("该请领单已经发药或取消，不允许取消!");
			}
			//验证该清理退费提交请退的不能取消 low 5min
		    StringBuilder dtBack = new StringBuilder(" SELECT COUNT(1) from bl_ip_dt ipback where ipback.pk_cgip_back in ( ");
		    dtBack.append(" SELECT ipdt.pk_cgip from  bl_ip_dt ipdt ");
			dtBack.append(" where ipdt.pk_pdapdt in (select dt.pk_pdapdt from ex_pd_apply_detail dt  ");
			dtBack.append(" inner join ex_pd_apply ap on dt.pk_pdap = ap.pk_pdap ");
			dtBack.append(" where ap.pk_pdap = ? )) and ipback.pk_pdapdt is not NULL  ");
		    Integer count =  DataBaseHelper.queryForScalar(dtBack.toString(), Integer.class, map.get("pkPdap"));
		    // 改写成mapper文件形式

			if(count>0){
				throw new BusException("该请领单中有药品已经提交请退，请先取消对应的请退!");
			}
		    //更新医嘱执行单数据
			StringBuilder dtAp = new StringBuilder("update ex_order_occ set pk_pdapdt = null,ts=:dateCanc   ");
			dtAp.append("where pk_pdapdt in (select dt.pk_pdapdt from ex_pd_apply_detail dt ");
			dtAp.append("inner join ex_pd_apply ap on dt.pk_pdap = ap.pk_pdap ");
			dtAp.append("where ap.pk_pdap = :pkPdap) ");
			DataBaseHelper.update(dtAp.toString(), map);	
			
			//更新收费记录数据 low 35s
			StringBuilder dtBl = new StringBuilder("update bl_ip_dt set pk_pdapdt = null,ts=:dateCanc   ");
			dtBl.append("where pk_pdapdt in (select dt.pk_pdapdt from ex_pd_apply_detail dt ");
			dtBl.append("inner join ex_pd_apply ap on dt.pk_pdap = ap.pk_pdap ");
			dtBl.append("where ap.pk_pdap = :pkPdap) ");
			if (StringUtils.isNotBlank(paramOfPkpv)){
                dtBl.append("and  PK_PV in " +"("+ paramOfPkpv+")");
            }
			DataBaseHelper.update(dtBl.toString(), map);

		//}
		  //执行更新请领状态
		   StringBuilder upAp = new StringBuilder("update ex_pd_apply set flag_cancel = '1' , pk_emp_cancel = :pkEmp, ");
		   upAp.append("name_emp_cancel = :nameEmp , pk_dept_cancel = :pkDept , date_cancel = :dateCanc ,");
		   upAp.append("eu_status = '9',ts=:dateCanc  where pk_pdap = :pkPdap ");
			map.put("pkEmp", u.getPkEmp());
			map.put("nameEmp", u.getNameEmp());
			map.put("pkDept", u.getPkDept());
			DataBaseHelper.update(upAp.toString(), map);
		//更新请领单明细状态
			map.put("reason", "药房停发");
			StringBuilder updateSql = new StringBuilder("update ex_pd_apply_detail set flag_stop = '1' , reason_stop = :reason,");
			updateSql.append("pk_emp_stop = :pkEmp, name_emp_stop = :nameEmp,ts=:dateCanc  ");
			updateSql.append("where pk_pdap = :pkPdap and flag_de = '0' and flag_stop = '0'");
			DataBaseHelper.update(updateSql.toString(), map);
	}
	
	
	/**
     * 013002012012
     * 病区领药查询的取消退药服务
     *
     * @param param
     * @param user
     */
    public void cancelCollarDrug(String param, IUser user) {
        List<ExPdApplyDetail> exPdapdts = JsonUtil.readValue(param, new TypeReference<List<ExPdApplyDetail>>() {
        });
        if (exPdapdts == null) return;
        Set<String> pkPdapdtSet = new HashSet<String>();
        Set<String> pkPdapSet = new HashSet<String>();
        for (ExPdApplyDetail expdapdt : exPdapdts) {
            pkPdapdtSet.add(expdapdt.getPkPdapdt());
            pkPdapSet.add(expdapdt.getPkPdap());
        }
        String euDirect = exPdapdts.get(0).getEuDirect();
        User nuser = (User) user;
        String pkPdapdts = CommonUtils.convertSetToSqlInPart(pkPdapdtSet, "pk_pdapdt");
		//验证该条明细是否提交请退，提交请退的不能取消
		/*StringBuilder dtBack = new StringBuilder(" SELECT COUNT(1) from bl_ip_dt ipback where ipback.pk_cgip_back in ( ");
		dtBack.append(" SELECT ipdt.pk_cgip from  bl_ip_dt ipdt  ");
		dtBack.append(" where ipdt.pk_pdapdt in (");
		dtBack.append(pkPdapdts);
		dtBack.append(" ) ) and ipback.pk_pdapdt is not NULL ");
		Integer countBack =  DataBaseHelper.queryForScalar(dtBack.toString(), Integer.class);*/
		// 谢君威：优化速度改用mapper写，添加pkpv一起查询
        Integer countBack = deptPdApplyPubMapper.queryCgipCount(exPdapdts);
        if(countBack>0){
			throw new BusException("选择的药品有已经提交请退，请先取消对应的请退!");
		}
        //1.更新请领明细中flag_cancel,pk_emp_back,name_emp_back,date_bace,note
        StringBuffer upapdtSql = new StringBuffer("update ex_pd_apply_detail set flag_canc=1 ,");
        upapdtSql.append(" pk_emp_back=?,name_emp_back=? ,date_back=? ,note='科室取消',flag_stop=1 ");
        upapdtSql.append(" where pk_pdapdt in (");
        upapdtSql.append(pkPdapdts);
        upapdtSql.append(") and (flag_canc='0' or flag_canc is null) and flag_de='0' ");
        int count = DataBaseHelper.update(upapdtSql.toString(), new Object[]{nuser.getPkEmp(), nuser.getNameEmp(), new Date()});
        if (count != pkPdapdtSet.size()) {
            throw new BusException("您本次提交的药品发放明细中已有被其他人处理的记录,或者已经取消领药，请刷新请领单后重新处理！");
        }
        
        //更新收费记录数据
	   /* StringBuilder dtBl = new StringBuilder("update bl_ip_dt set pk_pdapdt = null,ts=?   ");
	    dtBl.append("where pk_pdapdt in (");
	    dtBl.append(pkPdapdts);
	    dtBl.append(")");
	    DataBaseHelper.update(dtBl.toString(),new Object[]{new Date()});*/
        // 重写sql  用pkpv作为条件参数
        deptPdApplyPubMapper.updatePkPdApdt(exPdapdts);

        String pkPdaps = CommonUtils.convertSetToSqlInPart(pkPdapSet, "pk_pdap");
        //3.更新请领单--如果请领明细全部被退回，同时更新请领单为取消状态(不包含已经发药的)
        StringBuffer upPddts = new StringBuffer("update ex_pd_apply  set ex_pd_apply.flag_cancel='1',ex_pd_apply.eu_status='9' ");
        upPddts.append(" ,ex_pd_apply.pk_emp_cancel=? ,ex_pd_apply.name_emp_cancel=?,ex_pd_apply.date_cancel=? ");
        upPddts.append(" where not exists (select 1 from ex_pd_apply_detail dt ");
        upPddts.append(" where ex_pd_apply.pk_pdap=dt.pk_pdap and (dt.flag_canc='0' or dt.flag_canc is null ))");
        upPddts.append(" and not exists (select 1 from ex_pd_apply_detail dt where ex_pd_apply.pk_pdap=dt.pk_pdap  and dt.flag_finish='1'  )");
        upPddts.append(" and ex_pd_apply.pk_pdap in (");
        upPddts.append(pkPdaps);
        upPddts.append(")");
        DataBaseHelper.update(upPddts.toString(), new Object[]{nuser.getPkEmp(), nuser.getNameEmp(), new Date()});

        //4.更新请领单-如果请领单对应的明细即包含已经完成发药，或者退回处理的将请领单置为完成状态
        StringBuffer upPdaps = new StringBuffer("update ex_pd_apply   set ex_pd_apply.flag_finish='1',ex_pd_apply.eu_status='1' where ex_pd_apply.pk_pdap in (");
        upPdaps.append(pkPdaps);
        upPdaps.append(") and exists (select 1 from ex_pd_apply_detail dt where ex_pd_apply.pk_pdap=dt.pk_pdap and dt.flag_finish='1') ");
        upPdaps.append(" and not exists (select 1 from ex_pd_apply_detail dt1  where ex_pd_apply.pk_pdap=dt1.pk_pdap and dt1.flag_finish='0' and (dt1.flag_canc='0' or dt1.flag_canc is null) and dt1.flag_stop='0') ");
        DataBaseHelper.update(upPdaps.toString());
    }
   /**
	 * 
	 * @param ordVO
	 * @param param
	 * @return
	 * @throws BusException
	 */
	private ExPdApply createPdAp(String pk_org_occ,String pk_dept_occ,User user,String pdType,String euAptype){
		ExPdApply vo = new ExPdApply();
		vo.setPkOrg(user.getPkOrg());
		String pk_pdap = NHISUUID.getKeyId();
		vo.setPkPdap(pk_pdap);
		vo.setEuDirect(euAptype);
		String code = ExSysParamUtil.getAppCode();
		vo.setCodeApply(code);
		vo.setEuAptype("1");//科室领药
		vo.setPkDeptAp(user.getPkDept());
		vo.setPkEmpAp(user.getPkEmp());
		vo.setNameEmpAp(user.getNameEmp());
		vo.setDateAp(new Date());
		vo.setPkOrgDe(pk_org_occ);
		vo.setPkDeptDe(pk_dept_occ);
		vo.setFlagCancel("0");
		vo.setFlagFinish("0");
		vo.setEuStatus("0");
		vo.setCreateTime(new Date());
		vo.setCreator(user.getPkEmp());
		if("1".equals(pdType))//毒麻药品需要设置备注
			vo.setNote("毒麻领用");
		vo.setTs(new Date());
		vo.setDelFlag("0");
		return vo;
	}
	
	
	/**
	 * 根据物品执行信息生成请领明细
	 * @param ordList
	 * @param appVO
	 * @param exceVO
	 * @return
	 * @throws BusException
	 */
	private Map<String,List> createApDt(List<GeneratePdApExListVo> ordlist,User user,ExPdApply appVO) throws BusException {
		if(ordlist == null ||ordlist.size()<=0){
			return null;
		}
		
		List<ExPdApplyDetail> dtlist= new ArrayList<ExPdApplyDetail>();
		List<String> sqllist= new ArrayList<String>();
		for(GeneratePdApExListVo dt:ordlist){
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("pkDeptOcc", dt.getPkDeptOcc());
			paramMap.put("pkPd", dt.getPkPd());
			Map<String,Object> pdstore = deptPdApplyPubMapper.getPdStoreInfo(paramMap);
			if(pdstore ==null ||pdstore.get("pkPd")==null||pdstore.get("pkUnitStore")==null)
				throw new BusException("未能从该药房物品表中获取到物品"+dt.getPdname()+"，无法生成请领单！");
			ExPdApplyDetail vo = new ExPdApplyDetail();
			String pk_pdapdt = NHISUUID.getKeyId();
			vo.setCreateTime(new Date());
			vo.setCreator(user.getPkEmp());
			vo.setTs(new Date());
			vo.setDelFlag("0");
			vo.setEuDirect("1");
			vo.setPkCnord(dt.getPkCnord());
			vo.setPkOrg(appVO.getPkOrg());
			vo.setPkPdap(appVO.getPkPdap());
			vo.setPkPdapdt(pk_pdapdt);
			vo.setPkPv(dt.getPkPv());
			vo.setPkPres(dt.getPkPres());
			vo.setOrds(dt.getOrds());
			vo.setFlagBase(dt.getFlagBase());
			vo.setFlagSelf(dt.getFlagSelf());
			vo.setFlagDe("0");
			vo.setFlagFinish("0");
			vo.setFlagStop("0");
			vo.setPkPd(dt.getPkPd());
			vo.setPkUnit(CommonUtils.getString(pdstore.get("pkUnitStore")));
			vo.setPackSize(CommonUtils.getInteger(pdstore.get("packSizeStore")));
			vo.setPrice(MathUtils.mul(MathUtils.div(dt.getPrice(), CommonUtils.getDouble(dt.getPackSizeP())),CommonUtils.getDouble(vo.getPackSize())));
			String eu_muputype = dt.getEuMuputype();
			String flag_medout = dt.getFlagMedout();//出院带药
			vo.setFlagMedout(flag_medout);
			if(null != flag_medout && flag_medout.equals("1")){
				//出院带药，总量取证
				vo.setQuanPack(getQuanPack(MathUtils.upRound(dt.getQuanOcc()),vo.getPackSize()));
			}else if(eu_muputype == null ){
				//取整模式为空,规则：单次取整
				vo.setQuanPack(getQuanPack(dt.getQuanOcc(),vo.getPackSize()));
			}else if( eu_muputype.equals("0")){
				//0：单次取证
				vo.setQuanPack(getQuanPack(dt.getQuanOcc(),vo.getPackSize()));
			}else if(eu_muputype.equals("1"))
				//1：按批取整;
			     vo.setQuanPack(getQuanPack(Math.ceil(dt.getQuanOcc()),vo.getPackSize()));
			else if(eu_muputype.equals("2")){
				//2:余量法
				vo.setQuanPack(getQuanPack(dt.getQuanOcc(),vo.getPackSize()));
			}else
				//其他：不取整
			 vo.setQuanPack(getQuanPack(dt.getQuanOcc(),vo.getPackSize()));
			 vo.setQuanMin(MathUtils.mul(vo.getQuanPack(),CommonUtils.getDouble(vo.getPackSize())));
			//参考价格为零售参考价，需要转换为基本单位的价格再计算
			vo.setAmount(MathUtils.mul(vo.getQuanPack(),vo.getPrice()));
			//发放分类 0:药房发
			vo.setEuDetype("0");
			dtlist.add(vo);
			sqllist.add("update ex_order_occ set pk_pdapdt = '"+vo.getPkPdapdt()+"' where pk_exocc = '"+dt.getPkExocc()+"'");
		}
		Map<String,List> result = new HashMap<String,List>();
		result.put("dtlist", dtlist);
		result.put("sqllist", sqllist);
		return result;
	}
	
	
	private void createApplyDt(List<GeneratePdApExListVo> ordlist,User user,ExPdApply appVO,Map<String,Object> paramMap,int euDirect) {
		List<ExPdApplyDetail> dtList=new ArrayList<ExPdApplyDetail>();
		List<String> upSqls=new ArrayList<String>();
	   //重复操作校验标志
	   String checkFlag = null != paramMap.get("checkFlag") ? paramMap.get("checkFlag").toString() : "";
		for (GeneratePdApExListVo exVo : ordlist) {
			ExPdApplyDetail appDt=new ExPdApplyDetail();
			String pkPdapdt=NHISUUID.getKeyId();
			appDt.setPkPdapdt(pkPdapdt);
			appDt.setPkOrg(user.getPkOrg());
			appDt.setPkPdap(appVO.getPkPdap());
			appDt.setEuDirect(euDirect+"");
			appDt.setPkPv(exVo.getPkPv());
			appDt.setPkPd(exVo.getPkPd());
			appDt.setPkCnord(exVo.getPkCnord());
			appDt.setEuDetype(EnumerateParameter.ZERO);
			appDt.setFlagBase(EnumerateParameter.ZERO);
			appDt.setFlagMedout(EnumerateParameter.ZERO);
			appDt.setFlagSelf(EnumerateParameter.ZERO);
			appDt.setPkUnit(exVo.getPkUnit());
			appDt.setPackSize(exVo.getPackSizeStore());
			appDt.setQuanPack(exVo.getQuanOcc()*euDirect);
			appDt.setQuanMin(MathUtils.mul(exVo.getQuanOcc(),CommonUtils.getDouble(exVo.getPackSizeStore()))*euDirect);
			appDt.setOrds(1);
			appDt.setPrice(exVo.getPriceMin());//零售单价
			appDt.setPriceCost(exVo.getPriceMin());//成本价格
			appDt.setAmount(exVo.getAmount()*euDirect);
			appDt.setFlagDe(EnumerateParameter.ZERO);
			appDt.setFlagFinish(EnumerateParameter.ZERO);
			appDt.setFlagStop(EnumerateParameter.ZERO);
			appDt.setFlagPivas(exVo.getFlagPivas());
			appDt.setCreator(user.getPkEmp());
			appDt.setCreateTime(new Date());
			appDt.setTs(new Date());
			appDt.setFlagCanc("0");
			dtList.add(appDt);
			StringBuffer blSql=new StringBuffer("update bl_ip_dt set pk_pdapdt='");
			blSql.append(pkPdapdt);
			blSql.append("',batch_no = '");
			blSql.append(exVo.getBatchNo());
			if (CommonUtils.isEmptyString(exVo.getPkCgip())){
				throw new BusException("生成科室领药未传入有效计费主键，无法生成请领，请检查！");
			}
			blSql.append("' where pk_cgip ='");
			blSql.append(exVo.getPkCgip());
			blSql.append("' and pk_pdapdt is null and pk_dept_ex='");
			blSql.append(user.getPkDept());
			blSql.append("' and date_cg>=to_date('"+paramMap.get("dateStart")+"','YYYYMMDDHH24MISS')"); 
			blSql.append(" and date_cg<=to_date('"+paramMap.get("dateEnd")+"','YYYYMMDDHH24MISS')");
			blSql.append(" and flag_pd = '1' ");
			if("1".equals(checkFlag))
			{
				//开始校验,不完美
				Integer updatedCount = DataBaseHelper.queryForScalar(
						"select count(1) from bl_ip_dt where batch_no=? and pk_cgip=? and pk_pdapdt is not null and date_cg>=to_date(?,'YYYYMMDDHH24MISS') and date_cg<=to_date(?,'YYYYMMDDHH24MISS') and flag_pd = '1'",
							Integer.class, exVo.getBatchNo(), exVo.getPkCgip(), paramMap.get("dateStart"), paramMap.get("dateEnd"));
				if(updatedCount > 0)
				{
					throw new BusException("发生了重复请领，请重新查询后重试!");
				}
			}
			upSqls.add(blSql.toString());
		}
		if(dtList.size()>0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExPdApplyDetail.class),dtList);
		}
		if(upSqls.size()>0){
			DataBaseHelper.batchUpdate(upSqls.toArray(new String[0]));
		}
	}
	/**
	 * 计算仓库单位下的发放数量
	 * @param quan_min -- 基本单位下的数量
	 * @param pack_size--仓库单位下的包装量
	 * @return
	 */
	private double getQuanPack(double quan_min,int pack_size){
		double num = MathUtils.upRound(MathUtils.div(quan_min, CommonUtils.getDouble(pack_size)));
		return num;
	}
	
	
}
