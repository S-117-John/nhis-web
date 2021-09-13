package com.zebone.nhis.base.pub.service;

import com.zebone.nhis.base.pub.dao.BdResPubMapper;
import com.zebone.nhis.common.module.base.bd.res.BdResBed;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ex.pub.support.ExSysParamUtil;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 床位的公共服务
 * @author wj
 */
@Service
public class BdResPubService {

	@Resource
	private BdResPubMapper bdBedPubMapper;
	
	/**
	 * 判断是否含有该床位,没有则添加该床位
	 * @param pkDeptNs 当前护理单元
	 * @param bed 大人床位
	 * @param code 婴儿床位编码
	 * @param u 当前操作人
	 * @param bedSpc 婴儿床位分隔符
	 * @return
	 */
	public List<BdResBed> isHaveBedAndAdd(String pkDeptNs,BdResBed bed,String code,String codeList, User u) {
		if (CommonUtils.isEmptyString(pkDeptNs))
			throw new BusException("请判断是否传入待操作的护理单元主键！");
		//婴儿床位分隔符
		String bedSpc = ExSysParamUtil.getSpcOfCodeBed();//获取婴儿床位分隔符
		if (CommonUtils.isEmptyString(bedSpc))
			throw new BusException("请维护系统参数【BD0007】-婴儿床位编码分隔符！");
		//婴儿床收费项目
		String pkItemOfBed = ExSysParamUtil.getPkItemOfInfBed(pkDeptNs);//获取婴儿收费项目编码
		List<BdResBed> resList = new ArrayList<BdResBed>();
		Date ts = new Date();
		String[] codes = null;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("pkOrg", u.getPkOrg());
		param.put("pkDeptNs", pkDeptNs);
		param.put("dtBedtype", "09");
		if (!CommonUtils.isEmptyString(codeList)) {//传入多个婴儿床位校验是否存在
			codes = codeList.split(",");
			if(null != codes && codes.length > 0)
				param.put("codes", codes);
		}
		else if(!CommonUtils.isEmptyString(code))//传入单个婴儿床位校验是否存在
			param.put("code", code);
		else //传入大人床位，校验是否存在足够的子项
			param.put("codeMa", bed.getCode());
		List<BdResBed> listBed = bdBedPubMapper.qryInfBedByCode(param);
		
		String codeBed = "";
		BdResBed addBed = null;
		List<BdResBed> AddBedList = new ArrayList<BdResBed>();
		if(!CommonUtils.isEmptyString(code)){//传入单个婴儿,不存在则新增
			if(null == listBed || listBed.size() < 1){
				addBed = setDefInfBedInfo(bed,code,pkItemOfBed,u,ts);
				DataBaseHelper.insertBean(addBed);
				AddBedList.add(addBed);
				return AddBedList;
			}else if(listBed!=null && listBed.size()==1){
				resList.addAll(listBed);
			}
		}else if(!CommonUtils.isEmptyString(codeList)){//传入多个婴儿,不存在则新增
			if(null != codes && (null == listBed || listBed.size() != codes.length)){
				for (String codeB : codes) {
					if(listBed.size() < 1){
						addBed = setDefInfBedInfo(bed,codeB,pkItemOfBed,u,ts);
						AddBedList.add(addBed);
						continue;
					}else{
						for (BdResBed bdResBed : listBed) {
							if(bdResBed.getCode().equals(codeB)) {
								resList.add(bdResBed);
								break;
							}
							else
							{
								addBed = setDefInfBedInfo(bed,codeB,pkItemOfBed,u,ts);
								AddBedList.add(addBed);
								break;
							}
						}
					}
				}
			}
		}else {//床位总数不够，则缺啥补啥
			if(null == listBed || listBed.size() < 5){
				for (int i = 1; i <= 5; i++) {
					codeBed = bed.getCode() + bedSpc + i;
					if(listBed.size() < 1){
						addBed = setDefInfBedInfo(bed,codeBed,pkItemOfBed,u,ts);
						AddBedList.add(addBed);
						continue;
					}
					for (BdResBed bdResBed : listBed) {
						if(bdResBed.getCode().equals(codeBed)) 
							break;
						else
						{
							addBed = setDefInfBedInfo(bed,codeBed,pkItemOfBed,u,ts);
							AddBedList.add(addBed);
							break;
						}
					}
				}
			}}
		//存在多个床位未插入，则添加多个床位
		if(null != AddBedList && AddBedList.size() > 0){
			DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(BdResBed.class), AddBedList);
			resList.addAll(AddBedList);
		}
		return resList;
	}
	
	/**
	 * 组装婴儿床位
	 * @return
	 */
	private BdResBed setDefInfBedInfo(BdResBed maBed,String code,String pkItem,User u,Date ts){
		BdResBed addBed = new BdResBed();
		ApplicationUtils.copyProperties(addBed, maBed);
		addBed.setPkBed(NHISUUID.getKeyId());
		addBed.setCode(code);
		addBed.setName(code);
		addBed.setPkPi(null);
		addBed.setPkDeptUsed(null);
		addBed.setPkItem(pkItem);
		addBed.setFlagActive("0");
		addBed.setDelFlag("1");
		addBed.setDtBedtype("09");
		addBed.setEuStatus("01");
		addBed.setFlagOcupy("0");
		addBed.setFlagTemp("0");
		addBed.setCreateTime(ts);
		addBed.setCreator(u.getPkEmp());
		addBed.setTs(ts);
		return addBed;
	}

	/**
	 * 当前科室是否可维护婴儿床位
	 * @return
	 */
	public boolean isHaveInfByPkDeptNs(String pkDeptNs,String pkOrg){
		String deptCodeOfAddInf = ExSysParamUtil.getCodeDeptOfAddInf();//获取可维护婴儿的科室编码
		if(CommonUtils.isEmptyString(deptCodeOfAddInf)) return false;
		String[] deptCodes = deptCodeOfAddInf.split(",");
		if(null == deptCodes || deptCodes.length < 1) return false;
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("pkOrg", pkOrg);
		param.put("pkDeptNs", pkDeptNs);
		param.put("codes", deptCodes);
		Map<String,Object> cnt = bdBedPubMapper.chkDeptAbleAddInf(param);
		if(null == cnt || null == cnt.get("cnt") 
				|| CommonUtils.isEmptyString(cnt.get("cnt").toString())
				|| "0".equals(cnt.get("cnt").toString())) 
			return false;
		else
			return true;
	}

    public BdResBed queMomBed(Map<String, Object> data) {
		return bdBedPubMapper.queMomBed(data);
    }
}
