package com.zebone.nhis.cn.ipdw.service;

import com.zebone.nhis.bl.pub.service.OpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.cn.ipdw.aop.OrderVoid;
import com.zebone.nhis.cn.ipdw.dao.CnConsultApply2Mapper;
import com.zebone.nhis.cn.ipdw.dao.CnOpMapper;
import com.zebone.nhis.cn.ipdw.support.Constants;
import com.zebone.nhis.cn.ipdw.vo.CnBlVo;
import com.zebone.nhis.cn.ipdw.vo.CnConsultAllVo;
import com.zebone.nhis.cn.ipdw.vo.CnConsultResponseCaVO;
import com.zebone.nhis.cn.ipdw.vo.OrdCaVo;
import com.zebone.nhis.cn.opdw.service.SyxCnOpApplyOrdService;
import com.zebone.nhis.cn.pub.service.BdSnService;
import com.zebone.nhis.cn.pub.service.CnPubService;
import com.zebone.nhis.common.module.cn.ipdw.CnConsultApply;
import com.zebone.nhis.common.module.cn.ipdw.CnConsultResponse;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnSignCa;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.service.CnNoticeService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import com.zebone.platform.web.support.ResponseJson;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 中二住院医生站：开立会诊申请
 * @author jd_em
 *
 */
@Service
public class CnConsultApply2Service {

	private Logger logger = LoggerFactory.getLogger("nhis.Consult");
	@Resource
	private CnConsultApply2Mapper cnConsultApply2Mapper;

	@Resource
	private BdSnService bdSnService;
	@Autowired
	private CnPubService cnPubService;
	@Autowired
	private CnOpMapper cnOpMapper;

	@Autowired
	private CnNoticeService cnNoticeService;

	@Autowired
	private OpCgPubService opCgPubService;
	@Autowired
	private SyxCnOpApplyOrdService syxCnOpApplyOrdService;
	/**
	 * 004004005010
	 * 查询会诊列表信息
	 * @param param {"pkPv":" 就诊主键"}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryOrderConsultList(String param,IUser user){
		String pkPv=JsonUtil.getFieldValue(param, "pkPv");
		if(pkPv==null || "".equals(pkPv))throw new BusException("未获得当前患者就诊信息！");

		return cnConsultApply2Mapper.qryOrderConsultList(pkPv);
	}

	/**
	 * 004004005011
	 * 查询会诊申请详细信息
	 * @param param {"pkCnord":" 医嘱主键"}
	 * @param user
	 * @return
	 */
	public Map<String,Object> qryConsultInfo(String param,IUser user){
		String pkCnord=JsonUtil.getFieldValue(param, "pkCnord");
		if(pkCnord==null || "".equals(pkCnord))throw new BusException("未获得会诊申请信息！");
		Map<String,Object> consultMap=cnConsultApply2Mapper.qryConsultApplyInfo(pkCnord);
		if(consultMap==null||consultMap.get("pkCons")==null ||"".equals(consultMap))return null;
		List<Map<String,Object>> consultResList=cnConsultApply2Mapper.qryConsultResList(consultMap.get("pkCons").toString());

		consultMap.put("consultResList", consultResList);
		return consultMap;

	}

