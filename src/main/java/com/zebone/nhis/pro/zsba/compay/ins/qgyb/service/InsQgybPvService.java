package com.zebone.nhis.pro.zsba.compay.ins.qgyb.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.compay.ins.pub.service.InsPubSignInService;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.dao.InsQgybFeedetailMapper;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.dao.InsQgybPvMapper;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.dao.InsQgybSignInMapper;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.utils.HanziZhuanPinyin;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.utils.YbFunUtils;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input1201Medinsinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input1901Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2001Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input90100Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Output1201Medinsinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Output1901Downdatadld;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input1101Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Output1101Insuinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2301Feedetail;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Output2001Trtinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2302Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2303Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2304Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Output2301Result;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Output2304Setldetail;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2305Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2401Diseinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2401Mdtrtinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2402Diseinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2402Dscginfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2403Adminfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2403Diseinfo;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2404Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Input2405Data;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.BottomClass.Output5204Fymx;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaDiseinfoQg;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaFeedetail2301Qg;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaFeedetailQg;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaPvQg;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaSignInQg;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaStQg;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.Business.InsZsbaStSetldetailQg;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input1101;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input1201;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input1901;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input2001;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input2301;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input2302;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input2303;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input2304;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input2305;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input2401;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input2402;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input2403;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input2404;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input2405;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Input90100;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Output1901;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Output2301;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.Output90100;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData1101;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData1201;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData1901;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData2001;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData2301;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData2302;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData2303;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData2304;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData2305;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData2401;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData2402;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData2403;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData2404;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData2405;
import com.zebone.nhis.pro.zsba.compay.ins.qgyb.vo.TransferClass.OutputData90100;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.dao.InsZsybPvMapper;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbDict;
import com.zebone.nhis.pro.zsba.compay.ins.zsyb.vo.InsZsBaYbDicttype;
import com.zebone.nhis.pro.zsba.mz.ins.zsba.service.qgyb.ZsbaQGUtils;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class InsQgybPvService {

	@Autowired
	private InsZsybPvMapper insPvMapper;
	@Autowired
	private InsQgybPvMapper insQgybPvMapper;
	@Autowired
	private InsQgybSignInMapper insQgybSignInMapper;
	@Autowired
	private InsQgybFeedetailMapper insQgybFeedetailMapper;
	
	@Autowired
	private InsPubSignInService insPubSignInService;
	
	/**
	 * 医保字典查询1901
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> getDict(String param,IUser user){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		JSONObject jo = JSONObject.fromObject(param);
		String type = jo.getString("type");
		//String date = jo.getString("date");
		String ip = jo.getString("ip");
		String mac = jo.getString("mac");
		InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
		if(signIn.getCode().equals("0")){
			Input1901 input  = new Input1901();
			Input1901Data data = new Input1901Data();
			data.setType(type);
			data.setDate(DateUtils.getDateTime());
			data.setAdmdvs("442000");
			data.setValiFlag("1");
			input.setData(data);
			OutputData1901 paramOut = YbFunUtils.fun1901(input, signIn.getSignNo());
			if(paramOut.getInfcode()!=null && paramOut.getInfcode().equals("0")){
				returnMap.put("code", "0");
				returnMap.put("msg", "获取字典成功！");
				returnMap.put("dictList", paramOut.getOutput().getList());
			}else{
				returnMap.put("code", "-1");
				returnMap.put("msg", paramOut.getErr_msg()+paramOut.getMessage());
			}
		}else{
			returnMap.put("code", signIn.getCode());
			returnMap.put("msg",  signIn.getMsg());
		}
		return returnMap;
	}
	
	/**
	 * 保存医保字典
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> saveDict(String param,IUser user){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("code", "0");
		returnMap.put("msg", "保存成功");
		Output1901 output1901 =  JsonUtil.readValue(param, Output1901.class);
		if(output1901.getList().size()>0){
			BdHp bdHp = DataBaseHelper.queryForBean("select * from bd_hp where code = '00051' and del_flag = '0'", BdHp.class);
			if(bdHp!=null){
				InsZsBaYbDicttype dictType = DataBaseHelper.queryForBean("select * from INS_DICTTYPE where pk_hp = ? and code_type = ? and del_flag = '0'", 
						InsZsBaYbDicttype.class, new Object[]{bdHp.getPkHp(), output1901.getList().get(0).getType()});
				if(dictType!=null){
					StringBuffer codes = new StringBuffer();
					String type = "";
					for (Output1901Downdatadld dict : output1901.getList()) {
						type  = dict.getType();
						codes.append("'"+dict.getValue()+"',");
						StringBuffer sql = new StringBuffer("select a.* from ins_dict a ");
						sql.append(" inner join INS_DICTTYPE b on a.pk_insdicttype = b.pk_insdicttype");
						sql.append(" inner join bd_hp c on b.pk_hp = c.pk_hp");
						sql.append(" where a.del_flag = '0' and b.del_flag = '0' and c.del_flag = '0'");
						sql.append(" and c.code = '00051' and b.code_type = ? and a.code = ?");
						InsZsBaYbDict bdDict = DataBaseHelper.queryForBean(sql.toString(), InsZsBaYbDict.class, new Object[]{dict.getType(), dict.getValue()});
						if(bdDict==null){
							bdDict = new InsZsBaYbDict();
							bdDict.setPkInsdicttype(dictType.getPkInsdicttype());
							bdDict.setCode(dict.getValue());
							bdDict.setName(dict.getLabel());
							bdDict.setSpcode(HanziZhuanPinyin.ToFirstChar(dict.getLabel()));
							bdDict.setFlagDef("0");
							bdDict.setDelFlag("0");
							DataBaseHelper.insertBean(bdDict);
						}
					}
					//医保没有但本地有的字典都假删
					StringBuffer sql = new StringBuffer("select a.* from ins_dict a ");
					sql.append(" inner join INS_DICTTYPE b on a.pk_insdicttype = b.pk_insdicttype");
					sql.append(" inner join bd_hp c on b.pk_hp = c.pk_hp");
					sql.append(" where a.del_flag = '0' and b.del_flag = '0' and c.del_flag = '0'");
					sql.append(" and c.code = '00051' and b.code_type = ? and a.code not in (?)");
					List<InsZsBaYbDict> DictList = DataBaseHelper.queryForList(sql.toString(), InsZsBaYbDict.class, new Object[]{type, codes.toString()});
					for (InsZsBaYbDict insZsBaYbDict : DictList) {
						insZsBaYbDict.setDelFlag("0");
						DataBaseHelper.updateBeanByPk(insZsBaYbDict);
					}
				}else{
					returnMap.put("code", "-1");
					returnMap.put("msg", "查询不到键值为‘"+output1901.getList().get(0).getType()+"’的字典类型，请联系信息科处理！");
				}
			}else{
				returnMap.put("code", "-1");
				returnMap.put("msg", "查询不到‘全国普通医保’的医保计划，请联系信息科处理！");
			}
		}else{
			returnMap.put("code", "-1");
			returnMap.put("msg", "保存数据为0行，无需保存");
		}
		return returnMap;
	}
	
	/**
	 * 1101获取人员参保信息
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getData1101(String param,IUser user){
		List<Map<String, Object>> ryList = new ArrayList<Map<String,Object>>();
		InsZsbaPvQg insPv = JsonUtil.readValue(param, InsZsbaPvQg.class);
		
		InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(insPv.getIp(), insPv.getMac());
		if(signIn.getCode().equals("0")){
			Input1101Data data = new Input1101Data();
			data.setMdtrt_cert_type(insPv.getMdtrtCertType());
			if(insPv.getMdtrtCertType().equals("02") && insPv.getMdtrtCertNo()!=null && insPv.getMdtrtCertNo().length()==15){
				//15为身份证转18位
				data.setMdtrt_cert_no(ZsbaQGUtils.getEighteenIDCard(insPv.getMdtrtCertNo()));
			}else{
				data.setMdtrt_cert_no(insPv.getMdtrtCertNo());
			}
				
			data.setCard_sn(insPv.getCardSn());
			data.setCertno(insPv.getCertno());
			data.setBegntime(insPv.getBegntime());
			//data.setCertno(insPv.getMdtrtCertNo());
			data.setPsn_name(insPv.getPsnName());
			if(insPv.getDtIdtype()==null || (!insPv.getDtIdtype().equals("01") && !insPv.getDtIdtype().equals("98"))){
				//data.setPsn_cert_type("99");
				data.setMdtrt_cert_type("99");
				data.setCertno(insPv.getMdtrtCertNo());
			}
			/*data.setPsn_cert_type("06");
			data.setBegntime(com.zebone.nhis.pro.zsba.compay.ins.qgyb.utils.DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
			data.setPsn_name("陈宝欣");*/
			Input1101 input1101 = new Input1101();
			input1101.setData(data);
			OutputData1101 out1101 = YbFunUtils.fun1101(input1101, insPv.getInsuplcAdmdvs(), signIn.getSignNo());
			
			StringBuffer sql = new StringBuffer("select a.* from ins_dict a ");
			sql.append(" inner join INS_DICTTYPE b on a.pk_insdicttype = b.pk_insdicttype");
			sql.append(" inner join bd_hp c on b.pk_hp = c.pk_hp");
			sql.append(" where a.del_flag = '0' and b.del_flag = '0' and c.del_flag = '0'");
			sql.append(" and c.code = ? and b.code_type = ? and a.code = ?");
			
			if(out1101.getInfcode()==null || !out1101.getInfcode().equals("0")){
				Map<String, Object> ry = new HashMap<String, Object>();
				ry.put("code", "-1");
				ry.put("msg",  out1101.getErr_msg()+out1101.getMessage());
				ryList.add(ry);
			}else{
				if(out1101.getOutput()!=null && out1101.getOutput().getInsuinfo()!=null && out1101.getOutput().getInsuinfo().size()>0){
					Map<String, Object> zdbjdx = new HashMap<String, Object>();//重点保健对象
					for (int i = 0; i < out1101.getOutput().getIdetinfo().size(); i++) {
						if(out1101.getOutput().getIdetinfo().get(i).getPsn_idet_type()!=null && (out1101.getOutput().getIdetinfo().get(i).getPsn_idet_type().equals("7001") || out1101.getOutput().getIdetinfo().get(i).getPsn_idet_type().equals("7002"))){
							zdbjdx.put("psnIdetType", out1101.getOutput().getIdetinfo().get(i).getPsn_idet_type());
							zdbjdx.put("idetBegntime", out1101.getOutput().getIdetinfo().get(i).getBegntime());
							zdbjdx.put("idetEndtime", out1101.getOutput().getIdetinfo().get(i).getEndtime());
							break;
						}
					}
					for (Output1101Insuinfo insuinfo : out1101.getOutput().getInsuinfo()) {
						//if(insuinfo.getPsn_insu_stas().equals("1")){
						if(insPv.getHpCode()!=null&&(insPv.getHpCode().equals("00058")||insPv.getHpCode().equals("00059"))){
							//if(insuinfo.getInsutype().equals("310")||insuinfo.getInsutype().equals("390")){
							if(1==1){
								Map<String, Object> ry = new HashMap<String, Object>();
								ry.put("balc", insuinfo.getBalc());//余额
								InsZsBaYbDict dict = DataBaseHelper.queryForBean(sql.toString(), InsZsBaYbDict.class, new Object[]{"00051","insutype", insuinfo.getInsutype()});
								ry.put("insutype", insuinfo.getInsutype());//险种类型
								ry.put("insuName", dict==null?"":dict.getName());
								dict = DataBaseHelper.queryForBean(sql.toString(), InsZsBaYbDict.class, new Object[]{"00051","psn_type", insuinfo.getPsn_type()});
								ry.put("psnType", insuinfo.getPsn_type());//人员类别
								ry.put("psnTypeName", dict==null?"":dict.getName());
								ry.put("cvlservFlag", insuinfo.getCvlserv_flag());//公务员标志
								ry.put("cvlservName", insuinfo.getCvlserv_flag().toString() == "1"?"是":"否");
								dict = DataBaseHelper.queryForBean(sql.toString(), InsZsBaYbDict.class, new Object[]{"00031","AAB301", insuinfo.getInsuplc_admdvs()});
								ry.put("insuplcAdmdvs", insuinfo.getInsuplc_admdvs());//参保地医保区划
								ry.put("insuplcAdmdvsName", dict==null?"":dict.getName());//参保地医保区划
								ry.put("empName", insuinfo.getEmp_name());//单位名称
								ry.put("psnNo", out1101.getOutput().getBaseinfo().getPsn_no());//人员编号
								dict = DataBaseHelper.queryForBean(sql.toString(), InsZsBaYbDict.class, new Object[]{"00051","psn_cert_type", out1101.getOutput().getBaseinfo().getPsn_cert_type()});
								ry.put("psnCertType", out1101.getOutput().getBaseinfo().getPsn_cert_type());//人员证件类型
								ry.put("psnCertName", dict==null?"":dict.getName());//人员证件类型
								ry.put("certno", out1101.getOutput().getBaseinfo().getCertno());//证件号码
								ry.put("psnName", out1101.getOutput().getBaseinfo().getPsn_name());//人员姓名
								ry.put("gend", out1101.getOutput().getBaseinfo().getGend());//性别
								ry.put("naty", out1101.getOutput().getBaseinfo().getNaty());//民族
								ry.put("brdy", out1101.getOutput().getBaseinfo().getBrdy());//出生日期
								ry.put("age", out1101.getOutput().getBaseinfo().getAge());//年龄
								ry.put("psnInsuDate", insuinfo.getPsn_insu_date());//参保时间
								ry.put("pausInsuDate", insuinfo.getPaus_insu_date());//停保时间
								ry.put("psnInsuStas", insuinfo.getPsn_insu_stas());//参保状态
								ry.put("code", "0");
								ry.put("msg", "");
								if(insuinfo.getInsuplc_admdvs().equals("442000")){
									ry.put("psnIdetType", zdbjdx.get("psnIdetType"));
									ry.put("idetBegntime",  zdbjdx.get("idetBegntime"));
									ry.put("idetEndtime",  zdbjdx.get("idetEndtime"));
								}else{
									ry.put("psnIdetType", null);
									ry.put("idetBegntime",  null);
									ry.put("idetEndtime",  null);
								}
								ryList.add(ry);
							}
						}else{
							//if(insuinfo.getPsn_insu_stas()!=null && insuinfo.getPsn_insu_stas().equals("1")&&(insuinfo.getInsutype().equals("310")||insuinfo.getInsutype().equals("390"))){
							if(insuinfo.getPsn_insu_stas()!=null &&(insuinfo.getInsutype().equals("310")||insuinfo.getInsutype().equals("390"))){	
								Map<String, Object> ry = new HashMap<String, Object>();
								ry.put("balc", insuinfo.getBalc());//余额
								InsZsBaYbDict dict = DataBaseHelper.queryForBean(sql.toString(), InsZsBaYbDict.class, new Object[]{"00051","insutype", insuinfo.getInsutype()});
								ry.put("insutype", insuinfo.getInsutype());//险种类型
								ry.put("insuName", dict==null?"":dict.getName());
								dict = DataBaseHelper.queryForBean(sql.toString(), InsZsBaYbDict.class, new Object[]{"00051","psn_type", insuinfo.getPsn_type()});
								ry.put("psnType", insuinfo.getPsn_type());//人员类别
								ry.put("psnTypeName", dict==null?"":dict.getName());
								ry.put("cvlservFlag", insuinfo.getCvlserv_flag());//公务员标志
								ry.put("cvlservName", insuinfo.getCvlserv_flag().toString() == "1"?"是":"否");
								dict = DataBaseHelper.queryForBean(sql.toString(), InsZsBaYbDict.class, new Object[]{"00031","AAB301", insuinfo.getInsuplc_admdvs()});
								ry.put("insuplcAdmdvs", insuinfo.getInsuplc_admdvs());//参保地医保区划
								ry.put("insuplcAdmdvsName", dict==null?"":dict.getName());//参保地医保区划
								ry.put("empName", insuinfo.getEmp_name());//单位名称
								ry.put("psnNo", out1101.getOutput().getBaseinfo().getPsn_no());//人员编号
								dict = DataBaseHelper.queryForBean(sql.toString(), InsZsBaYbDict.class, new Object[]{"00051","psn_cert_type", out1101.getOutput().getBaseinfo().getPsn_cert_type()});
								ry.put("psnCertType", out1101.getOutput().getBaseinfo().getPsn_cert_type());//人员证件类型
								ry.put("psnCertName", dict==null?"":dict.getName());//人员证件类型
								ry.put("certno", out1101.getOutput().getBaseinfo().getCertno());//证件号码
								ry.put("psnName", out1101.getOutput().getBaseinfo().getPsn_name());//人员姓名
								ry.put("gend", out1101.getOutput().getBaseinfo().getGend());//性别
								ry.put("naty", out1101.getOutput().getBaseinfo().getNaty());//民族
								ry.put("brdy", out1101.getOutput().getBaseinfo().getBrdy());//出生日期
								ry.put("age", out1101.getOutput().getBaseinfo().getAge());//年龄
								ry.put("psnInsuDate", insuinfo.getPsn_insu_date());//参保时间
								ry.put("pausInsuDate", insuinfo.getPaus_insu_date());//停保时间
								ry.put("psnInsuStas", insuinfo.getPsn_insu_stas());//参保状态
								ry.put("code", "0");
								ry.put("msg", "");
								if(insuinfo.getInsuplc_admdvs().equals("442000")){
									ry.put("psnIdetType", zdbjdx.get("psnIdetType"));
									ry.put("idetBegntime",  zdbjdx.get("idetBegntime"));
									ry.put("idetEndtime",  zdbjdx.get("idetEndtime"));
								}else{
									ry.put("psnIdetType", null);
									ry.put("idetBegntime",  null);
									ry.put("idetEndtime",  null);
								}
								ryList.add(ry);
							}
						}
					}
					if(ryList.size()==0){
						Map<String, Object> ry = new HashMap<String, Object>();
						ry.put("code", "-1");
						ry.put("msg",  "该患者的险种没有可以正常使用的参保信息，请确认是否停保了！");
						ryList.add(ry);
					}	
				}else{
					Map<String, Object> ry = new HashMap<String, Object>();
					ry.put("code", "-1");
					ry.put("msg",  "1101接口没有返回险种信息！");
					ryList.add(ry);
				}
			}
		}else{
			Map<String, Object> ry = new HashMap<String, Object>();
			ry.put("code", signIn.getCode());
			ry.put("msg",  signIn.getMsg());
			ryList.add(ry);
		}
		return ryList;
	}
	
	
	/**
	 * 1201机构信息获取
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getData1201(String param,IUser user){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		JSONObject jo = JSONObject.fromObject(param);
		String ip = jo.getString("ip");
		String mac = jo.getString("mac");
		String code = jo.getString("code");
		String name = jo.getString("name");
		
		InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
		if(signIn.getCode().equals("0")){
			Input1201Medinsinfo data = new Input1201Medinsinfo();
			data.setFixmedins_code(code);
			data.setFixmedins_name(name);
			data.setFixmedins_type("1");
			Input1201 input = new Input1201();
			input.setMedinsinfo(data);
			OutputData1201 out1201 = YbFunUtils.fun1201(input, signIn.getSignNo());
			if(out1201.getInfcode()!=null &&out1201.getInfcode().equals("0")){
				returnMap.put("code", "0");
				returnMap.put("msg",  "");
/*				String sql = "select a.* from ins_dict a inner join ins_dicttype b on a.pk_insdicttype = b.pk_insdicttype and b.code_type = ? and b.del_flag = '0'";
				sql += " inner join bd_hp c on b.pk_hp = c.pk_hp  and c.code='00051' and c.del_flag = '0' ";
				sql += " where a.code=? and a.del_flag = '0'";
				
				for (Output1201Medinsinfo medinsinfo : out1201.getOutput().getMedinsinfo()) {
					if(medinsinfo.getFixmedins_type()!=null){
						Map<String, Object> typeMap = DataBaseHelper.queryForMap(sql, "fixmedins_type", medinsinfo.getFixmedins_type());
						if(typeMap.get("name")!=null){
							medinsinfo.setFixmedins_type(typeMap.get("name").toString());
						}
					}
					if(medinsinfo.getHosp_lv()!=null){
						Map<String, Object> typeMap = DataBaseHelper.queryForMap(sql, "hosp_lv", medinsinfo.getHosp_lv());
						if(typeMap.get("name")!=null){
							medinsinfo.setHosp_lv(typeMap.get("name").toString());
						}
					}
				}*/
				for (Output1201Medinsinfo medinsinfo : out1201.getOutput().getMedinsinfo()) {
					if(medinsinfo.getFixmedins_type()!=null){
						switch (medinsinfo.getFixmedins_type()){
						 	case "1":
						 		medinsinfo.setFixmedins_type("定点医疗机构");
				                break;
						 	case "2":
						 		medinsinfo.setFixmedins_type("定点零售药店");
				                break;
						 	case "3":
						 		medinsinfo.setFixmedins_type("工伤定点康复机构");
				                break;
						 	case "4":
						 		medinsinfo.setFixmedins_type("辅助器具配置机构");
				                break;
						 	case "5":
						 		medinsinfo.setFixmedins_type("计划生育服务机构");
				                break;
						}
					}
					if(medinsinfo.getHosp_lv()!=null){
						switch (medinsinfo.getHosp_lv()){
						 	case "01":
						 		medinsinfo.setHosp_lv("三级特等");
				                break;
						 	case "02":
						 		medinsinfo.setHosp_lv("三级甲等");
				                break;
						 	case "03":
						 		medinsinfo.setHosp_lv("三级乙等");
				                break;
						 	case "04":
						 		medinsinfo.setHosp_lv("三级丙等");
				                break;
						 	case "05":
						 		medinsinfo.setHosp_lv("二级甲等");
				                break;
						 	case "06":
						 		medinsinfo.setHosp_lv("二级乙等");
				                break;
						 	case "07":
						 		medinsinfo.setHosp_lv("二级丙等");
				                break;
						 	case "08":
						 		medinsinfo.setHosp_lv("一级甲等");
				                break;
						 	case "09":
						 		medinsinfo.setHosp_lv("一级乙等");
				                break;
						 	case "10":
						 		medinsinfo.setHosp_lv("一级丙等");
				                break;
						 	case "11":
						 		medinsinfo.setHosp_lv("无等级");
				                break;
						}
					}
				}
				returnMap.put("medinsinfo", out1201.getOutput().getMedinsinfo());
			}

		}else{
			returnMap.put("code", signIn.getCode());
			returnMap.put("msg",  signIn.getMsg());
		}
		return returnMap;
	}
	
	/**
	 * 【2001】人员待遇享受检查
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getData2001(String param,IUser user){
		List<Map<String, Object>> ryList = new ArrayList<Map<String,Object>>();
		InsZsbaPvQg insPv = JsonUtil.readValue(param, InsZsbaPvQg.class);
		
		InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(insPv.getIp(), insPv.getMac());
		if(signIn.getCode().equals("0")){
			Input2001Data data = new Input2001Data();
			data.setPsn_no(insPv.getPsnNo());
			data.setInsutype(insPv.getInsutype());
			data.setFixmedins_code("H44200100009");
			data.setMed_type(insPv.getMedType());
			data.setBegntime(insPv.getBegntime());
			//data.setBegntime(DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
			Input2001 input2001 = new Input2001();
			input2001.setData(data);
			OutputData2001 out2001 = YbFunUtils.fun2001(input2001, insPv.getInsuplcAdmdvs(), signIn.getSignNo());
			if(out2001.getInfcode()==null || !out2001.getInfcode().equals("0")){
				Map<String, Object> ry = new HashMap<String, Object>();
				ry.put("code", "-1");
				ry.put("msg",  out2001.getErr_msg()+out2001.getMessage());
				ryList.add(ry);
			}else{
				if(out2001.getOutput().getTrtinfo().size()==0){
					Map<String, Object> ry = new HashMap<String, Object>();
					ry.put("code", "-1");
					ry.put("msg",  "该患者没有人员待遇享受检查信息！");
					ryList.add(ry);
				}else{
					for (Output2001Trtinfo trtinfo : out2001.getOutput().getTrtinfo()) {
						Map<String, Object> ry = new HashMap<String, Object>();
						ry.put("psnNo", trtinfo.getPsn_no());
						ry.put("trtChkType", trtinfo.getTrt_chk_type());
						ry.put("fundPayType", trtinfo.getFund_pay_type());
						ry.put("trtEnjymntFlag", trtinfo.getTrt_enjymnt_flag());
						ry.put("begndate", trtinfo.getBegndate());
						ry.put("enddate", trtinfo.getEnddate());
						ry.put("trtChkRslt", trtinfo.getTrt_chk_rslt());
						ry.put("code", "0");
						ry.put("msg", "");
						ryList.add(ry);
					}
				}
			}
		}else{
			Map<String, Object> ry = new HashMap<String, Object>();
			ry.put("code", signIn.getCode());
			ry.put("msg",  signIn.getMsg());
			ryList.add(ry);
		}
		return ryList;
	}
	
	/**
	 * 缴费查询接口
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> getData90100(String param,IUser user){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		JSONObject jo = JSONObject.fromObject(param);
		String psnNo = jo.getString("psnNo");
		String ip = jo.getString("ip");
		String mac = jo.getString("mac");
		String insuplcAdmdvs = jo.getString("insuplcAdmdvs");
		
		InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
		if(signIn.getCode().equals("0")){
			Input90100Data data = new Input90100Data();
			data.setPsn_no(psnNo);
			Input90100 input = new Input90100();
			input.setData(data);
			List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
			OutputData90100 out90100 = YbFunUtils.fun90100(input, insuplcAdmdvs, signIn.getSignNo());
			if(out90100.getInfcode()!=null && out90100.getInfcode().equals("0")){
				for (Output90100 out : out90100.getOutput()) {
					Map<String, Object> jfMap = new HashMap<String, Object>();
					jfMap.put("poolareaNo", out.getPoolarea_no());
					jfMap.put("poolareaNoName", out.getPoolarea_no_name());
					jfMap.put("insutype", out.getInsutype());
					jfMap.put("insutypeName", out.getInsutype_name());
					jfMap.put("clctType", out.getClct_type());
					jfMap.put("clctTypeName", out.getClct_type_name());
					jfMap.put("clctFlag", out.getClct_flag());
					jfMap.put("clctFlagName", out.getClct_flag_name());
					jfMap.put("accrymBegn", out.getAccrym_begn());
					jfMap.put("accrymEnd", out.getAccrym_end());
					jfMap.put("clctTime", out.getClct_time());
					mapList.add(jfMap);
				}
				returnMap.put("list", mapList);
				returnMap.put("code", "0");
				returnMap.put("msg", "调用成功！");
				
			}else{
				returnMap.put("code", "-1");
				returnMap.put("msg", out90100.getErr_msg()+out90100.getMessage());
			}
		}else{
			returnMap.put("code", signIn.getCode());
			returnMap.put("msg",  signIn.getMsg());
		}
		return returnMap;
		
		
	}
	
	
	/**
	 * 医保入院登记
	 * @param param
	 * @param user
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	public InsZsbaPvQg saveQgybReg(String param,IUser user){
		
		//JSONObject jo = JSONObject.fromObject(param);
		InsZsbaPvQg insPv = JsonUtil.readValue(param, InsZsbaPvQg.class);
		if(insPv.getInsuplcAdmdvs()==null || insPv.getInsuplcAdmdvs().trim().length()==0){
			insPv.setInsuplcAdmdvs("442000");
		}
		
		InsZsbaPvQg returnPv = new InsZsbaPvQg();
		returnPv.setCode("0");
		String hpSql  = "select * from bd_hp where pk_hp = ? and del_flag = '0'";
		BdHp hp = DataBaseHelper.queryForBean(hpSql, BdHp.class, insPv.getPkInsu());
		//1.签到9001
		InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(insPv.getIp(), insPv.getMac());
		if(signIn.getCode().equals("0")){
			//3.查询数据库获取入院登记信息
			Map<String,Object> param_h = new HashMap<String,Object>();
			param_h.put("pkPv", insPv.getPkPv());
			List<Map<String,Object>> diagDataList = new ArrayList<Map<String,Object>> ();
			if(ApplicationUtils.getSysparam("CN0016", false).equals("0")){
				 diagDataList = insPvMapper.getDiagData(param_h);
			}else{
				diagDataList = insPvMapper.getDiagData2(param_h);
			}
			if(diagDataList!=null && diagDataList.size()!=0){
				List<Map<String,Object>> basicDataList = insPvMapper.getBasicData(param_h);
				if(basicDataList.size()==0){
					returnPv.setCode("-1");
					returnPv.setMsg("查不到该患者的住院信息，无法获取数据！");
					return returnPv;
				}else{
					if(insPv.getMdtrtCertType().equals("02")&&insPv.getMdtrtCertNo()!=null && insPv.getMdtrtCertNo().length()==15){
						//15位身份证转18位
						insPv.setMdtrtCertNo(ZsbaQGUtils.getEighteenIDCard(insPv.getMdtrtCertNo()));
					}
					//4.入院登记 2401
					Input2401 input  = new Input2401();
					Input2401Mdtrtinfo  mdtrtinfo = new Input2401Mdtrtinfo();
					mdtrtinfo.setPsn_no(insPv.getPsnNo());
					mdtrtinfo.setInsutype(insPv.getInsutype());
					mdtrtinfo.setBegntime(insPv.getBegntime());
					if(insPv.getHpCode().equals("00059")&&insPv.getMdtrtCertType().equals("03")){
						mdtrtinfo.setMdtrt_cert_type("03");
						mdtrtinfo.setMdtrt_cert_no(insPv.getCertno());
					}else{
						mdtrtinfo.setMdtrt_cert_type(insPv.getMdtrtCertType());
						mdtrtinfo.setMdtrt_cert_no(insPv.getMdtrtCertNo());
					}
					
					mdtrtinfo.setMed_type(insPv.getMedType());
					mdtrtinfo.setIpt_no(basicDataList.get(0).get("code_ip")!=null?basicDataList.get(0).get("code_ip").toString():null);
					mdtrtinfo.setAtddr_no(basicDataList.get(0).get("code_emp_phy")!=null?basicDataList.get(0).get("code_emp_phy").toString():null);
					mdtrtinfo.setChfpdr_name(basicDataList.get(0).get("name_emp_phy")!=null?basicDataList.get(0).get("name_emp_phy").toString():null);
					
					mdtrtinfo.setDscg_maindiag_code(insPv.getDscgMaindiagCode());
					mdtrtinfo.setDscg_maindiag_name(insPv.getDscgMaindiagName());
					mdtrtinfo.setAdm_diag_dscr(insPv.getDscgMaindiagName());
					//mdtrtinfo.setDscg_maindiag_code(diagDataList.get(0).get("diagcode")==null?null:diagDataList.get(0).get("diagcode").toString());
					//mdtrtinfo.setDscg_maindiag_name(diagDataList.get(0).get("diagname")==null?null:diagDataList.get(0).get("diagname").toString());
					//mdtrtinfo.setAdm_diag_dscr(diagDataList.get(0).get("diagDesc")==null?diagDataList.get(0).get("diagname").toString():diagDataList.get(0).get("diagDesc").toString());
					mdtrtinfo.setAdm_dept_codg(basicDataList.get(0).get("code_dept_nation_ins")!=null?basicDataList.get(0).get("code_dept_nation_ins").toString():null);
					mdtrtinfo.setAdm_dept_name(basicDataList.get(0).get("name_dept")!=null?basicDataList.get(0).get("name_dept").toString():null);
					mdtrtinfo.setAdm_bed(basicDataList.get(0).get("bed_no")!=null?basicDataList.get(0).get("bed_no").toString():null);
					mdtrtinfo.setDise_codg(insPv.getDiseCodg());
					mdtrtinfo.setDise_name(insPv.getDiseName());
					mdtrtinfo.setOprn_oprt_code(insPv.getOprnOprtCode());
					mdtrtinfo.setOprn_oprt_name(insPv.getOprnOprtName());
					mdtrtinfo.setMatn_type(insPv.getMatnType());
					mdtrtinfo.setBirctrl_type(insPv.getBirctrlType());
					mdtrtinfo.setLatechb_flag(insPv.getLatechbFlag());
					mdtrtinfo.setGeso_val(insPv.getGesoVal());
					mdtrtinfo.setFetts(insPv.getFetts());
					mdtrtinfo.setFetus_cnt(insPv.getFetusCnt());
					mdtrtinfo.setPret_flag(insPv.getPretFlag());
					mdtrtinfo.setBirctrl_matn_date(insPv.getBirctrlMatnDate());
					//诊断
					List<Input2401Diseinfo> diseinfoList = new ArrayList<Input2401Diseinfo>();
					Input2401Diseinfo diseinfo = new Input2401Diseinfo();
					diseinfo.setPsn_no(insPv.getPsnNo());
					diseinfo.setDiag_type("3"); //1 	西医主要诊断  2 	西医其他诊断  3 	中医主病诊断  4 	中医主证诊断 
					diseinfo.setMaindiag_flag("1");
					diseinfo.setDiag_srt_no("1");
					diseinfo.setDiag_code(insPv.getDscgMaindiagCode());
					diseinfo.setDiag_name(insPv.getDscgMaindiagName());
					//diseinfo.setDiag_code(diagDataList.get(0).get("diagcode")==null?null:diagDataList.get(0).get("diagcode").toString());
					//diseinfo.setDiag_name(diagDataList.get(0).get("diagname")==null?null:diagDataList.get(0).get("diagname").toString());
					diseinfo.setDiag_dept("A01");
					diseinfo.setDise_dor_no(diagDataList.get(0).get("diagCodeEmp")==null?null:diagDataList.get(0).get("diagCodeEmp").toString());
					diseinfo.setDise_dor_name(diagDataList.get(0).get("diagNameEmp")==null?null:diagDataList.get(0).get("diagNameEmp").toString());
					diseinfo.setDiag_time(diagDataList.get(0).get("diagDate")==null?null:diagDataList.get(0).get("diagDate").toString().substring(0, 19));
					diseinfoList.add(diseinfo);
					
	/*				Input2401Diseinfo diseinfo2 = new Input2401Diseinfo();
					diseinfo2.setPsn_no(insPv.getPsnNo());
					diseinfo2.setDiag_type("3"); //1 	西医主要诊断  2 	西医其他诊断  3 	中医主病诊断  4 	中医主证诊断 
					diseinfo2.setMaindiag_flag("0");
					diseinfo2.setDiag_srt_no("2");
					diseinfo2.setDiag_code(insPv.getDscgMaindiagCode());
					diseinfo2.setDiag_name(insPv.getDscgMaindiagName());
					//diseinfo.setDiag_code(diagDataList.get(0).get("diagcode")==null?null:diagDataList.get(0).get("diagcode").toString());
					//diseinfo.setDiag_name(diagDataList.get(0).get("diagname")==null?null:diagDataList.get(0).get("diagname").toString());
					diseinfo2.setDiag_dept("A01");
					diseinfo2.setDise_dor_no(diagDataList.get(0).get("diagCodeEmp")==null?null:diagDataList.get(0).get("diagCodeEmp").toString());
					diseinfo2.setDise_dor_name(diagDataList.get(0).get("diagNameEmp")==null?null:diagDataList.get(0).get("diagNameEmp").toString());
					diseinfo2.setDiag_time(diagDataList.get(0).get("diagDate")==null?null:diagDataList.get(0).get("diagDate").toString().substring(0, 19));
					diseinfoList.add(diseinfo2);*/
					input.setMdtrtinfo(mdtrtinfo);
					input.setDiseinfo(diseinfoList);
					OutputData2401 paramOut = YbFunUtils.fun2401(input, insPv.getInsuplcAdmdvs(), signIn.getSignNo());
					
					if(paramOut.getInfcode()!=null &&paramOut.getInfcode().equals("0")){
						//5.保存登记信息
						InsZsbaPvQg savePv = YbToInsPvQg(mdtrtinfo);
						savePv.setMsgid2401(paramOut.getMsgid());
						savePv.setMdtrtId(paramOut.getOutput().getResult().getMdtrt_id());
						savePv.setPkPi(insPv.getPkPi());
						savePv.setPkPv(insPv.getPkPv());
						savePv.setPkInsu(insPv.getPkInsu());
						savePv.setInsuplcAdmdvs(insPv.getInsuplcAdmdvs());
						savePv.setOpterType("1");
						savePv.setOpter(UserContext.getUser().getCodeEmp());
						savePv.setOpterName(UserContext.getUser().getNameEmp());
						savePv.setEmpName(insPv.getEmpName());
						savePv.setRjzd(insPv.getRjzd());
						if(hp.getCode().equals("00051")||hp.getCode().equals("00058")||hp.getCode().equals("00059")){
							List<Map<String, Object>> gjzList = DataBaseHelper.queryForList("select * from INS_EXAMINE_KEY_FIELDS where CHARINDEX(rtrim(KEY_FIELDS),?) > 0 ;", savePv.getDscgMaindiagName());
							if(gjzList!=null && gjzList.size()>0){
								savePv.setExamineStatus("0");//包含关键字，审核不通过
							}else{
								savePv.setExamineStatus("4");//自动审核通过
							}
						}else{
							savePv.setExamineStatus("3");
						}
						
						savePv.setPsnIdetType(insPv.getPsnIdetType());
						savePv.setIdetBegntime(insPv.getIdetBegntime());
						savePv.setIdetEndtime(insPv.getIdetEndtime());
						
						DataBaseHelper.insertBean(savePv);
						
						for(Input2401Diseinfo dis : diseinfoList){
							//保存诊断信息
							InsZsbaDiseinfoQg diseinfoSave  = ybToDiseinfoQg(dis, savePv.getPkInspvqg(), savePv.getMdtrtId());
							DataBaseHelper.insertBean(diseinfoSave);
						}
						//6.修改就诊表的身份
						PvEncounter pv = DataBaseHelper.queryForBean("select * from PV_ENCOUNTER where pk_pv = ?", PvEncounter.class, insPv.getPkPv());
						pv.setPkInsu(savePv.getPkInsu());
						DataBaseHelper.updateBeanByPk(pv);
						return savePv;
					}else{
						returnPv.setCode("-1");
						returnPv.setMsg(paramOut.getErr_msg()+paramOut.getMessage());
						return returnPv;
					}
				}
			}else{
				returnPv.setCode("-1");
				returnPv.setMsg("该患者没有入院诊断，请找医生维护入院诊断，不要在此界面选择入院诊断！");
				return returnPv;
			}
		}else{
			returnPv.setCode(signIn.getCode());
			returnPv.setMsg(signIn.getMsg());
			return returnPv;
		}
		//return returnPv;
	}

	/**
	 * 将医保返回的值赋值到入院登记实体类
	 * @param mdtrtinfo
	 * @return
	 */
	private InsZsbaPvQg YbToInsPvQg(Input2401Mdtrtinfo  mdtrtinfo){
		InsZsbaPvQg savePv = new InsZsbaPvQg();
		savePv.setEuPvtype("3");
		savePv.setStatus("1");
		savePv.setPsnNo(mdtrtinfo.getPsn_no());
		savePv.setInsutype(mdtrtinfo.getInsutype());
		savePv.setConerName(mdtrtinfo.getChfpdr_name());
		savePv.setTel(mdtrtinfo.getTel());
		savePv.setBegntime(mdtrtinfo.getBegntime());
		savePv.setMdtrtCertType(mdtrtinfo.getMdtrt_cert_type());
		savePv.setMdtrtCertNo(mdtrtinfo.getMdtrt_cert_no());
		savePv.setMedType(mdtrtinfo.getMed_type());
		savePv.setMedrcdno(mdtrtinfo.getMedrcdno());
		savePv.setIptNo(mdtrtinfo.getIpt_no());
		savePv.setMedrcdno(mdtrtinfo.getMedrcdno());
		savePv.setAtddrNo(mdtrtinfo.getAtddr_no());
		savePv.setChfpdrName(mdtrtinfo.getChfpdr_name());
		savePv.setAdmDeptCodg(mdtrtinfo.getAdm_dept_codg());
		savePv.setAdmDeptName(mdtrtinfo.getAdm_dept_name());
		savePv.setAdmDiagDscr(mdtrtinfo.getAdm_diag_dscr());
		savePv.setAdmBed(mdtrtinfo.getAdm_bed());
		savePv.setDscgMaindiagCode(mdtrtinfo.getDscg_maindiag_code());
		savePv.setDscgMaindiagName(mdtrtinfo.getDscg_maindiag_name());
		savePv.setMainCondDscr(mdtrtinfo.getMain_cond_dscr());
		savePv.setDiseCodg(mdtrtinfo.getDise_codg());
		savePv.setDiseName(mdtrtinfo.getDise_name());
		savePv.setOprnOprtCode(mdtrtinfo.getOprn_oprt_code());
		savePv.setOprnOprtName(mdtrtinfo.getOprn_oprt_name());
		savePv.setFpscNo(mdtrtinfo.getFpsc_no());
		savePv.setMatnType(mdtrtinfo.getMatn_type());
		savePv.setBirctrlType(mdtrtinfo.getBirctrl_type());
		savePv.setLatechbFlag(mdtrtinfo.getLatechb_flag());
		savePv.setGesoVal(mdtrtinfo.getGeso_val());
		savePv.setFetts(mdtrtinfo.getFetts());
		savePv.setFetusCnt(mdtrtinfo.getFetus_cnt());
		savePv.setPretFlag(mdtrtinfo.getPret_flag());
		savePv.setBirctrlMatnDate(mdtrtinfo.getBirctrl_matn_date());
		return savePv;
	}
	
	/**
	 * 将医保返回的值赋值到入院登记诊断实体类
	 * @param dis
	 * @param pkInspvqg
	 * @param mdtrtId
	 * @return
	 */
	private InsZsbaDiseinfoQg ybToDiseinfoQg(Input2401Diseinfo dis, String pkInspvqg, String mdtrtId){
		InsZsbaDiseinfoQg diseinfo = new InsZsbaDiseinfoQg();
		diseinfo.setInoutDiagType("1");
		diseinfo.setPkInspvqg(pkInspvqg);
		diseinfo.setMdtrtId(mdtrtId);
		diseinfo.setPsnNo(dis.getPsn_no());
		diseinfo.setDiagType(dis.getDiag_type());
		diseinfo.setMaindiagFlag(dis.getMaindiag_flag());
		diseinfo.setDiagSrtNo(dis.getDiag_srt_no());
		diseinfo.setDiagCode(dis.getDiag_code());
		diseinfo.setDiagName(dis.getDiag_name());
		diseinfo.setAdmCond(dis.getAdm_cond());
		diseinfo.setDiagDept(dis.getDiag_dept());
		diseinfo.setDiseDorNo(dis.getDise_dor_no());
		diseinfo.setDiseDorName(dis.getDise_dor_name());
		diseinfo.setDiagTime(dis.getDiag_time());
		return diseinfo;
	}
	
	/**
	 * 将医保返回的值赋值到出院院登记诊断实体类
	 * @param dis
	 * @param pkInspvqg
	 * @param mdtrtId
	 * @return
	 */
	private InsZsbaDiseinfoQg ybToDiseinfoQg(Input2402Diseinfo dis, String pkInspvqg, String mdtrtId){
		InsZsbaDiseinfoQg diseinfo = new InsZsbaDiseinfoQg();
		diseinfo.setInoutDiagType("2");
		diseinfo.setPkInspvqg(pkInspvqg);
		diseinfo.setMdtrtId(mdtrtId);
		diseinfo.setPsnNo(dis.getPsn_no());
		diseinfo.setDiagType(dis.getDiag_type());
		diseinfo.setMaindiagFlag(dis.getMaindiag_flag());
		diseinfo.setDiagSrtNo(dis.getDiag_srt_no());
		diseinfo.setDiagCode(dis.getDiag_code());
		diseinfo.setDiagName(dis.getDiag_name());
		diseinfo.setAdmCond(dis.getAdm_cond());
		diseinfo.setDiagDept(dis.getDiag_dept());
		diseinfo.setDiseDorNo(dis.getDise_dor_no());
		diseinfo.setDiseDorName(dis.getDise_dor_name());
		diseinfo.setDiagTime(dis.getDiag_time());
		return diseinfo;
	}
	
	/**
	 * 修改医保登记信息
	 * 交易号：022003024012
	 * @param param
	 * @param user
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	public InsZsbaPvQg UpdateReg(String param,IUser user) {
		InsZsbaPvQg insPv = JsonUtil.readValue(param, InsZsbaPvQg.class);
		InsZsbaPvQg returnPv = new InsZsbaPvQg();
		String ip = insPv.getIp();
		String mac = insPv.getMac();
		InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
		if(signIn.getCode().equals("0")){
			//获取日间诊断信息
			List<Map<String,Object>> rjDiagList = new ArrayList<Map<String,Object>>();
			if(insPv.getRjzd()!=null){
				Map<String,Object> param_h = new HashMap<String, Object>();
				param_h.put("rjCode", insPv.getRjzd());
				rjDiagList = insQgybPvMapper.getRjzd(param_h);
			}
			
			//1.根据登记主键，获取已登记信息
			InsZsbaPvQg savePv = DataBaseHelper.queryForBean("select * from ins_pv_qg where pk_inspvqg = ?", InsZsbaPvQg.class, insPv.getPkInspvqg());
			if(savePv!=null){
				Input2403 input  = new Input2403();
				Input2403Adminfo  mdtrtinfo = new Input2403Adminfo();
				mdtrtinfo.setMdtrt_id(savePv.getMdtrtId());
				mdtrtinfo.setPsn_no(savePv.getPsnNo());
				mdtrtinfo.setBegntime(insPv.getBegntime());
				mdtrtinfo.setMdtrt_cert_type(insPv.getMdtrtCertType());
				mdtrtinfo.setMed_type(insPv.getMedType());
				mdtrtinfo.setIpt_otp_no(savePv.getIptNo());
				mdtrtinfo.setAtddr_no(savePv.getAtddrNo());
				mdtrtinfo.setChfpdr_name(savePv.getChfpdrName());
				mdtrtinfo.setAdm_diag_dscr(savePv.getAdmDiagDscr());
				mdtrtinfo.setAdm_dept_codg(savePv.getAdmDeptCodg());
				mdtrtinfo.setAdm_dept_name(savePv.getAdmDeptName());
				mdtrtinfo.setAdm_bed(savePv.getAdmBed());
				if(insPv.getRjzd()!=null){
					mdtrtinfo.setDscg_maindiag_code(rjDiagList.get(0).get("diagcode")==null?null:rjDiagList.get(0).get("diagcode").toString());
					mdtrtinfo.setDscg_maindiag_name(rjDiagList.get(0).get("diagname")==null?null:rjDiagList.get(0).get("diagname").toString());
				}else{
					mdtrtinfo.setDscg_maindiag_code(savePv.getDscgMaindiagCode());
					mdtrtinfo.setDscg_maindiag_name(savePv.getDscgMaindiagName());
				}
				mdtrtinfo.setDise_codg(insPv.getDiseCodg());
				mdtrtinfo.setDise_name(insPv.getDiseName());
				mdtrtinfo.setOprn_oprt_code(insPv.getOprnOprtCode());
				mdtrtinfo.setOprn_oprt_name(insPv.getOprnOprtName());
				mdtrtinfo.setMatn_type(insPv.getMatnType());
				mdtrtinfo.setBirctrl_type(insPv.getBirctrlType());
				mdtrtinfo.setLatechb_flag(insPv.getLatechbFlag());
				mdtrtinfo.setGeso_val(insPv.getGesoVal());
				mdtrtinfo.setFetts(insPv.getFetts());
				mdtrtinfo.setFetus_cnt(insPv.getFetusCnt());
				mdtrtinfo.setPret_flag(insPv.getPretFlag());
				mdtrtinfo.setBirctrl_matn_date(insPv.getBirctrlMatnDate());
				//诊断
				List<InsZsbaDiseinfoQg> diseinfoSaveList = DataBaseHelper.queryForList("select * from INS_DISEINFO_QG where pk_inspvqg = ? and inout_diag_type = '1' and del_flag = '0'", InsZsbaDiseinfoQg.class, savePv.getPkInspvqg());
				List<Input2403Diseinfo> diseinfoList = new ArrayList<Input2403Diseinfo>();
				for (InsZsbaDiseinfoQg insZsbaDiseinfoQg : diseinfoSaveList) {
					Input2403Diseinfo diseinfo = new Input2403Diseinfo();
					diseinfo.setMdtrt_id(insZsbaDiseinfoQg.getMdtrtId());
					diseinfo.setPsn_no(insZsbaDiseinfoQg.getPsnNo());
					diseinfo.setMaindiag_flag(insZsbaDiseinfoQg.getMaindiagFlag());
					diseinfo.setDiag_srt_no(insZsbaDiseinfoQg.getDiagSrtNo());
					if(insPv.getRjzd()!=null){
						diseinfo.setDiag_type("3"); //1 	西医主要诊断  2 	西医其他诊断  3 	中医主病诊断  4 	中医主证诊断 
						diseinfo.setDiag_code(rjDiagList.get(0).get("diagcode")==null?null:rjDiagList.get(0).get("diagcode").toString());
						diseinfo.setDiag_name(rjDiagList.get(0).get("diagname")==null?null:rjDiagList.get(0).get("diagname").toString());
						
					}else{
						diseinfo.setDiag_type(insZsbaDiseinfoQg.getDiagType()); //1 	西医主要诊断  2 	西医其他诊断  3 	中医主病诊断  4 	中医主证诊断 
						diseinfo.setDiag_code(insZsbaDiseinfoQg.getDiagCode());
						diseinfo.setDiag_name(insZsbaDiseinfoQg.getDiagName());
					}
					diseinfo.setDiag_dept(insZsbaDiseinfoQg.getDiagDept());
					diseinfo.setDise_dor_no(insZsbaDiseinfoQg.getDiseDorNo());
					diseinfo.setDise_dor_name(insZsbaDiseinfoQg.getDiseDorName());
					diseinfo.setDiag_time(insZsbaDiseinfoQg.getDiagTime());
					diseinfoList.add(diseinfo);
				}
				
				input.setAdminfo(mdtrtinfo);
				input.setDiseinfo(diseinfoList);
				OutputData2403 paramOut = YbFunUtils.fun2403(input, savePv.getInsuplcAdmdvs(), signIn.getSignNo());
				if(paramOut.getInfcode()!=null && paramOut.getInfcode().equals("0")){
					//5.保存登记信息
					savePv.setBegntime(insPv.getBegntime());
					savePv.setMedType(insPv.getMedType());
					savePv.setMdtrtCertType(insPv.getMdtrtCertType());
					savePv.setMdtrtCertNo(insPv.getMdtrtCertNo());
					savePv.setDiseCodg(insPv.getDiseCodg());
					savePv.setDiseName(insPv.getDiseName());
					savePv.setOprnOprtCode(insPv.getOprnOprtCode());
					savePv.setOprnOprtName(insPv.getOprnOprtName());
					savePv.setMatnType(insPv.getMatnType());
					savePv.setBirctrlType(insPv.getBirctrlType());
					savePv.setLatechbFlag(insPv.getLatechbFlag());
					savePv.setGesoVal(insPv.getGesoVal());
					savePv.setFetts(insPv.getFetts());
					savePv.setFetusCnt(insPv.getFetusCnt());
					savePv.setPretFlag(insPv.getPretFlag());
					savePv.setBirctrlMatnDate(insPv.getBirctrlMatnDate());
					savePv.setOpterType("1");
					savePv.setOpter(UserContext.getUser().getCodeEmp());
					savePv.setOpterName(UserContext.getUser().getNameEmp());
					savePv.setStatus("3");
					savePv.setRjzd(insPv.getRjzd());
					DataBaseHelper.updateBeanByPk(savePv, false);
					returnPv = savePv;
					returnPv.setCode("0");
					returnPv.setMsg("修改医保登记信息成功！");
				}else{
					returnPv.setCode("-1");
					returnPv.setMsg(paramOut.getErr_msg()+paramOut.getMessage());
					return returnPv;
				}
			}else{
				returnPv.setCode("0");
				returnPv.setMsg("his系统查询不到医保登记信息！");
			}
		}else{
			returnPv.setCode(signIn.getCode());
			returnPv.setMsg(signIn.getMsg());
			return returnPv;
		}

		return returnPv;
	}
	
	/**
	 * 取消医保入院登记
	 * @param param
	 * @param user
	 * @return
	 */
	public InsZsbaPvQg CancelReg(String param,IUser user){
		InsZsbaPvQg insPv = JsonUtil.readValue(param, InsZsbaPvQg.class);
		InsZsbaPvQg returnPv = new InsZsbaPvQg();
		returnPv.setCode("0");
		Input2404Data data = new Input2404Data();
		data.setPsn_no(insPv.getPsnNo());
		data.setMdtrt_id(insPv.getMdtrtId());
		Input2404 input = new Input2404();
		input.setData(data);
		String ip = insPv.getIp();
		String mac = insPv.getMac();
		InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
		if(signIn.getCode().equals("0")){
			//有费用的时候，取消登记会报错，所以取消登记前先删除费用
			Input2302Data data2302  = new Input2302Data();
			data2302.setFeedetl_sn("0000");//0000取消全部
			data2302.setMdtrt_id(insPv.getMdtrtId());
			data2302.setPsn_no(insPv.getPsnNo());
			//dataList2302.add(data2302);
			Input2302 input2302 = new Input2302();
			input2302.setData(data2302);
			OutputData2302 out2302 = YbFunUtils.fun2302(input2302, insPv.getInsuplcAdmdvs(), signIn.getSignNo());
			if((out2302.getInfcode()!=null && out2302.getInfcode().equals("0"))||
					(out2302.getErr_msg()!=null && out2302.getErr_msg().contains("根据传来的参数在费用明细表中未查询到任何有效相关的记录"))
					||(out2302.getErr_msg()!=null && out2302.getErr_msg().contains("没有需要撤销的处方"))
					||(out2302.getErr_msg()!=null && (out2302.getErr_msg().contains("汕头市")||out2302.getErr_msg().contains("梅州市")) && out2302.getMessage() == null)
					){
				OutputData2404 paramOut = YbFunUtils.fun2404(input, insPv.getInsuplcAdmdvs(), signIn.getSignNo());
				if(paramOut.getInfcode()!=null && paramOut.getInfcode().equals("0")){
					InsZsbaPvQg canPv = DataBaseHelper.queryForBean("select * from ins_pv_qg where pk_inspvqg = ?", InsZsbaPvQg.class, insPv.getPkInspvqg());
					canPv.setDelFlag("1");
					canPv.setStatus("9");
					DataBaseHelper.updateBeanByPk(canPv);
					DataBaseHelper.execute("update ins_diseinfo_qg   set del_flag = '1' where pk_inspvqg = ?", insPv.getPkInspvqg());
					PvEncounter pv = DataBaseHelper.queryForBean("select * from PV_ENCOUNTER where pk_pv = ?", PvEncounter.class, insPv.getPkPv());
					BdHp hp = DataBaseHelper.queryForBean("select * from bd_hp where del_flag = '0' and  code = ?", BdHp.class, "0001");
					pv.setPkInsu(hp.getPkHp());
					DataBaseHelper.updateBeanByPk(pv);
				}else{
					returnPv.setCode("-1");
					returnPv.setMsg(paramOut.getErr_msg()+paramOut.getMessage());
				}
			}else{
				returnPv.setCode("-1");
				returnPv.setMsg(out2302.getErr_msg()+out2302.getMessage());
			}
		}else{
			returnPv.setCode(signIn.getCode());
			returnPv.setMsg(signIn.getMsg());
		}
		return returnPv;
	}

	/**
	 * 判断是否可以进行结算
	 * @param param
	 * @param user
	 * @return
	 */
	public Map<String, Object> getWhetherSettle(String param , IUser user){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("msg", "");
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv");
		
		//医保未入院，不需要医保结算 1
		//医保未出院，需做上传明细、出院登记 2
		//医保已出院、未结算，需做预结算 3
		//医保已结算，需提示    0
		
		InsZsbaPvQg insPv = new InsZsbaPvQg();
		
		insPv = DataBaseHelper.queryForBean("select * from ins_pv_qg where del_flag = '0' and pk_pv = ?", InsZsbaPvQg.class, pkPv);
		
		if(insPv==null){
			//医保未入院
			returnMap.put("code", "1");
			return returnMap;
		}
		
		if(insPv.getExamineStatus().equals("0")){
			returnMap.put("code", "0");
			returnMap.put("msg", "医保未审核，请联系医保科!");
			return returnMap;
		}else if(insPv.getExamineStatus().equals("2")){
			returnMap.put("code", "0");
			returnMap.put("msg", "医保审核未通过，原因："+insPv.getExamineMsg());
			return returnMap;
		}else{
			if(insPv.getStatus().equals("1") || insPv.getStatus().equals("3") || insPv.getStatus().equals("4")
					||insPv.getStatus().equals("6") || insPv.getStatus().equals("7") || insPv.getStatus().equals("10")){
				//医保未出院，需做上传明细、出院登记
				returnMap.put("code", "2");
			}else if(!(insPv.getStatus().equals("5") || insPv.getStatus().equals("8") || insPv.getStatus().equals("13"))){
				//5：出院登记成功 8：取消出院登记失败 13：取消结算成功    只有这3中状态才可以进行结算
				if(insPv.getStatus().equals("11")||insPv.getStatus().equals("14")){
					//11和14为已结算，不需要进行医保结算
					returnMap.put("code", "1");
					return returnMap;
				}else{
					//当前状态不可以进行结算
					returnMap.put("code", "0");
					returnMap.put("msg", "不能进行医保结算!");
					return returnMap;
				}
			}else{
				returnMap.put("code", "3");//需做出院登记
			}
		}
		return returnMap;
	}
	
	/**
	 * 判断是否可结算并获取待上传明细(先获取正数的明细)
	 * @param param
	 * @param user
	 * @return
	 */
	public  Map<String, Object> getUploadDetailsCollect(String param,IUser user){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("code", "0");
		returnMap.put("msg", "");
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv").toString();
		String ip = jo.getString("ip").toString();
		String mac = jo.getString("mac").toString();
		String dateEnd = jo.getString("dateEnd").toString();
		InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
		if(signIn.getCode().equals("0")){
			Map<String,Object> param_h = new HashMap<String,Object>();
			param_h.put("pkPv", pkPv);
			param_h.put("dateEnd", dateEnd);
			InsZsbaPvQg pvQg = DataBaseHelper.queryForBean("select * from ins_pv_qg where pk_pv = ? and del_flag='0'", InsZsbaPvQg.class, pkPv);
			if(pvQg!=null){
				returnMap.put("insuplcAdmdvs", pvQg.getInsuplcAdmdvs());
				//日间手术需要判断是否已维护病种和手术
				boolean rjFlag = true;
				boolean jsFlag = true;
				if(pvQg.getMedType().equals("2101")){
					if(pvQg.getDiseCodg()==null || pvQg.getDiseCodg().length()==0
							||pvQg.getOprnOprtCode()==null || pvQg.getOprnOprtCode().length()==0){
						rjFlag = false;
					}
				}else if(pvQg.getMedType().equals("5302")){
					//计划生育需要判断是否已维护
					if(pvQg.getBirctrlType()==null||pvQg.getBirctrlType().length()==0){
						jsFlag = false;
					}
				}
				if(rjFlag&&jsFlag){
					List<Map<String,Object>> drData = insPvMapper.getYdDischargeRegistrationData(param_h);
					if(drData.get(0).get("name_dept")!=null&&drData.get(0).get("name_dept")!=""){
						List<Map<String,Object>> diagDataList = new ArrayList<Map<String,Object>>();
						if(ApplicationUtils.getSysparam("CN0016", false).equals("0")){
							 diagDataList = insPvMapper.getCyDiagData(param_h);
						}else{
							diagDataList = insPvMapper.getCyDiagData2(param_h);
						}
						if(diagDataList.size()!=0){
							//2.取消费用明细上传 
							//List<Input2302Data> dataList2302 = new ArrayList<Input2302Data>();
							Input2302Data data2302  = new Input2302Data();
							data2302.setFeedetl_sn("0000");//0000取消全部
							data2302.setMdtrt_id(pvQg.getMdtrtId());
							data2302.setPsn_no(pvQg.getPsnNo());
							//dataList2302.add(data2302);
							Input2302 input2302 = new Input2302();
							input2302.setData(data2302);
							OutputData2302 out2302 = YbFunUtils.fun2302(input2302, pvQg.getInsuplcAdmdvs(), signIn.getSignNo());
							if((out2302.getInfcode()!=null && out2302.getInfcode().equals("0"))||
									(out2302.getErr_msg()!=null && out2302.getErr_msg().contains("根据传来的参数在费用明细表中未查询到任何有效相关的记录"))
									||(out2302.getErr_msg()!=null && out2302.getErr_msg().contains("没有需要撤销的处方"))
									||(out2302.getErr_msg()!=null && (out2302.getErr_msg().contains("汕头市")||out2302.getErr_msg().contains("梅州市")) && out2302.getMessage() == null)
									){
							//String josn2302 = YbFunUtils.fun2302Json(input2302, pvQg.getInsuplcAdmdvs(), signIn.getSignNo());
/*							JSONObject jo2302 = JSONObject.fromObject(josn2302);
							if((jo2302.get("infcode")!=null && jo2302.get("infcode").toString().equals("0"))||
									(jo2302.get("err_msg")!=null && jo2302.get("err_msg").toString().contains("根据传来的参数在费用明细表中未查询到任何有效相关的记录"))){*/
								
								List<Map<String, Object>> feeHisList = DataBaseHelper.queryForList("select * from ins_feedetail_qg where del_flag = '0' and pk_inspvqg = ?", pvQg.getPkInspvqg());
								if(feeHisList!=null && feeHisList.size()>0){
									String sql = "update ins_feedetail_qg WITH (TABLOCKX)  set del_flag = '1' where del_flag = '0' and pk_inspvqg = ?";
									DataBaseHelper.execute(sql,pvQg.getPkInspvqg());	
								}
								List<Map<String, Object>> feeHisList2301 = DataBaseHelper.queryForList("select * from ins_feedetail2301_qg where del_flag = '0' and pk_inspvqg = ?", pvQg.getPkInspvqg());
								if(feeHisList2301!=null && feeHisList2301.size()>0){
									String sql = "update ins_feedetail2301_qg WITH (TABLOCKX)  set del_flag = '1' where del_flag = '0' and pk_inspvqg = ?";
									DataBaseHelper.execute(sql,pvQg.getPkInspvqg());	
								}
								//3.上传费用明细
								//3.1获取待上传明细
								//数量为正和数量为负的要分开上传，一起上传会报错
								
								param_h.put("pkPv", pkPv);
								param_h.put("dateEnd", dateEnd);
								
								//项目  收
								List<Map<String,Object>> basicDataList = insQgybPvMapper.getChargeDetails(param_h);
								//药品 收
								List<Map<String,Object>> YpBasicDataList = insQgybPvMapper.getChargeDetailsToYp(param_h);
								
								//项目 退
								//List<Map<String,Object>> basicDataRefundList = insQgybPvMapper.getChargeDetailsRefund(param_h);
								//药品 退
								//List<Map<String,Object>> YpBasicDataRefundList = insQgybPvMapper.getChargeDetailsToYpRefund(param_h);
								String xmDzErrTip = "";
								for (Map<String, Object> map : basicDataList) {
									if(map.get("med_list_codg")==null){
										xmDzErrTip += "\n项目编码:"+map.get("code")+",项目名称:"+map.get("yymlName")+";";
									}
								}
								if(xmDzErrTip.length()>0){
									xmDzErrTip = "以下项目未对照国家编码，请联系医保科进行对照：" + xmDzErrTip;
								}
								String ypDzErrTip = "";
								for (Map<String, Object> map : YpBasicDataList) {
									if(map.get("med_list_codg")==null){
										ypDzErrTip += "\n药品编码:"+map.get("code")+",药品名称:"+map.get("yymlName")+";";
									}
								}
								if(ypDzErrTip.length()>0){
									if(xmDzErrTip.length()>0){
										ypDzErrTip = "\n以下药品未对照国家编码，请联系药剂科进行对照：" + ypDzErrTip;
									}else{
										ypDzErrTip = "以下药品未对照国家编码，请联系药剂科进行对照：" + ypDzErrTip;
									}
								}
								if(xmDzErrTip.length()==0&&ypDzErrTip.length()==0){
									basicDataList.addAll(YpBasicDataList);
									List<Input2301Feedetail> feeList = new ArrayList<Input2301Feedetail>();
									BigDecimal totalAmount = new BigDecimal(0);  
									BigDecimal totalAmountBd = new BigDecimal(0);  
									for(int j=0; j<basicDataList.size(); j++){
										Map<String, Object> scMap = basicDataList.get(j);
										Input2301Feedetail fee = new Input2301Feedetail();
										fee.setFeedetl_sn(scMap.get("feedetl_sn").toString());
										fee.setInit_feedetl_sn(scMap.get("init_feedetl_sn")==null?"":scMap.get("init_feedetl_sn").toString());
										fee.setMdtrt_id(pvQg.getMdtrtId());
										//医嘱号是必传的，但博爱的系统费用不一定有医嘱号，所以没有医嘱号的传病历号(华资-周工说的)；博爱系统病历号就是住院号
										//System.out.println(scMap.get("ordsn"));
										//System.out.println(scMap.get("code_ip"));
										fee.setDrord_no(scMap.get("ordsn")==null?scMap.get("code_ip").toString():scMap.get("ordsn").toString());
										fee.setPsn_no(pvQg.getPsnNo());
										fee.setMed_type(pvQg.getMedType());
										fee.setFee_ocur_time(scMap.get("date_hap")==null?null:scMap.get("date_hap").toString().substring(0, 19));
										//fee.setMed_list_codg("XA11CCW047A001010102954");
										
										fee.setMed_list_codg(scMap.get("med_list_codg")==null?null:scMap.get("med_list_codg").toString());
										fee.setMedins_list_codg(scMap.get("code")==null?null:scMap.get("code").toString());
																				
										fee.setDet_item_fee_sumamt((new BigDecimal(scMap.get("amount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP)).toString());
										fee.setCnt(scMap.get("quan")==null?null:scMap.get("quan").toString());
										fee.setPric(scMap.get("price")==null?null:scMap.get("price").toString());
										fee.setBilg_dept_codg(scMap.get("code_dept_nation_ins")==null?null:scMap.get("code_dept_nation_ins").toString());
										fee.setBilg_dept_name(scMap.get("name_dept")==null?null:scMap.get("name_dept").toString());
										fee.setBilg_dr_codg(scMap.get("code_doc_emp")==null?null:scMap.get("code_doc_emp").toString());
										fee.setBilg_dr_name(scMap.get("name_doc_emp")==null?null:scMap.get("name_doc_emp").toString());
										fee.setHosp_appr_flag(scMap.get("flag_fit")==null?"0":scMap.get("flag_fit").toString());
										
										feeList.add(fee);
										totalAmount = totalAmount.add(new BigDecimal(scMap.get("amount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP));  
										totalAmountBd =  totalAmountBd.add(new BigDecimal(scMap.get("amountBd").toString()));  
									}
									returnMap.put("feedetail", feeList);
									returnMap.put("totalAmount", totalAmount);
									returnMap.put("totalAmountBd", totalAmountBd);
								}else{
									//有目录未对照
									returnMap.put("code", "-1");
									returnMap.put("msg", xmDzErrTip+ypDzErrTip);
								}
							}else{
								//2302失败
								returnMap.put("code", "-1");
								returnMap.put("msg", out2302.getErr_msg()+out2302.getMessage());
								//returnMap.put("msg", jo2302.get("err_msg")==null?"":jo2302.get("err_msg").toString());
										//+jo2302.get("message")==null?"":jo2302.get("message").toString());
							}
						}else{
							returnMap.put("code", "-1");
							returnMap.put("msg", "查不到该患者的出院诊断信息！");
						}
					}else{
						returnMap.put("code", "-1");
						returnMap.put("msg", "出院科室不能为空");
					}
				}else{
					if(!rjFlag){
						returnMap.put("code", "-1");
						returnMap.put("msg", "日间手术需要维护病种和手术操作，请联系医生进行维护！");
					}else{
						returnMap.put("code", "-1");
						returnMap.put("msg", "计划生育医保需要维护计划生育手术类别，请联系医生进行维护！");
					}
				}
			}else{
				returnMap.put("code", "-1");
				returnMap.put("msg", "查不到该患者医保登记信息！");
			}
		}else{
			returnMap.put("code", signIn.getCode());
			returnMap.put("msg", signIn.getMsg());
		}
		return returnMap;
	}
	
	/**
	 * 获取待上传明细(负数)
	 * @param param
	 * @param user
	 * @return
	 */
	public  Map<String, Object> getUploadDetailsBack(String param,IUser user){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("code", "0");
		returnMap.put("msg", "");
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv").toString();
		String dateEnd = jo.getString("dateEnd").toString();

		Map<String,Object> param_h = new HashMap<String,Object>();
		param_h.put("pkPv", pkPv);
		param_h.put("dateEnd", dateEnd);
		InsZsbaPvQg pvQg = DataBaseHelper.queryForBean("select * from ins_pv_qg where pk_pv = ? and del_flag='0'", InsZsbaPvQg.class, pkPv);
		//项目 退
		List<Map<String,Object>> basicDataRefundList = insQgybPvMapper.getChargeDetailsRefund(param_h);
		//药品 退
		List<Map<String,Object>> YpBasicDataRefundList = insQgybPvMapper.getChargeDetailsToYpRefund(param_h);
		basicDataRefundList.addAll(YpBasicDataRefundList);
		List<Input2301Feedetail> feeList = new ArrayList<Input2301Feedetail>();
		BigDecimal totalAmount = new BigDecimal(0);  
		BigDecimal totalAmountBd = new BigDecimal(0);  
		for(int j=0; j<basicDataRefundList.size(); j++){
			Map<String, Object> scMap = basicDataRefundList.get(j);
			Input2301Feedetail fee = new Input2301Feedetail();
			fee.setFeedetl_sn(scMap.get("feedetl_sn").toString());
			fee.setInit_feedetl_sn(scMap.get("init_feedetl_sn")==null?"":scMap.get("init_feedetl_sn").toString());
			fee.setMdtrt_id(pvQg.getMdtrtId());
			//医嘱号是必传的，但博爱的系统费用不一定有医嘱号，所以没有医嘱号的传病历号(华资-周工说的)；博爱系统病历号就是住院号
			fee.setDrord_no(scMap.get("ordsn")==null?scMap.get("code_ip").toString():scMap.get("ordsn").toString());
			fee.setPsn_no(pvQg.getPsnNo());
			fee.setMed_type(pvQg.getMedType());
			fee.setFee_ocur_time(scMap.get("date_hap")==null?null:scMap.get("date_hap").toString().substring(0, 19));
			//fee.setMed_list_codg("XA11CCW047A001010102954");
			
			fee.setMed_list_codg(scMap.get("med_list_codg")==null?null:scMap.get("med_list_codg").toString());
			fee.setMedins_list_codg(scMap.get("code")==null?null:scMap.get("code").toString());
			fee.setDet_item_fee_sumamt((new BigDecimal(scMap.get("amount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP)).toString());
			fee.setCnt(scMap.get("quan")==null?null:scMap.get("quan").toString());
			fee.setPric(scMap.get("price")==null?null:scMap.get("price").toString());
			fee.setBilg_dept_codg(scMap.get("code_dept_nation_ins")==null?null:scMap.get("code_dept_nation_ins").toString());
			fee.setBilg_dept_name(scMap.get("name_dept")==null?null:scMap.get("name_dept").toString());
			fee.setBilg_dr_codg(scMap.get("code_doc_emp")==null?null:scMap.get("code_doc_emp").toString());
			fee.setBilg_dr_name(scMap.get("name_doc_emp")==null?null:scMap.get("name_doc_emp").toString());
			fee.setHosp_appr_flag(scMap.get("flag_fit")==null?"0":scMap.get("flag_fit").toString());
			feeList.add(fee);
			totalAmount = totalAmount.add(new BigDecimal(scMap.get("amount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP));  
			totalAmountBd =  totalAmountBd.add(new BigDecimal(scMap.get("amountBd").toString()));  
		}
		returnMap.put("feedetail", feeList);
		returnMap.put("totalAmount", totalAmount);
		returnMap.put("totalAmountBd", totalAmountBd);
		return returnMap;
	}
	
	/**
	 * 上传明细
	 * @param param
	 * @param user
	 * @return
	 */
	public  Map<String, Object> saveUploadDetails(String param,IUser user){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("code", "0");
		returnMap.put("msg", "");
		JSONObject jo = JSONObject.fromObject(param);
		String ip = jo.getString("ip").toString();
		String mac = jo.getString("mac").toString();
		String insuplcAdmdvs = jo.getString("insuplcAdmdvs").toString();
		Input2301 dscInput = JsonUtil.readValue(param, Input2301.class);
		String pkPv = jo.getString("pkPv").toString();
		InsZsbaPvQg pvQg = DataBaseHelper.queryForBean("select * from ins_pv_qg where pk_pv = ? and del_flag='0'", InsZsbaPvQg.class, pkPv);
		
		InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
		if(signIn.getCode().equals("0")){
			
			int uploadTimes = 0;//上传次数
			int lastNum = 100;//每次要上传的数据  会变
			int maxNum = 100;//最多可上传的数量  不会变
			if(dscInput.getFeedetail().size()%maxNum==0){
				uploadTimes = dscInput.getFeedetail().size()/maxNum;
			}else{
				uploadTimes = (dscInput.getFeedetail().size()/maxNum)+1;
				lastNum = dscInput.getFeedetail().size()%maxNum;
			}
			
			Input2301 input2301 = new Input2301();
			int beginNum = 0;
			int endNum = 0;
			boolean uploadFlag = true;
			String msg = null;
			for(int i=1; i<=uploadTimes; i++){
				List<Input2301Feedetail> feeList = new ArrayList<Input2301Feedetail>();
				if(i==uploadTimes){
					endNum += lastNum;
				}else{
					endNum += maxNum;
				}
				for(int j=beginNum; j<endNum; j++){
					Input2301Feedetail fee = dscInput.getFeedetail().get(j);
					feeList.add(fee); 
				}
				beginNum = endNum;
				input2301.setFeedetail(feeList);
				OutputData2301 out2301 = YbFunUtils.fun2301(input2301, insuplcAdmdvs, signIn.getSignNo());
				if(out2301.getInfcode()==null || !out2301.getInfcode().equals("0")){
					msg = out2301.getErr_msg()+out2301.getMessage();
					uploadFlag = false;
					break;
				}else{
					//保存数据
					for (Output2301Result result : out2301.getOutput().getResult()) {
						//将医保返回的信息set到实体类中
						DataBaseHelper.insertBean(YbToInsFeedetail(result, pvQg));
					}
				}
			}
			if(!uploadFlag){
				//2301失败
				returnMap.put("code", "-1");
				returnMap.put("msg", msg);
			}
		}else{
			returnMap.put("code", signIn.getCode());
			returnMap.put("msg", signIn.getMsg());
		}
		return returnMap;
	}
	
	/**
	 *  将医保返回的值赋值到费用明细实体类
	 * @param result
	 * @param st
	 * @return
	 */
	private InsZsbaFeedetail2301Qg YbToInsFeedetail(Output2301Result result, InsZsbaPvQg pv){
		InsZsbaFeedetail2301Qg feedetail = new InsZsbaFeedetail2301Qg();
		feedetail.setPkInspvqg(pv.getPkInspvqg());
		feedetail.setPkPi(pv.getPkPi());
		feedetail.setPkPv(pv.getPkPv());
		feedetail.setMdtrtId(pv.getMdtrtId());
		feedetail.setFeedetlSn(result.getFeedetl_sn());
		feedetail.setCnt(new BigDecimal(result.getCnt()==null?"0":result.getCnt()));
		feedetail.setPric(new BigDecimal(result.getPric()==null?"0":result.getPric()));
		feedetail.setDetItemFeeSumamt(new BigDecimal(result.getDet_item_fee_sumamt()==null?"0":result.getDet_item_fee_sumamt()));
		feedetail.setPricUplmtAmt(new BigDecimal(result.getPric_uplmt_amt()==null?"0":result.getDet_item_fee_sumamt()));
		feedetail.setSelfpayProp(new BigDecimal(result.getSelfpay_prop()==null?"0":result.getSelfpay_prop()));
		feedetail.setFulamtOwnpayAmt(new BigDecimal(result.getFulamt_ownpay_amt()==null?"0":result.getFulamt_ownpay_amt()));
		feedetail.setOverlmtAmt(new BigDecimal(result.getOverlmt_amt()==null?"0":result.getOverlmt_amt()));
		feedetail.setPreselfpayAmt(new BigDecimal(result.getPreselfpay_amt()==null?"0":result.getPreselfpay_amt()));
		feedetail.setInscpScpAmt(new BigDecimal(result.getInscp_scp_amt()==null?"0":result.getInscp_scp_amt()));
		feedetail.setChrgitmLv(result.getChrgitm_lv());
		feedetail.setMedChrgitmType(result.getMed_chrgitm_type());
		feedetail.setBasMednFlag(result.getBas_medn_flag());
		feedetail.setHiNegoDrugFlag(result.getHi_nego_drug_flag());
		feedetail.setChldMedcFlag(result.getChld_medc_flag());
		feedetail.setListSpItemFlag(result.getList_sp_item_flag());
		feedetail.setDrtReimFlag(result.getDrt_reim_flag());
		feedetail.setMemo(result.getMemo());
		return feedetail;
	}
	
	
	/**
	 * 医保预结算
	 * @param param
	 * @param user
	 */
	public Map<String, Object> getPreSettleData(String param,IUser user){
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("code", "0");
		returnMap.put("msg", "");
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv").toString();
		String ip = jo.getString("ip").toString();
		String mac = jo.getString("mac").toString();
		String totalAmount = jo.getString("totalAmount").toString();
		String flagHalfwaySt = jo.getString("flagHalfwaySt").toString();//是否院内中途结算  1是0否   （院内是中途结算，医保是出院结算）
		String midSetlFlag = jo.getString("midSetlFlag").toString();//医保中途结算，一次住院多次医保结算，一般用于异地年度结算 （院内是中途结算，医保也是中途结算）
		String dateEnd = jo.getString("dateEnd").toString();//中途结算截止时间
		if(flagHalfwaySt==null){
			flagHalfwaySt = "0";
		}
		InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
		if(signIn.getCode().equals("0")){
			Map<String,Object> param_h = new HashMap<String,Object>();
			param_h.put("pkPv", pkPv);
			//1.根据主键查登记信息
			InsZsbaPvQg pvQg = DataBaseHelper.queryForBean("select * from ins_pv_qg where pk_pv = ? and del_flag='0'", InsZsbaPvQg.class, pkPv);
			if(pvQg!=null){
				boolean cydjFlag = true;
				if(!midSetlFlag.equals("1")){//等于1的时候不用做出院登记
					
					//登记接口也得改，这个晚点再弄
					/*//跨省的要先调用1101接口，不然无法出院登记
					BdHp hp = DataBaseHelper.queryForBean("select * from bd_hp where pk_hp = ? and del_flag='0'", BdHp.class, pvQg.getPkInsu());
					if(hp.getCode().equals("00059")){
						
					}*/
					
					//查询出院登记信息
					List<Map<String,Object>> drData = insPvMapper.getYdDischargeRegistrationData(param_h);
					List<Map<String,Object>> diagDataList = new ArrayList<Map<String,Object>>();
					if(ApplicationUtils.getSysparam("CN0016", false).equals("0")){
						 diagDataList = insPvMapper.getCyDiagData(param_h);
					}else{
						diagDataList = insPvMapper.getCyDiagData2(param_h);
					}			
	/*				if(!(totalAmount.compareTo(totalAmountBd.add(new BigDecimal("1")))==1 || 
							totalAmount.compareTo(totalAmountBd.subtract(new BigDecimal("1")))==-1)){*/
						Input2402Dscginfo dscginfo = new Input2402Dscginfo();
						dscginfo.setMdtrt_id(pvQg.getMdtrtId());
						dscginfo.setPsn_no(pvQg.getPsnNo());
						dscginfo.setInsutype(pvQg.getInsutype());
						if(flagHalfwaySt.equals("1")){
							//院内中途结算时，用中途截止时间作为出院时间
							dscginfo.setEndtime(dateEnd);
						}else{
							dscginfo.setEndtime(drData.get(0).get("date_end").toString().substring(0, 19));
						}
						dscginfo.setDise_codg(pvQg.getDiseCodg());
						dscginfo.setDise_name(pvQg.getDiseName());
						dscginfo.setOprn_oprt_code(pvQg.getOprnOprtCode());
						dscginfo.setOprn_oprt_name(pvQg.getOprnOprtName());
						dscginfo.setFpsc_no(pvQg.getFpscNo());
						dscginfo.setMatn_type(pvQg.getMatnType());
						dscginfo.setBirctrl_type(pvQg.getBirctrlType());
						dscginfo.setLatechb_flag(pvQg.getLatechbFlag());
						dscginfo.setGeso_val(pvQg.getGesoVal());
						dscginfo.setFetts(pvQg.getFetts());
						dscginfo.setFetus_cnt(pvQg.getFetusCnt());
						dscginfo.setPret_flag(pvQg.getPretFlag());
						dscginfo.setBirctrl_matn_date(pvQg.getBirctrlMatnDate());
						dscginfo.setDscg_dept_codg(drData.get(0).get("code_dept_nation_ins").toString());
						dscginfo.setDscg_dept_name(drData.get(0).get("name_dept").toString());
						dscginfo.setDscg_bed(drData.get(0).get("bed_no").toString());
						dscginfo.setDscg_way("9");//9：其他
						
						List<Input2402Diseinfo> diseinfoList = new ArrayList<Input2402Diseinfo>();
						Input2402Diseinfo diseinfo = new Input2402Diseinfo();
						//日间手术出院诊断传维护的诊断
						if(pvQg.getMedType().equals("2101")){
							if(pvQg.getRjzd()!=null){
								param_h.put("rjCode", pvQg.getRjzd());
								List<Map<String,Object>> rjDiagList = insQgybPvMapper.getRjzd(param_h);
								diseinfo.setDiag_type("3"); //1 	西医主要诊断  2 	西医其他诊断  3 	中医主病诊断  4 	中医主证诊断 
								diseinfo.setDiag_code(rjDiagList.get(0).get("diagcode")==null?null:rjDiagList.get(0).get("diagcode").toString());
								diseinfo.setDiag_name(rjDiagList.get(0).get("diagname")==null?null:rjDiagList.get(0).get("diagname").toString());
							}else{
								diseinfo.setDiag_type("3"); //1 	西医主要诊断  2 	西医其他诊断  3 	中医主病诊断  4 	中医主证诊断 
								diseinfo.setDiag_code(diagDataList.get(0).get("diagcode")==null?null:diagDataList.get(0).get("diagcode").toString());
								diseinfo.setDiag_name(diagDataList.get(0).get("diagname")==null?null:diagDataList.get(0).get("diagname").toString());
							}
						}else{
							diseinfo.setDiag_type("3"); //1 	西医主要诊断  2 	西医其他诊断  3 	中医主病诊断  4 	中医主证诊断 
							diseinfo.setDiag_code(diagDataList.get(0).get("diagcode")==null?null:diagDataList.get(0).get("diagcode").toString());
							diseinfo.setDiag_name(diagDataList.get(0).get("diagname")==null?null:diagDataList.get(0).get("diagname").toString());
						}
						diseinfo.setPsn_no(pvQg.getPsnNo());
						diseinfo.setMdtrt_id(pvQg.getMdtrtId());
						diseinfo.setMaindiag_flag("1");
						diseinfo.setDiag_srt_no("1");
						diseinfo.setDiag_dept(drData.get(0).get("code_dept").toString());
						diseinfo.setDise_dor_no(diagDataList.get(0).get("diagCodeEmp")==null?null:diagDataList.get(0).get("diagCodeEmp").toString());
						diseinfo.setDise_dor_name(diagDataList.get(0).get("diagNameEmp")==null?null:diagDataList.get(0).get("diagNameEmp").toString());
						diseinfo.setDiag_time(diagDataList.get(0).get("diagDate")==null?null:diagDataList.get(0).get("diagDate").toString());
						diseinfoList.add(diseinfo);
						
						Input2402 input2402 = new Input2402();
						input2402.setDiseinfo(diseinfoList);
						input2402.setDscginfo(dscginfo);
						OutputData2402 out2402 = YbFunUtils.fun2402(input2402, pvQg.getInsuplcAdmdvs(), signIn.getSignNo());
						if(out2402.getInfcode()!=null && out2402.getInfcode().equals("0")){
							//4..3出院信息保存his
							pvQg.setMsgid2402(out2402.getMsgid());
							pvQg.setEndtime(dscginfo.getEndtime());
							pvQg.setDscgDeptCodg(dscginfo.getDscg_dept_codg());
							pvQg.setDscgDeptName(dscginfo.getDscg_dept_name());
							pvQg.setDscgBed(dscginfo.getDscg_bed());
							pvQg.setDscgWay(dscginfo.getDscg_way());
							pvQg.setStatus("5");
							DataBaseHelper.updateBeanByPk(pvQg);
							for(Input2402Diseinfo dis : diseinfoList){
								//4.4保存出院诊断信息
								InsZsbaDiseinfoQg diseinfoSave  = ybToDiseinfoQg(dis, pvQg.getPkInspvqg(), pvQg.getMdtrtId());
								DataBaseHelper.insertBean(diseinfoSave);
							}
						}else{
							returnMap.put("code", "-1");
							returnMap.put("msg", out2402.getErr_msg()+out2402.getMessage());
							cydjFlag = false;
						}
				}
				if(cydjFlag){
					//5.预结算
					Input2303Data data2303 = new Input2303Data();
					data2303.setPsn_no(pvQg.getPsnNo());
					data2303.setMdtrt_cert_type(pvQg.getMdtrtCertType());
					data2303.setMdtrt_cert_no(pvQg.getMdtrtCertNo());
					data2303.setMedfee_sumamt(totalAmount);
					data2303.setPsn_setlway("01"); //01 	按项目结算 	02 	按定额结算    中山没有02 传01就行
					data2303.setMdtrt_id(pvQg.getMdtrtId());
					data2303.setAcct_used_flag("0");
					data2303.setInsutype(pvQg.getInsutype());
					data2303.setMid_setl_flag(midSetlFlag);
					Input2303 input2303 = new Input2303();
					input2303.setData(data2303);
					OutputData2303 out2303 = YbFunUtils.fun2303(input2303, pvQg.getInsuplcAdmdvs(), signIn.getSignNo());
					if(out2303.getInfcode()!=null && out2303.getInfcode().equals("0")){
						returnMap.put("medfeeSumamt", out2303.getOutput().getSetlinfo().getMedfee_sumamt());//医疗费总金额
						returnMap.put("fulamtOwnpayAmt", out2303.getOutput().getSetlinfo().getFulamt_ownpay_amt());//全自费金额  指那些丙类全自费的
						returnMap.put("psnPartAmt", out2303.getOutput().getSetlinfo().getPsn_cash_pay());//个人负担总金额  患者需要出的钱  这部分可用个账也可用患者的钱
						returnMap.put("acctPay", out2303.getOutput().getSetlinfo().getAcct_pay());//个账
						returnMap.put("psn_cash_pay", out2303.getOutput().getSetlinfo().getPsn_cash_pay());//个人现金支出,个账为0的时候，这个跟个人负担总金额应该是相同的,但跨省不同，跨省个人负担总金额可能为0
						returnMap.put("ybbx", Double.parseDouble(out2303.getOutput().getSetlinfo().getMedfee_sumamt())-
								Double.parseDouble(out2303.getOutput().getSetlinfo().getPsn_cash_pay()));
						returnMap.put("tcje", Double.parseDouble(out2303.getOutput().getSetlinfo().getMedfee_sumamt())-
								Double.parseDouble(out2303.getOutput().getSetlinfo().getPsn_cash_pay()));
						returnMap.put("inscpScpAmt", out2303.getOutput().getSetlinfo().getInscp_scp_amt());//符合报销金额
						returnMap.put("mafPay", out2303.getOutput().getSetlinfo().getMaf_pay());//医疗救助基金支出
						returnMap.put("zdbjdxAmt", "0.00");//重点保健对象金额 默认为0
						
						

						//判断是否是重点保健对象，是否在有效期内
						if(pvQg.getPsnIdetType()!=null && (pvQg.getPsnIdetType().equals("7001") || pvQg.getPsnIdetType().equals("7002"))){
							boolean dietBegmtimeFlag = false;
							boolean dietEndtimeFlag= false;
							try {
								dietBegmtimeFlag = DateUtils.belongCalendar(DateUtils.parseDate(pvQg.getBegntime()), DateUtils.parseDate(pvQg.getIdetBegntime()), DateUtils.parseDate(pvQg.getIdetEndtime()));
								dietEndtimeFlag = DateUtils.belongCalendar(DateUtils.parseDate(pvQg.getEndtime()), DateUtils.parseDate(pvQg.getIdetBegntime()), DateUtils.parseDate(pvQg.getIdetEndtime()));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							 
							if(dietBegmtimeFlag&&dietEndtimeFlag){
								if(pvQg.getPsnIdetType().equals("7001")){
									returnMap.put("zdbjdxAmt", out2303.getOutput().getSetlinfo().getPsn_cash_pay());
								}else if(pvQg.getPsnIdetType().equals("7002")){
									BigDecimal zdbjdxAmt = new BigDecimal(out2303.getOutput().getSetlinfo().getPsn_cash_pay());
									if(zdbjdxAmt.compareTo(BigDecimal.ZERO)==0){
										returnMap.put("zdbjdxAmt", "0.00");
									}else{
										//JAVA中如果用BigDecimal做除法的时候一定要在divide方法中传递第二个参数，定义精确到小数点后几位，否则在不整除的情况下，结果是无限循环小数时，就会抛出异常。
										zdbjdxAmt = zdbjdxAmt.divide(new BigDecimal("2"), 2, BigDecimal.ROUND_HALF_UP);
										returnMap.put("zdbjdxAmt", zdbjdxAmt.setScale(2,BigDecimal.ROUND_HALF_UP));
									}
									
								}
							}
						}
						
/*							medfee_sumamt 医疗费总额
							fulamt_ownpay_amt 全自费金额
							acct_pay 个人账户支出*/
					}else{
						//2303预结算失败
						//取消出院登记
						Input2405Data data2405 = new Input2405Data();
						data2405.setMdtrt_id(pvQg.getMdtrtId());
						data2405.setPsn_no(pvQg.getPsnNo());
						Input2405 input2405 = new Input2405();
						input2405.setData(data2405);
						OutputData2405 out2405 = YbFunUtils.fun2405(input2405, pvQg.getInsuplcAdmdvs(), signIn.getSignNo());
						if(out2405.getInfcode()!=null && out2405.getInfcode().equals("0")){
							//his保存取消信息
							pvQg.setStatus("7");
							pvQg.setEndtime("");
							pvQg.setDscgDeptCodg("");
							pvQg.setDscgDeptName("");
							pvQg.setDscgBed("");
							pvQg.setDscgWay("");
							DataBaseHelper.updateBeanByPk(pvQg);
							DataBaseHelper.execute("update ins_diseinfo_qg   set del_flag = '1', inout_diag_type='2' where pk_inspvqg = ?", pvQg.getPkInspvqg());
							//删除上传的费用明细
							//out2302 = YbFunUtils.fun2302(input2302, pvQg.getInsuplcAdmdvs());
							//delFeedetail(input2302, pvQg.getInsuplcAdmdvs(), pvQg.getPkInspvqg(), signIn.getSignNo());
							returnMap.put("code", "-1");
							returnMap.put("msg", out2303.getErr_msg()+out2303.getMessage());
						}else{
							//2303预结算失败并且2405取消出院登记也失败
							pvQg.setStatus("8");
							DataBaseHelper.updateBeanByPk(pvQg);
							returnMap.put("code", "-1");
							returnMap.put("msg", out2303.getErr_msg()+out2303.getMessage());
						}
						/*returnMap.put("code", "-1");
						returnMap.put("msg", out2303.getErr_msg()+out2303.getMessage());*/
					}
				}
			}else{
				returnMap.put("code", "-1");
				returnMap.put("msg", "查不到该患者医保登记信息！");
			}
		}else{
			returnMap.put("code", signIn.getCode());
			returnMap.put("msg", signIn.getMsg());
		}
		
		return returnMap;
	}

	/**
	 * 取消费用明细上传，并删除his中已上传返回的医保数据
	 * @param input2302
	 * @param insuplcAdmdvs
	 * @param pkInspvqg
	 */
	private void delFeedetail(Input2302 input2302, String insuplcAdmdvs, String pkInspvqg, String signNo){
/*		OutputData2302 out2302 = YbFunUtils.fun2302(input2302, insuplcAdmdvs, signNo);
		if((out2302.getInfcode()!=null && out2302.getInfcode().equals("0"))||
				(out2302.getErr_msg()!=null && out2302.getErr_msg().contains("根据传来的参数在费用明细表中未查询到任何有效相关的记录"))){
			//医保删除成功，删除his的
			insQgybFeedetailMapper.delInsFeedetailQg(pkInspvqg);
		}*/
	}
	
	/**
	 * 没进行正式结算就关闭结算弹窗，需要取消出院，回退费用
	 * @param param
	 * @param user
	 * @return
	 */
	public InsZsbaPvQg cancelOutReg(String param,IUser user){
		InsZsbaPvQg returnPv = new InsZsbaPvQg();
		returnPv.setCode("0");
		returnPv.setMsg("");
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv").toString();
		String ip = jo.getString("ip").toString();
		String mac = jo.getString("mac").toString();
		InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
		if(signIn.getCode().equals("0")){
			InsZsbaPvQg pvQg = DataBaseHelper.queryForBean("select * from ins_pv_qg where pk_pv = ? and del_flag='0'", InsZsbaPvQg.class, pkPv);
			if(pvQg!=null){
				//取消出院登记
				Input2405Data data2405 = new Input2405Data();
				data2405.setMdtrt_id(pvQg.getMdtrtId());
				data2405.setPsn_no(pvQg.getPsnNo());
				Input2405 input2405 = new Input2405();
				input2405.setData(data2405);
				OutputData2405 out2405 = YbFunUtils.fun2405(input2405, pvQg.getInsuplcAdmdvs(), signIn.getSignNo());
				if(out2405.getInfcode()!=null && out2405.getInfcode().equals("0")){
				//String out2405 = YbFunUtils.fun2405Json(input2405, pvQg.getInsuplcAdmdvs(), signIn.getSignNo());
				//JSONObject jo2405 = JSONObject.fromObject(out2405);
				//if(jo2405.get("infcode")!=null && jo2405.get("infcode").toString().equals("0")){
					//his保存取消信息
					pvQg.setStatus("7");
					pvQg.setEndtime("");
					pvQg.setDscgDeptCodg("");
					pvQg.setDscgDeptName("");
					pvQg.setDscgBed("");
					pvQg.setDscgWay("");
					DataBaseHelper.updateBeanByPk(pvQg);
					DataBaseHelper.execute("update ins_diseinfo_qg   set del_flag = '1' where pk_inspvqg = ? and inout_diag_type='2' and del_flag = '0'", pvQg.getPkInspvqg());
					//删除上传的费用明细
					//List<Input2302Data> dataList2302 = new ArrayList<Input2302Data>();
					Input2302Data data2302  = new Input2302Data();
					data2302.setFeedetl_sn("0000");//0000取消全部
					data2302.setMdtrt_id(pvQg.getMdtrtId());
					data2302.setPsn_no(pvQg.getPsnNo());
					//dataList2302.add(data2302);
					Input2302 input2302 = new Input2302();
					input2302.setData(data2302);
					delFeedetail(input2302, pvQg.getInsuplcAdmdvs(), pvQg.getPkInspvqg(), signIn.getSignNo());
					returnPv = pvQg;
					returnPv.setCode("0");
				}else{
					//2405取消出院登记也失败
					//pvQg.setStatus("8");
					//DataBaseHelper.updateBeanByPk(pvQg);
					//st.setCode("-1");
					//st.setMsg(out2304.getErr_msg()+out2304.getMessage());
					returnPv.setCode("-1");
					returnPv.setMsg(out2405.getErr_msg()+out2405.getMessage());
					//returnPv.setMsg(jo2405.get("err_msg")==null?"":jo2405.get("err_msg").toString());
					//+jo24050.get("message")==null?"":jo2404.get("message").toString());
				}
			}else{
				returnPv.setCode("-1");
				returnPv.setMsg("查不到该患者医保登记信息！");
			}
		}else{
			returnPv.setCode(signIn.getCode());
			returnPv.setMsg(signIn.getMsg());
		}
		return returnPv;
	}
	
	/**
	 * 医保正式结算
	 */
	public InsZsbaStQg saveSettle(String param,IUser user){
		InsZsbaStQg st = new InsZsbaStQg();
		st.setCode("0");
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv").toString();
		String totoalAmt =  jo.getString("totoalAmt").toString();
		String posSn =  jo.getString("lsh").toString();
		String ybzhye =  jo.getString("ybzhye").toString();
		String nd =  jo.getString("nd").toString();
		String rylb =  jo.getString("rylb").toString();
		String kh =  jo.getString("kh").toString();
		String jym =  jo.getString("jym").toString();
		String psam =  jo.getString("psam").toString();
		String cbh =  jo.getString("cbh").toString();
		String ndk =  jo.getString("ndk").toString();
		String ip = jo.getString("ip").toString();
		String mac = jo.getString("mac").toString();
		String acctPay = jo.getString("acctPay").toString();//此次扣除的个账
		String midSetlFlag = jo.getString("midSetlFlag").toString();//是否医保中途结算
		if(acctPay.trim().length()==0){
			acctPay = "0";
		}
		InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
		if(signIn.getCode().equals("0")){
			InsZsbaPvQg pvQg = DataBaseHelper.queryForBean("select * from ins_pv_qg where pk_pv = ? and del_flag='0'", InsZsbaPvQg.class, pkPv);
			if(pvQg!=null){
				Input2304Data data2304 = new Input2304Data();
				data2304.setPsn_no(pvQg.getPsnNo());
				data2304.setMdtrt_cert_type(pvQg.getMdtrtCertType());
				data2304.setMdtrt_cert_no(pvQg.getMdtrtCertNo());
				data2304.setMedfee_sumamt(totoalAmt);
				data2304.setPsn_setlway("01"); //01 	按项目结算 	02 	按定额结算    中山没有02 传01就行
				data2304.setMdtrt_id(pvQg.getMdtrtId());
				data2304.setAcct_used_flag("0");
				data2304.setInsutype(pvQg.getInsutype());
				data2304.setMid_setl_flag(midSetlFlag);
				Input2304 input2304 = new Input2304();
				input2304.setData(data2304);
				OutputData2304 out2304 = YbFunUtils.fun2304(input2304, pvQg.getInsuplcAdmdvs(), signIn.getSignNo());
				if(out2304.getInfcode()!=null && out2304.getInfcode().equals("0")){
					st.setMsgid2304(out2304.getMsgid());
					st.setPkInspvqg(pvQg.getPkInspvqg());
					st.setPkPi(pvQg.getPkPi());
					st.setPkPv(pvQg.getPkPv());
					st.setEuPvtype("3");
					st.setMdtrtCertType(data2304.getMdtrt_cert_type());
					st.setMdtrtCertNo(data2304.getMdtrt_cert_no());
					st.setPsnSetlway(data2304.getPsn_setlway());
					st.setMdtrtId(data2304.getMdtrt_id());
					st.setAcctUsedFlag(data2304.getAcct_used_flag());
					st.setInvono(data2304.getInvono());
					st.setSetlId(out2304.getOutput().getSetlinfo().getSetl_id());
					st.setPsnNo(out2304.getOutput().getSetlinfo().getPsn_no());
					st.setPsnName(out2304.getOutput().getSetlinfo().getPsn_name());
					st.setPsnCertType(out2304.getOutput().getSetlinfo().getPsn_cert_type());
					st.setCertno(out2304.getOutput().getSetlinfo().getCertno());
					st.setGend(out2304.getOutput().getSetlinfo().getGend());
					st.setNaty(out2304.getOutput().getSetlinfo().getNaty());
					st.setBrdy(out2304.getOutput().getSetlinfo().getBrdy());
					st.setAge(out2304.getOutput().getSetlinfo().getAge());
					st.setInsutype(out2304.getOutput().getSetlinfo().getInsutype());
					st.setPsnType(out2304.getOutput().getSetlinfo().getPsn_type());
					st.setCvlservFlag(out2304.getOutput().getSetlinfo().getCvlserv_flag());
					try {
						st.setSetlTime(DateUtils.parseDate(out2304.getOutput().getSetlinfo().getSetl_time(), "yyyy-MM-dd HH:mm:ss"));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					st.setMedType(out2304.getOutput().getSetlinfo().getMed_type());
					st.setMedfeeSumamt(new BigDecimal(out2304.getOutput().getSetlinfo().getMedfee_sumamt()));
					st.setFulamtOwnpayAmt(new BigDecimal(out2304.getOutput().getSetlinfo().getFulamt_ownpay_amt()));
					st.setOverlmtSelfpay(new BigDecimal(out2304.getOutput().getSetlinfo().getOverlmt_selfpay()));
					st.setPreselfpayAmt(new BigDecimal(out2304.getOutput().getSetlinfo().getPreselfpay_amt()));
					st.setInscpScpAmt(new BigDecimal(out2304.getOutput().getSetlinfo().getInscp_scp_amt()));
					st.setActPayDedc(new BigDecimal(out2304.getOutput().getSetlinfo().getAct_pay_dedc()));
					st.setHifpPay(new BigDecimal(out2304.getOutput().getSetlinfo().getHifp_pay()));
					st.setPoolPropSelfpay(new BigDecimal(out2304.getOutput().getSetlinfo().getPool_prop_selfpay()));
					st.setCvlservPay(new BigDecimal(out2304.getOutput().getSetlinfo().getCvlserv_pay()));
					st.setHifesPay(new BigDecimal(out2304.getOutput().getSetlinfo().getHifes_pay()));
					st.setHifmiPay(new BigDecimal(out2304.getOutput().getSetlinfo().getHifmi_pay()));
					st.setHifobPay(new BigDecimal(out2304.getOutput().getSetlinfo().getHifob_pay()));
					st.setMafPay(new BigDecimal(out2304.getOutput().getSetlinfo().getMaf_pay()));
					st.setHospPartAmt(new BigDecimal(out2304.getOutput().getSetlinfo().getHosp_part_amt()));
					st.setOthPay(new BigDecimal(out2304.getOutput().getSetlinfo().getOth_pay()));
					st.setFundPaySumamt(new BigDecimal(out2304.getOutput().getSetlinfo().getFund_pay_sumamt()));
					st.setPsnPartAmt(new BigDecimal(out2304.getOutput().getSetlinfo().getPsn_part_amt()));
					st.setAcctPay(new BigDecimal(acctPay));
					if(st.getPsnPartAmt().equals(BigDecimal.ZERO)){
						st.setPsnCashPay(new BigDecimal(out2304.getOutput().getSetlinfo().getPsn_cash_pay()));
					}else{
						st.setPsnCashPay(st.getPsnPartAmt().subtract(st.getAcctPay()));
					}
					
					st.setBalc(new BigDecimal(out2304.getOutput().getSetlinfo().getBalc()));
					st.setAcctMulaidPay(new BigDecimal(out2304.getOutput().getSetlinfo().getAcct_mulaid_pay()));
					st.setMedinsSetlId(out2304.getOutput().getSetlinfo().getMedins_setl_id());
					st.setClrOptins(out2304.getOutput().getSetlinfo().getClr_optins());
					st.setClrWay(out2304.getOutput().getSetlinfo().getClr_way());
					st.setClrType(out2304.getOutput().getSetlinfo().getClr_type());
					st.setPosSn(posSn);
					st.setStatus("11");
					if(ybzhye!=null && ybzhye.trim().length()!=0){
						st.setYbzhye(new BigDecimal(ybzhye));
					}else{
						st.setYbzhye(new BigDecimal(0.00));
					}
					st.setNd(nd);
					st.setRylb(rylb);
					st.setKh(kh);
					st.setJym(jym);
					st.setPsam(psam);
					st.setCbh(cbh);
					st.setNdk(ndk);
					st.setMid_setl_flag(midSetlFlag);
					
					st.setZdbjdxAmt("0.00");//重点保健对象金额 默认为0
					
					
					//判断是否是重点保健对象，是否在有效期内
						if(pvQg.getPsnIdetType()!=null && (pvQg.getPsnIdetType().equals("7001") || pvQg.getPsnIdetType().equals("7002"))){
						boolean dietBegmtimeFlag = false;
						boolean dietEndtimeFlag= false;
						try {
							dietBegmtimeFlag = DateUtils.belongCalendar(DateUtils.parseDate(pvQg.getBegntime()), DateUtils.parseDate(pvQg.getIdetBegntime()), DateUtils.parseDate(pvQg.getIdetEndtime()));
							dietEndtimeFlag = DateUtils.belongCalendar(DateUtils.parseDate(pvQg.getEndtime()), DateUtils.parseDate(pvQg.getIdetBegntime()), DateUtils.parseDate(pvQg.getIdetEndtime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						 
						if(dietBegmtimeFlag&&dietEndtimeFlag){
							if(pvQg.getPsnIdetType().equals("7001")){
								st.setZdbjdxAmt(out2304.getOutput().getSetlinfo().getPsn_cash_pay());
							}else if(pvQg.getPsnIdetType().equals("7002")){
								BigDecimal zdbjdxAmt = new BigDecimal(out2304.getOutput().getSetlinfo().getPsn_cash_pay());
								if(zdbjdxAmt.compareTo(BigDecimal.ZERO)==0){
									st.setZdbjdxAmt("0.00");
								}else{
									//JAVA中如果用BigDecimal做除法的时候一定要在divide方法中传递第二个参数，定义精确到小数点后几位，否则在不整除的情况下，结果是无限循环小数时，就会抛出异常。
									zdbjdxAmt = zdbjdxAmt.divide(new BigDecimal("2"), 2, BigDecimal.ROUND_HALF_UP);
									st.setZdbjdxAmt(zdbjdxAmt.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
								}
								
							}
						}
					}
					
					DataBaseHelper.insertBean(st);
					for (Output2304Setldetail setldetail : out2304.getOutput().getSetldetail()) {
						InsZsbaStSetldetailQg saveSl = new InsZsbaStSetldetailQg();
						saveSl.setPkInsstqg(st.getPkInsstqg());
						saveSl.setFundPayType(setldetail.getFund_pay_type());
						saveSl.setInscpScpAmt(setldetail.getInscp_scp_amt());
						saveSl.setCrtPaybLmtAmt(setldetail.getCrt_payb_lmt_amt());
						saveSl.setFundPayAmt(setldetail.getFund_payamt());
						saveSl.setFundPayTypeName(setldetail.getFund_pay_type_name());
						saveSl.setSetlProcInfo(setldetail.getSetl_proc_info());
						DataBaseHelper.insertBean(saveSl);
					}
					//修改医保登记表的状态
					pvQg.setStatus("11");
					DataBaseHelper.updateBeanByPk(pvQg, false);
					
					
				}else{
					st.setCode("-1");
					st.setMsg(out2304.getErr_msg()+out2304.getMessage());
				}
			}else{
				st.setCode("-1");
				st.setMsg("查不到该患者医保登记信息");
			}
		}else{
			st.setCode(signIn.getCode());
			st.setMsg(signIn.getMsg());
		}
		
		return st;
	}

	/**
	 * his结算失败需调用取消结算
	 * @param param
	 * @param user
	 * @return
	 */
	public InsZsbaStQg cancelSettle(String param,IUser user){
		InsZsbaStQg st = new InsZsbaStQg();
		st.setCode("0");
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv").toString();
		String ip = jo.getString("ip").toString();
		String mac = jo.getString("mac").toString();
		String setlId = jo.getString("setlId").toString();
		InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
		if(signIn.getCode().equals("0")){
			st = DataBaseHelper.queryForBean("select * from ins_st_qg where pk_pv = ? and setl_id = ? and del_flag='0'", InsZsbaStQg.class, pkPv, setlId);
			InsZsbaPvQg pvQg = DataBaseHelper.queryForBean("select * from ins_pv_qg where pk_pv = ? and del_flag='0'", InsZsbaPvQg.class, pkPv);
			
			Input2305Data data = new Input2305Data();
			data.setMdtrt_id(st.getMdtrtId());
			data.setPsn_no(st.getPsnNo());
			data.setSetl_id(st.getSetlId());
			
			Input2305 input = new Input2305();
			input.setData(data);
			
			OutputData2305 out2305 = YbFunUtils.fun2305(input, pvQg.getInsuplcAdmdvs(), signIn.getSignNo());
			if(out2305.getInfcode()!=null && out2305.getInfcode().equals("0")){
				pvQg.setStatus("13");
				DataBaseHelper.updateBeanByPk(pvQg, false);
				st.setDelFlag("1");
				st.setStatus("13");
				DataBaseHelper.updateBeanByPk(st, false);
				//取消结算要插入一条反的记录
				InsZsbaStQg cancelSt = (InsZsbaStQg)st.clone();
				BigDecimal big = new BigDecimal("-1");
				cancelSt.setMedfeeSumamt(st.getMedfeeSumamt().multiply(big));
				cancelSt.setFulamtOwnpayAmt(st.getFulamtOwnpayAmt().multiply(big));
				cancelSt.setOverlmtSelfpay(st.getOverlmtSelfpay().multiply(big));
				cancelSt.setPreselfpayAmt(st.getPreselfpayAmt().multiply(big));
				cancelSt.setInscpScpAmt(st.getInscpScpAmt().multiply(big));
				cancelSt.setActPayDedc(st.getActPayDedc().multiply(big));
				cancelSt.setHifpPay(st.getHifpPay().multiply(big));
				cancelSt.setCvlservPay(st.getCvlservPay().multiply(big));
				cancelSt.setHifesPay(st.getHifesPay().multiply(big));
				cancelSt.setHifmiPay(st.getHifmiPay().multiply(big));
				cancelSt.setHifobPay(st.getHifobPay().multiply(big));
				cancelSt.setMafPay(st.getMafPay().multiply(big));
				cancelSt.setHospPartAmt(st.getHospPartAmt().multiply(big));
				cancelSt.setOthPay(st.getOthPay().multiply(big));
				cancelSt.setFundPaySumamt(st.getFundPaySumamt().multiply(big));
				cancelSt.setPsnPartAmt(st.getPsnPartAmt().multiply(big));
				cancelSt.setAcctPay(st.getAcctPay().multiply(big));
				cancelSt.setPsnCashPay(st.getPsnCashPay().multiply(big));
				cancelSt.setBalc(st.getBalc().multiply(big));
				cancelSt.setAcctMulaidPay(st.getAcctMulaidPay().multiply(big));
				cancelSt.setPkInsstqg(null);
				DataBaseHelper.insertBean(cancelSt);
				st.setCode("0");
			}else{
				st.setCode("-1");
				st.setMsg(out2305.getErr_msg()+out2305.getMessage());
			}
		}else{
			st.setCode(signIn.getCode());
			st.setMsg(signIn.getMsg());
		}
		return st;
	}
	
	/**
	 * his取消结算时调用医保取消结算
	 * @param param
	 * @param user
	 * @return
	 */
	public InsZsbaStQg cancelSettleLc(String param,IUser user){
		InsZsbaStQg st = new InsZsbaStQg();
		st.setCode("0");
		JSONObject jo = JSONObject.fromObject(param);
		String pkPv = jo.getString("pkPv").toString();
		String pkSettle = jo.getString("pkSettle").toString();
		String ip = jo.getString("ip").toString();
		String mac = jo.getString("mac").toString();
		InsZsbaSignInQg signIn = insPubSignInService.saveSignIn(ip, mac);
		if(signIn.getCode().equals("0")){
			st = DataBaseHelper.queryForBean("select * from ins_st_qg where pk_pv = ? and pk_settle = ? "
					+ " and del_flag='0'", InsZsbaStQg.class, pkPv, pkSettle);
			if(st==null){
				st = new InsZsbaStQg();
				st.setCode("0");
				st.setCode("不需要取消医保结算！");
			}else{
				InsZsbaPvQg pvQg = DataBaseHelper.queryForBean("select * from ins_pv_qg where pk_pv = ? and del_flag='0'", InsZsbaPvQg.class, pkPv);
				
				Input2305Data data = new Input2305Data();
				data.setMdtrt_id(st.getMdtrtId());
				data.setPsn_no(st.getPsnNo());
				data.setSetl_id(st.getSetlId());
				
				Input2305 input = new Input2305();
				input.setData(data);
				
				OutputData2305 out2305 = YbFunUtils.fun2305(input, pvQg.getInsuplcAdmdvs(), signIn.getSignNo());
				if(out2305.getInfcode()!=null && out2305.getInfcode().equals("0")){
					pvQg.setStatus("13");
					DataBaseHelper.updateBeanByPk(pvQg, false);
					st.setDelFlag("1");
					st.setStatus("13");
					DataBaseHelper.updateBeanByPk(st, false);
					//取消结算要插入一条反的记录
					InsZsbaStQg cancelSt = (InsZsbaStQg)st.clone();
					BigDecimal big = new BigDecimal("-1");
					cancelSt.setMedfeeSumamt(st.getMedfeeSumamt().multiply(big));
					cancelSt.setFulamtOwnpayAmt(st.getFulamtOwnpayAmt().multiply(big));
					cancelSt.setOverlmtSelfpay(st.getOverlmtSelfpay().multiply(big));
					cancelSt.setPreselfpayAmt(st.getPreselfpayAmt().multiply(big));
					cancelSt.setInscpScpAmt(st.getInscpScpAmt().multiply(big));
					cancelSt.setActPayDedc(st.getActPayDedc().multiply(big));
					cancelSt.setHifpPay(st.getHifpPay().multiply(big));
					cancelSt.setCvlservPay(st.getCvlservPay().multiply(big));
					cancelSt.setHifesPay(st.getHifesPay().multiply(big));
					cancelSt.setHifmiPay(st.getHifmiPay().multiply(big));
					cancelSt.setHifobPay(st.getHifobPay().multiply(big));
					cancelSt.setMafPay(st.getMafPay().multiply(big));
					cancelSt.setHospPartAmt(st.getHospPartAmt().multiply(big));
					cancelSt.setOthPay(st.getOthPay().multiply(big));
					cancelSt.setFundPaySumamt(st.getFundPaySumamt().multiply(big));
					cancelSt.setPsnPartAmt(st.getPsnPartAmt().multiply(big));
					cancelSt.setAcctPay(st.getAcctPay().multiply(big));
					cancelSt.setPsnCashPay(st.getPsnCashPay().multiply(big));
					cancelSt.setBalc(st.getBalc().multiply(big));
					cancelSt.setAcctMulaidPay(st.getAcctMulaidPay().multiply(big));
					cancelSt.setPkInsstqg(null);
					DataBaseHelper.insertBean(cancelSt);
					st.setCode("0");
				}else{
					st.setCode("-1");
					st.setMsg(out2305.getErr_msg()+out2305.getMessage());
				}
			}
		}else{
			st.setCode(signIn.getCode());
			st.setMsg(signIn.getMsg());
		}
		
		return st;
	}
	
	/**
	 * 保存审核结果
	 * @param param
	 * @param user
	 * @return
	 */
	public InsZsbaPvQg saveExamine(String param,IUser user){
		JSONObject jo = JSONObject.fromObject(param);
		String pkInspvqg = jo.getString("pkInspvqg").toString();
		String examineStatus = jo.getString("examineStatus").toString();
		String examineMsg = jo.getString("examineMsg").toString();
		InsZsbaPvQg pv = new InsZsbaPvQg();
		if(StringUtils.isEmpty(pkInspvqg)||StringUtils.isEmpty(examineStatus)){
			pv.setCode("-1");
			pv.setMsg("入参错误");
		}else{
			pv.setPkInspvqg(pkInspvqg);
			pv.setExamineMsg(examineMsg);
			pv.setExamineStatus(examineStatus);
			DataBaseHelper.updateBeanByPk(pv, false);
			pv.setCode("0");
		}
		return pv;
	}
}
