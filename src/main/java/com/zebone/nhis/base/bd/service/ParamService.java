package com.zebone.nhis.base.bd.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.base.bd.dao.ParamMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;











import com.zebone.nhis.base.bd.vo.MoreParamVo;
import com.zebone.nhis.base.bd.vo.SaveDeptType;
import com.zebone.nhis.base.bd.vo.SaveMoreParam;
import com.zebone.nhis.base.bd.vo.SaveParam;
import com.zebone.nhis.common.module.base.bd.res.BdResPcArgu;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class ParamService {

	@Autowired
	private ParamMapper paramMapper;

	/**
	 * 保存修改已选参数列表
	 * 001002002076
	 * @param param
	 * @param user
	 * @throws Exception
	 */
	public void saveList(String param,IUser user) throws Exception{
		SaveParam saParam = JsonUtil.readValue(param, SaveParam.class);
		String pkDept = saParam.getPkDept();
		if(StringUtils.isEmpty(pkDept))return;
		List<BdResPcArgu> list = saParam.getArguList();
		DataBaseHelper.execute("delete from bd_res_pc_argu where pk_dept=?", new Object[]{pkDept});
		if(list == null){
			DataBaseHelper.execute("delete from bd_res_pc_argu where pk_dept=?", new Object[]{pkDept});
		}else {
			for (BdResPcArgu bdResPcArgu : list) {
				bdResPcArgu.setPkPcargu(NHISUUID.getKeyId());
				bdResPcArgu.setPkDept(pkDept);
				bdResPcArgu.setDelFlag("0");
				DataBaseHelper.insertBean(bdResPcArgu);
			}	
		}
	}
	

	/**
	 * 修改已选参数列表
	 * 001002002077
	 * @param param
	 * @param user
	 * @throws Exception
	 */
	public void UpdateList(String param,IUser user) throws Exception{
		SaveParam saParam = JsonUtil.readValue(param, SaveParam.class);
		List<BdResPcArgu> list = saParam.getArguList();
		for (BdResPcArgu bdResPcArgu : list) {
			bdResPcArgu.setDelFlag("0");
			DataBaseHelper.updateBeanByPk(bdResPcArgu, false);
		}
	}
	
	/**
	 * 根据当前科室保存当前科室下的子科室
	 * 001002002078
	 */
	public void SaveMoreList(String param,IUser user) throws Exception{
		SaveMoreParam saMoreParam = JsonUtil.readValue(param, SaveMoreParam.class);
		List<MoreParamVo> listM = saMoreParam.getList();
		String pkDept = null;
		String aa = null;//
		for (MoreParamVo mpv : listM) {
			pkDept = mpv.getPkDept();
			String sqlOrg ="SELECT * FROM BD_OU_DEPT WHERE 1=1  and del_flag = '0' and flag_active='1' ";//
			List<BdOuDept> allDept =  DataBaseHelper.queryForList(sqlOrg, BdOuDept.class, new Object[]{});//
			List<BdOuDept> lista =   getChildDept(allDept,new ArrayList<BdOuDept>(),pkDept,new ArrayList<BdOuDept>());//
			for (BdOuDept bdOuDept : lista) {
				aa = bdOuDept.getPkDept();
			}
			if (aa == null) {
				DataBaseHelper.execute("delete from bd_res_pc_argu where pk_dept=?", new Object[]{pkDept});
				List<BdResPcArgu> list = saMoreParam.getArguList();
				for (BdResPcArgu bdResPcArgu : list) {
					bdResPcArgu.setPkPcargu(NHISUUID.getKeyId());
					bdResPcArgu.setPkDept(pkDept);
					bdResPcArgu.setDelFlag("0");
					DataBaseHelper.insertBean(bdResPcArgu);
				}
			}else{
			//DataBaseHelper.execute("delete from bd_res_pc_argu where pk_dept=?", new Object[]{pkDept});
			DataBaseHelper.execute("delete from bd_res_pc_argu where pk_dept=?", new Object[]{aa});
			List<BdResPcArgu> list = saMoreParam.getArguList();
			for (BdResPcArgu bdResPcArgu : list) {
				bdResPcArgu.setPkPcargu(NHISUUID.getKeyId());
				bdResPcArgu.setPkDept(aa);
				bdResPcArgu.setDelFlag("0");
				DataBaseHelper.insertBean(bdResPcArgu);
				}
			}
			
		}
		
		
		/*SaveParam saParam = JsonUtil.readValue(param, SaveParam.class);
		String pkDept = saParam.getPkDept();
		String aa = null;
		if(StringUtils.isEmpty(pkDept))return;	
		String sqlOrg ="SELECT * FROM BD_OU_DEPT WHERE 1=1  and del_flag = '0' and flag_active='1' ";
		List<BdOuDept> allDept =  DataBaseHelper.queryForList(sqlOrg, BdOuDept.class, new Object[]{});
		List<BdOuDept> lista = new ArrayList<BdOuDept>();
		lista =  getChildDept(allDept,new ArrayList<BdOuDept>(),pkDept,new ArrayList<BdOuDept>());
		for (BdOuDept bdOuDept : lista) {
			 aa =bdOuDept.getPkDept(); 
			 DataBaseHelper.execute("delete from bd_res_pc_argu where pk_dept=?", new Object[]{aa});
			 List<BdResPcArgu> list = saParam.getArguList();
			 for (BdResPcArgu bdResPcArgu : list) {
				 bdResPcArgu.setPkPcargu(NHISUUID.getKeyId());
				 bdResPcArgu.setPkDept(aa);
				 DataBaseHelper.insertBean(bdResPcArgu);
			 }
		}*/
	}
	private List<BdOuDept>  getChildDept(List<BdOuDept> allDept,List<BdOuDept> list ,String pkDept,List<BdOuDept> childDeptParam){  
		List<BdOuDept> childList  = childDeptParam.size()>0?childDeptParam:IsHasChild (allDept,pkDept); 
        for (BdOuDept d : childList){
        	  List<BdOuDept> hasChild = IsHasChild(allDept,d.getPkDept());
        	  if(hasChild.size()>0) getChildDept(allDept,list ,d.getPkDept(),hasChild);
        	  else list.add(d);
        }
        return list;
	}
	private List<BdOuDept> IsHasChild(List<BdOuDept> allDept,String pkDept){
		    List<BdOuDept> childList = new ArrayList<BdOuDept>();
	        for(BdOuDept d : allDept){
	        	
	        	if(pkDept.equals(d.getPkFather())){
	        		childList.add(d);
	        	}
	        }
	        return childList;
	}
	
	/**
	 * 按科室类别保存
	 * 001002002081
	 * @param param
	 * @param user
	 */
	public void saveRealityDept(String param,IUser user){
		SaveDeptType sdt = JsonUtil.readValue(param, SaveDeptType.class); 
		String code = sdt.getCode();
		List<BdResPcArgu> Argulist = sdt.getArguList();
		String sql = "SELECT * FROM BD_OU_DEPT WHERE DEPT_TYPE = 'N' AND DT_DEPTTYPE = ? AND PK_ORG = ?";
		List<BdOuDept> list = DataBaseHelper.queryForList(sql, BdOuDept.class, new Object[] {code,((User) user).getPkOrg()});
		for (BdOuDept bdOuDept : list) {
			String pkDept = bdOuDept.getPkDept();
			DataBaseHelper.execute("delete from bd_res_pc_argu where pk_dept=?", new Object[]{pkDept});	
			for (BdResPcArgu bdResPcArgu : Argulist) {
				bdResPcArgu.setPkPcargu(NHISUUID.getKeyId());
				bdResPcArgu.setPkDept(pkDept);
				bdResPcArgu.setDelFlag("0");
				DataBaseHelper.insertBean(bdResPcArgu);
			}
		}
	}


	/**
	 * 查询参数信息
	 * 001002002107
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String,Object>> getParamList(String param,IUser user){
		String orgId = JsonUtil.getFieldValue(param,"orgId");
		String name = JsonUtil.getFieldValue(param,"name");
		String deptId = JsonUtil.getFieldValue(param,"deptId");
		String code = JsonUtil.getFieldValue(param,"code");
		Map<String,Object> map = new HashMap<>();
		map.put("orgId",orgId);
		map.put("name",name);
		map.put("deptId",deptId);
		map.put("code",code);
		List<Map<String,Object>> result = paramMapper.getParamList(map);
		return result;
	}



}
