package com.zebone.platform.manage.transcode.service.impl;

import com.zebone.nhis.common.module.base.transcode.SysLog;
import com.zebone.nhis.common.module.base.transcode.SysServiceRegister;
import com.zebone.nhis.common.module.base.transcode.SysTestCase;
import com.zebone.nhis.common.module.base.transcode.TransCode;
import com.zebone.nhis.common.support.RedisUtils;
import com.zebone.platform.manage.transcode.dao.TransMapper;
import com.zebone.platform.manage.transcode.service.TranscodeService;
import com.zebone.platform.manage.transcode.vo.QueryForm;
import com.zebone.platform.manage.transcode.vo.SysLogQueryForm;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.support.Page;
import com.zebone.platform.modules.exception.BusException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("transcodeService")
public class TranscodeServiceImpl implements TranscodeService {
	
	@Resource
	private TransMapper transMapper;
	
	@Override
	public Page<?> getTranscodePage(int pageIndex, int pageSize, QueryForm queform){
		String sql = "SELECT trans_code as transcode,name,address,remark,type,condition,inputformat,outputformat,zt FROM sys_service_register where 1=1";
				/*+ queform.getTranscode() + "'";*/
		if(queform.getTranscode()!=""&&queform.getTranscode()!=null){
			sql += " and trans_code like '"+queform.getTranscode()+"%'";
		}
		if(queform.getName()!=""&&queform.getName()!=null){
			sql += " and name like '%"+queform.getName()+"%'";
		}
		if(queform.getZt()!=""&&queform.getZt()!=null){
			if("all".equals(queform.getZt().toString())){
				
			}else{
				sql += " and zt = '"+queform.getZt()+"'";
			}
		}
		
		sql += " order by trans_code";
		
		Page<?> page = DataBaseHelper.queryForPage(sql, pageIndex, pageSize);
		return page;
	}
	
	@Override
	public List<Map<String, Object>> fuzzySearchTranscodes(SysServiceRegister sysregister) {
		String sql = "SELECT trans_code as transcode,name,address,remark,type,condition,inputformat,outputformat,zt FROM sys_service_register where trans_code like '"
				+ sysregister.getTranscode() + "%' order by trans_code";
		List<Map<String, Object>> sysregisterlist = DataBaseHelper.queryForList(sql);
		return sysregisterlist;
	}

	@Override
	public void saveTranscode(SysServiceRegister sysregister) {
		RedisUtils.delCacheHashObj("sys:trancode", sysregister.getTranscode());
		DataBaseHelper.insertBean(sysregister);
	}
	

	@Override
	public void updateTranscode(SysServiceRegister sysregister) {
		RedisUtils.delCacheHashObj("sys:trancode", sysregister.getTranscode());
		DataBaseHelper.updateBeanByPk(sysregister);
		
	}
	
	@Override
	public void delTranscode(List<SysServiceRegister> registerlist) {
		for(int i=0;i<registerlist.size();i++){
			RedisUtils.delCacheHashObj("sys:trancode", registerlist.get(i).getTranscode());
			DataBaseHelper.deleteBeanByPk(registerlist.get(i));
		}
	}
	
	@Override
	public Map<String, Object> getTranscode(SysServiceRegister sysregister) {
		String sql = "select trans_code as transcode,name,address,remark,type,condition,inputformat,outputformat,zt from sys_service_register where trans_code = ?";
		Map<String, Object> map = DataBaseHelper.queryForMap(sql, sysregister.getTranscode());
		return map;
	}
	
	@Override
	public List<Map<String, Object>> gettranscodeSet() {
		List<Map<String, Object>> transList = getTrans_codes(null);
		Map<String ,Object> stub=new HashMap<String ,Object>();
		stub.put("id", "0");
		stub.put("text", "交易码字典");
		transList.add(stub);
		return transList;
	}
	
	@Override
	public List<Map<String, Object>> getRelativeProxyNames(TransCode transCode) {
		List<Map<String, Object>> transList = new ArrayList<Map<String, Object>>();
		if("".equals(transCode.getPid())){
			//没有选中第三层叶子节点就加载代理
			String sql = "select id,text,pid,proxyname from sys_service_trans_code c where length(c.id) <= 6 and c.proxyname is not null";
			transList =  DataBaseHelper.queryForList(sql);
		}else{
			//选中第三层叶子节点加载代理
			String sql = "select id,text,pid,proxyname from sys_service_trans_code c where length(c.id) <= 6 and c.id = ?";
			Map<String, Object> ptrans = DataBaseHelper.queryForMap(sql, transCode.getPid());
			Map<String, Object> pptrans = pptrans = DataBaseHelper.queryForMap(sql, ptrans.get("pid").toString());
			transList.add(pptrans);
			if(ptrans.get("proxyname") != null){
				transList.add(ptrans);
			}
		}
		//更换id 和 pid的key
		for(Map<String, Object> code : transList){
			code.put("proid", code.get("id").toString());
			code.put("parentid", code.get("pid").toString());
			code.remove("id");
			code.remove("pid");
		}
		return transList;
	}

