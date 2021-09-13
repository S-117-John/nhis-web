package com.zebone.nhis.scm.material.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.scm.material.dao.MtlQueryStatMapper;
import com.zebone.nhis.scm.material.vo.BdPdBillDtlsVo;
import com.zebone.nhis.scm.material.vo.BdPdQuyBillDtlsVo;
import com.zebone.nhis.scm.material.vo.BdPdBillVo;
import com.zebone.nhis.scm.material.vo.BdPdPurVo;
import com.zebone.nhis.scm.material.vo.BdPdReceiveVo;
import com.zebone.nhis.scm.material.vo.BdPdStDetailsVo;
import com.zebone.nhis.scm.material.vo.BdPdStInfoVo;
import com.zebone.nhis.scm.material.vo.BdPdStRecordVo;
import com.zebone.nhis.scm.material.vo.BdPdStVo;
import com.zebone.nhis.scm.material.vo.MtlDeptPdStVo;
import com.zebone.nhis.scm.material.vo.MtlLicensePdVo;
import com.zebone.nhis.scm.material.vo.MtlLicenseSupVo;
import com.zebone.nhis.scm.material.vo.MtlTransSumVo;
import com.zebone.nhis.scm.material.vo.PdDeptUsingVo;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 物资-查询统计
 * @author c
 *
 */
@Service
public class MtlQueryStatService {
	
	@Autowired
	private MtlQueryStatMapper mtlQueryStatMapper;
	
	
	/**
	 * 交易号：008007018001
	 * 查询科室在用物品记录
	 * @param param
	 * @param user
	 */
	public List<PdDeptUsingVo> searchPdSDeptUsing(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		List<PdDeptUsingVo> queryList = mtlQueryStatMapper.searchPdSDeptUsing(paramMap);
		return queryList;
	}
	
	/**
	 * 交易号：008007018002
	 * 查询物品领用信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdPdReceiveVo> searchReceiveInfo(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		//获取当前仓库信息并添加到paramMap
		paramMap.put("pkStoreSt", UserContext.getUser().getPkStore());
		
		List<BdPdReceiveVo> queryList = mtlQueryStatMapper.searchReceiveInfo(paramMap);
		return queryList;
	}
	
	
	/**
	 * 交易号：008007018003
	 * 查询入库单信息
	 * @param param
	 * @param user
	 * @return
	 */
	public BdPdStInfoVo searchStInfo(String param,IUser user){
		Map paramMap = JsonUtil.readValue(param, Map.class);
		String pkPdst = paramMap.get("pkPdst").toString();
		
		//查询入库单信息
		BdPdStVo bdPdStVo = mtlQueryStatMapper.searchReceiveByPk(pkPdst);
		//查询入库明细信息
		List<BdPdStDetailsVo> details = mtlQueryStatMapper.searchStDtsByPkPdst(pkPdst);
		
		BdPdStInfoVo vo = new BdPdStInfoVo();
		
		vo.setBdPdSt(bdPdStVo);
		vo.setDetailsList(details);
		
		return vo;
	}
	
	/**
	 * 交易码：008007018004
	 * 获取物品采购信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdPdPurVo> searchBdPdPurInfo(String param,IUser user){
		
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		//获取当前仓库信息并添加到paramMap
		paramMap.put("pkStoreSt", UserContext.getUser().getPkStore());
		
		List<BdPdPurVo> queryList = mtlQueryStatMapper.searchBdPdPurInfo(paramMap);
		return queryList;
		
	}
	
	/**
	 * 交易码：008007018005
	 * 查询当前仓库下的物品出入库记录
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdPdStRecordVo> searchPdStRecord(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		//获取当前仓库信息并添加到paramMap
		paramMap.put("pkStoreSt", UserContext.getUser().getPkStore());
		if( CommonUtils.isNotNull(paramMap.get("dateBegin")) &&
				CommonUtils.isNotNull(paramMap.get("dateEnd"))){
			String dateBegin = paramMap.get("dateBegin").toString();
			String dateEnd = paramMap.get("dateEnd").toString();
			if(dateBegin.equals(dateEnd))
			{
				paramMap.put("dateBegin", dateBegin.substring(0, dateBegin.length()-6)+"000000");
				paramMap.put("dateEnd", dateBegin.substring(0, dateBegin.length()-6)+"235959");
			}
		}
		
		List<BdPdStRecordVo> queryList = mtlQueryStatMapper.searchPdStRecord(paramMap);
		return queryList;
	}
	
	/**
	 * 交易号：008007018006
	 * 查询库存台账信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdPdBillVo> searchBdPdBillInfo(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		//获取当前仓库信息并添加到paramMap
		paramMap.put("pkStoreSt", UserContext.getUser().getPkStore());
		
		/**检索起止财务月份的开始日期和结束日期(1.开始日期为开始财务月份下的开始日期   2.结束日期为结束财务月份下的结束日期)*/
		String monBegin = paramMap.get("monBegin").toString();
		String monEnd = paramMap.get("monEnd").toString();
		
