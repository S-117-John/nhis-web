package com.zebone.nhis.ex.pub.service;

import com.zebone.nhis.base.pub.service.BdResPubService;
import com.zebone.nhis.common.module.base.bd.res.BdResBed;
import com.zebone.nhis.common.module.base.bd.srv.BdItem;
import com.zebone.nhis.common.module.labor.nis.PvLabor;
import com.zebone.nhis.common.module.labor.nis.PvLaborRec;
import com.zebone.nhis.common.module.labor.nis.PvLaborRecDt;
import com.zebone.nhis.common.module.pi.PiCate;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvInfant;
import com.zebone.nhis.common.module.pv.PvInsurance;
import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.nhis.common.module.pv.support.PvConstant;
import com.zebone.nhis.common.support.*;
import com.zebone.nhis.ex.pub.dao.PvInfantPubMapper;
import com.zebone.nhis.ex.pub.support.ExSysParamUtil;
import com.zebone.nhis.ex.pub.vo.BabyBedVO;
import com.zebone.nhis.ex.pub.vo.BdDaycgSetItemVo;
import com.zebone.nhis.ex.pub.vo.PvEncounterVo;
import com.zebone.nhis.ex.pub.vo.PvInfantVo;
import com.zebone.nhis.pi.pub.service.PiPubService;
import com.zebone.nhis.pi.pub.vo.PiMasterParam;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
/**
 * 婴儿服务公共类（增删改查及就诊流程）
 * @author yangxue
 *
 */
@Service
public class PvInfantPubService {
	
	@Resource
	private PiPubService piPubService;
	@Resource
	private PvInfantPubMapper pvInfantPubMapper;
	@Resource
	private BdResPubService bdResPubService;//处理婴儿床位公共类
	@Resource
	private PatiDeptInAndOutPubService piDeptInAndOutPubService;//处理入科、取消入科操作
	
	/**
	 * 保存婴儿信息(含就诊信息)
	 * @param infant
	 */
    public PvInfantVo saveInfant(PvInfantVo vo,User user){
    	if(CommonUtils.isEmptyString(vo.getPkInfant())){
    		vo.setPkInfant(NHISUUID.getKeyId());//设置婴儿主键
    	}
    	vo.setEuStatusAdt("0");//设置婴儿转科状态
    	if(vo.getSortNo()==0||vo.getSortNo()==null){//未设置婴儿序号，取最大婴儿序号加1
    		vo.setSortNo(ExSysParamUtil.getInfantSortNo(vo.getPkPv()));
    	}
    	//设置婴儿编码，规则同婴儿的住院号
    	vo.setCode(ExSysParamUtil.getInfantCodeIp(vo.getPkPv()));
    	//先插入婴儿患者信息
    	PvEncounterVo pv_inf = savePiMaster(vo);
    	//插入婴儿虚拟就诊信息
    	savePvInfo(vo,pv_inf,user);
    	//插入婴儿信息
    	PvInfant infant = new PvInfant();
    	ApplicationUtils.copyProperties(infant, vo);
    	DataBaseHelper.insertBean(infant);
    	//更新pv_ip表的新生儿字段
    	String sql = "update pv_ip set flag_infant = '1',quan_infant = nvl(quan_infant,0) + 1 where pk_pv = '"+vo.getPkPv()+"'";
    	DataBaseHelper.update(sql, new Object());
    	//查询该婴儿母亲是否在产房就诊（如果未入产房，则该婴儿不在产房生成就诊记录以及床位信息）
		String queSql = "select * from pv_labor where pk_pv = ? and eu_status = '1'";
		PvLabor pvLabor = DataBaseHelper.queryForBean(queSql,PvLabor.class,vo.getPkPv());
    	if(pvLabor != null){
			//自动生成产房婴儿床位，新增婴儿产房就诊信息
			saveLaborForChd(vo,user,pv_inf);
		}
    	//2019-07-02：婴儿入科,先校验是否允许入科分床
    	boolean isAllowDeptIn = ExSysParamUtil.getFlagAddInfByDeptNs(pv_inf.getPkDeptNs(), user.getPkOrg());
		
    	//2020-02-25 手动分床模式在此不分床
    	String BD0013 = ApplicationUtils.getSysparam("BD0013",false);
    	if(!"1".equals(BD0013)){
    		if (vo.getBabyBed() != null && !CommonUtils.isEmptyString(vo.getBabyBed().getPkBed())) {
				packBedForInfManual(vo,user);
			}
		}else{
			PvInfantVo infantVo = new PvInfantVo();
			ApplicationUtils.copyProperties(infantVo, vo);
			infantVo.setHpcode(vo.getHpcode());
			if(isAllowDeptIn){
				packBedForInf(infantVo,user,new Date());
			} else if("0".equals(infantVo.getFlagDept()) && infantVo.getBabyBed() != null){
				packBedForInf(infantVo,user,new Date());
			}
		} 	
    	
    	//savePvRecInfo(vo,infant);//保存孕产 + 婴儿分娩信息
    	return vo;
    }