	/**
	 * 004004005012
	 * 保存会诊申请信息
	 * @param param {"cnOrder":"医嘱信息","cnConsultApply":"会诊信息","consultResList":"应答信息集合"}
	 * @param user
	 */
	public CnConsultAllVo saveConsultAllInfo(String param,IUser user){
		CnConsultAllVo cnConsultAllVo=JsonUtil.readValue(param, CnConsultAllVo.class);
		User doUser=(User)user;
		//1.保存医嘱信息：获取申请单号，医嘱序号，父医嘱号，临时医嘱默认频次
		CnOrder order=cnConsultAllVo.getCnOrder();
		String onceDefCodeFreq = ApplicationUtils.getSysparam("CN0019", false);
	    if(StringUtils.isBlank(onceDefCodeFreq)) onceDefCodeFreq="once";
		order.setCodeFreq(onceDefCodeFreq);
		Date dateNow=new Date();
		order.setDateEnter(dateNow);
		order.setDateStart(dateNow);
		if("".equals(order.getPkCnord())|| order.getPkCnord()==null){
			String codeApply = ApplicationUtils.getCode("0405");
			order.setCodeApply(codeApply);
			int ordsn=bdSnService.getSerialNo("CN_ORDER", "ORDSN", 1, doUser);
			order.setOrdsn(ordsn);
			order.setFlagItc("1");
			order.setOrdsnParent(ordsn);
			DataBaseHelper.insertBean(order);
		}else{
			DataBaseHelper.updateBeanByPk(order, false);
		}
		//2.保存会诊申请记录
		CnConsultApply consult=cnConsultAllVo.getCnConsultApply();
		String conSql="delete from cn_consult_apply where pk_cnord=?";
		DataBaseHelper.execute(conSql, order.getPkCnord());
		consult.setPkCnord(order.getPkCnord());
		consult.setDateApply(dateNow);
		DataBaseHelper.insertBean(consult);
		//3.保存会诊应答记录
		//写入对应的日志
		CnConsultApply cn=DataBaseHelper.queryForBean("select * from cn_consult_response where PK_CONS =?" ,CnConsultApply.class,new Object[]{consult.getPkCons()});
		logger.info("此处saveConsultAllInfo开始删除会诊申请应答，删除前{}",JsonUtil.writeValueAsString(cn));
		String sql="delete from cn_consult_response where pk_cons=?";
		DataBaseHelper.execute(sql, new Object[]{consult.getPkCons()});

		List<CnConsultResponse> resList=cnConsultAllVo.getConsultResList();
		for (CnConsultResponse consultRes : resList) {
			consultRes.setPkOrg(doUser.getPkOrg());
			consultRes.setPkCons(consult.getPkCons());
			consultRes.setPkConsrep(NHISUUID.getKeyId());
			consultRes.setCreateTime(new Date());
			consultRes.setCreator(doUser.getId());
			consultRes.setTs(new Date());
		}
		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnConsultResponse.class), resList);
		return cnConsultAllVo;
	}

	/**
	 * 004004005013
	 * 签署会诊信息
	 * @param param {"pkCnord":" 医嘱主键"}
	 * @param user
	 */
	public void commitConsultInfo(String param,IUser user){
		String pkCnord=JsonUtil.getFieldValue(param, "pkCnord");
		if(pkCnord==null || "".equals(pkCnord))throw new BusException("未获得会诊申请信息！");
		String sql="update cn_order set eu_status_ord='1', flag_sign='1', date_sign=? where  ordsn_parent in (select ordsn_parent from cn_order where pk_cnord = ? ) and  flag_sign='0'";
		DataBaseHelper.execute(sql, new Object[]{new Date(),pkCnord});
	}

	/**
	 * 004004005014
	 * 取消签署会诊申请
	 * @param param {"pkCnord":" 医嘱主键"}
	 * @param user
	 */
	public void unCommitConsultInfo(String param,IUser user){
		CnConsultApply cnConsultApply=JsonUtil.readValue(param, CnConsultApply.class);
		if(cnConsultApply==null || CommonUtils.isEmptyString(cnConsultApply.getPkCnord())){
			throw new BusException("未获得会诊申请信息！");
		}
		String pkCnord=cnConsultApply.getPkCnord();

		//查询当前医嘱信息
		String chkSql="select count(1) from cn_order where pk_cnord=? and eu_status_ord='1' ";
		int count=DataBaseHelper.queryForScalar(chkSql, Integer.class, new Object[]{pkCnord});
		if(count<=0){
			throw new BusException("当前医嘱非签署状态，无法取消签署！");
		}

		//if(pkCnord==null || "".equals(pkCnord))throw new BusException("未获得会诊申请信息！");
		String sql="update cn_order set eu_status_ord='0', flag_sign='0', date_sign=null where ordsn_parent in (select ordsn_parent from cn_order where pk_cnord = ? )  and  flag_sign='1'";
		DataBaseHelper.execute(sql, pkCnord);

		//保存CA认证信息
		if(cnConsultApply.getCnSignCa()!=null &&
				!CommonUtils.isEmptyString(cnConsultApply.getCnSignCa().getEuOptype())){
			List<CnSignCa> caList = new ArrayList<>();
			caList.add(cnConsultApply.getCnSignCa());
			cnPubService.caRecord(caList);
		}
		
		//发送平台消息
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		map.put("unCommitConsultInfo", "");//方法标志（深圳）
		PlatFormSendUtils.sendConsultMsg(map);
	}

	/**
	 * 004004005015
	 * 删除会诊申请
	 * @param param  {"pkCnord":" 医嘱主键"}
	 * @param user
	 */
	public void delConsultInfo(String param,IUser user){
		String pkCnord=JsonUtil.getFieldValue(param, "pkCnord");
		if(pkCnord==null || "".equals(pkCnord))throw new BusException("未获得会诊申请信息！");

		StringBuffer Sql=new StringBuffer("select cn_consult_response.* from cn_consult_response ");
		Sql.append("  where exists (select 1  from cn_consult_apply");
		Sql.append("  where cn_consult_response.pk_cons=cn_consult_apply.pk_cons and cn_consult_apply.pk_cnord=?)");
		CnConsultApply cn=DataBaseHelper.queryForBean(Sql.toString() ,CnConsultApply.class,pkCnord);
		logger.info("此处delConsultInfo开始删除会诊申请应答，删除前{}",JsonUtil.writeValueAsString(cn));

		StringBuffer resSql=new StringBuffer("delete from cn_consult_response ");
		resSql.append("  where exists (select 1  from cn_consult_apply");
		resSql.append("  where cn_consult_response.pk_cons=cn_consult_apply.pk_cons and cn_consult_apply.pk_cnord=?)");
		DataBaseHelper.execute(resSql.toString(), pkCnord);

		String conSql="delete from cn_consult_apply where pk_cnord=?";
		DataBaseHelper.execute(conSql, pkCnord);

		String ifHd=JsonUtil.getFieldValue(param, "ifHd");
		String ordSql="delete from cn_order where ordsn_parent in (select ordsn_parent from cn_order where pk_cnord = ? )  and flag_sign='0'";
		//核对后不允许删除医嘱，提交、签署状态允许删除
		if("1".equals(ifHd)){
			ordSql="delete from cn_order where ordsn_parent in (select ordsn_parent from cn_order where pk_cnord = ? )  and flag_sign < 2 ";
		}
		DataBaseHelper.execute(ordSql, pkCnord);
	}

	/**
	 * 004004005016
	 * 作废会诊申请
	 * @param param {"pkCnord":" 医嘱主键"}
	 * @param user
	 */
	@OrderVoid(param = "param",type = "会诊")
	public void updateConsultInfo(String param,IUser user){
		CnConsultApply cnConsultApply=JsonUtil.readValue(param, CnConsultApply.class);
		String pkCnord=cnConsultApply.getPkCnord();     //JsonUtil.getFieldValue(param, "pkCnord");
		if(pkCnord==null || "".equals(pkCnord))throw new BusException("未获得会诊申请信息！");
		String chkSql="select count(1) from cn_consult_apply where pk_cnord=? and eu_status='0'";
		int count=DataBaseHelper.queryForScalar(chkSql, Integer.class, new Object[]{pkCnord});
		if(count<=0){
			throw new BusException("当前会诊记录已经提交，无法作废！");
		}

		User doUser=(User)user;
		Map<String,Object> paramMap=new HashMap<String, Object>();
		paramMap.put("ordsnParent", cnConsultApply.getOrdsnParent());
		//paramMap.put("pkCnord", cnConsultApply.getPkCnord());
		paramMap.put("dateErasse", new Date());
		paramMap.put("pkEmpErase", doUser.getPkEmp());
		paramMap.put("nameEmpErase", doUser.getNameEmp());

		//cnConsultApply2Mapper.updateConsultInfo(paramMap);
		//作废医嘱是否需要护士核对(只处理作废标志、作废人、作废时间)
		String ifNeedNurseChk = ApplicationUtils.getSysparam("CN0028", false);
		if(ifNeedNurseChk==null) ifNeedNurseChk="0";
		String sqlStr="update cn_order set flag_erase_chk='1', eu_status_ord = '9',flag_erase = '1',date_erase = :dateErasse,pk_emp_erase =:pkEmpErase,name_emp_erase = :nameEmpErase WHERE ordsn_parent = :ordsnParent AND flag_erase = '0'  ";
		if(ifNeedNurseChk.equals("1")){
			sqlStr = "update cn_order set flag_erase = '1',date_erase = :dateErasse,pk_emp_erase =:pkEmpErase,name_emp_erase = :nameEmpErase WHERE ordsn_parent = :ordsnParent AND flag_erase = '0'  ";
		}
		DataBaseHelper.execute("delete from CP_REC_EXP where PK_CNORD = ?",pkCnord);
		DataBaseHelper.update(sqlStr, paramMap);
		
		//发送平台消息
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		map.put("updateConsultInfo", "");//方法标志（深圳）
		PlatFormSendUtils.sendConsultMsg(map);
	}

	/**
	 * 004004005017
	 * 查询会诊应答科室列表
	 * @param param {"pkCons":" 会诊申请主键"}
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> qryConsultResList(String param,IUser user){
		String pkCons=JsonUtil.getFieldValue(param, "pkCons");
		String euType=JsonUtil.getFieldValue(param, "euType");

		if(pkCons==null || "".equals(pkCons))throw new BusException("未获得会诊申请信息！");

		if(StringUtils.isNotBlank(euType) && "2".equals(euType)){
			String sql = "select rsp.* from cn_consult_response rsp where rsp.pk_cons = ?";
			List<Map<String, Object>> list = DataBaseHelper.queryForList(sql,pkCons);
			return list;
		}
		List<Map<String,Object>> consultResList=cnConsultApply2Mapper.qryConsultResList(pkCons);
		return consultResList;
	}

	/**
	 * 004004005018
	 * 保存会诊申请信息（huanghaisheng重写）
	 * @param param
	 * @param user
	 */
	public CnConsultApply saveConsult(String param , IUser user) throws IllegalAccessException, InvocationTargetException{
		CnConsultApply rtn=new CnConsultApply();
		CnConsultApply cnConsultApplyVo=JsonUtil.readValue(param, CnConsultApply.class);
		List<CnConsultResponse> consultResList = cnConsultApplyVo.getConsultResList();
		List<CnConsultResponse> consultResListForDel = cnConsultApplyVo.getConsultResListForDel();
		Date dateNow = new Date();
		User userInfo = (User)user;
		Date dd = cnPubService.getOutOrdDate(cnConsultApplyVo.getPkPv());
		if (dd != null && dd.compareTo(dateNow) < 0) {
			dateNow = dd;
		}

		String pkWgOrg=null;
		String pkWg=null;
		if(org.apache.commons.lang3.StringUtils.isNotEmpty(cnConsultApplyVo.getPkPv())){
			//判断是否符合开立条件
			if(checkPvInfo(cnConsultApplyVo.getPkPv())){
				throw  new BusException("前患者就诊的科室为日间病房,住院超过允许开立医嘱的天数！不允许开立新医嘱！");
			}

			PvEncounter pvInfo = DataBaseHelper.queryForBean("Select * From PV_ENCOUNTER Where pk_pv=? ",PvEncounter.class, new Object[]{cnConsultApplyVo.getPkPv()});
			pkWgOrg=pvInfo.getPkWgOrg();
			pkWg=pvInfo.getPkWg();
		}


		CnOrder order = null;
		if( cnConsultApplyVo.getPkCnord() != null)
			order = cnOpMapper.selectOrder(cnConsultApplyVo.getPkCnord());
		else
			order = new CnOrder();

		String euPvType = "3";
		if(StringUtils.isNotBlank(cnConsultApplyVo.getPkPv())){
			euPvType = MapUtils.getString(DataBaseHelper.queryForMap("select eu_pvtype from pv_encounter where pk_pv = ?",
					new Object[]{cnConsultApplyVo.getPkPv()}),"euPvtype","3");
		}
		if(StringUtils.isBlank(cnConsultApplyVo.getPkCons())){
			order.setPkCnord(NHISUUID.getKeyId());
			order.setEuAlways("1");						//临时
			order.setOrdsn(bdSnService.getSerialNo("CN_ORDER", "ORDSN", 1, user));
			order.setOrdsnParent(order.getOrdsn());
			order.setDescOrd(order.getNameOrd());
			order.setCodeFreq(ApplicationUtils.getSysparam("CN0019", false));
			order.setQuanCg((double)consultResList.size());
			order.setQuan((double)consultResList.size()); 			//医嘱的数量
			order.setDosage(1.0);
			order.setEuStatusOrd("0");		//保存
			order.setEuPvtype(euPvType);
			order.setFlagDoctor("1");
			order.setFlagErase("0");
			order.setFlagStopChk("0");
			order.setFlagStop("0");
			order.setFlagEraseChk("0");
			order.setFlagItc("1");
			order.setEuIntern("0");
			order.setFlagFirst(Constants.FALSE);
			order.setFlagDurg(Constants.FALSE);
			order.setFlagSelf(Constants.FALSE);
			order.setFlagNote(Constants.FALSE);
			order.setFlagBase(Constants.FALSE);
			order.setFlagBl(Constants.FALSE);
			order.setFlagStop(Constants.FALSE);
			order.setFlagCp(Constants.FALSE);
			order.setFlagPrint(Constants.FALSE);
			order.setFlagMedout(Constants.FALSE);
			order.setFlagEmer(Constants.FALSE);
			order.setFlagThera(Constants.FALSE);
			order.setFlagPrev(Constants.FALSE);
			order.setFlagFit(Constants.FALSE);
			order.setTs(dateNow);
			order.setDateEnter(dateNow);    //暂用服务器时间，db时间后期处理
			order.setDateStart(dateNow);
			order.setInfantNo(0);
			order.setFlagSign("0");


			order.setPkDept(userInfo.getPkDept());
			order.setPkEmpInput(userInfo.getPkEmp());
			order.setNameEmpInput(userInfo.getUserName());
			order.setPkEmpOrd(userInfo.getPkEmp());
			order.setNameEmpOrd(userInfo.getUserName());

			order.setCodeOrdtype(cnConsultApplyVo.getCodeOrdtype());
			order.setCodeOrd(cnConsultApplyVo.getCodeOrd());
			order.setPkOrgExec(cnConsultApplyVo.getPkOrgExec());
			order.setPkDeptExec(cnConsultApplyVo.getPkDeptExec());
			order.setPkDeptNs(cnConsultApplyVo.getPkDeptNs());
			order.setCodeApply(cnConsultApplyVo.getCodeApply());
			order.setNameOrd(cnConsultApplyVo.getNameOrd());
			order.setPkPv(cnConsultApplyVo.getPkPv());
			order.setPkPi(cnConsultApplyVo.getPkPi());
			order.setPkOrg(cnConsultApplyVo.getPkOrg());
			order.setPriceCg(cnConsultApplyVo.getPriceCg());
			order.setFlagBl(cnConsultApplyVo.getFlagBl());
			order.setPkOrd(cnConsultApplyVo.getPkOrd());
			order.setFlagEmer(cnConsultApplyVo.getFlagEmer());
			order.setNoteOrd(cnConsultApplyVo.getNoteOrd());
			//路径变异记录
			order.setPkCprec(cnConsultApplyVo.getPkCprec());
			order.setPkCpexp(cnConsultApplyVo.getPkCpexp());
			order.setExpNote(cnConsultApplyVo.getExpNote());
			order.setPkCpphase(cnConsultApplyVo.getPkCpphase());
			order.setNameExp(cnConsultApplyVo.getNameExp());

			order.setDateEffe(getDateEffec(euPvType));
			order.setDays(EnumerateParameter.THREE.equals(euPvType)?null:1L);

			order.setPkWgOrg(pkWgOrg);
			order.setPkWg(pkWg);
			cnConsultApplyVo.setPkCons(NHISUUID.getKeyId());
			cnConsultApplyVo.setTs(dateNow);
			cnConsultApplyVo.setDelFlag(Constants.FALSE);
			cnConsultApplyVo.setEuStatus(EnumerateParameter.THREE.equals(euPvType)?"0":"1");
			cnConsultApplyVo.setPkOrg(userInfo.getPkOrg());
			cnConsultApplyVo.setDateApply(dateNow);

			if (StringUtils.isBlank(order.getPkOrd())) {
				throw new BusException("数据有误，请重新开立！");
			}

			DataBaseHelper.insertBean(order);
			cnConsultApplyVo.setPkCnord(order.getPkCnord());
			try
			{
				DataBaseHelper.insertBean(cnConsultApplyVo);
			}
			catch(Exception e)
			{
				logger.error("insert error-", e);
				String errorMsg = e.getMessage();
				if(errorMsg != null && (errorMsg.indexOf("ORA-01461") >= 0 || (errorMsg.indexOf("ORA-12899") >= 0 && errorMsg.indexOf("最大值: 1500") >= 0)))
				{
					throw new BusException("病情描述内容超长，最大只能录入1500个汉字或英文字符.");
				}
				else
				{
					throw e;
				}
			}
		}else{
			order.setNameOrd(cnConsultApplyVo.getNameOrd());
			order.setDescOrd(order.getNameOrd());
			order.setTs(dateNow);
			order.setCodeOrdtype(cnConsultApplyVo.getCodeOrdtype());
			order.setCodeOrd(cnConsultApplyVo.getCodeOrd());
			order.setPkOrgExec(cnConsultApplyVo.getPkOrgExec());
			order.setPkDeptExec(cnConsultApplyVo.getPkDeptExec());
			order.setPkDeptNs(cnConsultApplyVo.getPkDeptNs());
			order.setCodeApply(cnConsultApplyVo.getCodeApply());
			order.setNameOrd(cnConsultApplyVo.getNameOrd());
			order.setPkPv(cnConsultApplyVo.getPkPv());
			order.setPkPi(cnConsultApplyVo.getPkPi());
			order.setPkOrg(cnConsultApplyVo.getPkOrg());
			order.setPriceCg(cnConsultApplyVo.getPriceCg());
			order.setFlagBl(cnConsultApplyVo.getFlagBl());
			order.setPkOrd(cnConsultApplyVo.getPkOrd());
			order.setFlagEmer(cnConsultApplyVo.getFlagEmer());
			order.setNoteOrd(cnConsultApplyVo.getNoteOrd());
			order.setQuanCg((double)consultResList.size());
			order.setQuan((double)consultResList.size()); 			//医嘱的数量
			//路径变异记录
			order.setPkCprec(cnConsultApplyVo.getPkCprec());
			order.setPkCpexp(cnConsultApplyVo.getPkCpexp());
			order.setExpNote(cnConsultApplyVo.getExpNote());
			order.setPkCpphase(cnConsultApplyVo.getPkCpphase());
			order.setNameExp(cnConsultApplyVo.getNameExp());

			order.setPkWgOrg(pkWgOrg);
			order.setPkWg(pkWg);
			cnConsultApplyVo.setTs(dateNow);
			CnConsultApply applyLog = DataBaseHelper.queryForBean("select * from cn_consult_apply where pk_cons=?", CnConsultApply.class, cnConsultApplyVo.getPkCons());
			cnConsultApplyVo.setEuStatus(applyLog.getEuStatus());

			DataBaseHelper.updateBeanByPk(order, false);
			try
			{
				DataBaseHelper.updateBeanByPk(cnConsultApplyVo,false);
			}
			catch(Exception e)
			{
				logger.info("update error-", e);
				String errorMsg = e.getMessage();
				if(errorMsg != null && (errorMsg.indexOf("ORA-01461") >= 0 || (errorMsg.indexOf("ORA-12899") >= 0 && errorMsg.indexOf("最大值: 1500") >= 0)))
				{
					throw new BusException("病情描述内容超长，最大只能录入1500个汉字或英文字符.");
				}
				else
				{
					throw e;
				}
			}
		}

		List<CnConsultResponse> addConResList= new ArrayList<CnConsultResponse>();
		List<CnConsultResponse> updateConResList = new ArrayList<CnConsultResponse>();
		if(consultResList != null)
		{
			for(CnConsultResponse conRes: consultResList ){
				conRes.setPkCons(cnConsultApplyVo.getPkCons());
				conRes.setFlagFinish(StringUtils.isBlank(conRes.getFlagFinish())?EnumerateParameter.ZERO:conRes.getFlagFinish());
				CnConsultResponse res = new CnConsultResponse();
				BeanUtils.copyProperties(res, conRes);
				if(StringUtils.isBlank(res.getPkConsrep())){
					res.setPkConsrep(NHISUUID.getKeyId());
					res.setTs(dateNow);
					res.setCreateTime(dateNow);
					res.setCreator(userInfo.getPkEmp());
					addConResList.add(res);
				}else{
					updateConResList.add(res);
				}
			}
		}
		if (consultResListForDel != null && consultResListForDel.size() > 0) {
			StringBuffer Sql=new StringBuffer("select * from cn_consult_response where PK_CONSREP = ? ");
			CnConsultApply cn=DataBaseHelper.queryForBean(Sql.toString() ,CnConsultApply.class,consultResListForDel.get(0).getPkConsrep());
			logger.info("此处saveConsult开始删除会诊申请应答，删除前{}",JsonUtil.writeValueAsString(cn));

			DataBaseHelper.batchUpdate("delete from cn_consult_response where PK_CONSREP = :pkConsrep ", consultResListForDel);
		}
		if (addConResList != null && addConResList.size() > 0) {
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(CnConsultResponse.class), addConResList);
		}
		if (updateConResList != null && updateConResList.size() > 0) {
			DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(CnConsultResponse.class), updateConResList);
		}

		List<CnConsultResponse> rtnCnConsultResponseList= DataBaseHelper.queryForList("select * from cn_consult_response where del_flag='0' and PK_CONS = ?", CnConsultResponse.class, new Object[] { cnConsultApplyVo.getPkCons() });
		BeanUtils.copyProperties(rtn, cnConsultApplyVo);
		rtn.setConsultResList(rtnCnConsultResponseList);
		rtn.setConsultCnOrd(order);
		return rtn;
	}

	/**
	 * 004004005019
	 * 签署会诊申请（重写）
	 */
	public void signConsult(String param,IUser user) throws IllegalAccessException, InvocationTargetException{
		//签署前先保存
		CnConsultApply cnConsultApply=  this.saveConsult(param,user);
		CnOrder order=cnConsultApply.getConsultCnOrd();
		List<CnOrder> cnOrdList=new ArrayList<CnOrder>();
		cnOrdList.add(order);
		cnPubService.recExpOrder(false,cnOrdList,(User)user); //路径外医嘱变异
		User userInfo = (User)user;
		String pkCnord=cnConsultApply.getPkCnord();
		if(pkCnord==null || "".equals(pkCnord))throw new BusException("未获得会诊申请信息！");

		String sql="update cn_order set eu_status_ord='1', flag_sign='1', date_sign=? ,pk_emp_ord=?,name_emp_ord=? where  ordsn_parent in (select ordsn_parent from cn_order where pk_cnord = ? ) and  flag_sign='0'";
		DataBaseHelper.execute(sql, new Object[]{new Date(),userInfo.getPkEmp(),userInfo.getNameEmp(),pkCnord});

		//保存CA认证信息
		if(cnConsultApply.getCnSignCa()!=null &&
				!CommonUtils.isEmptyString(cnConsultApply.getCnSignCa().getEuOptype())){
			List<CnSignCa> caList = new ArrayList<>();
			cnConsultApply.getCnSignCa().setPkBu(pkCnord);//CA认证业务主键赋值
			caList.add(cnConsultApply.getCnSignCa());
			cnPubService.caRecord(caList);
		}

		//保存临床提醒消息
		cnNoticeService.saveCnNotice(cnOrdList);
		
		//发送平台消息
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		map.put("signConsult", "");//方法标志（深圳）
		map.put("pkCnord", pkCnord);
		PlatFormSendUtils.sendConsultMsg(map);		
	}

	/**
	 * 保存会诊应答信息  -- 004004005020
	 * @param param
	 * @param user
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public CnConsultResponse saveResponse(String param,IUser user) throws IllegalAccessException, InvocationTargetException{

		CnConsultResponseCaVO response=JsonUtil.readValue(param, CnConsultResponseCaVO.class);

		CnConsultResponse resp = new CnConsultResponse();
		BeanUtils.copyProperties(resp,response);
		//校验
		if(response==null)
			throw new BusException("未获得会诊应答信息！");
		if(response.getIsEditing() == 1) {
			//编辑
			String sql="update cn_consult_response set con_reply=:conReply,pk_emp_input=:pkEmpInput,name_emp_input=:nameEmpInput," +
					"con_advice=:conAdvice,date_rep=:dateRep where pk_cons=:pkCons and pk_dept_rep=:pkDeptRep ";
			DataBaseHelper.update(sql, resp);
		}
		else {
			DataBaseHelper.updateBeanByPk(resp,false);
		}
		return resp;
	}


	/**
	 * 提交会诊应答信息 -- 004004005021
	 * @param param
	 * @param user
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public int submitResponse(String param,IUser user) throws IllegalAccessException, InvocationTargetException{

		CnConsultResponseCaVO response=JsonUtil.readValue(param, CnConsultResponseCaVO.class);
		logger.info("会诊应答提交前保存{}",param);
		//1.先调用保存
		CnConsultResponse resp = saveResponse(param,user);
		//校验
		if(response==null)
			throw new BusException("未获得会诊应答信息！");

		//保存CA认证信息
		if(response.getCnSignCa()!=null &&
				!CommonUtils.isEmptyString(response.getCnSignCa().getEuOptype())){
			List<CnSignCa> caList = new ArrayList<>();
			response.getCnSignCa().setPkBu(response.getPkConsrep());//CA认证业务主键赋值
			caList.add(response.getCnSignCa());
			cnPubService.caRecord(caList);
		}

		CnConsultApply cn=DataBaseHelper.queryForBean("select * from cn_consult_apply where PK_CONS =?" ,CnConsultApply.class,resp.getPkCons());
		logger.info("此处开始修改会诊申请单状态，修改前{}",JsonUtil.writeValueAsString(cn));

		//当所前会诊科室应答完成后改申请单状态为会诊
		String sql="update cn_consult_apply set EU_STATUS='2' where " +
				"exists( select 1 from  CN_CONSULT_RESPONSE t where t.PK_CONS =cn_consult_apply.PK_CONS " +
				"and (t.flag_finish = '0' or t.flag_finish is null)) and PK_CONS =:pkCons and EU_STATUS = '1'";
		DataBaseHelper.update(sql, resp);

		cn=DataBaseHelper.queryForBean("select * from cn_consult_apply where PK_CONS = ? " ,CnConsultApply.class,resp.getPkCons());
		logger.info("修改后会诊申请单状态前{}",JsonUtil.writeValueAsString(cn));

		//计费--判断是否启用计费，并处理 1：启用 0：不启用  当所前会诊科室应答完成后改计费
		if (EnumerateParameter.ONE.equals(ApplicationUtils.getSysparam("BL0058", false))) {
			//医嘱信息
			List<CnOrder> orders=DataBaseHelper.queryForList("select * from cn_order where pk_cnord=? ",CnOrder.class,response.getPkCnord());
			CnOrder order=orders.get(0);
			//收费项目信息
			Map<String,Object> mapQ=new HashMap<String,Object>();
			mapQ.put("pkOrg",order.getPkOrg());
			mapQ.put("pkOrd",order.getPkOrd());
			List<Map<String,Object>> list = cnConsultApply2Mapper.queryItemBySrv(mapQ);
			List<CnBlVo> ipDtList=new ArrayList<>();
			for (Map<String,Object> map: list) {
				CnBlVo vo = new CnBlVo();
				vo.setPkOrg(order.getPkOrg());
				vo.setEuPvType(order.getEuPvtype());
				vo.setPkPv(order.getPkPv());
				vo.setPkPi(order.getPkPi());
				vo.setPkOrd(order.getPkOrd());
				vo.setpkCnord(order.getPkCnord());
				vo.setPkItem(map.get("pkItem").toString());
				vo.setPkUnitCg(order.getPkUnitCg());
				vo.setPkOrgEx(order.getPkOrgExec());
				vo.setPkOrgApp(order.getPkOrg());
				vo.setPkDeptEx(order.getPkDeptExec());
				vo.setPkDeptApp(order.getPkDept());
				vo.setPkDeptNsApp(order.getPkDeptNs());
				vo.setPkEmpApp(order.getPkEmpOrd());
				vo.setNameEmpApp(order.getNameEmpOrd());
				vo.setFlagPd(map.get("flagPd").toString());
				vo.setFlagPv("0");
				vo.setDateHap(new Date());
				vo.setPkDeptCg(resp.getPkDeptRep());
				vo.setPkEmpCg(resp.getPkEmpRep());
				vo.setNameEmpCg(resp.getNameEmpRep());
				vo.setName(map.get("name").toString());
				vo.setSpec(order.getSpec());
				vo.setPrice(Double.parseDouble(map.get("price").toString()));
				vo.setQuanCg(Double.parseDouble("1"));
				vo.setAmount(vo.getPrice()*vo.getQuanCg());
				vo.setAmountPi(vo.getAmount());
				vo.setPkEmpEx(resp.getPkEmpRep());
				vo.setNameEmpEx(resp.getNameEmpRep());
				vo.setEuBltype("9");
				ipDtList.add(vo);
			}
			if (ipDtList.size()>0){
				ApplicationUtils apputil = new ApplicationUtils();
				ResponseJson rs = apputil.execService("BL", "IpCgPubService",
						"savePatiCgInfo", ipDtList, user);
			}
		}

		//当所有会诊科室应答完成后改申请单状态为完成
		sql="update cn_consult_apply set EU_STATUS='3' where not " +
				"exists( select 1 from  CN_CONSULT_RESPONSE t where t.PK_CONS =cn_consult_apply.PK_CONS " +
				"and (t.flag_finish = '0' or t.flag_finish is null)) and PK_CONS =:pkCons and EU_STATUS >= '1'";
		int nUpdateCount = DataBaseHelper.update(sql, resp);
		cn=DataBaseHelper.queryForBean("select * from cn_consult_apply where PK_CONS =? " ,CnConsultApply.class,resp.getPkCons());
		logger.info("全部修改完成后会诊申请单状态前{}",JsonUtil.writeValueAsString(cn));

		//发送平台消息
		Map<String,Object> map = JsonUtil.readValue(param, Map.class);
		map.put("submitResponse", "");//方法标志（深圳）
		PlatFormSendUtils.sendConsultResponeMsg(map);		
		
		return nUpdateCount;
	}
	/**
	 * 根据会诊应答查询签名图片
	 * @param param
	 * @param user
	 * @return
	 */
	public OrdCaVo qryConRespEmp(String param, IUser user) {
		String pkConsrep = JsonUtil.getFieldValue(param, "pkConsrep");
		OrdCaVo ordCaVo = cnConsultApply2Mapper.qryConRespEmp(pkConsrep);
		return ordCaVo;
	}

	private Date getDateEffec(String euPvType) {

		String val = ApplicationUtils.getSysparam("CN0004", false);
		if (StringUtils.isEmpty(val)) {
			if (EnumerateParameter.TWO.equals(euPvType))// 急诊
			{
				val = EnumerateParameter.TWO;
			} else {// 门诊
				val = EnumerateParameter.THREE;
			}

		}
		Date dateEffec = DateUtils.getSpecifiedDay(new Date(), Integer.parseInt(val));
		return dateEffec;
	}

	/**
	 * 保存门诊会诊，并计费
	 * @param param
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public CnConsultApply saveOpConsult(String param , IUser user) throws Exception{
		CnConsultApply consultApply = saveConsult(param, user);
		List<BlPubParamVo> blOpCgList = new ArrayList<BlPubParamVo>();
		CnOrder cnOrder = consultApply.getConsultCnOrd();
		//删除该医嘱对应的计费信息
		boolean flagSt = true;
		if(cnOrder.getPkCnord()!=null){
			//校验费用明细是否已经结算，结算过则不处理费用明细
			Integer stCnt = DataBaseHelper.queryForScalar(
					"select count(pk_cgop) from bl_op_dt where pk_pv = ? and pk_cnord = ? and pk_settle is not null",
					Integer.class,new Object[]{cnOrder.getPkPv(),cnOrder.getPkCnord()});

			if(stCnt!=null && stCnt<=0){
				flagSt = false;
				DataBaseHelper.execute("delete from bl_op_dt where pk_cnord=?",cnOrder.getPkCnord());
			}
		}

		//然后新增
		cnOrder.setPackSize(1d);
		syxCnOpApplyOrdService.opOrdToOpCg(cnOrder,blOpCgList);
		if(blOpCgList.size()>0 && !flagSt){
			opCgPubService.blOpCgBatch(blOpCgList);
		}
		return consultApply;
	}

	public void delOpConsultInfo(String param,IUser user){
		String pkCnord=JsonUtil.getFieldValue(param, "pkCnord");
		int hadSettleCgCount = DataBaseHelper.queryForScalar("select count(1) from bl_op_dt where pk_cnord =? and flag_settle='1' and del_flag='0'", Integer.class, new Object[]{pkCnord});
		if(hadSettleCgCount>0){
			throw new BusException("有收费项已进行缴费，不能操作");
		}
		if(DataBaseHelper.update("delete from bl_op_dt where pk_cnord =? and flag_settle='0'", new Object[]{pkCnord}) ==0){
			throw new BusException("有收费项已进行缴费，删除失败");
		}
		if(DataBaseHelper.queryForScalar("select Count(1) from  cn_consult_apply Where pk_cnord=? And eu_status<>'1'",Integer.class, new Object[]{pkCnord}) >0){
			throw new BusException("已经有应答不能删除");
		}
		delConsultInfo(param, user);
	}

	public List<Map<String, Object>> qryConsultAndPiInfo(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, new TypeReference<Map<String, Object>>() {});
		User u = (User)user;
		if(paramMap == null){
			paramMap = new HashMap<>();
		}
		String dateBegin = MapUtils.getString(paramMap, "dateBegin");
		String dateEnd = MapUtils.getString(paramMap, "dateEnd");
		paramMap.put("dateBegin", StringUtils.isNotBlank(dateBegin) && dateBegin.length()>8?dateBegin.substring(0,8):dateBegin);
		paramMap.put("dateEnd",StringUtils.isNotBlank(dateEnd) && dateEnd.length()>8?dateEnd.substring(0,8):dateEnd);
		paramMap.put("pkDept", u.getPkDept());
		paramMap.put("pkEmp", u.getPkEmp());
		return cnConsultApply2Mapper.qryConsultApplyAndPiInfo(paramMap);
	}

	/*************************************医嘱属性 前端不需要处理 start**************************************/
	//处理日间病房医嘱开立期限的校验
	private boolean checkPvInfo(String pkPv){
		boolean ok=false;//默认通过校验
		String day=ApplicationUtils.getSysparam("CN0107", false);
		String sql="Select PV.* From pv_encounter pv \n" +
				"Inner Join bd_dictattr dict On pv.pk_dept=dict.pk_dict \n" +
				"Where  pv.pk_pv=? And dict.code_attr='0605' And dict.val_attr='1'";
		PvEncounter pvEncounter=DataBaseHelper.queryForBean(sql,PvEncounter.class,new Object[]{pkPv});
		if(org.apache.commons.lang3.StringUtils.isNotEmpty(day) && pvEncounter!=null){
			int dayIp= DateUtils.getDateSpace(pvEncounter.getDateBegin(), new Date());
			int dayI= Integer.parseInt(day);
			//判断
			if(dayI>0 && dayI<dayIp) ok=true;
		}
		return ok;
	}
	/*************************************医嘱属性 前端不需要处理 end**************************************/
}
