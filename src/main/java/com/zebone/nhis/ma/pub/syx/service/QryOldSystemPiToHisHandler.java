package com.zebone.nhis.ma.pub.syx.service;

import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.syx.dao.QryOldSysPiMapper;
import com.zebone.nhis.ma.pub.syx.vo.OldPiInfo;
import com.zebone.nhis.ma.pub.syx.vo.Tippatientseqno;
import com.zebone.nhis.ma.pub.syx.vo.Tpatient;
import com.zebone.nhis.pi.pub.support.ClientUtils;
import com.zebone.nhis.pv.pub.vo.AdtRegParam;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QryOldSystemPiToHisHandler {

	@Autowired
	private QryOldSystemPiToHisService oldSystemPiToHisService;

	@Autowired
	private QryOldSysPiMapper qryOldSysPiMapper;

	public Object invokeMethod(String methodName, Object... args) {
		Object result = null;
		switch (methodName) {
		case "piInfo":
			result = this.qryPiInfo(args);
			break;
		case "qryCodeIp4OldSys":
			result = this.qryCodeIp4OldSys(args);
			break;
		case "savePvToOldSys":
			result = this.savePvToOldSys(args);
			break;
		case "updateOldSysPi":
			result = this.updateOldSysPi(args);
			break;
		case "cancelInhospital":
			this.cancelInhospital(args);
			break;
		case "isExistPi":
			result = this.isExistPi(args);
			break;
		}
		return result;
	}

	/**
	 * 为webservice提供调用方式
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public Object invokeMethod(String param, IUser user) {
		Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);

		Object result = null;
		Object args = paramMap.get("param");
		switch (paramMap.get("methodName").toString()) {
		case "piInfo":
			result = this.qryPiInfo(param);
			break;
		case "qryCodeIp4OldSys":
			result = this.qryCodeIp4OldSys(args);
			break;
		case "savePvToOldSys":
			result = this.savePvToOldSys(args);
			break;
		case "updateOldSysPi":
			result = this.updateOldSysPi(args);
			break;
		case "cancelInhospital":
			this.cancelInhospital(args);
			break;
		}
		return result;
	}

	/**
	 * 查询新系统是否存在该患者，因为查询旧系统患者时会根据旧系统的住院号删除掉新系统没有就诊记录的患者，
	 * 故患者入院前先判断pi_master表的数据是否被删除
	 * 
	 * @param args
	 * @return
	 */
	private Object isExistPi(Object[] args) {
		Integer pi = -1;
		if (args != null && args.length > 1) {
			if (args[0] != null && !"".equals(args[0].toString())) {
				pi = DataBaseHelper.queryForScalar(
						"select count(1) from pi_master where pk_pi=?",
						Integer.class, args[0]);
				if (pi == 0) {
					oldSystemPiToHisService.delNhisPi(args[1].toString());
				}
			}
		}
		return pi;
	}

	/**
	 * 取消入院，更新旧系统患者信息
	 * 
	 * @param args
	 */
	private void cancelInhospital(Object... args) {
		if (args != null) {
			String pkPv = (String) args[0];
			PiMaster pi = qryOldSysPiMapper.cancelInhospital(pkPv);
			try {
				DataSourceRoute.putAppId("syxip");// 切换数据源
				// 调用Service类保存数据并提交事务
				oldSystemPiToHisService.cancelInhospital(pi);
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusException("更新旧系统患者失败！\n" + e.getMessage());
			} finally {
				DataSourceRoute.putAppId("default");// 切换数据源
			}
		}
	}

	/**
	 * 更新旧系统患者
	 * 
	 * @param args
	 */
	private Object updateOldSysPi(Object... args) {
		Object ipParam = null;
		if (args != null) {
			AdtRegParam regParam = JsonUtil.readValue((String) args[0],
					AdtRegParam.class);
			try {
				DataSourceRoute.putAppId("syxip");// 切换数据源
				// 调用Service类保存数据并提交事务
				ipParam = oldSystemPiToHisService.updateOldSysPi(regParam
						.getPiMaster());
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusException("更新旧系统患者失败！\n" + e.getMessage());
			} finally {
				DataSourceRoute.putAppId("default");// 切换数据源
			}
		}
		return ipParam;
	}

	/**
	 * 查询该患者是否在就系统中入院，如果入院则查出住院号和住院次数，并返回更新方法，否则返回新增方法
	 */
	private Object qryCodeIp4OldSys(Object... args) {
		Object ipParam = null;
		if (args != null) {
			PiMaster master = new PiMaster();
			AdtRegParam regParam = JsonUtil.readValue((String) args[0],
					AdtRegParam.class);
			if (regParam.getPiMaster() != null) {
				master = regParam.getPiMaster();
			} else {
				master = JsonUtil.readValue((String) args[0], PiMaster.class);
			}
			try {
				DataSourceRoute.putAppId("syxip");// 切换数据源
				// 调用Service类保存数据并提交事务
				ipParam = oldSystemPiToHisService.qryCodeIp4OldSys(master);
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusException("从旧系统获取患者住院号失败！\n" + e.getMessage());
			} finally {
				DataSourceRoute.putAppId("default");// 切换数据源
			}
		}
		return ipParam;
	}

	/**
	 * 保存患者就诊至旧系统
	 * 
	 * @param args
	 */
	@SuppressWarnings("deprecation")
	private String savePvToOldSys(Object... args) {
		String codeIp = null;
		if (args != null) {
			AdtRegParam regParam = JsonUtil.readValue((String) args[0],
					AdtRegParam.class);
			// 组装插入对象
			Tippatientseqno tippatientseqno = new Tippatientseqno();
			tippatientseqno.setIpseqno(regParam.getPiMaster().getCodeIp());// 住院流水号
			tippatientseqno.setIpseqprefix("");// 住院号前缀
			tippatientseqno.setPatientid(regParam.getPiMaster().getCodePi());// 患者ID
			tippatientseqno.setIptimes(regParam.getIpTimes());// 住院次数
			tippatientseqno.setStatusflag("0");// 状态标志
			tippatientseqno.setLastinpatientid("-1");
			tippatientseqno.setOldipno("");// 旧住院号
			tippatientseqno.setIpseqnotext(regParam.getPiMaster().getCodeIp());

			Tpatient tpatient = new Tpatient();
			tpatient.setPatientid(regParam.getPiMaster().getCodePi());// 门诊患者ID
			tpatient.setPatientname(regParam.getPiMaster().getNamePi());// 姓名
			tpatient.setPatientno(regParam.getPiMaster().getCodePi());// 患者编号(门诊病历号)
			tpatient.setPatienttypelistid("-1");// 患者类型明细ID
			tpatient.setPhone(regParam.getPiMaster().getMobile());// 电话
			tpatient.setPisspecialcompanyid("-1");// 体检单位
			tpatient.setSecretflag("0");// 保密标志
			tpatient.setBirthday(regParam.getPiMaster().getBirthDate());// 出生日期
			tpatient.setDiagnoseflag("0");// 诊疗标志
			tpatient.setDwtranflag("1");// WH转换标志
			tpatient.setEldercertificateno("");// 老人优待证
			tpatient.setEmployeefamilyid("-1");// 员工家属ID
			tpatient.setEmployeeid("-1");// 员工ID
			tpatient.setHeightcm("0");// 身高(厘米)
			tpatient.setIdentitycardno(regParam.getPiMaster().getIdNo());// 身份证号
			tpatient.setInvaliddatetime(DateUtils.strToDate("19000101000000"));// 无效时间
			tpatient.setInvalidflag("0");// 无效标志
			tpatient.setIpproportion("10000");// 住院自负比例(万分数)
			tpatient.setMedicalcertificateno(regParam.getMcno() + "");// 医疗证号
			tpatient.setMedicarecardno(regParam.getPiMaster().getInsurNo() + "");// 医保卡号
			tpatient.setMedicarepersonnelno("");// 医保个人编号
			tpatient.setMedicaretype("0");// 医保类型
			tpatient.setOpproportion("10000");// 门诊自负比例(万分数)
			tpatient.setOpspecialitemipflag("255");// 门诊特定项目病人住院标志
			tpatient.setPatientcardno("");// 就诊卡号
			if ("02".equals(regParam.getPiMaster().getDtSex())) {
				tpatient.setSexflag("1");// 性别
			} else if ("03".equals(regParam.getPiMaster().getDtSex())) {
				tpatient.setSexflag("2");// 性别
			} else {
				tpatient.setSexflag("0");// 性别
			}

			tpatient.setSpecialcompanyid("-1");// 特约单位ID
			tpatient.setVipcardno("");// VIP卡号码
			tpatient.setVipcardtype("0");// VIP卡类型
			tpatient.setWeightkg("0");// 体重(千克)
			tpatient.setCompanycode(regParam.getDictSpecunit() + "");// 单位代码
			try {
				DataSourceRoute.putAppId("syxip");// 切换数据源
				// 调用Service类保存数据并提交事务
				codeIp = oldSystemPiToHisService.savePvToOldSys(
						tippatientseqno, tpatient);
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusException("保存患者至旧系统失败！\n" + e.getMessage());
			} finally {
				DataSourceRoute.putAppId("default");// 切换数据源
			}
		}
		return codeIp;
	}

	/**
	 * 查询旧系统患者信息保存新HIS系统（新系统与旧系统并行期间保持患者住院号，患者编码同步） 1.查询旧系统患者
	 * 2.判断HIS系统是否存在该患者，不存在则插入
	 * 
	 * @param args
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<OldPiInfo> qryPiInfo(Object... args) {
		List<OldPiInfo> qryPiInfo = null;
		if (args != null) {
			// 查询新系统是否存在该患者，且该患者存在就诊记录，和住院记录
			Map<String, Object> qryMap = JsonUtil.readValue((String) args[0],
					Map.class);
			String idno = (String) qryMap.get("idNo");
			String idCardNo = ""; // 15位
			int count = 0;
			// if (idno != null) {
			// qryMap.put("idNo", idno);
			// count = qryOldSysPiMapper.qrypiCount(qryMap);
			// if (count > 0) {
			// return null;
			// }
			// try {
			// DataSourceRoute.putAppId("syxip");// 切换数据源
			// // 调用Service类保存数据并提交事务
			// qryMap.put("idNo", idno);
			// qryPiInfo = oldSystemPiToHisService.qryPiInfo(qryMap);
			// if (qryPiInfo == null) {
			// if (idno.length() == 18) {
			// idCardNo = get15Ic(idno);
			// qryMap.put("idCardNo", idCardNo);
			// }
			// qryPiInfo = oldSystemPiToHisService
			// .qryPiInfo(qryMap);
			// }
			// }
			// } catch (Exception e) {
			// e.printStackTrace();
			// throw new BusException("查询旧系统患者信息失败！\n" + e.getMessage());
			// } finally {
			// DataSourceRoute.putAppId("default");// 切换数据源
			// }
			// if (qryPiInfo != null) {
			// // 如果该患者在新HIS系统中不存在就诊记录和住院记录，那么删除该患者，在前台操作重新新增
			// oldSystemPiToHisService.delNhisPi(qryPiInfo);
			// }
			// } else {
			count = qryOldSysPiMapper.qrypiCount(qryMap);
			if (count > 0) {
				return null;
			}
			try {
				DataSourceRoute.putAppId("syxip");// 切换数据源
				// 调用Service类保存数据并提交事务
				// qryMap.put("idNo", idno);
				qryPiInfo = oldSystemPiToHisService.qryPiInfo(qryMap);
			} catch (Exception e) {
				e.printStackTrace();
				throw new BusException("查询旧系统患者信息失败！\n" + e.getMessage());
			} finally {
				DataSourceRoute.putAppId("default");// 切换数据源
			}

			// if (qryPiInfo != null) {
			// // 如果该患者在新HIS系统中不存在就诊记录和住院记录，那么删除该患者，在前台操作重新新增
			// oldSystemPiToHisService.delNhisPi(qryPiInfo);
			// }
			// }

			// oldSystemPiToHisService.savePiToHis(qryPiInfo);
		}
		return qryPiInfo;
	}

	/**
	 * 将18位身份证号转化为15位返回,非18位身份证号原值返回
	 * 
	 * @param identityCard
	 *            18位身份证号
	 * @return 15身份证
	 */
	public String get15Ic(String identityCard) {
		String retId = "";
		if (null == identityCard) {
			return retId;
		}
		if (identityCard.length() == 18) {
			retId = identityCard.substring(0, 6)
					+ identityCard.substring(8, 17);
		} else {
			return identityCard;
		}
		return retId;
	}

	/**
	 * 中山二院需求，新老系统并行使用期间，读卡器查询旧系统患者保存至新系统 002003001034
	 * 
	 * @param param
	 * @param user
	 */
	public List<OldPiInfo> qryOldPiInfoToHis(String param, IUser user) {
		List<OldPiInfo> oldPiInfos = qryPiInfo(param);

		if (oldPiInfos == null || oldPiInfos.size() < 1) {
			PiMaster piMaster = JsonUtil.readValue(param, PiMaster.class);
			ClientUtils.queryEmpi(piMaster);

			// if (!StringUtils.isEmpty(piMaster.getCodeIp())) {
			// try {
			// DataSourceRoute.putAppId("syxip");// 切换数据源
			// Map<String, Object> ipTimes =
			// DataBaseHelper.queryForMap("select IPTimes from tIPPatientSeqNO where IPSeqNoText=? ",
			// new Object[]{piMaster.getCodeIp()});
			// } catch (Exception e) {
			// e.printStackTrace();
			// throw new BusException("查询旧系统患者信息失败！\n" + e.getMessage());
			// } finally {
			// DataSourceRoute.putAppId("default");// 切换数据源
			// }
			// }
		}
		return oldPiInfos;
	}
}