    public void saveLaborForChd(PvInfantVo vo,User user,PvEncounterVo pvMom){
    	//获取母亲在产房的就诊记录
		PvLabor pvLaborM = DataBaseHelper.queryForBean("select * from pv_labor where pk_pv = ?",PvLabor.class,vo.getPkPv());
		BdResBed bedMa = new BdResBed();
		String deptNs = "";
		String bedNo = "";
		//获取母亲所在产房床位,母亲如果不在产房根据产房空床生成一个婴儿床位
		if(pvLaborM != null){
			bedMa = DataBaseHelper.queryForBean("select * from bd_res_bed where pk_pi = ? and pk_ward = ? and code = ? "
					, BdResBed.class, new Object[]{CommonUtils.getString(pvMom.getPkPi()),
							CommonUtils.getString(pvLaborM.getPkDeptNs()),pvLaborM.getBedNo()});
			if(bedMa == null){
				bedMa = DataBaseHelper.queryForBean("select * from bd_res_bed where pk_ward = ? and code = ? "
						, BdResBed.class, new Object[]{CommonUtils.getString(pvLaborM.getPkDeptNs()),pvLaborM.getBedNo()});
			}
			deptNs = pvLaborM.getPkDeptNs();
			bedNo = pvLaborM.getBedNo();
			if(pvLaborM != null && bedMa == null){
                String queSql = " select * " +
                        " from bd_res_bed bed " +
                        " inner join BD_OU_DEPT dept on dept.PK_DEPT = bed.PK_WARD and dept.DEL_FLAG = '0' " +
                        " where bed.del_flag = '0' " +
                        " and bed.FLAG_ACTIVE = '1' " +
                        " and bed.FLAG_OCUPY = '0' " +
                        " and dept.DT_DEPTTYPE = '0310' ";
                List<BdResBed> beds = DataBaseHelper.queryForList(queSql,BdResBed.class);
                bedMa = beds.get(0);
                deptNs = bedMa.getPkWard();
                bedNo = bedMa.getCode();
            }
		}else{
			String queSql = " select * " +
							" from bd_res_bed bed " +
							" inner join BD_OU_DEPT dept on dept.PK_DEPT = bed.PK_WARD and dept.DEL_FLAG = '0' " +
							" where bed.del_flag = '0' " +
							" and bed.FLAG_ACTIVE = '1' " +
							" and bed.FLAG_OCUPY = '0' " +
							" and dept.DT_DEPTTYPE = '0310' ";
			List<BdResBed> beds = DataBaseHelper.queryForList(queSql,BdResBed.class);
			bedMa = beds.get(0);
			deptNs = bedMa.getPkWard();
			bedNo = bedMa.getCode();
		}

		String bedSpc = ExSysParamUtil.getSpcOfCodeBed();//获取婴儿床位分隔符
		String sortNo = vo.getSortNo().toString();//获取婴儿序号
		//产房自动生成婴儿床位
		List<BdResBed> bedList = bdResPubService.isHaveBedAndAdd(deptNs, bedMa, bedNo + bedSpc + sortNo, null, (User)user);
		if(null == bedList || bedList.size() < 1)
			throw new BusException("维护婴儿床位失败，无法保存婴儿信息！");

		String pkBedInf = bedList.get(0).getPkBed();
		int cntBed = DataBaseHelper.update("update bd_res_bed set flag_active = '1', del_flag = '0',pk_pi = ?,pk_item = null,eu_status='02',FLAG_OCUPY = '1',DT_BEDTYPE = '03'"
				                             + " where pk_bed = ? and del_flag = '1' and pk_pi is null", new Object[]{vo.getPkPiInfant(),pkBedInf});
		if(cntBed < 1)
			throw new BusException("当前婴儿床位已被占用，无法保存婴儿信息！");
		Date date = new Date();
		//保存婴儿的产房就诊信息
		PvLabor pvLaborChd = new PvLabor();
		pvLaborChd.setPkPvlabor(NHISUUID.getKeyId());
		pvLaborChd.setFlagIn("1");
		pvLaborChd.setPkPv(vo.getPkPvInfant());
		pvLaborChd.setBedNo(bedList.get(0).getCode());
		pvLaborChd.setEuStatus("1");
		pvLaborChd.setFlagInfant("1");
		pvLaborChd.setDateIn(date);
		if(vo.getBabyBed()!=null){
			pvLaborChd.setPkEmpDoctor(vo.getBabyBed().getPkIpPsnMain());
			pvLaborChd.setNameEmpDoctor(vo.getBabyBed().getNameIpPsnMain());
			pvLaborChd.setPkEmpNurse(vo.getBabyBed().getPkIpPsnNs());
			pvLaborChd.setNameEmpNurse(vo.getBabyBed().getNameIpPsnNs());
		}
		if(pvLaborM != null){
			pvLaborChd.setPkOrg(pvLaborM.getPkOrg());
			pvLaborChd.setPkPvlaborMother(pvLaborM.getPkPvlabor());
			pvLaborChd.setPkDept(pvLaborM.getPkDept());
			pvLaborChd.setPkDeptNs(pvLaborM.getPkDeptNs());
			pvLaborChd.setPkDeptSrc(pvLaborM.getPkDept());
			pvLaborChd.setPkDeptNsSrc(pvLaborM.getPkDeptNs());
		}else{
			pvLaborChd.setPkOrg(bedList.get(0).getPkOrg());
			pvLaborChd.setPkDept(user.getPkDept());
			pvLaborChd.setPkDeptNs(user.getPkDept());
			pvLaborChd.setPkDeptSrc(user.getPkDept());
			pvLaborChd.setPkDeptNsSrc(user.getPkDept());
		}
		pvLaborChd.setCreator(user.getPkEmp());
		pvLaborChd.setCreateTime(date);
		DataBaseHelper.insertBean(pvLaborChd);
	}


    /**
     * 更新婴儿
     * @param vo
     */
    public void updateInfant(PvInfantVo vo, User user){
		PvInfant infant = new PvInfant();
		ApplicationUtils.copyProperties(infant, vo);
		vo.setWeight(CommonUtils.getDouble(vo.getWeight())/1000);
		//获取前台传过来的bean
		DataBaseHelper.updateBeanByPk(infant,false);
		//级联更新pi_master,pv_encounter
		String sql_pi = " update pi_master set name_pi=:name, birth_date = :dateBirth, dt_sex = :dtSex  where pk_pi = :pkPiInfant";
		DataBaseHelper.update(sql_pi, vo);
		String sql_pv = " update pv_encounter set name_pi=:name, dt_sex = :dtSex,height=:len,weight=:weight,date_begin =:dateBirth,date_admit=:dateBirth where pk_pv = :pkPvInfant";
		DataBaseHelper.update(sql_pv, vo);
		
		//手动分床模式
		String BD0013 = ApplicationUtils.getSysparam("BD0013",false);
    	if("0".equals(BD0013)){
			if (vo.getBabyBed() != null && !CommonUtils.isEmptyString(vo.getBabyBed().getPkBed())) {
				packBedForInfManual(vo,user);
			}
		} 
		
		//savePvRecInfo(vo,infant);//保存孕产 + 婴儿分娩信息
    }
     