	@Override
	public Map<String, Object> getSelfProxy(TransCode transCode){
		Map<String, Object> proxycode = new HashMap<String, Object>();
		if("".equals(transCode.getPid())){
			//没有选中第三层叶子节点就加载代理
			String sql = "select * from ("
					+ "select c.id,c.text,c.pid,c.proxyname from sys_service_trans_code c where length(c.id)<=6 and c.proxyname is not null order by c.id"
					+ ") where rownum = 1";
			proxycode =  DataBaseHelper.queryForMap(sql);
		}else{
			//选中第三层叶子节点加载代理
			String sql = "select id,text,pid,proxyname from sys_service_trans_code where id = ?";
			proxycode =  DataBaseHelper.queryForMap(sql, transCode.getPid());
			if(proxycode.get("proxyname") == null){
				proxycode =  DataBaseHelper.queryForMap(sql, proxycode.get("pid").toString());
			}
		}
		return proxycode;
	}
	
	@Override
	public void saveProxyNames(TransCode transCode){
		DataBaseHelper.insertBean(transCode);
	}
	
	@Override
	public void updateProxyNames(TransCode transCode){
		DataBaseHelper.updateBeanByPk(transCode);
	}
	
	@Override
	public List<Map<String, Object>> loadAllProxyNames(){
		String where = " c where length(c.id) <= 6";
		List<Map<String, Object>> transList = getTrans_codes(where);
		return transList;
	}
	
	private List<Map<String, Object>> getTrans_codes(String where) {
		String sql = "select id,text,pid,proxyname from sys_service_trans_code ";
		if(where!=null){
			sql += where;
		}
		sql+=" order by id";
		List<Map<String, Object>> transList = DataBaseHelper.queryForList(sql);
		return transList;
	}

	@Override
	public List<Map<String, Object>> gettranscodeSetNoTwoLevelLeaf(){
		//有子节点的节点
		String sql = "select a.id as id,a.text as text,a.pid as pid from sys_service_trans_code a where exists (select b.pid from sys_service_trans_code b where b.pid = a.id) ";

		sql += "union ";

		//第三层叶子节点
		sql += "select c.id as id,c.text as text,c.pid as pid from sys_service_trans_code c where c.id not in ("
		+ "select a.id from sys_service_trans_code a where exists (select b.pid from sys_service_trans_code b where b.pid = a.id)"
		+ ") and length(c.id) = 9";
		
		List<Map<String, Object>> transList = DataBaseHelper.queryForList(sql);
		//检查根节点下是否有子节点（第二层）
		List<Integer> delindexArr = new ArrayList<Integer>();//保存需要舍去的根节点索引
		List<Map<String, Object>> dellist = new ArrayList<Map<String, Object>>();
		for(int i=0;i<transList.size();i++){
			if("0".equals(transList.get(i).get("pid").toString())){
				Map<String, Object> root = new HashMap<String, Object>();
				List<Map<String, Object>> levellist = new ArrayList<Map<String, Object>>();//同层级组装的list
				
				root=transList.get(i);//先找到根节点
				String rootid = (String) root.get("id");
				//寻找所有根节点,把根节点的pid去掉，让前台可以识别
				root.remove("pid");
				for(int j=0;j<transList.size();j++){
					String leafpid = (String) transList.get(j).get("pid");
					if(rootid.equals(leafpid)){
						levellist.add(transList.get(j));
					}
				}
				//没有（第二层）子节点的根节点
				if(levellist.size()==0){
					delindexArr.add(i);
					dellist.add(root);
				}
			}
		}
		//没有第二层子节点的根节点舍去
		Iterator<Map<String, Object>> tr = transList.iterator();
		while(tr.hasNext()){
			Map<String, Object> x = tr.next();
			for(Map<String, Object> del:dellist){
				String tid = (String) x.get("id");
				String did = (String) del.get("id");
			    if(tid.equals(did)){
			        tr.remove();
			    }
			}
		}
		
		return transList;
	}
	//TODO 条件查询根节点必须是以0开头得交易码，不是的话加载不出来
	@Override
	public List<Map<String, Object>> gettranscodeSetByTj(String id) {
		String sql="";
		int length=0;
		if(id.length()<3){
			length=3;
		}else if(id.length()==3){
			length=6;
		}else if(id.length()==6){
			length=9;
		}
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(id.length()>6){
			sql="select id,text,pid,cjsj from sys_service_trans_code where id ='"+id+"' order by id";
		}else{
			sql="select *  from sys_service_trans_code where length(id)='"+length+"' and id like '"+id+"%' order by id";
		}
		List<Map<String, Object>> getNeedLst = DataBaseHelper.queryForList(sql);
		for(int i=0;i<getNeedLst.size();i++){
			Map<String, Object> check=getNeedLst.get(i);
			if(check.get("cjsj")==null){
				check.put("cjsj", sdf.format(new Date()));
			}else{
				check.put("cjsj", sdf.format(check.get("cjsj")));
			}
		}
		return getNeedLst;
	}


