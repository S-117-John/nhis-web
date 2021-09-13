package com.zebone.nhis.pro.zsba.compay.other.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.pro.zsba.compay.other.dao.DrugOptMapper;
import com.zebone.nhis.pro.zsba.compay.other.vo.DrugOptConfig;
import com.zebone.nhis.pro.zsba.compay.other.vo.DrugOptEmp;
import com.zebone.nhis.pro.zsba.compay.other.vo.DrugOptResult;
import com.zebone.nhis.pro.zsba.compay.other.vo.DrugOptVote;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 投票服务
 * @author 85102
 *
 */
@Service
public class DrugOptService {


	@Autowired
	private DrugOptMapper drugOptMapper;
	
	/**
	 * 1-1、随机抽取固定人数的名单
	 * @param param
	 * @param user
	 * @return
	 */
	public List<DrugOptEmp> qryEmpListByCnt(String param,IUser user){
		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		List<DrugOptEmp> list = new ArrayList<DrugOptEmp>();
		if(null == params || !params.containsKey("cnt"))
			throw new BusException("请传入 待随机抽取的人数！");
		String cnt =  CommonUtils.getString(params.get("cnt"), "") ;
		if(CommonUtils.isEmptyString(cnt))
			throw new BusException("请传入 待随机抽取的人数！");
		String type = CommonUtils.getString(params.get("type"), "");
		list = DataBaseHelper.queryForList("select top "+ cnt +" * from drug_opt_emp where flag_choose = '0' "
				+ (!CommonUtils.isEmptyString(type) ? " and eu_type = '"+type+"' " : "")
				+ " order by newid()",DrugOptEmp.class);
		
		//2、批量设置当前选中人员记录
		String pkList = "";
		for (DrugOptEmp drugOptEmp : list) {
			drugOptEmp.setFlagChoose("1");
			pkList += "'" + drugOptEmp.getPkEmp() + "',";
		}
		pkList = pkList.substring(0,pkList.length() - 1);
		DataBaseHelper.execute("update drug_opt_emp set flag_choose = '1' where pk_emp in ("+pkList+")", null);

		return list;
	}
	
	 /**
     * 1-2、确定到场人员名单【更新eu_join】
     * @param param
     * @param user
     */
    public List<DrugOptEmp> saveEmpList(String param,IUser user){
    	List<DrugOptEmp> empList = JsonUtil.readValue(param,new TypeReference<List<DrugOptEmp>>(){});
    	if(null == empList || empList.size() < 1)
			throw new BusException("请传入 待确认的人员名单！");
    	
    	//1、更新传入的到场人员名单
    	DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(DrugOptEmp.class),empList);
    	//2、回置未到场人员名单
    	String pkList = "";
		for (DrugOptEmp drugOptEmp : empList) 
			pkList += "'" + drugOptEmp.getPkEmp() + "',";
		
		pkList = pkList.substring(0,pkList.length() - 1);
		DataBaseHelper.execute("update drug_opt_emp set eu_join = '0' where pk_emp not in ("+pkList+")", null);
		