    /**
	 * 根据婴儿主键删除婴儿信息,同时删除婴儿信息
	 * @param pk_infant
	 * @param pk_pv
	 * @param pk_pv_infant
	 * @param pk_pi_infant
	 */
	public void deleteInfantByPk(String pk_infant,String pk_pv,String pk_pv_infant,String pk_pi_infant){
		//删除前校验是否产生了有效医嘱
		String sql = " select count(1) from cn_order where pk_pv = ? and eu_status_ord != '9'";
		Integer count_ord =DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{pk_pv_infant});
		String sql_bl = " select sum(amount) from bl_ip_dt where pk_pv = ? and del_flag = '0' ";
		Double count_bl =DataBaseHelper.queryForScalar(sql_bl, Double.class, new Object[]{pk_pv_infant});
		String sql_pv = "select count(1) from pv_encounter where pk_pv = ? and flag_in = '1' and ( bed_no is not null or  bed_no !='') ";
		Integer count_bed =DataBaseHelper.queryForScalar(sql_pv, Integer.class, new Object[]{pk_pv_infant});
		//校验是否允许添加婴儿
		boolean isAllowDeptIn = ExSysParamUtil.getFlagAddInfByDeptNs(UserContext.getUser().getPkDept(), UserContext.getUser().getPkOrg());
    	String BD0013 = ApplicationUtils.getSysparam("BD0013",false);
		