		if(!CommonUtils.isEmptyString(monBegin) && 
				!CommonUtils.isEmptyString(monEnd)){
			 //获取起止财务月份的开始日期和结束日期
			 Map<String,String> dateMap = getDateByMonthFin(monBegin,monEnd);
			 //给声明的dateBegin、dateBegin字段赋值
			 if(dateMap!=null && dateMap.size()>0){
				/**把开始时间和结束时间添加到查询参数*/
				paramMap.put("dateEnd", dateMap.get("dateEnd"));
				paramMap.put("dateBegin", dateMap.get("dateBegin"));
			 }
		}
		
		if(!CommonUtils.isEmptyString(paramMap.get("calMonBegin").toString())){
			paramMap.put("calMonBegin", paramMap.get("calMonBegin").toString().substring(0, 6));
		}
		
		//存放查询出的结果集
		List<BdPdBillVo> queryList = new ArrayList<>();
		
		/**判断统计方式，确认要查询的是成本金额or零售金额(0:成本价  1:零售价)*/
		if(paramMap.get("euStatType").equals("0"))		//成本价
			queryList = mtlQueryStatMapper.searchBdPdBillAmtCost(paramMap);
		else if(paramMap.get("euStatType").equals("1"))	//零售价
			queryList = mtlQueryStatMapper.searchBdPdBillAmt(paramMap);
		
		
		if(queryList!=null && queryList.size()>0){
			for(BdPdBillVo bill:queryList){
				/**根据调价金额计算收入\支出(增值算收入，减值算支出)*/
				if(!CommonUtils.isEmptyString(bill.getTjAmt().toString())){
					if(bill.getTjAmt()>0)		//算收入
						bill.setSrAmt(bill.getSrAmt()+bill.getTjAmt());
					else if(bill.getTjAmt()<0)	//算支出
						bill.setZcAmt(bill.getZcAmt()-(bill.getTjAmt()));
				}
				
				//计算结存数量
				Long jcQuan = bill.getCqQuan()+bill.getSrQuan()-bill.getZcQuan();
				//计算结存金额。结存金额 = 期初金额+收入金额-支出金额。
				Double jcAmt = bill.getCqAmt()+bill.getSrAmt()-bill.getZcAmt();
				bill.setJcQuan(jcQuan);
				bill.setJcAmt(jcAmt);
			}
		}
		
