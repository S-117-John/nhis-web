package com.zebone.nhis.compay.ins.syx.service.gzyb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.common.module.compay.ins.syx.GzybDataSource;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.GzybSt;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.GzybStCity;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.GzybStXnh;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.GzybVisit;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.GzybVisitCity;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.GzybVisitXnh;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.InsGzybStInjuryVo;
import com.zebone.nhis.common.module.compay.ins.syx.gzyb.InsGzybTrialstVo;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.compay.ins.syx.dao.gzyb.GzybMapper;
import com.zebone.nhis.compay.ins.syx.vo.gzxnh.XnhDeptVo;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.BlipVo;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.BlipdtReUploadVo;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.BlipdtVo;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.BlopdtVo;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.DeptVo;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.GzDiagVo;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.MiddleHisCfxm;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.MiddleHisCfxmJksybz;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.MiddleHisFyjs;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.MiddleHisMzdj;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.MiddleHisMzjs;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.MiddleHisMzxm;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.MiddleHisZydj;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.SettlementInfo;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.StVo;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.VisitInfo;
import com.zebone.nhis.compay.ins.syx.vo.gzyb.VisitVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class GzybService {
	@Autowired
	private GzybMapper gzybMapper;

	/**
	 * 015001007004 ???????????????????????????????????????????????????????????????????????????
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public GzybDataSource queryDsInfo(String param, IUser user) {
		User u = (User) user;
		String pkhp = JsonUtil.getFieldValue(param, "pkhp");
		String pkOrg = u.getPkOrg();
		String pkdept = u.getPkDept();
		if (pkhp != null && pkOrg != null && pkdept != null) {
			GzybDataSource info = gzybMapper.queryDsInfo(pkhp, pkOrg, pkdept);
			return info;
		} else {
			throw new BusException("?????????????????????");
		}
	}

	/**
	 * 015001007005 ??????????????????????????????
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public String save(String param, IUser user) {
		User u = (User) user;
		VisitVo vo = JsonUtil.readValue(param, VisitVo.class);
		GzybVisit visit = vo.getVisit();
		GzybVisitCity visitCity = vo.getVisitCity();
		GzybVisitXnh visitXnh = vo.getVisitXnh();
		String pkpv = "000000";
		DataBaseHelper.execute("delete from INS_GZYB_VISIT where PK_PV=?",
				new Object[] { pkpv });
		if (visit.getPvcodeIns() != null) {
			DataBaseHelper.execute(
					"delete from INS_GZYB_VISIT where PVCODE_INS=?",
					new Object[] { visit.getPvcodeIns() });
		}
		// ????????????
		if (visit != null) {
			visit.setDelFlag("0");
			visit.setPkOrg(u.getPkOrg());
			visit.setTs(new Date());
			visit.setCreateTime(new Date());
			visit.setCreator(u.getPkUser());
			DataBaseHelper.insertBean(visit);
			if (visitCity.getCodeIp() != null) {
				// ????????????-?????????
				visitCity.setDelFlag("0");
				visitCity.setPkOrg(u.getPkOrg());
				visitCity.setPkVisit(visit.getPkvisit());
				visitCity.setCreateTime(new Date());
				visitCity.setTs(new Date());
				visitCity.setCreator(u.getPkUser());
				savecity(visitCity, param, user);
			}
			if (visitXnh.getCodePv() != null) {
				// ????????????-?????????
				visitXnh.setDelFlag("0");
				visitXnh.setPkOrg(u.getPkOrg());
				visitXnh.setPkVisit(visit.getPkvisit());
				savennh(visitXnh, param, user);
			}
		}
		return visit.getPkvisit();
	}

	/**
	 * 015001007006 ??????????????????-???????????????
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public void savecity(String param, IUser user) {
		GzybVisitCity gzybvisitcity = JsonUtil.readValue(param,
				GzybVisitCity.class);
		User u = (User) user;
		if (gzybvisitcity != null) {
			gzybvisitcity.setPkOrg(u.getPkOrg());
			if (gzybvisitcity.getPkvisitcity() != null) {
				DataBaseHelper.updateBeanByPk(gzybvisitcity, false);
			} else {
				gzybvisitcity.setCreateTime(new Date());
				gzybvisitcity.setCreator(u.getUserName());
				DataBaseHelper.insertBean(gzybvisitcity);
			}
		}
	}

	public void savecity(GzybVisitCity visitCity, String param, IUser user) {
		User u = (User) user;
		if (visitCity != null) {
			visitCity.setPkOrg(u.getPkOrg());
			if (visitCity.getPkvisitcity() != null) {
				DataBaseHelper.updateBeanByPk(visitCity, false);
			} else {
				visitCity.setCreateTime(new Date());
				visitCity.setCreator(u.getUserName());
				DataBaseHelper.insertBean(visitCity);
			}
		}
	}

	/**
	 * 015001007007??????????????????-???????????????
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public void savennh(String param, IUser user) {
		GzybVisitXnh gzybvisitxnh = JsonUtil.readValue(param,
				GzybVisitXnh.class);
		User u = (User) user;
		if (gzybvisitxnh != null) {
			gzybvisitxnh.setPkOrg(u.getPkOrg());
			if (gzybvisitxnh.getPkVisitXnh() != null) {
				DataBaseHelper.updateBeanByPk(gzybvisitxnh, false);
			} else {
				gzybvisitxnh.setCreateTime(new Date());
				gzybvisitxnh.setCreator(u.getUserName());
				DataBaseHelper.insertBean(gzybvisitxnh);
			}
		}
	}

	public void savennh(GzybVisitXnh visitXnh, String param, IUser user) {
		User u = (User) user;
		if (visitXnh != null) {
			if (visitXnh.getPkVisitXnh() != null) {
				DataBaseHelper.updateBeanByPk(visitXnh, false);
			} else {
				visitXnh.setCreateTime(new Date());
				visitXnh.setCreator(u.getUserName());
				DataBaseHelper.insertBean(visitXnh);
			}
		}
	}

	/**
	 * 015001007008 ????????????????????????????????????????????????????????????-?????????????????????
	 * 
	 * @return
	 */
	public List<MiddleHisZydj> queryInfoList(String gmsfhm, String zyh,
			String xm, String xb) {
		if (gmsfhm != null && zyh != null && xm != null && xb != null) {
			// ?????????????????????
			List<MiddleHisZydj> list = gzybMapper.queryInfoList(gmsfhm, zyh,
					xm, xb);
			return list;
		} else {
			throw new BusException("?????????????????????");
		}
	}

	/**
	 * 015001007010 ??????????????????????????????HIS???????????????????????????????????????
	 * 
	 * @return
	 */
	public void updateVisit(String param, IUser user) {
		String pkvisit = JsonUtil.getFieldValue(param, "pkvisit");
		String pkpv = JsonUtil.getFieldValue(param, "pkpv");
		String pkpi = JsonUtil.getFieldValue(param, "pkpi");
		if (pkvisit != null && pkpv != null) {
			DataBaseHelper
					.execute(
							"update INS_GZYB_VISIT set PK_PV=?,PK_PI=? where pk_visit=?",
							pkpv, pkpi, pkvisit);
		} else {
			throw new BusException("?????????????????????");
		}
	}

	/**
	 * 015001007011????????????????????????????????????????????????
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public GzybVisit getGzybVisit(String param, IUser user) {
		// User u = (User) user;
		String pkpv = JsonUtil.getFieldValue(param, "pkpv");
		if (pkpv != null) {
			GzybVisit info = gzybMapper.getGzybVisit(pkpv);
			return info;
		} else {
			throw new BusException("???????????????????????????????????????");
		}
	}

	/**
	 * 015001007012 ??????????????????????????????
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public void addSt(String param, IUser user) {
		// 1.1???????????????ins_gzyb_st,ins_gzyb_st_city???
		StVo vo = JsonUtil.readValue(param, StVo.class);
		GzybSt st = vo.getSt();
		GzybStCity stCity = vo.getStCity();
		User u = (User) user;
		// 1.2?????????????????????
		if (st == null)
			throw new BusException("??????????????????????????????");
		if (stCity == null)
			throw new BusException("?????????????????????????????????");

		// 2.2????????????????????????????????????
		String qrySql = "select pk_insst from ins_gzyb_st where pk_visit=? and del_flag='0' ";
		Map<String, Object> insstMap = DataBaseHelper.queryForMap(qrySql,
				new Object[] { st.getPkvisit() });

		// 2.1?????????????????????????????????-????????????????????????del_flag='1'
		String sql = "update ins_gzyb_st set del_flag='1' where pk_visit=?";
		DataBaseHelper.update(sql, new Object[] { st.getPkvisit() });

		// 2.3??????pk_insst?????????????????????del_flag='1'
		if (CommonUtils.isNotNull(insstMap)
				&& CommonUtils.isNotNull(insstMap.get("pkInsst"))) {// ??????????????????
			String xnhSql = "update ins_gzyb_st_city set del_flag='1' where pk_insst=?";
			DataBaseHelper.execute(xnhSql,
					new Object[] { insstMap.get("pkInsst") });
		}

		// 2.4??????????????????
		st.setPkOrg(u.getPkOrg());
		st.setDelFlag("0");
		st.setCreateTime(new Date());
		st.setCreator(u.getPkUser());
		st.setTs(new Date());
		DataBaseHelper.insertBean(st);
		// 2.5???????????????????????????
		stCity.setPkInsst(st.getPkInsst());
		stCity.setPkOrg(u.getPkOrg());
		stCity.setDelFlag("0");
		stCity.setCreateTime(new Date());
		stCity.setCreator(u.getPkUser());
		stCity.setTs(new Date());
		DataBaseHelper.insertBean(stCity);

	}

	/**
	 * 015001007013???????????????????????????-???????????????
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public void addCity(String param, IUser user) {
		GzybStCity stCity = JsonUtil.readValue(param, GzybStCity.class);
		User u = (User) user;
		if (stCity != null) {
			stCity.setPkOrg(u.getPkOrg());
			stCity.setDelFlag("0");
			stCity.setCreator(u.getPkUser());
			stCity.setTs(new Date());
			if (stCity.getPkInsstcity() != null) {
				DataBaseHelper.updateBeanByPk(stCity, false);
			} else {
				stCity.setCreateTime(new Date());
				stCity.setCreator(u.getUserName());
				DataBaseHelper.insertBean(stCity);
			}
		}
	}

	public void addCity(GzybStCity stCity, String param, IUser user) {
		User u = (User) user;
		if (stCity != null) {
			stCity.setPkOrg(u.getPkOrg());
			stCity.setDelFlag("0");
			stCity.setCreator(u.getPkUser());
			if (stCity.getPkInsstcity() != null) {
				DataBaseHelper.updateBeanByPk(stCity, false);
			} else {
				stCity.setCreateTime(new Date());
				stCity.setCreator(u.getUserName());
				DataBaseHelper.insertBean(stCity);
			}
		}
	}

	/**
	 * 015001007014??????????????????-???????????????
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public void addXnh(String param, IUser user) {
		GzybStXnh stXnh = JsonUtil.readValue(param, GzybStXnh.class);
		User u = (User) user;
		if (stXnh != null) {
			stXnh.setPkOrg(u.getPkOrg());
			if (stXnh.getPkInsstxnh() != null) {
				DataBaseHelper.updateBeanByPk(stXnh, false);
			} else {
				stXnh.setCreateTime(new Date());
				stXnh.setCreator(u.getUserName());
				DataBaseHelper.insertBean(stXnh);
			}
		}
	}

	public void addXnh(GzybStXnh stXnh, String param, IUser user) {
		User u = (User) user;
		if (stXnh != null) {
			if (stXnh.getPkInsstxnh() != null) {
				DataBaseHelper.updateBeanByPk(stXnh, false);
			} else {
				stXnh.setPkOrg(u.getPkOrg());
				stXnh.setCreateTime(new Date());
				stXnh.setCreator(u.getUserName());
				DataBaseHelper.insertBean(stXnh);
			}
		}
	}

	/**
	 * 015001007015??????????????????????????????HIS_CFXM???
	 * 
	 * @param param
	 * @param user
	 */
	public void addMiddleHisCfxm(List<MiddleHisCfxm> cfxmList) {
		for (MiddleHisCfxm cfxm : cfxmList) {
			DataBaseHelper.insertBean(cfxm);
		}
	}

	/**
	 * 015001007016??????????????????pk_pv????????????????????????bl_ip_dt?????????????????????flag_insu???0?????????
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BlipdtVo> qryBdItemAndOrderByPkPv(String param, IUser user) {
		User u = (User) user;
		BlipVo allData = JsonUtil.readValue(param, BlipVo.class);
		String pkpv = allData.getPkpv();
		List<String> pkCgips = allData.getPkCgips();
		String euhpdicttype = allData.getEuhpdicttype();
		String dateEnd = allData.getDateEnd();
		String dateBegin = allData.getDateBegin();
		String pkdept = u.getPkDept();
		if (pkpv != null) {
			List<BlipdtVo> cBdItems = gzybMapper.qryBdItemAndOrderByPkPv(pkpv,
					pkdept, pkCgips, euhpdicttype, dateEnd, dateBegin);
			return cBdItems;
		} else {
			throw new BusException("???????????????????????????????????????");
		}
	}

	/**
	 * 015001007017
	 * ????????????????????????????????????PK_PV???PkSettle?????????????????????INS_GZYB_ST???????????????INS_GZYB_VISIT???
	 * 
	 * @param param
	 * @param user
	 */
	public void updatePkSettle(String param, IUser user) {
		String pkpv = JsonUtil.getFieldValue(param, "pkpv");
		String pkSettle = JsonUtil.getFieldValue(param, "pkSettle");
		if (pkpv != null && pkSettle != null) {
			if ("1".equals(pkSettle)) {
				DataBaseHelper.execute(
						"update INS_GZYB_ST set PK_SETTLE='' where PK_PV=?",
						pkpv);
				DataBaseHelper
						.execute(
								"update INS_GZYB_VISIT set EU_STATUS_ST='0' where PK_PV=?",
								pkpv);
			} else {
				DataBaseHelper.execute(
						"update INS_GZYB_ST set PK_SETTLE=? where PK_PV=? ",
						pkSettle, pkpv);
				DataBaseHelper
						.execute(
								"update INS_GZYB_VISIT set EU_STATUS_ST='1' where PK_PV=? ",
								pkpv);
			}
		} else {
			throw new BusException("?????????????????????");
		}
	}

	/**
	 * 015001007018????????????????????????????????????PK_PV???EU_STATUS_ST?????????????????????INS_GZYB_VISIT???
	 * 
	 * @param param
	 * @param user
	 */
	public void updateEuStatusSt(String param, IUser user) {
		String pkpv = JsonUtil.getFieldValue(param, "pkpv");
		String euStatusSt = JsonUtil.getFieldValue(param, "euStatusSt");
		if (pkpv != null && euStatusSt != null) {
			DataBaseHelper.execute(
					"update INS_GZYB_VISIT set EU_STATUS_ST=? where PK_PV=?",
					euStatusSt, pkpv);
		} else {
			throw new BusException("?????????????????????");
		}
	}

	/**
	 * 015001007021????????????????????????????????????-???????????????HIS_FYJS?????????
	 * 
	 * @param jydjh
	 * @return
	 */
	public MiddleHisFyjs queryCostInfo(String jydjh) {
		if (jydjh != null) {
			MiddleHisFyjs info = gzybMapper.queryCostInfo(jydjh);
			if (info != null) {
				return info;
			} else {
				return null;
			}
		} else {
			throw new BusException("????????????????????????");
		}
	}

	/**
	 * 015001007022??????????????????????????????????????????
	 * 
	 * @param param
	 * @param user
	 */
	public void deletevisit(String param, IUser user) {
		String pkvisit = JsonUtil.getFieldValue(param, "pkvisit");
		if (pkvisit != null) {
			DataBaseHelper.execute(
					"update INS_GZYB_VISIT set del_flag='1' where PK_VISIT=?",
					pkvisit);
			DataBaseHelper
					.execute(
							"update INS_GZYB_VISIT_CITY set del_flag='1' where PK_VISIT=?",
							pkvisit);
		} else {
			throw new BusException("?????????????????????");
		}
	}

	/**
	 * 
	 * @param param
	 * @param user
	 */
	public void deleteJsInfo(String param, IUser user) {
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		if (pkPv == null || "".equals(pkPv))
			throw new BusException("????????????????????????");

		String pkInsst = "";
		// 1.??????pkPv??????ins_gzyb_st???????????????
		String stSql = "select pk_insst as pkInsst from ins_gzyb_st  where pk_pv=? and (del_flag='0' or del_flag is null)";
		Map<String, Object> pkInsstMap = DataBaseHelper
				.queryForMap(stSql, pkPv);
		if (pkInsstMap != null && pkInsstMap.get("pkinsst") != null) {
			pkInsst = pkInsstMap.get("pkinsst").toString();
		}
		// 3.??????????????????ins_gzyb_st,ins_gzyb_st_xnh,ins_gzyb_st_xnh_d;
		String sql = "update ins_gzyb_st set del_flag='1' where pk_pv=?";
		DataBaseHelper.update(sql, new Object[] { pkPv });
		sql = "update ins_gzyb_st_city set del_flag='1' where pk_insst=?";
		DataBaseHelper.update(sql, new Object[] { pkInsst });
		sql = "update INS_GZYB_VISIT set EU_STATUS_ST='0' where PK_PV=?";
		DataBaseHelper.update(sql, new Object[] { pkPv });
	}

	/**
	 * 015001007024???????????????????????????????????????pk_pv??????????????????????????????
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public GzybVisit getSettledVisit(String param, IUser user) {
		String pkpv = JsonUtil.getFieldValue(param, "pkpv");
		if (pkpv == null || "".equals(pkpv)) {
			throw new BusException("????????????????????????");
		} else {
			GzybVisit info = gzybMapper.getSettledVisit(pkpv);
			return info;
		}
	}

	/** ????????????????????????HIS_CFXM_JKSYBZ??? */
	public void addMiddleHisCfxmJksybz(List<MiddleHisCfxmJksybz> jksybzlist) {
		for (MiddleHisCfxmJksybz cfxm : jksybzlist) {
			DataBaseHelper.insertBean(cfxm);
		}
	}

	public void updatedrbz(String param, IUser user) {
		String jydjh = JsonUtil.getFieldValue(param, "jydjh");
		String yybh = JsonUtil.getFieldValue(param, "yybh");
		String gmsfhm = JsonUtil.getFieldValue(param, "gmsfhm");
		Integer drbz = Integer.valueOf(JsonUtil.getFieldValue(param, "drbz"));
		if (jydjh != null && yybh != null && gmsfhm != null) {
			if ("0".equals(drbz)) {
				DataBaseHelper
						.update("update HIS_ZYDJ set DRBZ=0 where JYDJH=? and YYBH=? and GMSFHM=?",
								new Object[] { jydjh, yybh, gmsfhm });
			} else {
				DataBaseHelper
						.update("update HIS_ZYDJ set DRBZ=1 where JYDJH=? and YYBH=? and GMSFHM=?",
								new Object[] { jydjh, yybh, gmsfhm });
			}
		} else {
			throw new BusException("?????????????????????");
		}
	}

	public void updateCostlist(String param, IUser user) {
		String jydjh = JsonUtil.getFieldValue(param, "jydjh");
		String type = JsonUtil.getFieldValue(param, "type");
		if (jydjh != null) {
			if ("1".equals(type)) {
				DataBaseHelper.update(
						"update HIS_FYJS set DRBZ=1 where JYDJH=?",
						new Object[] { jydjh });
			} else {
				DataBaseHelper.update(
						"update HIS_FYJS set DRBZ=0 where JYDJH=?",
						new Object[] { jydjh });
			}
		} else {
			throw new BusException("?????????????????????");
		}
	}

	public void deleteMiddleHisCfxm(String param, IUser user) {
		String jydjh = JsonUtil.getFieldValue(param, "jydjh");
		if (jydjh != null) {
			DataBaseHelper.execute("delete from HIS_CFXM where JYDJH=?",
					new Object[] { jydjh });
			DataBaseHelper.execute("delete from HIS_CFXM_JKSYBZ where JYDJH=?",
					new Object[] { jydjh });
		} else {
			throw new BusException("?????????????????????");
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int getCfxmMaxXh(String jydjh) {
		if (jydjh != null) {
			int count = DataBaseHelper.queryForScalar(
					"select COUNT(1) from his_cfxm where jydjh=? ",
					Integer.class, jydjh);

			return count;
		} else {
			throw new BusException("?????????????????????");
		}
	}

	/*
	 * 015001007045 ??????????????????-???????????????????????????????????????bl_ip_dt???flag_insu??????
	 */
	public void updateFlagInsuByPk(String param, IUser user) {
		List<String> pkCgips = JsonUtil.readValue(param,
				new TypeReference<List<String>>() {
				});
		Set<String> finishDtList = new HashSet<String>(pkCgips);
		if (pkCgips != null && pkCgips.size() > 0) {
			DataBaseHelper
					.execute("update bl_ip_dt set flag_insu='1' where PK_CGIP IN ("
							+ CommonUtils.convertSetToSqlInPart(finishDtList,
									"PK_CGIP") + ")");
		} else {
			return;
		}
	}

	/*
	 * 015001007046 ??????PKPV???????????????????????????
	 */
	public void deleteVisitInfo(String param, IUser user) {
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		if (pkPv == null || "".equals(pkPv)) {
			throw new BusException("????????????????????????");
		}
		String pkVisit = "";
		// 1.??????pkPv??????INS_GZYB_VISIT???????????????
		String vistSql = "select PK_VISIT as pkVisit from INS_GZYB_VISIT  where pk_pv=? and (del_flag='0' or del_flag is null) and EU_STATUS_ST='0'";
		Map<String, Object> pkVisitMap = DataBaseHelper.queryForMap(vistSql,
				pkPv);
		if (pkVisitMap != null && pkVisitMap.get("pkvisit") != null) {
			pkVisit = pkVisitMap.get("pkvisit").toString();

			// 3.??????????????????INS_GZYB_VISIT_CITY,INS_GZYB_VISIT;
			String visitsql = "delete from INS_GZYB_VISIT_CITY where PK_VISIT=?";
			DataBaseHelper.execute(visitsql, new Object[] { pkVisit });
			visitsql = "delete from INS_GZYB_VISIT  where PK_VISIT=?";
			DataBaseHelper.execute(visitsql, new Object[] { pkVisit });
		}
	}

	/**
	 * 015001007048??????????????????????????????????????????????????????
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unused")
	public List<BlipdtReUploadVo> qryBdItemAndOrderByPkSettle(String param,
			IUser user) {
		String pkSettle = JsonUtil.getFieldValue(param, "pkSettle");
		String euhpdicttype = JsonUtil.getFieldValue(param, "euhpdicttype");
		if (pkSettle == null || "".equals(pkSettle)) {
			throw new BusException("????????????????????????");
		}
		if (pkSettle != null) {
			List<BlipdtReUploadVo> cBdItems = gzybMapper
					.qryBdItemAndOrderByPkSettle(pkSettle, euhpdicttype);
			return cBdItems;
		} else {
			return null;
		}
	}

	/** ?????? */
	/**
	 * ????????????????????????????????????????????????????????????-?????????????????????
	 * 
	 * @return
	 */
	public List<MiddleHisMzdj> queryMZInfoList(String gmsfhm, String zyh,
			String xm, String xb) {
		if (gmsfhm != null && zyh != null && xm != null && xb != null) {
			// ?????????????????????
			List<MiddleHisMzdj> list = gzybMapper.queryMZInfoList(gmsfhm, zyh,
					xm, xb);
			return list;
		} else {
			throw new BusException("?????????????????????");
		}
	}

	public void updateMZdrbz(String param, IUser user) {
		String jydjh = JsonUtil.getFieldValue(param, "jydjh");
		String yybh = JsonUtil.getFieldValue(param, "yybh");
		String gmsfhm = JsonUtil.getFieldValue(param, "gmsfhm");
		Integer drbz = Integer.valueOf(JsonUtil.getFieldValue(param, "drbz"));
		if (jydjh != null && yybh != null && gmsfhm != null) {
			if ("0".equals(drbz)) {
				DataBaseHelper
						.update("update HIS_MZDJ set DRBZ=0 where JYDJH=? and YYBH=? and GMSFHM=?",
								new Object[] { jydjh, yybh, gmsfhm });
			} else {
				DataBaseHelper
						.update("update HIS_MZDJ set DRBZ=1 where JYDJH=? and YYBH=? and GMSFHM=?",
								new Object[] { jydjh, yybh, gmsfhm });
			}
		} else {
			throw new BusException("?????????????????????");
		}
	}

	public void addMiddleHisMZxm(List<MiddleHisMzxm> mzxmList) {
		for (MiddleHisMzxm mzxm : mzxmList) {
			DataBaseHelper.insertBean(mzxm);
		}
	}

	public void updateMZCostlist(String param, IUser user) {
		String jydjh = JsonUtil.getFieldValue(param, "jydjh");
		String type = JsonUtil.getFieldValue(param, "type");
		if (jydjh != null) {
			if ("1".equals(type)) {
				DataBaseHelper.update(
						"update HIS_MZJS set DRBZ=1 where JYDJH=?",
						new Object[] { jydjh });
			} else {
				DataBaseHelper.update(
						"update HIS_MZJS set DRBZ=0 where JYDJH=?",
						new Object[] { jydjh });
			}
		} else {
			throw new BusException("?????????????????????");
		}
	}

	public void deleteMiddleHisMZxm(String param, IUser user) {
		String jydjh = JsonUtil.getFieldValue(param, "jydjh");
		if (jydjh != null) {
			DataBaseHelper.execute("delete from HIS_MZXM where JYDJH=?",
					new Object[] { jydjh });
			DataBaseHelper.execute("delete from HIS_CFXM_JKSYBZ where JYDJH=?",
					new Object[] { jydjh });
		} else {
			throw new BusException("?????????????????????");
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public int getMZxmMaxXh(String jydjh) {
		if (jydjh != null) {
			int count = DataBaseHelper.queryForScalar(
					"select COUNT(1) from HIS_MZXM where jydjh=? ",
					Integer.class, jydjh);

			return count;
		} else {
			throw new BusException("?????????????????????");
		}
	}

	public MiddleHisMzjs queryMZCostInfo(String jydjh) {
		if (jydjh != null) {
			MiddleHisMzjs info = gzybMapper.queryMZCostInfo(jydjh);
			if (info != null) {
				return info;
			} else {
				return null;
			}
		} else {
			throw new BusException("?????????????????????");
		}
	}

	public List<BlopdtVo> queryNoSettleInfoForCgByPkPv(String param, IUser user) {

		BlipVo allData = JsonUtil.readValue(param, BlipVo.class);
		String pkpv = allData.getPkpv();
		List<String> pkCgips = allData.getPkCgips();
		if (pkpv != null) {
			List<BlopdtVo> opdItems = gzybMapper.queryNoSettleInfoForCgByPkPv(
					pkpv, pkCgips);
			return opdItems;
		} else {
			throw new BusException("?????????????????????");
		}
	}

	public void saveTrialInfo(String param, IUser user) {
		InsGzybTrialstVo trialstVo = JsonUtil.readValue(param,
				InsGzybTrialstVo.class);
		User u = (User) user;
		if (trialstVo != null) {
			trialstVo.setDelFlag("0");
			trialstVo.setPkOrg(u.getPkOrg());
			if (trialstVo.getPvcodeIns() != null) {
				DataBaseHelper.execute(
						"delete from INS_GZYB_TRIALST where PVCODE_INS=?",
						new Object[] { trialstVo.getPvcodeIns() });
			}
			if (trialstVo.getPkTrial() != null) {
				DataBaseHelper.updateBeanByPk(trialstVo, false);
			} else {
				trialstVo.setCreateTime(new Date());
				trialstVo.setCreator(u.getUserName());
				trialstVo.setTs(new Date());
				DataBaseHelper.insertBean(trialstVo);
			}
		}
	}

	public void updatePvcodeIns(String param, IUser user) {
		String pkpv = JsonUtil.getFieldValue(param, "pkpv");
		String oldjydjh = JsonUtil.getFieldValue(param, "oldjydjh");
		String jydjh = JsonUtil.getFieldValue(param, "jydjh");
		if (pkpv != null && jydjh != null && oldjydjh != null) {
			DataBaseHelper
					.execute(
							"update INS_GZYB_VISIT set PVCODE_INS=? where PK_PV=? and PVCODE_INS=?",
							jydjh, pkpv, oldjydjh);
		} else if (pkpv != null && jydjh != null) {
			DataBaseHelper.execute(
					"update INS_GZYB_VISIT set PVCODE_INS=? where PK_PV=? ",
					jydjh, pkpv);
		} else {
			throw new BusException("?????????????????????");
		}
	}

	/**
	 * ???????????????????????????????????????
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public VisitInfo qrycityPiInfoByPkPv(String param, IUser user) {

		String pkpv = JsonUtil.getFieldValue(param, "pkpv");
		if (pkpv != null) {
			VisitInfo visitInfo = gzybMapper.qrycityPiInfoByPkPv(pkpv);
			return visitInfo;
		} else {
			throw new BusException("?????????????????????????????????");
		}
	}

	/**
	 * ??????????????????????????????
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unused")
	public List<BlopdtVo> querySettleInfoByPkPvAndPkSettle(String param,
			IUser user) {
		String pkpv = JsonUtil.getFieldValue(param, "pkpv");
		String pkSettle = JsonUtil.getFieldValue(param, "pkSettle");
		String euhpdicttype = JsonUtil.getFieldValue(param, "euhpdicttype");
		if (pkpv == null || "".equals(pkpv)) {
			throw new BusException("?????????????????????????????????");
		}
		if (pkSettle == null || "".equals(pkSettle)) {
			throw new BusException("????????????????????????");
		}
		if (pkSettle != null) {
			List<BlopdtVo> cBdItems = gzybMapper
					.querySettleInfoByPkPvAndPkSettle(pkpv, pkSettle,
							euhpdicttype);
			return cBdItems;
		} else {
			return null;
		}
	}

	public void updateCodePc(String param, IUser user) {
		String pkpv = JsonUtil.getFieldValue(param, "pkpv");
		String codePc = JsonUtil.getFieldValue(param, "codePc");
		if (pkpv != null && codePc != null) {
			DataBaseHelper
					.execute(
							"UPDATE INS_GZYB_VISIT_CITY SET CODE_PC=? where PK_VISIT IN (SELECT PK_VISIT FROM INS_GZYB_VISIT WHERE PK_PV=?)",
							codePc, pkpv);
		} else {
			throw new BusException("?????????????????????");
		}
	}

	public void updateIDNOMZMB(String param, IUser user) {
		String pkpv = JsonUtil.getFieldValue(param, "pkpv");
		String idnomzmb = JsonUtil.getFieldValue(param, "idnomzmb");
		if (pkpv != null && idnomzmb != null) {
			DataBaseHelper
					.execute(
							"UPDATE INS_GZYB_VISIT_CITY SET IDNO_MZMB=? where PK_VISIT IN (SELECT PK_VISIT FROM INS_GZYB_VISIT WHERE PK_PV=?)",
							idnomzmb, pkpv);
		} else {
			throw new BusException("?????????????????????");
		}
	}

	public List<GzDiagVo> qryDiagVo(String param, IUser user) {
		String pkpv = JsonUtil.getFieldValue(param, "pkPv");
		if (pkpv == null || "".equals(pkpv)) {
			throw new BusException("???????????????????????????????????????");
		}
		List<GzDiagVo> list = gzybMapper.qryDiagVo(pkpv);
		return list;
	}

	public void updatePzh(String param, IUser user) {
		String pkpv = JsonUtil.getFieldValue(param, "pkpv");
		String number = JsonUtil.getFieldValue(param, "number");
		String type = JsonUtil.getFieldValue(param, "number");
		if (pkpv != null && number != null) {
			if ("0".equals(type)) {
				DataBaseHelper
						.execute(
								"UPDATE INS_GZYB_VISIT_CITY SET IDNO_GS=? where PK_VISIT IN (SELECT PK_VISIT FROM INS_GZYB_VISIT WHERE PK_PV=?)",
								number, pkpv);
			}
			if ("1".equals(type)) {
				DataBaseHelper
						.execute(
								"UPDATE INS_GZYB_VISIT_CITY SET IDNO_SY=? where PK_VISIT IN (SELECT PK_VISIT FROM INS_GZYB_VISIT WHERE PK_PV=?)",
								number, pkpv);
			}
		} else {
			throw new BusException("?????????????????????");
		}
	}

	public String getDiseaseNote(String param, IUser user) {
		String pkPv = JsonUtil.getFieldValue(param, "pkPv");
		if (pkPv == null || "".equals(pkPv)) {
			throw new BusException("???????????????????????????????????????");
		}
		String note = "";
		String stSql = "SELECT G.NOTE FROM PV_DIAG D INNER JOIN INS_GZGY_DISEASE G ON D.PK_DIAG=G.PK_DIAG WHERE D.EU_SPTYPE ='2' AND D.PK_PV=? AND D.DEL_FLAG='0' AND G.DEL_FLAG='0'";
		Map<String, Object> noteMap = DataBaseHelper.queryForMap(stSql, pkPv);
		if (noteMap != null && noteMap.get("note") != null) {
			note = noteMap.get("note").toString();
		}
		return note;
	}

	public VisitInfo qrycityPiInfoByPkVisit(String param, IUser user) {

		String pkVisit = JsonUtil.getFieldValue(param, "pkVisit");
		if (pkVisit != null) {
			VisitInfo visitInfo = gzybMapper.qrycityPiInfoByPkVisit(pkVisit);
			return visitInfo;
		} else {
			throw new BusException("?????????????????????????????????");
		}
	}

	public SettlementInfo qrycitySettlementInfoByPkVisit(String param,
			IUser user) {
		String pkVisit = JsonUtil.getFieldValue(param, "pkVisit");
		if (pkVisit != null) {
			SettlementInfo sitInfo = gzybMapper
					.qrycitySettlementInfoByPkVisit(pkVisit);
			return sitInfo;
		} else {
			throw new BusException("?????????????????????????????????");
		}
	}

	public void saveInsGzybStInjuryVo(String param, IUser user) {
		InsGzybStInjuryVo stInjuryVo = JsonUtil.readValue(param,
				InsGzybStInjuryVo.class);
		User u = (User) user;
		if (stInjuryVo != null) {
			stInjuryVo.setDelFlag("0");
			stInjuryVo.setPkOrg(u.getPkOrg());
			if (stInjuryVo.getPkInsst() != null) {
				DataBaseHelper.execute(
						"delete from INS_GZYB_ST_INJURY where PK_INSST= ?",
						Integer.class, stInjuryVo.getPkInsst());
			}
			if (stInjuryVo.getPkInsstinjury() != null) {
				DataBaseHelper.updateBeanByPk(stInjuryVo, false);
			} else {
				stInjuryVo.setCreateTime(new Date());
				stInjuryVo.setCreator(u.getUserName());
				stInjuryVo.setTs(new Date());
				DataBaseHelper.insertBean(stInjuryVo);
			}
		}
	}

	public InsGzybStInjuryVo qryInsGzybStInjuryVoByPkInsst(String param,
			IUser user) {
		String pkInsst = JsonUtil.getFieldValue(param, "pkInsst");
		if (pkInsst != null) {
			InsGzybStInjuryVo stInjuryVo = gzybMapper
					.qryInsGzybStInjuryVoByPkInsst(pkInsst);
			return stInjuryVo;
		} else {
			throw new BusException("????????????????????????");
		}
	}

	public String update(String param, IUser user) {
		User u = (User) user;
		VisitVo vo = JsonUtil.readValue(param, VisitVo.class);
		GzybVisit visit = vo.getVisit();
		GzybVisitCity visitCity = vo.getVisitCity();
		String pkVisit = "";
		// 1.??????pkPv??????INS_GZYB_VISIT???????????????
		String vistSql = "select PK_VISIT as pkVisit from INS_GZYB_VISIT  where pk_pv=? and (del_flag='0' or del_flag is null) and EU_STATUS_ST='0'";
		Map<String, Object> pkVisitMap = DataBaseHelper.queryForMap(vistSql,
				visit.getPkpv());
		if (pkVisitMap != null && pkVisitMap.get("pkvisit") != null) {
			pkVisit = pkVisitMap.get("pkvisit").toString();
			String visitsql = "delete from INS_GZYB_VISIT_CITY where PK_VISIT=?";
			DataBaseHelper.execute(visitsql, new Object[] { pkVisit });
			visitsql = "delete from INS_GZYB_VISIT  where PK_VISIT=?";
			DataBaseHelper.execute(visitsql, new Object[] { pkVisit });
		}
		// ????????????
		if (visit != null) {
			visit.setDelFlag("0");
			visit.setPkOrg(u.getPkOrg());
			if (visit.getPkvisit() != null) {
				DataBaseHelper.updateBeanByPk(visit, false);
			} else {
				visit.setCreateTime(new Date());
				visit.setCreator(u.getUserName());
				DataBaseHelper.insertBean(visit);
			}
			if (visitCity.getCodeIp() != null) {
				// ????????????-?????????
				visitCity.setDelFlag("0");
				visitCity.setPkOrg(u.getPkOrg());
				visitCity.setPkVisit(visit.getPkvisit());
				if (visitCity.getPkvisitcity() != null) {
					DataBaseHelper.updateBeanByPk(visitCity, false);
				} else {
					visitCity.setCreateTime(new Date());
					visitCity.setCreator(u.getUserName());
					DataBaseHelper.insertBean(visitCity);
				}
			}
		}
		return visit.getPkvisit();
	}

	/**
	 * ???????????????????????????
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public DeptVo qryDeptVo(String param, IUser user) {
		String pkdept = JsonUtil.getFieldValue(param, "pkdept");
		DeptVo deptVo = gzybMapper.qryDeptVo(pkdept);
		return deptVo;
	}

	/**
	 * ??????????????????????????????
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getHpAttr(String param, IUser user) {
		String pkHp = JsonUtil.getFieldValue(param, "pkHp");
		List<Map<String, Object>> attresList = new ArrayList<Map<String, Object>>();
		if (StringUtils.isNotBlank(pkHp)) {
			attresList = DataBaseHelper
					.queryForList(
							"select   attrtmp.code_attr, attr.val_attr from bd_hp hp  inner join bd_dictattr attr on hp.pk_hp = attr.pk_dict inner join bd_dictattr_temp attrtmp on attrtmp.pk_dictattrtemp = attr.pk_dictattrtemp  where hp.pk_hp=? ",
							new Object[] { pkHp });
		}
		return attresList;
	}
}