		if(count_ord > 0){
			throw new BusException("存在婴儿有效医嘱，不允许删除!");
		}else if(null != count_bl && count_bl > 0){
			throw new BusException("婴儿已发生费用，不允许删除!");
		}
		else if (!isAllowDeptIn && count_bed > 0){//不允许添加婴儿则校验床位是否分配了
			throw new BusException("婴儿已分配了床位，不允许删除！");
		}else if("0".equals(BD0013) && count_bed > 0){
			//手动模式下不允许删除已经分床的婴儿
			throw new BusException("婴儿已分配了床位，不允许删除！");
		}else{
			//删除患者信息
			//DataBaseHelper.execute("delete from pi_master where pk_pi = ?",new Object[]{pk_pi_infant});
			//由物理删除更改软删除-博爱数据有限制
			DataBaseHelper.execute("update pi_master set del_flag='1' where pk_pi = ?",new Object[]{pk_pi_infant});
			//删除pv_encounter
			DataBaseHelper.execute("delete from pv_encounter where pk_pv = ?",new Object[]{pk_pv_infant});
			//删除pv_ip
			DataBaseHelper.execute("delete from pv_ip where pk_pv = ?",new Object[]{pk_pv_infant});
			//删除pv_insurance
			DataBaseHelper.execute("delete from pv_insurance where pk_pv = ?",new Object[]{pk_pv_infant});
			//删除婴儿评分
			DataBaseHelper.execute("delete from pv_infant_grade where pk_infant = ?", new Object[]{pk_infant});
			//删除pk_infant
			DataBaseHelper.execute("delete from pv_infant where pk_infant = ?",new Object[]{pk_infant});
			String sqln = "select quan_infant from pv_ip where pk_pv = ? ";
			List<Map<String,Object>> listn =DataBaseHelper.queryForList(sqln, new Object[]{pk_pv});
			if(listn!=null&&listn.size()>0&&CommonUtils.getInteger(listn.get(0).get("quanInfant"))>1){
				DataBaseHelper.update("update pv_ip set quan_infant = quan_infant - 1 where pk_pv = ? ", new Object[]{pk_pv});
			}else{
				DataBaseHelper.update("update pv_ip set flag_infant = 0, quan_infant = 0 where pk_pv = ? ", new Object[]{pk_pv});
			}
			//删除婴儿产房就诊记录
			DataBaseHelper.execute("delete from pv_labor where pk_pv = ?",new Object[]{pk_pv_infant});
			//查询婴儿产房床位，如果自动生成的床位删除床位，如果是召回至物价维护的床位则还原床位状态
			String queSql = " select bed.* from bd_res_bed bed" +
							" inner join bd_ou_dept dept on dept.pk_dept = bed.pk_ward and dt_depttype = '0310'"+
							" where bed.pk_pi = ?";
			BdResBed bdResBed = DataBaseHelper.queryForBean(queSql,BdResBed.class,pk_pi_infant);
			if (bdResBed != null && bdResBed.getPkItem() != null && "婴儿占用标志".equals(bdResBed.getPkItem().trim())) {
				DataBaseHelper.update("update bd_res_bed set pk_item = null,eu_status = '02',flag_ocupy = '0',pk_pi = null where pk_bed = ? ", new Object[]{bdResBed.getPkBed()});
			} else if (bdResBed != null) {
				DataBaseHelper.execute("delete from bd_res_bed where pk_bed = ?", new Object[]{bdResBed.getPkBed()});
			}
			if (isAllowDeptIn) {
				//清空床位占用记录
				DataBaseHelper.execute("update bd_res_bed set del_flag = '1',flag_active = '0',eu_status = '01'"
						+ ",flag_ocupy = '0',pk_pi = null,pk_dept_used = null where pk_pi = ? ", new Object[]{pk_pi_infant});
				//删除就诊记录
				DataBaseHelper.execute("delete from pv_adt where pk_pv = ?", new Object[]{pk_pv_infant});
				//删除就诊床位记录
				DataBaseHelper.execute("delete from pv_bed where pk_pv = ?", new Object[]{pk_pv_infant});
				//删除就诊医疗组
				DataBaseHelper.execute("delete from pv_clinic_group where pk_pv = ?",new Object[]{pk_pv_infant});
				//删除就诊医护人员
				DataBaseHelper.execute("delete from pv_staff where pk_pv = ?",new Object[]{pk_pv_infant});
			}
		}
	}
	   
    /**
	 * 根据婴儿主键删除婴儿信息,同时删除婴儿信息 + 产程信息
	 * @param pk_infant
	 * @param pk_pv
	 * @param pk_pv_infant
	 * @param pk_pi_infant
	 */
	public void deleteInfAndRecByPk(String pk_infant,String pk_pv,String pk_pv_infant,String pk_pi_infant,String pkLaborRecDt){
		//删除前校验是否产生了医嘱
		String sql = " select count(1) from cn_order where pk_pv = ? ";
		Integer count_ord =DataBaseHelper.queryForScalar(sql, Integer.class, new Object[]{pk_pv_infant});
		String sql_bl = " select sum(amount) from bl_ip_dt where pk_pv = ? and del_flag = '0' ";
		Double count_bl =DataBaseHelper.queryForScalar(sql_bl, Double.class, new Object[]{pk_pv_infant});
		String sql_pv = "select count(1) from pv_encounter where pk_pv = ? and flag_in = '1' and ( bed_no is not null or  bed_no !='') ";
		Integer count_bed =DataBaseHelper.queryForScalar(sql_pv, Integer.class, new Object[]{pk_pv_infant});
		//校验是否允许添加婴儿
		boolean isAllowDeptIn = ExSysParamUtil.getFlagAddInfByDeptNs(UserContext.getUser().getPkDept(), UserContext.getUser().getPkOrg());
		if(count_ord > 0){
			throw new BusException("存在婴儿医嘱，不允许删除!");
		}else if(null != count_bl && count_bl > 0){
			throw new BusException("婴儿已发生费用，不允许删除!");
		}
		else if (!isAllowDeptIn && count_bed > 0){//不允许添加婴儿则校验床位是否分配了
			throw new BusException("婴儿已分配了床位，不允许删除！");
		}else{
			//删除患者信息
			DataBaseHelper.execute("delete from pi_master where pk_pi = ?",new Object[]{pk_pi_infant});
			//删除pv_encounter
			DataBaseHelper.execute("delete from pv_encounter where pk_pv = ?",new Object[]{pk_pv_infant});
			//删除pv_ip
			DataBaseHelper.execute("delete from pv_ip where pk_pv = ?",new Object[]{pk_pv_infant});
			//删除pv_insurance
			DataBaseHelper.execute("delete from pv_insurance where pk_pv = ?",new Object[]{pk_pv_infant});
			//删除婴儿评分
			DataBaseHelper.execute("delete from pv_infant_grade where pk_infant = ?", new Object[]{pk_infant});
			//删除pk_infant
			DataBaseHelper.execute("delete from pv_infant where pk_infant = ?",new Object[]{pk_infant});
			//删除婴儿产程明细
			if(!CommonUtils.isEmptyString(pkLaborRecDt)){
				//删除pk_infant
				DataBaseHelper.execute("delete from pv_labor_rec_dt where pk_laborrecdt = ? ",new Object[]{pkLaborRecDt});
			}
			if(isAllowDeptIn){
				//清空床位占用记录
				DataBaseHelper.execute("update bd_res_bed set del_flag = '1',flag_active = '0',eu_status = '01'"
						+ ",flag_ocupy = '0',pk_pi = null,pk_dept_used = null where pk_pi = ? ",new Object[]{pk_pi_infant});
				//删除就诊记录
				DataBaseHelper.execute("delete from pv_adt where pk_pv = ?",new Object[]{pk_pv_infant});
				//删除就诊床位记录
				DataBaseHelper.execute("delete from pv_bed where pk_pv = ?",new Object[]{pk_pv_infant});
				//删除就诊医疗组
				DataBaseHelper.execute("delete from pv_clinic_group where pk_pv = ?",new Object[]{pk_pv_infant});
				//删除就诊医护人员
				DataBaseHelper.execute("delete from pv_staff where pk_pv = ?",new Object[]{pk_pv_infant});
			}
			String sqln = "select quan_infant from pv_ip where pk_pv = ? ";
			List<Map<String,Object>> listn =DataBaseHelper.queryForList(sqln, new Object[]{pk_pv});
			if(listn!=null&&listn.size()>0&&CommonUtils.getInteger(listn.get(0).get("quanInfant"))>1){
				DataBaseHelper.update("update pv_ip set quan_infant = quan_infant - 1 where pk_pv = ? ", new Object[]{pk_pv});
			}else{
				DataBaseHelper.update("update pv_ip set flag_infant = 0, quan_infant = 0 where pk_pv = ? ", new Object[]{pk_pv});
			}
		}
	}
    
    /**
     * 保存婴儿就诊信息（pv_encounter,pv_ip,pv_insurance）
     */
    private void savePvInfo(PvInfantVo vo,PvEncounterVo pv_mother,User user){
    	PvEncounter pv = new PvEncounter();
    	ApplicationUtils.copyProperties(pv, pv_mother);
    	//保存pv
    	pv.setPkPi(vo.getPkPiInfant());
    	pv.setPkPv(NHISUUID.getKeyId());
    	pv.setCodePv(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_ZY));
    	if(vo.getDateBirth()!=null){
    		pv.setAgePv(DateUtils.getAgeByBirthday(vo.getDateBirth(),pv.getDateBegin()));
    	}else{
    		pv.setAgePv("0天");
    	}
    	pv.setDateBegin(new Date());
    	pv.setEuStatus("0");
    	pv.setFlagIn("1");
    	pv.setFlagSettle("0");
    	pv.setEuPvtype(PvConstant.ENCOUNTER_EU_PVTYPE_3);//就诊类型: 3 住院
    	pv.setNamePi(vo.getName());
    	pv.setDtSex(vo.getDtSex());
    	pv.setWeight(CommonUtils.getDouble(vo.getWeight())/1000);
    	pv.setHeight(CommonUtils.getDouble(vo.getLen()));
    	pv.setPkPicate(vo.getPkPiCate());
    	pv.setDateClinic(new Date());
    	pv.setPkEmpReg(user.getPkEmp());
    	pv.setNameEmpReg(user.getNameEmp());
    	pv.setDateReg(new Date());
    	pv.setFlagCancel("0");
    	pv.setEuStatusFp("0"); //随访状态
    	//设置医保类型
    	List<Map<String,Object>> ins = DataBaseHelper.queryForList("select pk_hp,code from bd_hp where eu_hptype = '0' and flag_ip = '1' and del_flag ='0'", new Object[]{});
    	if(ins==null||ins.size()<=0)
    		throw new BusException("登记婴儿时，未获取到类型为'全自费'的医保，请维护后重试！");
    	pv.setPkInsu(CommonUtils.getString(ins.get(0).get("pkHp")));
    	pv.setAddrcodeCur(pv_mother.getAddrcodeCur());
    	pv.setAddrCur(pv_mother.getAddrCur());
    	pv.setAddrCurDt(pv_mother.getAddrCurDt());
    	pv.setAddrcodeRegi(pv_mother.getAddrcodeRegi());
    	pv.setAddrRegi(pv_mother.getAddrRegi());
    	pv.setAddrRegiDt(pv_mother.getAddrRegiDt());
    	//添加联系人信息
    	//联系人：母亲姓名
    	pv.setNameRel(pv_mother.getNamePi());
    	//联系人关系:父母
    	pv.setDtRalation("05");
    	//联系人电话：母亲电话
    	pv.setTelRel(pv_mother.getMobile());
    	//联系人证件类型：母亲证据类型
    	pv.setDtIdtypeRel(pv_mother.getDtIdtype());
    	//联系人证件号码：母亲证件号码
    	pv.setIdnoRel(pv_mother.getIdNo());
    	//联系人地址：母亲现住址描述+母亲现住址详细地址
    	pv.setAddrRel(pv_mother.getAddrCur()+pv_mother.getAddrCurDt());
    	
		DataBaseHelper.insertBean(pv);
		
       //保存 pv_ip
		PvIp pvIp = new PvIp();
		pvIp.setPkPv(pv.getPkPv());
		pvIp.setIpTimes(1);
		pvIp.setPkDeptAdmit(pv.getPkDept());
		pvIp.setPkDeptNsAdmit(pv.getPkDeptNs());
		pvIp.setDtLevelDise(IDictCodeConst.DT_BQDJ_WD);
		pvIp.setFlagPrest("0"); //预结算标志
		pvIp.setFlagInfant("1");
		pvIp.setQuanInfant(new Long(0));
		pvIp.setPkPvip(NHISUUID.getKeyId());
		ApplicationUtils.setDefaultValue(pvIp,true);
		pvInfantPubMapper.saveBean(pvIp);

		//DataBaseHelper.insertBean(pvIp);
		
		//保存 pv_insurance
		PvInsurance pvInsurance = new PvInsurance();
		pvInsurance.setPkPv(pv.getPkPv()); 
		pvInsurance.setSortNo(1);//新建就诊记录时关联的医保计划也是首次插入
		pvInsurance.setPkHp(pv.getPkInsu());
		pvInsurance.setFlagMaj("1"); //主医保计划标识
		DataBaseHelper.insertBean(pvInsurance);

		vo.setPkPvInfant(pv.getPkPv());
		vo.setHpcode(CommonUtils.getString(ins.get(0).get("code")));

    }
   
    /**
     * 将婴儿注册为患者
     */
    private PvEncounterVo savePiMaster(PvInfantVo vo){
		PiMaster pi = new PiMaster();
		String pkCate = "";
		try{
			List<PiCate> picates = DataBaseHelper.queryForList("select * from PI_CATE "
					+ "where del_flag = '0' and flag_spec = '0' order by flag_def desc ", PiCate.class, new Object[]{});
			if(null != picates && picates.size() > 0){
				pkCate = picates.get(0).getPkPicate();
			}
		}catch(Exception e){
			throw new BusException("登记婴儿时，未能从患者分类中获取到名称为'普通'的类别，或者存在多个名称为'普通'的患者分类,请更正后重试！");
		}
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pkPv", vo.getPkPv());
		if(CommonUtils.isEmptyString(vo.getPkPv()))
			throw new BusException("登记婴儿时，传入的母亲就诊主键为空！");

		PvEncounterVo mother = pvInfantPubMapper.queryInfantMother(paramMap);
		if(mother == null)
			throw new BusException("登记婴儿时，根据母亲就诊主键【"+vo.getPkPv()+"】未获取到相关就诊信息！");
		pi.setPkPicate(pkCate);//患者分类
    	//pi.setPkPi(NHISUUID.getKeyId());
    	pi.setCodePi(ApplicationUtils.getCode(SysConstant.ENCODERULE_CODE_HZ));//患者编码-由编辑器生成
    	pi.setCodeIp(vo.getCode());//住院号设置为与患者编码相同
    	pi.setNamePi(vo.getName());
    	pi.setDtSex(vo.getDtSex());
    	pi.setBirthDate(vo.getDateBirth());
    	pi.setDtCountry(mother.getDtCountry());
    	pi.setDtNation(mother.getDtNation());
    	pi.setAddrCur(mother.getAddrCur());
    	pi.setAddrCurDt(mother.getAddrCurDt());
    	pi.setAddrcodeCur(mother.getAddrcodeCur());
    	pi.setAddrOrigin(mother.getAddrcodeRegi());
    	pi.setAddrcodeOrigin(mother.getAddrcodeRegi());
    	pi.setMobile(mother.getMobile());
    	//添加联系人信息
    	//联系人：母亲姓名
		pi.setNameRel(mother.getNamePi());
		//联系人关系:父母
		pi.setDtRalation("05");
		//联系人电话：母亲电话
		pi.setTelRel(mother.getMobile());
		//联系人证件类型：母亲证据类型
		pi.setDtIdtypeRel(mother.getDtIdtype());
		//联系人证件号码：母亲证件号码
		pi.setIdnoRel(mother.getIdNo());
		//联系人地址：母亲现住址描述+母亲现住址详细地址
		pi.setAddrRel(mother.getAddrCur() + mother.getAddrCurDt());
		PiMasterParam pati = new PiMasterParam();
		pati.setMaster(pi);
		pi = piPubService.savePiMasterParamAutoCommint(pati);
		vo.setPkPiInfant(pi.getPkPi());
		vo.setPkPi(mother.getPkPi());
		vo.setPkPiCate(pkCate);
		return mother;
	}

	/**
	 * 新生儿入科-分床
	 * @param pkPvMa 	母亲就诊主键
	 * @param pkPvInf 	婴儿就诊主键
	 * @param pkPiInf 	婴儿患者主键
	 * @param sortNo 	婴儿序号
	 * @param u			当前操作人信息
	 * @param ts		入科时间
	 */
	private void packBedForInf(PvInfantVo infant,User u,Date ts){
		//1、查询母亲相关信息
		PvEncounter pvMa = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv = ?", PvEncounter.class, new Object[]{infant.getPkPv()});
		
		//2、校验母亲对应床位是否存在当前婴儿的床位,不存在则添加相应床位
		BdResBed bedMa = DataBaseHelper.queryForBean("select * from bd_res_bed where pk_pi = ? and pk_ward = ? and code = ? "
				, BdResBed.class, new Object[]{pvMa.getPkPi(),pvMa.getPkDeptNs(),pvMa.getBedNo()});
		//婴儿床位分隔符
		String bedSpc = ExSysParamUtil.getSpcOfCodeBed();//获取婴儿床位分隔符
		if(CommonUtils.isEmptyString(bedSpc))
			throw new BusException("请维护系统参数【BD0007】-婴儿床位编码分隔符！");
		
		String infBedNo = pvMa.getBedNo()+bedSpc+infant.getSortNo();
		List<BdResBed> bedList = bdResPubService.isHaveBedAndAdd(pvMa.getPkDeptNs(), bedMa, infBedNo, null, u);

		//母亲不在产科病区，根据选择的产科病区成人床位生成婴儿床位（博爱--20200514--雷博勋）
		if("0".equals(infant.getFlagDept()) && infant.getBabyBed() != null){
			BabyBedVO babyBedVO = infant.getBabyBed();
			bedMa = DataBaseHelper.queryForBean("select * from bd_res_bed where  pk_ward = ? and code = ? "
					, BdResBed.class, new Object[]{babyBedVO.getPkDeptNs(),babyBedVO.getCodeBed()});

			infBedNo = babyBedVO.getCodeBed()+bedSpc+infant.getSortNo();

			bedList = bdResPubService.isHaveBedAndAdd(babyBedVO.getPkDeptNs(), bedMa, infBedNo, null, u);
		}

		if(null == bedList || bedList.size() < 1)
			throw new BusException("维护婴儿床位失败，无法保存婴儿信息！");
		
		String pkBedInf = bedList.get(0).getPkBed();
		int cntBed = DataBaseHelper.update("update bd_res_bed set flag_active = '1', del_flag = '0',code_fa = '婴儿' "
				+ "where pk_bed = ? and del_flag = '1' and pk_pi is null", new Object[]{pkBedInf});
		if(cntBed < 1)
			throw new BusException("当前婴儿床位已被占用，无法保存婴儿信息！");
		
		
		//3、查询科室定义的固定费用套
		Map<String, Object> dayCgParam = new HashMap<String, Object>();
		if("0".equals(infant.getFlagDept()) && infant.getBabyBed() != null){
			dayCgParam.put("pkDeptNs", infant.getBabyBed().getPkDeptNs());
		}else{
			dayCgParam.put("pkDeptNs", pvMa.getPkDeptNs());
		}
		dayCgParam.put("hpcode", infant.getHpcode());
		List<BdDaycgSetItemVo> dayCgItemList = queryDayCgItem(dayCgParam, u);
		List<Map<String, Object>> dayCgListMap = new ArrayList<Map<String, Object>>();
		for (BdDaycgSetItemVo bdDaycgSetItemVo : dayCgItemList) {
			dayCgListMap.add((Map<String, Object>) ApplicationUtils.beanToMap(bdDaycgSetItemVo));
		}
		
		//4、组装入科参数进行分配
		String dateBirthStr ="";
		try {
			dateBirthStr = DateUtils.getDateTimeStr(infant.getDateBirth());
		} catch (Exception e) {
			dateBirthStr = DateUtils.getDateTimeStr(ts);
		}
		Map<String, Object> insMap = new HashMap<String, Object>();
		insMap.put("pkPv", infant.getPkPvInfant());
		insMap.put("pkOrg", u.getPkOrg());
		insMap.put("pkPi", infant.getPkPiInfant());
		insMap.put("pkAdt", null);
		insMap.put("pkIpBed", bedList.get(0).getPkBed());
		insMap.put("codeIpBed", infBedNo);

		if("0".equals(infant.getFlagDept()) && infant.getBabyBed() != null){
			insMap.put("pkDept", infant.getBabyBed().getPkDept());
			insMap.put("pkDeptNs", infant.getBabyBed().getPkDeptNs());
			insMap.put("pkIpPsnMain", infant.getBabyBed().getPkIpPsnMain());
			insMap.put("nameIpPsnMain", infant.getBabyBed().getNameIpPsnMain());
			insMap.put("pkIpPsnNs", infant.getBabyBed().getPkIpPsnNs());
			insMap.put("nameIpPsnNs", infant.getBabyBed().getNameIpPsnNs());
			insMap.put("pkPicateNew", null);
		}else{
			insMap.put("pkDept", pvMa.getPkDept());
			insMap.put("pkDeptNs", pvMa.getPkDeptNs());
			insMap.put("pkIpPsnMain", pvMa.getPkEmpPhy());
			insMap.put("nameIpPsnMain", pvMa.getNameEmpPhy());
			insMap.put("pkIpPsnNs", pvMa.getPkEmpNs());
			insMap.put("nameIpPsnNs", pvMa.getNameEmpNs());
			insMap.put("pkPicateNew", pvMa.getPkPicate());
		}
		insMap.put("dateAdmit",	dateBirthStr);
		insMap.put("dateBegin", dateBirthStr); 
		insMap.put("euDaycgmode", "0");
		insMap.put("daycgList", dayCgListMap);
		piDeptInAndOutPubService.saveDeptInInfo(insMap, u);//完成婴儿入科接收
		
		//4、更新婴儿信息表
    	String infant_sql = "update pv_infant set eu_status_adt ='1',reason_adt ='新增婴儿时入院'"
    			+ ",date_adt = to_date(?,'YYYYMMDDHH24MISS') where pk_infant = ? ";
    	DataBaseHelper.update(infant_sql,new Object[]{DateUtils.getDateTimeStr(ts),infant.getPkInfant()});

		//5、更新pv_ip表的婴儿占床主键 pk_bed_an
		if("0".equals(infant.getFlagDept()) && infant.getBabyBed() != null){
			String sql = "update pv_ip set pk_bed_an = ?,pk_dept_admit = ?,pk_dept_ns_admit = ? where pk_pv = ?";
			BabyBedVO babyBed = infant.getBabyBed();
			DataBaseHelper.update(sql, new Object[]{babyBed.getPkBed(),babyBed.getPkDept(),babyBed.getPkDeptNs(),infant.getPkPvInfant()});
		}
		//添加对应的婴儿床位费
		if(bedList.size() > 0 ){
            //2020-05-26 获取婴儿床位费编码
            String BD0010 = ApplicationUtils.getDeptSysparam("BD0010", String.valueOf(dayCgParam.get("pkDeptNs")));
			String queSql = "select * from BD_ITEM where code = ?";
            BdItem bdItem = DataBaseHelper.queryForBean(queSql,BdItem.class,BD0010);
            //添加对应的婴儿床位费
            String updSql = "update BD_RES_BED set PK_ITEM = ? where PK_BED = ? ";
            if(bdItem != null){
				DataBaseHelper.update(updSql,new Object[]{bdItem.getPkItem(),pkBedInf});
			}
        }
	}
    
	/**
	 * 保存婴儿分娩信息
	 * @param vo
	 * @param infant
	 */
	private void savePvRecInfo(PvInfantVo vo,PvInfant infant){
		List<PvLaborRec> recs = DataBaseHelper.queryForList("select * from pv_labor_rec where pk_pv = ?"
				+ " and del_flag = '0'", PvLaborRec.class, new Object[]{vo.getPkPv()});
		if(null == recs || recs.size() < 1)
		{
			//新增，产程记录
			PvLaborRec rec = new PvLaborRec();
			getRecValueFromInfantVo(rec,vo); 
			DataBaseHelper.insertBean(rec);
			
			//新增，产程明细
			PvLaborRecDt recDt = new PvLaborRecDt();
			recDt.setPkPv(rec.getPkPv());
			recDt.setPkLaborrec(rec.getPkLaborrec());
			getRecDtValueFromInfantVo(recDt,vo,rec);
			DataBaseHelper.insertBean(recDt);
		}
		else if(recs.size() == 1)
		{
			//更新，产程记录
			getRecValueFromInfantVo(recs.get(0),vo);
			DataBaseHelper.updateBeanByPk(recs.get(0));
			
			List<PvLaborRecDt> recDts = DataBaseHelper.queryForList("select * from pv_labor_rec_dt where pk_laborrec = ? "
					+ "and pk_pv = ? and sort_no = ? and del_flag = '0' "
					, PvLaborRecDt.class, new Object[]{infant.getPkLaborrec(), infant.getPkPv(),infant.getSortNo()});
			if(null == recDts || recDts.size() < 1){
				//新增，产程明细
				PvLaborRecDt recDt = new PvLaborRecDt();
				recDt.setPkLaborrec(recs.get(0).getPkLaborrec());
				getRecDtValueFromInfantVo(recDt,vo,recs.get(0));
				DataBaseHelper.insertBean(recDt);
			}else if(recDts.size() == 1){
				if(!CommonUtils.isEmptyString(recDts.get(0).getPkLaborrecdt())){
					//更新，产程明细
					getRecDtValueFromInfantVo(recDts.get(0),vo,recs.get(0));
					DataBaseHelper.updateBeanByPk(recDts.get(0));
				}
			}
		}
	}
	
	/**
	 * 根据录入信息同步婴儿产程
	 * @param rec
	 * @param inf
	 * @return
	 */
	private PvLaborRec getRecValueFromInfantVo(PvLaborRec rec,PvInfantVo inf){
		rec.setPkPv(inf.getPkPv());  
		rec.setPreWeeks(inf.getWeekPreg());  
		rec.setDtExcep(inf.getDtExcep());            
		rec.setEuHb(inf.getEuHb());  
		return rec;
	}

	/**
	 * 根据录入信息同步婴儿产程明细
	 * @param rec
	 * @param inf
	 * @return
	 */
	private PvLaborRecDt getRecDtValueFromInfantVo(PvLaborRecDt recDt,PvInfantVo inf,PvLaborRec rec){
		recDt.setPkLaborrecdt(inf.getPkLaborrecdt());
		recDt.setPkLaborrec(rec.getPkLaborrec());
		recDt.setPkPv(inf.getPkPv());
		recDt.setOutAbout(inf.getEuBirth());
		recDt.setInfWight(CommonUtils.getDouble(inf.getWeight()));
		recDt.setInfLen(CommonUtils.getDouble(inf.getLen()));
		recDt.setDtSexInf(inf.getDtSex());
		recDt.setEuInf(inf.getEuInf());        
		recDt.setWeekPreg(inf.getWeekPreg());          
		recDt.setTimeEarly(inf.getTimeEarly());   
		recDt.setDtOutMode(inf.getDtOutMode());
		recDt.setOpReason(inf.getOpReason());      
		recDt.setAmFluidAfter(inf.getAmFluidAfter());
		recDt.setAmFluidOther(inf.getAmFluidOther());
		recDt.setUmbAbout(inf.getUmbAbout());
		recDt.setUmbCrossCycle(inf.getUmbCrossCycle());
		recDt.setUmbOthers(inf.getUmbOthers());
		recDt.setSpecial(inf.getSpecial());    
		recDt.setSortNo(inf.getSortNo());
		recDt.setDateOut(inf.getDateBirth());
		return recDt;
	}

	/**
	 * 新生儿入科-手动分床
	 * @param babyBed
	 * @param pkPi
	 * @param pkPv
	 */
	public void packBedForInfManual(PvInfantVo infant ,User u) {
		BabyBedVO babyBed = infant.getBabyBed();
		String pkBed = babyBed.getPkBed();
		String codeBed = babyBed.getCodeBed();
		String hpcode = infant.getHpcode();
		Date date = new Date();
		
		BdResBed bed = DataBaseHelper.queryForBean("select * from bd_res_bed where pk_pi = ? ", BdResBed.class, new Object[]{infant.getPkPiInfant()});
		//已经分床的婴儿不在分床
		if(bed != null){
			return;
		}
		
		//1、查询母亲相关信息
		PvEncounter pvMa = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv = ?", PvEncounter.class, new Object[]{infant.getPkPv()});
				
		//2、组装入科参数进行分配
		String dateBirthStr ="";
		try {
			dateBirthStr = DateUtils.getDateTimeStr(babyBed.getDateAdmit());
		} catch (Exception e) {
			dateBirthStr = DateUtils.getDateTimeStr(date);
		}
		
		//3、查询科室定义的固定费用套
		Map<String, Object> dayCgParam = new HashMap<String, Object>();
		dayCgParam.put("pkDeptNs", babyBed.getPkDeptNs());
		dayCgParam.put("hpcode", hpcode);
		List<BdDaycgSetItemVo> dayCgItemList = queryDayCgItem(dayCgParam, u);
		List<Map<String, Object>> dayCgListMap = new ArrayList<Map<String, Object>>();
		for (BdDaycgSetItemVo bdDaycgSetItemVo : dayCgItemList) {
			dayCgListMap.add((Map<String, Object>) ApplicationUtils.beanToMap(bdDaycgSetItemVo));
		}
		//4、入科接收
		Map<String, Object> insMap = new HashMap<String, Object>();
		insMap.put("pkPv", infant.getPkPvInfant());
		insMap.put("pkOrg", u.getPkOrg());
		insMap.put("pkDept", babyBed.getPkDept());
		insMap.put("pkDeptNs", babyBed.getPkDeptNs());
		insMap.put("pkPi", infant.getPkPiInfant());
		insMap.put("pkAdt", null);
		insMap.put("pkIpBed", pkBed);
		insMap.put("codeIpBed", codeBed); 
		insMap.put("pkIpPsnMain", babyBed.getPkIpPsnMain()); 
		insMap.put("nameIpPsnMain", babyBed.getNameIpPsnMain()); 
		insMap.put("pkIpPsnNs", babyBed.getPkIpPsnNs()); 
		insMap.put("pkIpWg", babyBed.getPkIpWg()); 
		insMap.put("nameIpPsnNs", babyBed.getNameIpPsnNs()); 
		insMap.put("pkPicateNew", pvMa.getPkPicate()); 
		insMap.put("dateAdmit",	dateBirthStr); 
		insMap.put("dateBegin", dateBirthStr); 
		insMap.put("euDaycgmode", "0");
		insMap.put("daycgList", dayCgListMap);
		piDeptInAndOutPubService.saveDeptInInfo(insMap, u);//完成婴儿入科接收

		//5、更新婴儿信息表
    	String infantSql = "update pv_infant set eu_status_adt ='1',reason_adt ='新增婴儿时入院'"
    			+ ",date_adt = to_date(?,'YYYYMMDDHH24MISS') where pk_infant = ? ";
    	DataBaseHelper.update(infantSql,new Object[]{DateUtils.getDateTimeStr(date),infant.getPkInfant()});
    	
	}


    /**
     * 查询科室定义下的固定费用
     * @param param{pkDeptNs的值}
     * @param user
     * @return
     */
    public List<BdDaycgSetItemVo> queryDayCgItem(Map map,IUser user){
    	String pkDeptNs = CommonUtils.getString(map.get("pkDeptNs"));
    	String hpcode = CommonUtils.getString(map.get("hpcode"));		
    	User u = (User)user;
    	if(CommonUtils.isEmptyString(pkDeptNs)) throw new BusException("未获取到科室主键！");
    	StringBuilder sql = new StringBuilder("select item.*,bdit.name,bdit.price,bdit.code from bd_daycg_set_item item inner join bd_daycg_set daycg on daycg.pk_daycgset = item.pk_daycgset ");
    	sql.append(" inner join bd_item bdit on bdit.pk_item = item.pk_item ");
    	sql.append(" where daycg.pk_dept = ? and daycg.eu_type = '1' and daycg.pk_org = '");
    	sql.append(u.getPkOrg());
    	sql.append("' and daycg.del_flag='0' and item.del_flag='0' ");
    	sql.append(" and (daycg.code_hps is null or (daycg.code_hps like  '%,"+hpcode+",%' ))");
    	List<BdDaycgSetItemVo> result = DataBaseHelper.queryForList(sql.toString(), BdDaycgSetItemVo.class, new Object[]{pkDeptNs});
    	if(result == null || result.size()<=0){
    		sql = new StringBuilder("select item.*,bdit.name,bdit.price,bdit.code  from bd_daycg_set_item item inner join bd_daycg_set daycg on daycg.pk_daycgset = item.pk_daycgset ");
    		sql.append(" inner join bd_item bdit on bdit.pk_item = item.pk_item ");
    		sql.append(" where daycg.eu_type = '0' and daycg.pk_org = '");
    		sql.append(u.getPkOrg());
    		sql.append("' and daycg.del_flag='0' and item.del_flag='0' ");
    		sql.append(" and (daycg.code_hps is null or (daycg.code_hps like '%,"+hpcode+",%' )) ");
    		result = DataBaseHelper.queryForList(sql.toString(),BdDaycgSetItemVo.class,new Object[]{});
    	}
    	return result;
    }
   
}