		return queryList;
	}
	
	/**
	 * 交易号：008007018007
	 * 查询物品台账明细信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<BdPdBillDtlsVo> searchBdPdBillDts(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		//获取当前仓库信息并添加到paramMap
		paramMap.put("pkStoreSt", UserContext.getUser().getPkStore());
		
		String dateBegin = null;	//财务月份开始时间
		String dateEnd = null;		//财务月份结束时间
		
		String monBegin = paramMap.get("monBegin").toString();	//获取开始财务月份
		String monEnd = paramMap.get("monEnd").toString();		//获取结束财务月份
		
		if(!CommonUtils.isEmptyString(monBegin) && 
				!CommonUtils.isEmptyString(monEnd)){
			 /**检索起止财务月份的开始日期和结束日期(1.开始日期为开始财务月份下的开始日期   2.结束日期为结束财务月份下的结束日期)*/
			 Map<String,String> dateMap = getDateByMonthFin(monBegin,monEnd);
			 //给声明的dateBegin、dateBegin字段赋值
			 if(dateMap!=null && dateMap.size()>0){
				 /**把开始时间和结束时间添加到查询参数*/
			 	 paramMap.put("dateEnd", dateMap.get("dateEnd"));
				 paramMap.put("dateBegin", dateMap.get("dateBegin"));
			 }
		}
		
		if(!CommonUtils.isEmptyString(paramMap.get("calMonBegin").toString())){
			paramMap.put("calMonBegin", paramMap.get("calMonBegin").toString().substring(0, 6));
		}
		
		/**查询物品台账明细信息*/
		List<BdPdQuyBillDtlsVo> quyList = mtlQueryStatMapper.searchBdPdBillDtls(paramMap);
		
		//存放组装后的结果集
		List<BdPdBillDtlsVo> billDtlsList = new ArrayList<>();
		
		if(quyList!=null && quyList.size()>0){
			
			//统计结存数量
			Long jcQuan = 0L;
			//统计结存金额
			Double jcAmt = 0.00;
			
			/**判断统计方式，确定是成本金额or零售金额(0:成本价  1:零售价)*/
			if(paramMap.get("euStatType").equals("0")){
				
				/**过滤查询出的信息：分为初期、 收入、支出、调价、结存*/
				for(BdPdQuyBillDtlsVo tempVo : quyList){
					BdPdBillDtlsVo billDtl = new BdPdBillDtlsVo(); 
					
					if(CommonUtils.isEmptyString(tempVo.getCodeSt()) &&
							CommonUtils.isEmptyString(tempVo.getCodeRep())){		//初期
						billDtl.setSttypeName("初期结存");	//摘要
						
						jcQuan = tempVo.getCqQuan();
						jcAmt = tempVo.getCqAmountCost();
					}else if(!CommonUtils.isEmptyString(tempVo.getCodeSt())){		//收支
						billDtl.setCodeSt(tempVo.getCodeSt());				//单据编号
						billDtl.setSttypeName(tempVo.getSttypeName());		//摘要
						
						if(tempVo.getEuDirect().equals("1")){			//收入
							jcQuan += tempVo.getStQuan();
							jcAmt += tempVo.getStAmountCost();
							
							billDtl.setSrQuan(tempVo.getStQuan());			//收入数量
							billDtl.setSrAmt(tempVo.getStAmountCost());		//收入金额
						}else if(tempVo.getEuDirect().equals("-1")){	//支出
							jcQuan -= tempVo.getStQuan();
							jcAmt -= tempVo.getStAmountCost();
							
							billDtl.setZcQuan(tempVo.getStQuan());			//支出数量
							billDtl.setZcAmt(tempVo.getStAmountCost());		//支出金额
						}
					} else if(!CommonUtils.isEmptyString(tempVo.getCodeRep())){		//调价
						billDtl.setCodeSt(tempVo.getCodeRep());				//单据编号
						billDtl.setSttypeName(tempVo.getReptypeName());		//摘要
						
						/**根据调价金额计算结存(增值算收入，减值算支出)*/
						if(tempVo.getTjEuDirect().equals("1")){			//收入
							jcAmt += tempVo.getTjAmt();
							jcQuan += tempVo.getTjQuan();
							
							billDtl.setSrQuan(tempVo.getTjQuan());			//输入数量
							billDtl.setSrAmt(tempVo.getTjAmt());			//收入金额
						}else if(tempVo.getTjEuDirect().equals("-1")){	//支出
							jcAmt -= tempVo.getTjAmt();
							jcQuan -= tempVo.getTjQuan();
							
							billDtl.setZcQuan(tempVo.getTjQuan());			//支出数量
							billDtl.setZcAmt(tempVo.getTjAmt());			//支出金额
						}
					}
					
					billDtl.setDateDtl(tempVo.getDateDtl());	//日期
					billDtl.setJcQuan(jcQuan);					//结存数量
					billDtl.setJcAmt(jcAmt);					//结存金额
					
					/**添加到结果集*/
					billDtlsList.add(billDtl);
				}
				
			}else if(paramMap.get("euStatType").equals("1")){
				
				/**过滤查询出的信息：分为 初期、收入、支出、结存*/
				for(BdPdQuyBillDtlsVo tempVo : quyList){
					BdPdBillDtlsVo billDtl = new BdPdBillDtlsVo(); 
					
					if(CommonUtils.isEmptyString(tempVo.getCodeSt()) &&
							CommonUtils.isEmptyString(tempVo.getCodeRep())){		//初期
						billDtl.setSttypeName("初期结存");	//摘要
						
						jcQuan = tempVo.getCqQuan();
						jcAmt = tempVo.getCqAmount();
					}else if(!CommonUtils.isEmptyString(tempVo.getCodeSt())){		//收支
						billDtl.setCodeSt(tempVo.getCodeSt());				//单据编号
						billDtl.setSttypeName(tempVo.getSttypeName());		//摘要
						
						if(tempVo.getEuDirect().equals("1")){			//收入
							jcQuan += tempVo.getStQuan();
							jcAmt += tempVo.getStAmount();
							
							billDtl.setSrQuan(tempVo.getStQuan());			//收入数量
							billDtl.setSrAmt(tempVo.getStAmount());			//收入金额
						}else if(tempVo.getEuDirect().equals("-1")){	//支出
							jcQuan -= tempVo.getStQuan();
							jcAmt -= tempVo.getStAmount();
							
							billDtl.setZcQuan(tempVo.getStQuan());			//支出数量
							billDtl.setZcAmt(tempVo.getStAmount());			//支出金额
						}
					} else if(!CommonUtils.isEmptyString(tempVo.getCodeRep())){		//调价
						billDtl.setCodeSt(tempVo.getCodeRep());				//单据编号
						billDtl.setSttypeName(tempVo.getReptypeName());		//摘要
						
						/**根据调价金额计算结存(增值算收入，减值算支出)*/
						if(tempVo.getTjEuDirect().equals("1")){			//收入
							jcAmt += tempVo.getTjAmt();
							jcQuan += tempVo.getTjQuan();
							
							billDtl.setSrQuan(tempVo.getTjQuan());			//输入数量
							billDtl.setSrAmt(tempVo.getTjAmt());			//收入金额
						}else if(tempVo.getTjEuDirect().equals("-1")){	//支出
							jcAmt -= tempVo.getTjAmt();
							jcQuan -= tempVo.getTjQuan();
							
							billDtl.setZcQuan(tempVo.getTjQuan());			//支出数量
							billDtl.setZcAmt(tempVo.getTjAmt());			//支出金额
						}
					}
					
					billDtl.setDateDtl(tempVo.getDateDtl());	//日期
					billDtl.setJcQuan(jcQuan);					//结存数量
					billDtl.setJcAmt(jcAmt);					//结存金额
					
					/**添加到结果集*/
					billDtlsList.add(billDtl);
				}
			
			}
		}
		
		return billDtlsList;
	}
	
	/**
	 * 根据财务月份获取对应的开始时间和结束时间
	 * @param monBegin
	 * @param monEnd
	 * @return
	 */
	private Map<String,String> getDateByMonthFin(String monBegin,String monEnd){
		//存放查询到的开始时间和结束时间
		Map<String,String> dateMap = new HashMap<String, String>();
		
		//格式化查询到的时间
		SimpleDateFormat sbf = new SimpleDateFormat("yyyyMMddHHmmss");		
		
		/**查询开始时间*/
		//截取开始财务月份(例：201805)
		String mBegin = monBegin.substring(0, 6);
		List<Map<String,Object>> qryDateBegin = DataBaseHelper.queryForList("select date_begin from pd_cc where month_fin=?", Long.valueOf(mBegin));
		
		if(qryDateBegin!=null && qryDateBegin.size()>0)
			dateMap.put("dateBegin", sbf.format(qryDateBegin.get(0).get("dateBegin")));
		else
			throw new BusException("您录入的开始财务月份无结账记录！");
		
		/**查询结束时间*/
		//截取结束财务月份(例：201807)
		String mEnd = monEnd.substring(0, 6);
		List<Map<String,Object>> qryDateEnd = DataBaseHelper
				.queryForList("select date_end from pd_cc where month_fin=?", Long.valueOf(mEnd));
		
		if(qryDateEnd!=null && qryDateEnd.size()>0)
			dateMap.put("dateEnd", sbf.format(qryDateEnd.get(0).get("dateEnd")));
		else
			throw new BusException("您录入的结束财务月份无结账记录！");
		
		return dateMap;
	}
	

	/**
	 * 交易号：008007018008
	 * 查询收发存汇总信息
	 * @param param
	 * @param user
	 * @return
	 */
	public List<MtlTransSumVo> queryMtlTransSum(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		//获取当前仓库信息并添加到paramMap
		paramMap.put("pkStoreSt", UserContext.getUser().getPkStore());
		
		/**检索起止财务月份的开始日期和结束日期(1.开始日期为开始财务月份下的开始日期   2.结束日期为结束财务月份下的结束日期)*/
		String monBegin = paramMap.get("monBegin").toString();
		String monEnd = paramMap.get("monEnd").toString();
		//System.out.println("monBegin:"+monBegin);
		//System.out.println("monEnd:"+monEnd);
		if(!CommonUtils.isEmptyString(monBegin) && !CommonUtils.isEmptyString(monEnd)){
			 //获取起止财务月份的开始日期和结束日期
			 Map<String,String> dateMap = getDateByMonthFin(monBegin,monEnd);
			 //给声明的dateBegin、dateBegin字段赋值
			 if(dateMap!=null && dateMap.size()>0){
				/**把开始时间和结束时间添加到查询参数*/
				//System.out.println("dateEnd:"+dateMap.get("dateEnd"));
				//System.out.println("dateBegin:"+dateMap.get("dateBegin"));
				paramMap.put("dateEnd", dateMap.get("dateEnd"));
				paramMap.put("dateBegin", dateMap.get("dateBegin"));
			 }
		}
		
		if(!CommonUtils.isEmptyString(paramMap.get("calMonBegin").toString())){
			paramMap.put("calMonBegin", paramMap.get("calMonBegin").toString().substring(0, 6));
			//System.out.println("calMonBegin:"+paramMap.get("calMonBegin"));
		}
	
		
		//存放查询出的结果集
		List<MtlTransSumVo> rtnist = new ArrayList<>();
		
		rtnist = mtlQueryStatMapper.queryMtlTransSum(paramMap);
		
		if(rtnist!=null && rtnist.size()>0){
			for(MtlTransSumVo item:rtnist){
				/**根据调价金额计算收入\支出(增值算收入，减值算支出)*/
				if(item.getAmtPrice()!=null){
					if(item.getAmtPrice().doubleValue()>0){
						item.setAmtIn(item.getAmtIn()==null?0:item.getAmtIn()+item.getAmtPrice());
					}
					else if(item.getAmtPrice().doubleValue()<0){
						item.setAmtOut(item.getAmtOut()==null?0:item.getAmtOut()+item.getAmtPrice());
					}
				}
				
				//结存金额 = 期初金额+收入金额-支出金额。
				Double amtBalance = (item.getAmtBegin()==null?0:item.getAmtBegin())+
									(item.getAmtIn()==null?0:item.getAmtIn())-
									(item.getAmtOut()==null?0:item.getAmtOut());
				item.setAmtBalance(amtBalance);
			}
		}
		//System.out.println("rtnist:"+rtnist.size());
		return rtnist;
	}
	
	/**
	 * 三证查询-物品注册证效期
	 * @param param
	 * @return
	 */
	public List<MtlLicensePdVo> queryMtlLicensePd(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		String begin = paramMap.get("beginDate").toString().substring(0, 8)+"000000";
		String end = paramMap.get("endDate").toString().substring(0, 8)+"235959";
		paramMap.put("beginDate", begin);
		paramMap.put("endDate", end);
		//System.out.println("beginDate:"+paramMap.get("beginDate"));
		//System.out.println("endDate:"+paramMap.get("endDate"));
		
		List<MtlLicensePdVo> list = mtlQueryStatMapper.queryMtlLicensePd(paramMap);
		
		return list;
	}
	
	/**
	 * 三证查询-供应商经营效期和许可效期
	 * @param param
	 * @return
	 */
	public List<MtlLicenseSupVo> queryMtlLicenseSup(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		
		List<MtlLicenseSupVo> list = mtlQueryStatMapper.queryMtlLicenseSup(paramMap);
		
		return list;
	}

	/**
	 * 查询科室物品领退记录
	 * @param param
	 * @return
	 */
	public List<MtlDeptPdStVo> queryDeptPdSt(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("pkDept", UserContext.getUser().getPkDept());
		String begin = paramMap.get("beginDate").toString().substring(0, 8)+"000000";
		String end = paramMap.get("endDate").toString().substring(0, 8)+"235959";
		paramMap.put("beginDate", begin);
		paramMap.put("endDate", end);
		//System.out.println("sttype"+paramMap.get("dtSttype").toString());
		List<MtlDeptPdStVo> list = mtlQueryStatMapper.queryDeptPdSt(paramMap);
		
		return list;
	}
}
