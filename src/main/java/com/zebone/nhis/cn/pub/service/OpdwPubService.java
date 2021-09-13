package com.zebone.nhis.cn.pub.service;

import com.zebone.nhis.arch.support.ArchUtil;
import com.zebone.nhis.cn.pub.vo.OpPdStorePackVo;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.pi.InsQgybPV;
import com.zebone.nhis.common.module.pi.PiInsurance;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvIpNotice;
import com.zebone.nhis.common.service.CommonService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.platform.Application;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OpdwPubService {

	@Autowired
	private CommonService comDao;
	
    public Map<String,Object> getLinesDrugStore(String param,IUser user){
    	Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
    	Map<String,Object> map = new HashMap<String,Object>();
    	Map<String, Object> mapParam = new HashMap<String,Object>();
    	User  u = (User) user;
    	mapParam.put("pkDept", StringUtils.isBlank(CommonUtils.getString(paramMap.get("pkDept")))?u.getPkDept():paramMap.get("pkDept"));
    	mapParam.put("dtDepttype", "0402");
    	mapParam.put("dtButype", "06");
		Map<String,Object> durgStore = comDao.getLinesBusiness(mapParam);
		map.put("pharmacy", durgStore.get("pkDept"));
		mapParam.put("dtButype", "10");
		durgStore =comDao.getLinesBusiness(mapParam); 
		map.put("chinesePharmacy", durgStore.get("pkDept"));
		return map;
    }
    public List<OpPdStorePackVo> qryPdStockPack(String param,IUser user){
    	//key :pkDept/ pkPd
    	List<OpPdStorePackVo> pds= JsonUtil.readValue(param, new TypeReference<List<OpPdStorePackVo>>(){});    	
    	return getPdStockPack(pds);
    }
	private List<OpPdStorePackVo> getPdStockPack(List<OpPdStorePackVo> pds) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		List<String> pkpds = new ArrayList<String>();
		for(OpPdStorePackVo pd :pds){
			pkpds.add(pd.getPkPd());
			paramMap.put(pd.getPkPd(), pd.getQuanDisp());
		}
		paramMap.put("pkDept", pds.get(0).getPkDeptExec());
		paramMap.put("pkPd", pkpds);
		List<OpPdStorePackVo> pdPack = new ArrayList<OpPdStorePackVo>();
		List<OpPdStorePackVo> pdPackQry = DataBaseHelper.queryForList("SELECT pds.pk_pd, psp.pk_unit,psp.pack_size from bd_pd_store pds inner join bd_pd_store_pack psp on psp.pk_pdstore = pds.pk_pdstore  WHERE  pds.pk_dept =:pkDept  AND pds.PK_PD in (:pkPd) ",OpPdStorePackVo.class, paramMap);
		if(pdPackQry!=null && pdPackQry.size()>0){
			List<List<OpPdStorePackVo>> pkPdGroup = dividerList(pdPackQry, new Comparator<OpPdStorePackVo>() {
				@Override
				public int compare(OpPdStorePackVo o1, OpPdStorePackVo o2) {
					
					return o1.getPkPd().equals(o2.getPkPd()) ? 0:-1;
				}
			});
			for(List<OpPdStorePackVo> pdPacks :pkPdGroup){
				 if(pdPacks.size()==1){
					 pdPack.add(pdPacks.get(0)); 
				 }else if(pdPacks.size()>1){
					 //关联药房包装的数量，自动计算差值最小的包装单位，如果需要的总量超过所有包装量，取余数最小的包装，如果有相同余数的包装，取包装量最大的单位
					 //例如：A药品有包装：5,10,15，开立总量为8时，取包装量为10的单位，开立总量为100时，取包装量为10的单位（余数为0）
					 double quanCgMin = (double) paramMap.get(pdPacks.get(0).getPkPd());
					 int size = pdPacks.size();
					 for(int i=0;i<size;i++){
						 OpPdStorePackVo pdStockPack = pdPacks.get(i);
						double packSizeMin = pdStockPack.getPackSize();
						if(quanCgMin<packSizeMin){
							OpPdStorePackVo vo = getQuanCgUnitBySub(quanCgMin,pdPacks);
							pdPack.add(vo);
							break;
						}else {
							if(i==size-1){
								OpPdStorePackVo vo = getQuanCgUnitByRemainDer(quanCgMin,pdPacks);
								pdPack.add(vo);
							}
						}
					 }
				 }
			}
		}
		return pdPack;
	}
	private OpPdStorePackVo getQuanCgUnitBySub(double quanCgMin,List<OpPdStorePackVo> pdPacks) {
		 double smallSubValue = Math.abs(MathUtils.sub(pdPacks.get(0).getPackSize(), quanCgMin));
		 Map<Object,Object> map = new HashMap<Object,Object>();
		 for(OpPdStorePackVo pdPack :pdPacks){
			 double subValue =Math.abs(MathUtils.sub(pdPack.getPackSize(), quanCgMin));
			 map.put(subValue, pdPack);
			 if(subValue<smallSubValue) smallSubValue = subValue;
		 }
		 return (OpPdStorePackVo) map.get(smallSubValue);	
	}
	private OpPdStorePackVo getQuanCgUnitByRemainDer(double quanCgMin,List<OpPdStorePackVo> pdPacks) {
		 
		 double smallRemainValue = quanCgMin%pdPacks.get(0).getPackSize();
		 Map<Object,Object> map = new HashMap<Object,Object>();
		 for(OpPdStorePackVo pdPack :pdPacks){
			 double remainValue =quanCgMin%pdPacks.get(0).getPackSize();
			 if(!map.containsKey(remainValue)){
				 map.put(remainValue, pdPack);
			 }else{
				 OpPdStorePackVo pd = (OpPdStorePackVo) map.get(remainValue);
				 if(pdPack.getPackSize()>pd.getPackSize()) map.put(remainValue, pdPack);
			 }
			 if(remainValue<smallRemainValue) smallRemainValue = remainValue;
		 }
		 return (OpPdStorePackVo) map.get(smallRemainValue);	
	}
	public static <T> List<List<T>> dividerList(List<T> list,Comparator<? super T> comparator) {
        List<List<T>> lists = new ArrayList<>();
        
        for (int i = 0; i < list.size(); i++) {
            boolean isContain = false;
            for (int j = 0; j < lists.size(); j++) {
                if (lists.get(j).size() == 0||comparator.compare(lists.get(j).get(0),list.get(i)) == 0) {
                    lists.get(j).add(list.get(i));
                    isContain = true;
                    break;
                }
            }
            if (!isContain) {
                List<T> newList = new ArrayList<>();
                newList.add(list.get(i));
                lists.add(newList);
            }
        }
        return lists;
    }

	/**
	 * 医保门诊就诊
	 * 004001005046
	 */
	public Map<String,Object> medicalInsuranceOutpatientVisit(String param,IUser user){
		Map<String,Object> resultMap = new HashMap<>();
    	Map<String,Object> paramMap = JsonUtil.readValue(param,Map.class);
		String pkPv = MapUtils.getString(paramMap,"pkPv");
		if(StringUtils.isEmpty(pkPv)){
			return resultMap;
//			throw new BusException("缺少参数【pkPv】");
		}
		String sql = "select * from PV_ENCOUNTER where PK_PV = ?";
    	//判断是否为医保患者
		PvEncounter pvEncounter = DataBaseHelper.queryForBean(sql,PvEncounter.class,new Object[]{pkPv});
		if(pvEncounter == null){
			return resultMap;
//			throw new BusException(String.format("根据就诊主键无法获取患者信息【%s】"),pkPv);
		}

		String msg="";
		//21天内有入院通知单//判断住院是否是日间手术科室
		sql = "select dept.*\n" +
				"from bd_ou_dept dept\n" +
				"         inner join bd_dictattr att\n" +
				"                    on dept.pk_dept = att.pk_dict\n" +
				"         inner join bd_dictattr_temp tmp\n" +
				"                    on att.pk_dictattrtemp = tmp.pk_dictattrtemp\n" +
				"         inner join pv_ip_notice nt\n" +
				"                    on nt.pk_dept_ip = dept.pk_dept\n" +
				"where tmp.code_attr='0601'\n" +
				"  and att.val_attr='1'\n" +
				"  and nt.eu_status ='3'\n" +
				"  and nt.pk_pi=?\n";
		if (Application.isSqlServer()) {
			sql+="  and nt.date_admit >= GETDATE() - 14 \n";
		}else{
			sql+="  and nt.date_admit >= sysdate - 14 \n";
		}

		BdOuDept bdOuDept = DataBaseHelper.queryForBean(sql,BdOuDept.class,new Object[]{pvEncounter.getPkPi()});
		if(bdOuDept!=null){
			//是日间手术科室
			resultMap.put("code","200");
			resultMap.put("msg","该患者14天内曾在日间手术病房住院，请按照日间手术术后随访流程进行诊疗!");
			msg="该患者14天内曾在日间手术病房住院，请按照日间手术术后随访流程进行诊疗!";
		}

		String pkInsu = pvEncounter.getPkInsu();
		if(StringUtils.isEmpty(pkInsu)){
			return resultMap;
//			throw new BusException("门诊医保就诊患者无医保主键PK_INSU");
		}
		sql = "select * from BD_HP where PK_HP = ?";
		BdHp bdHp = DataBaseHelper.queryForBean(sql,BdHp.class,new Object[]{pkInsu});
		if(bdHp == null){
			return resultMap;
//			throw new BusException(String.format("根据医保主键无法获取医保信息【%s】"),pkInsu);
		}
		if(StringUtils.isEmpty(bdHp.getDtExthp())){
			return resultMap;
//			throw new BusException("该患者非医保患者");
		}
		//查询21天内是否有入院通知单
		String pkPi = pvEncounter.getPkPi();
		if(StringUtils.isEmpty(pkPi)){
			return resultMap;
//			throw new BusException("患者主键为空");
		}
		String pkHp = bdHp.getPkHp();
		if(StringUtils.isEmpty(pkHp)){
			return resultMap;
//			throw new BusException("医保计划主键为空");
		}

		if (Application.isSqlServer()) {
			sql = "select *\n" +
					"from pv_ip_notice nt\n" +
					"where nt.pk_pi = ? and nt.date_admit >= GETDATE() - 21 order by date_admit desc";

		}else{
			sql = "select *\n" +
					"from pv_ip_notice nt\n" +
					"where nt.pk_pi = ? and nt.date_admit >= sysdate - 21 order by date_admit desc";
		}

//		sql = "select *\n" +
//				"from pv_ip_notice nt\n" +
//				"where nt.pk_pi = ? and nt.date_admit >= sysdate - 21 order by date_admit desc";
		List<PvIpNotice> pvIpNoticeList = DataBaseHelper.queryForList(sql,PvIpNotice.class,new Object[]{pkPi});
		//查询21天内是否开立过入院通知单
		if(pvIpNoticeList.size() <=0){
			//21天内没有开立入院通知单//查询患者是否在院
			medicarePatients(pkPi);
		}else {


			if (Application.isSqlServer()) {
				//21天内有入院通知单//判断住院是否是日间手术科室
				sql = "select dept.*\n" +
						"from bd_ou_dept dept\n" +
						"         inner join bd_dictattr att\n" +
						"                    on dept.pk_dept = att.pk_dict\n" +
						"         inner join bd_dictattr_temp tmp\n" +
						"                    on att.pk_dictattrtemp = tmp.pk_dictattrtemp\n" +
						"         inner join pv_ip_notice nt\n" +
						"                    on nt.pk_dept_ip = dept.pk_dept\n" +
						"where tmp.code_attr='0601'\n" +
						"  and att.val_attr='1'\n" +
						"  and nt.eu_status not in ('0','9')\n" +
						"  and nt.date_admit >= GETDATE() - 21 \n"+
						"  and nt.pk_pi=?\n";
			}else{
				//21天内有入院通知单//判断住院是否是日间手术科室
				sql = "select dept.*\n" +
						"from bd_ou_dept dept\n" +
						"         inner join bd_dictattr att\n" +
						"                    on dept.pk_dept = att.pk_dict\n" +
						"         inner join bd_dictattr_temp tmp\n" +
						"                    on att.pk_dictattrtemp = tmp.pk_dictattrtemp\n" +
						"         inner join pv_ip_notice nt\n" +
						"                    on nt.pk_dept_ip = dept.pk_dept\n" +
						"where tmp.code_attr='0601'\n" +
						"  and att.val_attr='1'\n" +
						"  and nt.eu_status not in ('0','9')\n" +
						"  and nt.date_admit >= sysdate - 21 \n"+
						"  and nt.pk_pi=?\n";
			}



			//21天内有入院通知单//判断住院是否是日间手术科室
//			sql = "select dept.*\n" +
//					"from bd_ou_dept dept\n" +
//					"         inner join bd_dictattr att\n" +
//					"                    on dept.pk_dept = att.pk_dict\n" +
//					"         inner join bd_dictattr_temp tmp\n" +
//					"                    on att.pk_dictattrtemp = tmp.pk_dictattrtemp\n" +
//					"         inner join pv_ip_notice nt\n" +
//					"                    on nt.pk_dept_ip = dept.pk_dept\n" +
//					"where tmp.code_attr='0601'\n" +
//					"  and att.val_attr='1'\n" +
//					"  and nt.eu_status not in ('0','9')\n" +
//					"  and nt.date_admit >= sysdate - 21 \n"+
//					"  and nt.pk_pi=?\n";
			bdOuDept = DataBaseHelper.queryForBean(sql,BdOuDept.class,new Object[]{pkPi});
			if(bdOuDept==null){
				//不是日间手术科室
				medicarePatients(pkPi);
			}else {
				//是日间手术科室
				resultMap.put("code","200");
				if("".equals(msg)){
					resultMap.put("msg","该患者为日间手术病人，请勿开具日间手术相关的检查治疗处方；手术相关问题请让患者到手术专科就诊");
				}else{
					resultMap.put("msg","该患者为日间手术病人，14天内曾在日间手术病房住院，请按照日间手术术后随访流程进行诊疗! 请勿开具日间手术相关的检查治疗处方；手术相关问题请让患者到手术专科就诊");
				}

			}
		}
		return resultMap;
	}


	/**
	 * 医保患者逻辑
	 * 根据入院通知单查询（不限时间，只查询是否有入院状态记录）患者是否在院
	 */
	private void medicarePatients(String pkPi){
		String sql = "select * from pv_ip_notice nt where nt.pk_pi = ? and nt.eu_status='2' order by create_time desc";
		List<PvIpNotice> pvIpNoticeList = DataBaseHelper.queryForList(sql,PvIpNotice.class,new Object[]{pkPi});
		//根据入院通知单查询（不限时间，只查询是否有入院状态记录）患者是否在院：
		if(pvIpNoticeList.size()>0){
			//在院
			//判断是否是门诊特定病种住院(ins_qgyb_pv.med_type=14),计生（51），产前医保（5301）。
			sql = "select *  from ins_qgyb_pv where pk_pv=? and med_type  in ('14','51','5301') ";
			List<InsQgybPV> insQgybPVList = DataBaseHelper.queryForList(sql,InsQgybPV.class,new Object[]{pvIpNoticeList.get(0).getPkPvOp()});
			if(insQgybPVList.size()>0){
				//是门诊特定病种住院,正常就诊

			}else {
				throw new BusException("住院医保患者，住院期间禁止门诊就诊");
			}
		}else {
			//不在院,正常就诊
		}
	}

	/**
	 * 门诊就诊验证手机号
	 *
	 * 004001005048
	 */
	public int mobilePhoneVerification(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param,Map.class);
		String pkPi = MapUtils.getString(paramMap,"pkPi");
		String sql ="SELECT * FROM PI_MASTER WHERE PK_PI=?";
		PiMaster piMaster = DataBaseHelper.queryForBean(sql,PiMaster.class,new Object[]{pkPi});
		if(piMaster==null){
			return 1;
		}
		String flag = piMaster.getFlagRealmobile();

		if(!"1".equalsIgnoreCase(flag)){
			//未验证
			sql = "SELECT COUNT(1) CNT FROM PV_ENCOUNTER WHERE PK_PI=? AND EU_STATUS <>'9'";

			Map<String,Object> countMap = DataBaseHelper.queryForMap(sql,new Object[]{pkPi});
			int numberOfVisits = 0;
			try{
				numberOfVisits = Integer.valueOf(MapUtils.getString(countMap,"cnt")) ;
			}catch (Exception e){

			}

			String pv0052 = ApplicationUtils.getSysparam("PV0052", false, "请维护好系统参数PV0052！");
			Integer max = Integer.valueOf(pv0052);
			if(numberOfVisits>max){
				//throw new BusException("需要手机认证通过后才允许继续就诊活动");
				return  0;
			}
		}
		return 1;

	}


	/**
	 *门诊医生站查询患者信息
	 * 004001005047
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> patientInformation(String param,IUser user) throws ParseException {
		Map<String,Object> paramMap = JsonUtil.readValue(param,Map.class);
		String pkPi = MapUtils.getString(paramMap,"pkPi");
		String pkEmpPhy = MapUtils.getString(paramMap,"pkEmpPhy");
		String startString =  MapUtils.getString(paramMap,"beginDate");
		String endString = MapUtils.getString(paramMap,"endDate");
		String ordType = MapUtils.getString(paramMap,"ordType");
		List<String> euPvtype= paramMap.containsKey("euPvtype") ? (List<String>) paramMap.get("euPvtype") :null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = simpleDateFormat.parse(startString);
		Date endDate = simpleDateFormat.parse(endString);
		List<Map<String,Object>> searchPv=new ArrayList<Map<String,Object>>();
		if (StringUtils.isNotEmpty(pkPi)){
			StringBuffer sql=new StringBuffer();
			sql.append(" select pi.name_pi,pv.pk_pv,pv.date_begin,pv.pk_dept,pv.eu_pvtype,dept.name_dept,pv.NAME_EMP_PHY,pi.code_op,diag.name_diag as diag_name  ");
			sql.append(" from pi_master pi inner join pv_encounter pv on pi.pk_pi = pv.pk_pi ");
			sql.append(" inner join bd_ou_dept dept on pv.pk_dept = dept.pk_dept ");
			sql.append(" inner join pv_diag diag on diag.pk_pv=pv.pk_pv and diag.DEL_FLAG='0' and diag.FLAG_MAJ='1' ");
			sql.append(" where pv.pk_pi = ? and pv.flag_cancel = 0 and pv.date_begin >= ? and pv.date_begin <= ?");
			if(StringUtils.isNotEmpty(ordType)){
				sql.append(" and exists( ");
				sql.append(" select 1 from CN_ORDER c where c.pk_pi=pi.PK_PI and c.pk_pv= pv.PK_PV and substr(c.code_ordtype,1,2) in ("+ordType+")");
				sql.append(" union all");
				sql.append(" select 1 from CN_ORDER_B b where b.pk_pi=pi.PK_PI and b.pk_pv= pv.PK_PV and substr(b.code_ordtype,1,2) in ("+ordType+") )") ;
			}
			if (CollectionUtils.isNotEmpty(euPvtype)){
				sql.append(" and pv.EU_PVTYPE in (");
				sql.append(CommonUtils.convertListToSqlInPart(euPvtype));
				sql.append(") ");
			}
			sql.append(" and pv.EU_STATUS<>'9' order by pv.date_begin desc ");
			searchPv=DataBaseHelper.queryForList(sql.toString(),new Object[]{pkPi,startDate,endDate});
		}

		if(StringUtils.isNotEmpty(pkEmpPhy)){
			StringBuffer sql=new StringBuffer();
			sql.append(" select pi.name_pi,pv.pk_pv,pv.date_begin,pv.pk_dept,pv.eu_pvtype,dept.name_dept,pv.NAME_EMP_PHY,pi.code_op,diag.name_diag as diag_name ");
			sql.append(" from pi_master pi inner join pv_encounter pv on pi.pk_pi = pv.pk_pi ");
			sql.append(" inner join bd_ou_dept dept on pv.pk_dept = dept.pk_dept ");
			sql.append(" inner join pv_diag diag on diag.pk_pv=pv.pk_pv and diag.DEL_FLAG='0' and diag.FLAG_MAJ='1' ");
			sql.append(" where pv.flag_cancel = 0 ");
			sql.append( " and pv.date_begin >= ? and pv.date_begin <= ? and pv.pk_emp_phy = ?  ");
			if (CollectionUtils.isNotEmpty(euPvtype)){
				sql.append(" and pv.EU_PVTYPE in (");
				sql.append(CommonUtils.convertListToSqlInPart(euPvtype));
				sql.append(") ");
			}
			sql.append(" and pv.EU_STATUS<>'9' order by pv.date_begin desc ");
			searchPv=DataBaseHelper.queryForList(sql.toString(),new Object[]{startDate,endDate,pkEmpPhy});
		}


		if(searchPv != null && searchPv.size() > 0) {

			Set<String> pkPvs = new HashSet<String>();
			for (Map<String, Object> map : searchPv) {
				pkPvs.add(map.get("pkPv").toString());
			}

			//查询全国医保信息
			StringBuffer str = new StringBuffer();
			str.append(" select pv.pk_inspv,pv.pk_pv,pv.med_type,dict.name as med_name,pv.dise_codg,pv.dise_name, ");
			str.append(" pv.birctrl_type,dict1.name as birctrl_name,pv.birctrl_matn_date,pv.matn_type,  ");
			str.append(" dict2.name as matn_name,pv.geso_val from ins_qgyb_pv pv");
			str.append(" left join ins_qgyb_dict dict on pv.med_type=dict.code and dict.code_type='med_type' ");
			str.append(" left join ins_qgyb_dict dict1 on pv.birctrl_type=dict1.code and dict1.code_type='birctrl_type' ");
			str.append(" left join ins_qgyb_dict dict2 on pv.matn_type=dict2.code and dict2.code_type='matn_type' ");
			str.append(" where pv.pk_pv in (");
			str.append(ArchUtil.convertSetToSqlInPart(pkPvs, "pk_pv"));
			str.append(") ");
			List<Map<String, Object>> qgybPvList = DataBaseHelper.queryForList(str.toString(), new Object[] {});
			if(qgybPvList != null && qgybPvList.size() > 0) {
				for (Map<String, Object> searchPvMap : searchPv) {
					String pkPv = searchPvMap.get("pkPv").toString();
					for (Map<String, Object> qgybPvMap : qgybPvList) {
						String qgybPv = qgybPvMap.get("pkPv").toString();
						if(pkPv.equals(qgybPv)) {
							searchPvMap.put("pkInspv", CommonUtils.getPropValueStr(qgybPvMap, "pkInspv"));
							searchPvMap.put("medType", CommonUtils.getPropValueStr(qgybPvMap, "medType"));
							searchPvMap.put("medName", CommonUtils.getPropValueStr(qgybPvMap, "medName"));
							searchPvMap.put("diseCodg", CommonUtils.getPropValueStr(qgybPvMap, "diseCodg"));
							searchPvMap.put("diseName", CommonUtils.getPropValueStr(qgybPvMap, "diseName"));
							searchPvMap.put("birctrlType", CommonUtils.getPropValueStr(qgybPvMap, "birctrlType"));
							searchPvMap.put("birctrlName", CommonUtils.getPropValueStr(qgybPvMap, "birctrlName"));
							searchPvMap.put("birctrlMatnDate", CommonUtils.getPropValueStr(qgybPvMap, "birctrlMatnDate"));
							searchPvMap.put("matnType", CommonUtils.getPropValueStr(qgybPvMap, "matnType"));
							searchPvMap.put("matnName", CommonUtils.getPropValueStr(qgybPvMap, "matnName"));
							searchPvMap.put("gesoVal", CommonUtils.getPropValueStr(qgybPvMap, "gesoVal"));
						}
					}

				}
			}

			//查询主诊断信息
//			str = new StringBuffer();
//			str.append(" select * from pv_diag diag where diag.DEL_FLAG='0' and diag.FLAG_MAJ='1'");
//			str.append(" and diag.pk_pv in (");
//			str.append(ArchUtil.convertSetToSqlInPart(pkPvs, "pk_pv"));
//			str.append(") ");
//			List<Map<String, Object>> qyDiagPvList = DataBaseHelper.queryForList(str.toString(), new Object[] {});
//			if(qyDiagPvList != null && qyDiagPvList.size() > 0) {
//				for (Map<String, Object> searchPvMap : searchPv) {
//					String pkPv = searchPvMap.get("pkPv").toString();
//					for (Map<String, Object> qgdiagPvMap : qyDiagPvList) {
//						String pv = qgdiagPvMap.get("pkPv").toString();
//						if(pkPv.equals(pv)) {
//							searchPvMap.put("diagName", CommonUtils.getPropValueStr(qgdiagPvMap, "nameDiag"));
//						}
//					}
//				}
//			}

		}

		return searchPv;
	}

	/**
	 * 患者就诊前校验信息 004003006004
	 * @param param
	 * @param user
	 * @return
	 * @throws ParseException
	 */
	public Map<String,Object> continueToCheck(String param,IUser user) throws ParseException{
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> paramMap = JsonUtil.readValue(param,Map.class);
		String pkPi = MapUtils.getString(paramMap,"pkPi");
		if(StringUtils.isEmpty(pkPi)){
			return resultMap;
		}
		String sql = "select * from PI_MASTER where PK_PI = ?";
		//判断是否为医保患者
		PiMaster piMaster = DataBaseHelper.queryForBean(sql,PiMaster.class,new Object[]{pkPi});
		if(piMaster == null){
			return resultMap;
		}

		String msg="";
		//21天内有入院通知单//判断住院是否是日间手术科室
		sql = "select dept.*\n" +
				"from bd_ou_dept dept\n" +
				"         inner join bd_dictattr att\n" +
				"                    on dept.pk_dept = att.pk_dict\n" +
				"         inner join bd_dictattr_temp tmp\n" +
				"                    on att.pk_dictattrtemp = tmp.pk_dictattrtemp\n" +
				"         inner join pv_ip_notice nt\n" +
				"                    on nt.pk_dept_ip = dept.pk_dept\n" +
				"where tmp.code_attr='0601'\n" +
				"  and att.val_attr='1'\n" +
				"  and nt.eu_status ='3'\n" +
				"  and nt.pk_pi=?\n";
		if (Application.isSqlServer()) {
			sql+="  and nt.date_admit >= GETDATE() - 14 \n";
		}else{
			sql+="  and nt.date_admit >= sysdate - 14 \n";
		}

		BdOuDept bdOuDept = DataBaseHelper.queryForBean(sql,BdOuDept.class,new Object[]{piMaster.getPkPi()});
		if(bdOuDept!=null){
			//是日间手术科室
			resultMap.put("code","200");
			resultMap.put("msg","该患者14天内曾在日间手术病房住院，请按照日间手术术后随访流程进行诊疗!");
			msg="该患者14天内曾在日间手术病房住院，请按照日间手术术后随访流程进行诊疗!";
		}

		String pkInsu = getPkHp(piMaster.getPkPi());
		if(StringUtils.isEmpty(pkInsu)){
			return resultMap;
		}
		sql = "select * from BD_HP where PK_HP = ?";
		BdHp bdHp = DataBaseHelper.queryForBean(sql,BdHp.class,new Object[]{pkInsu});
		if(bdHp == null){
			return resultMap;
//			throw new BusException(String.format("根据医保主键无法获取医保信息【%s】"),pkInsu);
		}
		if(StringUtils.isEmpty(bdHp.getDtExthp())){
			return resultMap;
//			throw new BusException("该患者非医保患者");
		}
		//查询21天内是否有入院通知单
		if(StringUtils.isEmpty(pkPi)){
			return resultMap;
//			throw new BusException("患者主键为空");
		}
		String pkHp = bdHp.getPkHp();
		if(StringUtils.isEmpty(pkHp)){
			return resultMap;
//			throw new BusException("医保计划主键为空");
		}

		if (Application.isSqlServer()) {
			sql = "select *\n" +
					"from pv_ip_notice nt\n" +
					"where nt.pk_pi = ? and nt.date_admit >= GETDATE() - 21 order by date_admit desc";

		}else{
			sql = "select *\n" +
					"from pv_ip_notice nt\n" +
					"where nt.pk_pi = ? and nt.date_admit >= sysdate - 21 order by date_admit desc";
		}

//		sql = "select *\n" +
//				"from pv_ip_notice nt\n" +
//				"where nt.pk_pi = ? and nt.date_admit >= sysdate - 21 order by date_admit desc";
		List<PvIpNotice> pvIpNoticeList = DataBaseHelper.queryForList(sql,PvIpNotice.class,new Object[]{pkPi});
		//查询21天内是否开立过入院通知单
		if(pvIpNoticeList.size() <=0){
			//21天内没有开立入院通知单//查询患者是否在院
			medicarePatients(pkPi);
		}else {
			if (Application.isSqlServer()) {
				//21天内有入院通知单//判断住院是否是日间手术科室
				sql = "select dept.*\n" +
						"from bd_ou_dept dept\n" +
						"         inner join bd_dictattr att\n" +
						"                    on dept.pk_dept = att.pk_dict\n" +
						"         inner join bd_dictattr_temp tmp\n" +
						"                    on att.pk_dictattrtemp = tmp.pk_dictattrtemp\n" +
						"         inner join pv_ip_notice nt\n" +
						"                    on nt.pk_dept_ip = dept.pk_dept\n" +
						"where tmp.code_attr='0601'\n" +
						"  and att.val_attr='1'\n" +
						"  and nt.eu_status not in ('0','9')\n" +
						"  and nt.date_admit >= GETDATE() - 21 \n"+
						"  and nt.pk_pi=?\n";
			}else{
				//21天内有入院通知单//判断住院是否是日间手术科室
				sql = "select dept.*\n" +
						"from bd_ou_dept dept\n" +
						"         inner join bd_dictattr att\n" +
						"                    on dept.pk_dept = att.pk_dict\n" +
						"         inner join bd_dictattr_temp tmp\n" +
						"                    on att.pk_dictattrtemp = tmp.pk_dictattrtemp\n" +
						"         inner join pv_ip_notice nt\n" +
						"                    on nt.pk_dept_ip = dept.pk_dept\n" +
						"where tmp.code_attr='0601'\n" +
						"  and att.val_attr='1'\n" +
						"  and nt.eu_status not in ('0','9')\n" +
						"  and nt.date_admit >= sysdate - 21 \n"+
						"  and nt.pk_pi=?\n";
			}
			//21天内有入院通知单//判断住院是否是日间手术科室
			bdOuDept = DataBaseHelper.queryForBean(sql,BdOuDept.class,new Object[]{pkPi});
			if(bdOuDept==null){
				//不是日间手术科室
				medicarePatients(pkPi);
			}else {
				//是日间手术科室
				resultMap.put("code","200");
				if("".equals(msg)){
					resultMap.put("msg","该患者为日间手术病人，请勿开具日间手术相关的检查治疗处方；手术相关问题请让患者到手术专科就诊");
				}else{
					resultMap.put("msg","该患者为日间手术病人，14天内曾在日间手术病房住院，请按照日间手术术后随访流程进行诊疗! 请勿开具日间手术相关的检查治疗处方；手术相关问题请让患者到手术专科就诊");
				}

			}
		}
		return resultMap;
	}

	//自费医保信息
	private String getPkHp(String pkPi) {

		if (StringUtils.isNotBlank(pkPi)) {
			PiInsurance piIns = DataBaseHelper.queryForBean("select * from pi_insurance ins where ins.pk_pi=? and ins.flag_def='1' and ins.del_flag='0'", PiInsurance.class, new Object[]{pkPi});
			if (piIns != null && StringUtils.isNotBlank(piIns.getPkHp())) {
				return piIns.getPkHp();
			}
		}
		BdHp bdHp = DataBaseHelper.queryForBean("select * from bd_hp where eu_hptype = '0' and flag_ip = '1'  and del_flag = '0'", BdHp.class);
		if (bdHp == null) {
			return null;
		} else {
			return bdHp.getPkHp();
		}
	}

}