    	//2、返回最新的确定到场人员名单
    	List<DrugOptEmp> list = DataBaseHelper.queryForList("select * from drug_opt_emp where flag_choose = '1' and eu_join = '1'",DrugOptEmp.class);
    	return list;
    }
    
    /**
     * 1-3、清理到场人员名单【还原 参与标志、到场标志】
     * @param param
     * @param user
     */
    public void delEmpList(String param,IUser user){
    	List<DrugOptEmp> empList = JsonUtil.readValue(param,new TypeReference<List<DrugOptEmp>>(){});
    	if(null == empList || empList.size() < 1)
			throw new BusException("请传入 待清理的人员名单！");
    	
    	String pkList = "";
		for (DrugOptEmp drugOptEmp : empList) 
			pkList += "'" + drugOptEmp.getPkEmp() + "',";
		
		pkList = pkList.substring(0,pkList.length() - 1);
		DataBaseHelper.execute("update drug_opt_emp set flag_choose = '0' ,eu_join = '0' where pk_emp in ("+pkList+")", null);
    }
    
    /**
     * 2-1、设置投票参数
     * @param param
     * @param user
     */
    public List<DrugOptConfig> saveOptConf(String param,IUser user){
    	List<DrugOptConfig> confList = JsonUtil.readValue(param,new TypeReference<List<DrugOptConfig>>(){});
    	if(null == confList || confList.size() < 1)
			throw new BusException("请传入 待保存的投票参数！");
    	
    	//1、删除原来的参数
    	DataBaseHelper.execute("update drug_opt_config set del_flag = '1' ", null);
    	
    	//2、重新插入有效的参数
    	for (DrugOptConfig drugOptConfig : confList) {
    		drugOptConfig.setPkConfig(NHISUUID.getKeyId());
		}
    	DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(DrugOptConfig.class), confList);
    	
    	//3、查询最新的返回
    	List<DrugOptConfig> confNewList = DataBaseHelper.queryForList("select * from drug_opt_config where del_flag = '0' ",DrugOptConfig.class, new Object[]{});
    	return confNewList;
    }

    /**
     * 2-2、查询投票明细
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> qryOptVote(String param,IUser user){
		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    	list = qryVoteDetail(params);
    	return list;
    }

    /**
     * 查询投票明细【先查询是否有二次，再查询一次投票】
     * @param params
     * @return
     */
	private List<Map<String, Object>> qryVoteDetail(Map<String, Object> params) {
		List<Map<String, Object>> list;
		//查询是否有二次投票记录
    	list = drugOptMapper.qryOptVoteSecond(params);
    	
    	//查询首次投票记录
    	if(null == list || list.size() < 1)
    		list = drugOptMapper.qryOptVoteS(params);
		return list;
	}
    
    /**
     * 2-3、打印投票【单个/多个】
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> saveOptVotePrt(String param,IUser user){
		Map<String,Object> params = JsonUtil.readValue(param, Map.class);
		String pkEmp = CommonUtils.getString( null != params ? params.get("pkEmp") : "", "");
		String flagSecond = CommonUtils.getString( null != params ? params.get("flagSecond") : "", "");

		//1、更新投票的打印结果
    	DataBaseHelper.execute("update drug_opt_vote set flag_print = '1' where del_flag = '0' "
    			+ (!CommonUtils.isEmptyString(pkEmp) ?  "and pk_emp = '"+pkEmp+"' " : "")
    			+ (!CommonUtils.isEmptyString(flagSecond) ?  "and flag_second = '"+flagSecond+"' " : ""), null);
    	
    	//2、返回最新的票选记录
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    	list = drugOptMapper.qryOptVoteS(!CommonUtils.isEmptyString(pkEmp) ?  params : null);
    	return list;
    }
     
    /**
     * 4-1、提交 投票明细
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> saveOptVote(String param,IUser user){
    	List<DrugOptVote> voteList = JsonUtil.readValue(param,new TypeReference<List<DrugOptVote>>(){});
    	if(null == voteList || voteList.size() < 1)
			throw new BusException("请传入 待保存的投票数据！");
    	String pkEmp = voteList.get(0).getPkEmp();
    	if(!CommonUtils.isEmptyString(voteList.get(0).getPkVote())){
    		DataBaseHelper.batchUpdate(DataBaseHelper.getUpdateSql(DrugOptVote.class), voteList);
    	}else{
    		//1、清理上一次投票明细
    		DataBaseHelper.execute("update drug_opt_vote set del_flag = '1' "
    				+ " where del_flag = '0' and pk_emp = '"+ pkEmp +"'  "
    				+ ("1".equals(voteList.get(0).getFlagSecond()) ? "and flag_second = '1'": ""), null);
    		
    		//2、单个插入投票数据
    		for (DrugOptVote drugOptVote : voteList) {
    			drugOptVote.setPkVote(NHISUUID.getKeyId());
    		}
    		DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(DrugOptVote.class), voteList);
    	}
    	
    	//3、查询返回最新的投票数据
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("pkEmp", pkEmp);
    	List<Map<String,Object>> list = qryVoteDetail(params);
    	return list;
    }
    
    /**
     * 4-2、取消提交 投票明细
     * @param param
     * @param user
     * @return
     */
    public void delOptVote(String param,IUser user){
    	List<DrugOptVote> voteList = JsonUtil.readValue(param,new TypeReference<List<DrugOptVote>>(){});
    	if(null == voteList || voteList.size() < 1)
			throw new BusException("请传入待 取消提交 的投票数据！");
    	
    	//1、校验当前结果是否已经打印，已打印则不可取消提交
    	int cnt = DataBaseHelper.queryForScalar("select count(*) from drug_opt_vote where del_flag = '0' "
    			+ "and flag_print = '1' and pk_emp = ?  ", Integer.class, new Object[]{voteList.get(0).getPkEmp()});
    	if(cnt > 0)
			throw new BusException("当前投票数据，已打印，不可取消提交！");
    	
    	//2、校验是否全部数据都删除
    	int cntD = DataBaseHelper.execute("update drug_opt_vote set del_flag = '0' where del_flag = '0' "
    			+ "and flag_print = '0' and pk_emp = ?  ", Integer.class, new Object[]{voteList.get(0).getPkEmp()});
    	if(cntD != voteList.size())
			throw new BusException("当前投票数据，取消提交 失败！");
    }

    /**
     * 3-1、查看投票结果
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> qryOptResult(String param,IUser user){
		Map<String,Object> params = new HashMap<String,Object>();
		if(null != param && !CommonUtils.isEmptyString(param))
			params = JsonUtil.readValue(param, Map.class);
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    	
    	//1、先查看是否存在最终结果
    	list = drugOptMapper.qryOptResultEnd();
    	
    	//2、若不存在最终结果，则先查询预测的结果
    	if(null == list || list.size() < 1)
    		list = drugOptMapper.qryOptResult(params);
    	
    	//3、若存在二次投票记录，则拼接二次投票数据
    	if(null != list && list.size() > 0 ){
    		boolean flagSecond = false;
    		for (Map<String, Object> map : list) {
				if("1".equals((CommonUtils.getString(map.get("flagSecond"), "")))){
					flagSecond = true;
					break;
				}
			}
    		if(!flagSecond)  return list;
    		
    		List<DrugOptResult> list2 = drugOptMapper.qryOptResult2(params); 
    		String pkPd = "";
    		String flagS = "";
    		if(null != list2 && list2.size() > 0){
    			for (Map<String,Object> mapr : list) {
    				flagS = CommonUtils.getString(mapr.get("flagSecond"), "");
    				pkPd = CommonUtils.getString(mapr.get("pkPd"), "");
    				if( "1".equals(flagS) && !CommonUtils.isEmptyString(pkPd)){
    					for (DrugOptResult rs2 : list2) {
    						if(pkPd.equals(rs2.getPkPd())){
    							mapr.remove("quanSecond");
    							mapr.put("quanSecond", rs2.getQuanSecond());
    							break;
    						}
    					}
    				}
				}
    		}
    	}
    	
    	return list;
    }
    
    /**
     * 3-2、保存投票结果
     * @param param
     * @param user
     * @return
     */
    public List<Map<String,Object>> saveOptResult(String param,IUser user){
    	List<DrugOptResult> rsList = JsonUtil.readValue(param,new TypeReference<List<DrugOptResult>>(){});
    	if(null == rsList )
			throw new BusException("请传入 待保存的投票结果！");
    	
    	//1、删除原来的结果
    	DataBaseHelper.execute("update drug_opt_result set del_flag = '1' ", null);
    	
    	//2、设置结果主键，插入结果
    	for (DrugOptResult rs : rsList) {
    		rs.setPkResult(NHISUUID.getKeyId());
    		if("1".equals(rs.getFlagPass()) && "1".equals(rs.getFlagSecond()))
    			rs.setPkResultSecond(NHISUUID.getKeyId());
		}
    	DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(DrugOptResult.class), rsList);
    	
    	//3、查询最终的结果
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    	list = drugOptMapper.qryOptResultEnd();
    	return list;
    }
    
    /**
     * 3-3、清空投票结果
     * @param param
     * @param user
     * @return
     */
    public void delOptResult(String param,IUser user){
    	//1、删除原来的结果
    	DataBaseHelper.execute("update drug_opt_result set del_flag = '1' ", null);
    	
    	//2、删除全部的投票记录
    	DataBaseHelper.execute("update drug_opt_vote set del_flag = '1' ", null);
    }

    /**
     * 3-4、二次投票[原逻辑：清空全部结果，全部重新选择]
     * @param param
     * @param user
     * @return
     
    public void delVotePrt(String param,IUser user){
    	List<DrugOptResult> rsList = JsonUtil.readValue(param,new TypeReference<List<DrugOptResult>>(){});
    	if(null == rsList || rsList.size() < 1)
			throw new BusException("请传入 待二次投票的相关数据！");
    	
    	//1、校验是否设置了二次投票的数据
    	int cnt = 0;
    	for (DrugOptResult rs : rsList) {
    		if("1".equals(rs.getFlagSecond()))
    			cnt++;
		}
    	if(cnt < 1)
    		throw new BusException("请设置 待二次投票的相关数据！");
    	
    	//2、查询待二次投票的数据
    	List<DrugOptVote> list = drugOptMapper.qryOptVoteSecond(null);
    	
    	if(null == list || list.size() < 1)
    		throw new BusException("请先投票！");
    	
    	//3、删除上一次的全部投票数据、包含结果
    	DataBaseHelper.execute("update drug_opt_vote set del_flag = '1' where del_flag = '0' ", null);
    	DataBaseHelper.execute("update drug_opt_result set del_flag = '1' where del_flag = '0' ", null);
    	
    	Date dateO = new Date();
    	//4、插入二次投票的数据
    	for (DrugOptVote vote : list) {
    		for (DrugOptResult rs1 : rsList) {
    			if(vote.getPkPd().equals(rs1.getPkPd()) && "1".equals(rs1.getFlagSecond()))
    				vote.setFlagSecond("1");
			}
    		vote.setDelFlag("0");
    		vote.setCreateTime(dateO);
    		vote.setPkVote(NHISUUID.getKeyId());
		}
    	DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(DrugOptVote.class), list);
    }
 * */

    /**
     * 3-5、设置二次投票【更新结果表中允许二次投票的记录 flag_second】
     * @return
     */
    public List<Map<String,Object>> setSecondVoote(String param,IUser user){
    	List<DrugOptResult> rsList = JsonUtil.readValue(param,new TypeReference<List<DrugOptResult>>(){});
    	if(null == rsList || rsList.size() < 1)
			throw new BusException("请传入 待二次投票的相关数据！");
    	
    	//1、校验是否设置了二次投票的数据
    	String pkResult = "";
    	for (DrugOptResult rs : rsList) {
    		if("1".equals(rs.getFlagSecond())){
    			pkResult += "'"+rs.getPkResult()+"',";
    		}
		}
    	if(!CommonUtils.isEmptyString(pkResult))
    		pkResult = pkResult.substring(0, pkResult.length() - 1);
    	if(CommonUtils.isEmptyString(pkResult))
    		throw new BusException("请设置 待二次投票的相关数据！");
    	
    	//2、回写二次投票标志
    	DataBaseHelper.execute("update drug_opt_result set flag_second = '1' where pk_result in ("+pkResult+")", null);

    	//3、获取最新的数据
    	List<Map<String,Object>> list2 = qryOptResult(null,user);
    	return list2;
    }

}