	@Override
	public void saveTransCodeModul(List<TransCode> tr) {
		DataBaseHelper.insertBean(tr);
	}

	@Override
	public boolean delTransCodeModul(List<TransCode> id) {
		boolean delYes=true;
		for(int i=0;i<id.size();i++){
			String sql="select count(*) from sys_service_trans_code where pid="+id.get(i).getId();
			int ca= DataBaseHelper.getJdbcTemplate().queryForObject(sql,Integer.class);
			System.out.println("--------------------"+ca);
			if(ca>0){
				delYes=false;
			}else if(ca==0){
				DataBaseHelper.deleteBeanByPk(id.get(i));
			}
		}
		return delYes;
	}

	@Override
	public void changeTransCodeModul(List<TransCode> tr) {
		for(int i=0;i<tr.size();i++){
			String sqlJudge="select id,text,pid,cjsj from sys_service_trans_code where id='"+tr.get(i).getId()+"'";
			List<Map<String, Object>> checkSql= DataBaseHelper.queryForList(sqlJudge);
			if(checkSql.size()==0){
				DataBaseHelper.insertBean(tr.get(i));
			}else{
				DataBaseHelper.updateBeanByPk(tr.get(i));
			}
		}
	}

	@Override
	public Map<String, Object> gettranscodeSetById(TransCode transcode) {
		String sql = "select id,text,pid from sys_service_trans_code where id=?";
		Map<String, Object> transco = DataBaseHelper.queryForMap(sql, transcode.getId());
		return transco;
	}

	@Override
	public List<Map<String, Object>> getTransCodeSetByText(String text) {
		String sql="select * from sys_service_trans_code where text like '%"+text.substring(1, text.length()-1)+"%'";
		List<Map<String, Object>> transList = DataBaseHelper.queryForList(sql);
		for(int i=0;i<transList.size();i++){
			Map<String, Object> ss=transList.get(i);
			if("0".equals(ss.get("pid"))){
				ss.remove("pid");
			}
		}
		return transList;
	}

	@Override
	public List<Map<String, Object>> getTestCasesByTranscode(SysServiceRegister sysregister) {
		String sql = "select id,trans_code,rcsj,yhm,ylmc,pk_org,creator,create_time,del_flag,ts "
				+ "from sys_service_test_case where trans_code = ? order by ts desc";
		List<Map<String, Object>> caselist = DataBaseHelper.queryForList(sql,sysregister.getTranscode());
		return caselist;
	}
	

	@Override
	public String addCaseYlmc(SysTestCase cas) {
		
		DataBaseHelper.insertBean(cas);
		String sql = "select * "
				+ "from ("
				+ "select id,trans_code,rcsj,yhm,ylmc,pk_org,creator,create_time,del_flag,ts "
				+ "from sys_service_test_case where trans_code = ? order by ts desc"
				+ ") where rownum = 1";
		Map<String, Object> recas = DataBaseHelper.queryForMap(sql, cas.getTransCode());
		return recas.get("id").toString();
	}

	@Override
	public String updateCaseYlmc(SysTestCase cas) {
		DataBaseHelper.updateBeanByPk(cas,false);
		return cas.getId();
	}

	@Override
	public void delTestCase(SysTestCase cas) {
		
		DataBaseHelper.deleteBeanByPk(cas);
		
	}

	@Override
	public void updateCaseRcsj(SysTestCase cas) {
		/*if(cas.getYhm()==null || "".equals(cas.getYhm())){
			cas.setYhm("admin");
		}*/
		DataBaseHelper.updateBeanByPk(cas,false);
	}

	@Override
	public void addCaseRcsj(SysTestCase cas) {
		DataBaseHelper.insertBean(cas);
	}

	@Override
	public void resetRegisterProxy() {
		/**
		 * 第一步：查询
		 */
		List<Map> rpcList = transMapper.queryRpcList();
		
		/**打印显示
		System.out.println("trans_code    pproxy  proxy    address");
		for(Map<String, Object> transcode : rpcList){
			System.out.println(transcode.get("transCode").toString()+"  "+transcode.get("pproxy")+"     "+transcode.get("proxy")+"   "+transcode.get("address"));
		}*/
		
		
		List<Map> proxyList = transMapper.queryProxyRange();
		
		/**打印显示
		System.out.println("id       proxyname       pid");
		for(Map<String, Object> proxy : proxyList){
			if(proxy.get("proxyname") != null){
				System.out.println(proxy.get("id").toString()+"  "+proxy.get("proxyname").toString()+"     "+proxy.get("pid").toString());
			}else{
				System.out.println(proxy.get("id").toString()+"                      "+proxy.get("pid").toString());
			}
		}*/
		
		
		/**
		 * 第二步：准备数据
		 */
		//System.out.println("trans_code    pproxy  proxy    newAddress");
		for(Map transcode : rpcList){
			String proxyid = transcode.get("proxy").toString();
			String proxypid = transcode.get("pproxy").toString();
			String address = transcode.get("address").toString();
			String newAddress = address;
			
			/**对每一条交易号做重置变动*/
			String proxyName = null;//重置的代理
			proxyName = produceProxy(proxyid, proxyList);
			if(proxyName == null){
				proxyName = produceProxy(proxypid, proxyList);
			}
			if(proxyName == null){
				throw new BusException(transcode.get("transCode").toString()+"接口代理不存在！");
			}else{
				String wOr = address.substring(0,1);// W or R
				String leftStr = address.substring(address.indexOf("/"),address.length());// '/'及右侧字符串
				newAddress = wOr + proxyName + "15" + leftStr;
				transcode.put("newaddress", newAddress);
			}
			//System.out.println(transcode.get("transCode").toString()+"  "+transcode.get("pproxy")+"     "+transcode.get("proxy")+"   "+newAddress);
			
		}
		
		
		/**
		 * 第三步：批量更新
		 */
		for(Map transcode : rpcList){
			DataBaseHelper.update("update sys_service_register set address = ? where trans_code = ?", transcode.get("newaddress").toString(), transcode.get("transCode").toString());
		}
	}

	/**
	 * 生成重置后的代理名，即proxyid对应的实际代理
	 * @param proxyid    代理id
	 * @param proxyList  整个代理范围
	 * @return
	 */
	private String produceProxy(String proxyid, List<Map> proxyList){
		String proxyName = null;
		for(int i=0;i<proxyList.size();i++){
			Map<String, Object> proxy1 = proxyList.get(i);
			if(proxyid.equals(proxy1.get("id")) && proxy1.get("proxyname") != null){
				proxyName = proxy1.get("proxyname").toString();
			}
		}
		return proxyName;
	}

	/****************************系统日志  sys_log  部分********************************/
	@Override
	public Page<?> getSysLogPage(int pageIndex, int pageSize, SysLogQueryForm queform) {
		String sql = "SELECT l.pk_log as pklog,l.trans_code as transcode,l.trans_name as transname,l.type,l.exec_time as exectime,u.NAME_EMP as creator,l.create_time as createtime FROM sys_log l left join bd_ou_employee u on u.PK_EMP = l.creator where 1=1";
		/*+ queform.getTranscode() + "'";*/
		if(queform.getTranscode() !="" && queform.getTranscode() != null){
			sql += " and l.trans_code like '"+queform.getTranscode()+"%'";
		}
		if(queform.getType() != "" && queform.getType() != null){
			if("9".equals(queform.getType().toString())){
				
			}else{
				sql += " and l.type = '"+queform.getType()+"'";
			}
		}
		if(queform.getBegintime() != "" && queform.getBegintime() != null){
			String begintime = queform.getBegintime();
			begintime = begintime.replace("T", " ");
			sql += " and l.create_time >= to_date('"+ begintime +"','yyyy-MM-dd HH24:mi:ss')";
		}
		if(queform.getEndtime() != "" && queform.getEndtime() != null){
			String endtime = queform.getEndtime();
			endtime = endtime.replace("T", " ");
			sql += " and l.create_time <= to_date('"+ endtime +"','yyyy-MM-dd HH24:mi:ss')";
		}
		sql += " order by l.create_time desc";
		
		Page<?> page = DataBaseHelper.queryForPage(sql, pageIndex, pageSize);
		return page;
	}

	@Override
	public Map<String, Object> getSysLogByPklog(SysLog syslog) {
		Map<String, Object> queryForMap = DataBaseHelper.queryForMap("select l.pk_log as pklog,"
				+ "l.trans_code as transcode,"
				+ "l.type,"
				+ "r.address,"
				+ "l.in_param as inparam,"
				+ "l.out_param as outparam,"
				+ "l.creator,"
				+ "l.create_time as createtime,"
				+ "l.exec_time as exectime,"
				+ "l.trans_name as transname,"
				+ "u.NAME_EMP as creator"
				+ " from sys_log l left join bd_ou_employee u on u.PK_EMP = l.creator "
				+ "left join sys_service_register r on r .trans_code = l.trans_code where l.pk_log = ?", syslog.getPklog());
		return queryForMap;
	}
}
