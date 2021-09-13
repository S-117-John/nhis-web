package com.zebone.nhis.scm.pub.service;

import com.zebone.nhis.common.module.scm.pub.BdPdGn;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.MultiDataSource;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PdUniversalNameService {
	
	/**
	 * 添加状态
	 */
	public static final String AddState = "_ADD";
	
	/**
	 *更新状态 
	 */
	public static final String UpdateState = "_UPDATE";
	
	/**
	 * 删除状态
	 */
	public static final String DelState = "_DELETE";
	
	/**
	 * 
	 * @param param 需要进行操作的数据,格式为json
	 * @param user
	 */
	public void operateSave(String param, IUser user){
		
		List<BdPdGn> infoList = JsonUtil.readValue(param, new TypeReference<List<BdPdGn>>() {});
		String dbType = MultiDataSource.getCurDbType();
		boolean flag = false;
		if("sqlserver".equals(dbType)){
			flag = true;
		}
		if(null != infoList && infoList.size() > 0){
			for( BdPdGn info : infoList){		
				info.setModifier(user.getUserName());
					switch (info.getState()) {
					case AddState:
						int isExistName = DataBaseHelper.queryForScalar("SELECT COUNT(1) FROM BD_PD_GN WHERE DEL_FLAG = '0' "
								+ "AND NAME = ?", Integer.class, info.getName());
						if(isExistName != 0){
							throw new BusException("通用名称重复！");
						}
						String sql ="";
						if(flag){
							sql = "SELECT MAX(SUBSTRING(pdgn.code,2,len(CODE))) maxcode from BD_PD_GN pdgn WHERE pdgn.EU_DRUG_TYPE=?";
						}else{
							sql = "SELECT MAX(SUBSTR(pdgn.code,2,length(CODE))) maxcode from BD_PD_GN pdgn WHERE pdgn.EU_DRUG_TYPE=?";
						}
						try
                        {

							String code = "";
							Map<String,Object> maxMap=DataBaseHelper.queryForMap(sql,new Object[]{info.getEuDrugType()});
							if(maxMap!=null && maxMap.get("maxcode")!=null){
								code= CommonUtils.getString(maxMap.get("maxcode"),"0");
							}else{
								code="0";
							}
                            String strCode = String.format("%0"+code.length()+"d",Integer.parseInt(code)+1);
						    info.setCode(info.getEuDrugType()+strCode);
                        }catch(Exception e){
						    throw new BusException("药品类别为"+info.getEuDrugType()+"存在不符合规则的通用编码，请先删除！");
                        }

						info.setPkPdgn(NHISUUID.getKeyId());
						DataBaseHelper.insertBean(info);
						break;
					case UpdateState:
						int count = DataBaseHelper.queryForScalar("SELECT COUNT(1) FROM BD_PD WHERE DEL_FLAG = '0' "
								+ "AND PK_ORG = ? AND PK_PDGN = ?", Integer.class, info.getPkOrg(),info.getPkPdgn());
						if(count>0){throw new BusException("通用名称已经被药品字典引用，不能修改！");}
						List<BdPdGn> existList = DataBaseHelper.queryForList("SELECT pk_pdgn FROM BD_PD_GN WHERE DEL_FLAG = '0' "
								+ "AND CODE = ? OR NAME = ?" , BdPdGn.class, info.getCode(), info.getName());
						for(BdPdGn existInfo : existList){
							if(!info.getPkPdgn().equals(existInfo.getPkPdgn())){ throw new BusException("通用编号名称存在重复！");}
						}
						DataBaseHelper.updateBeanByPk(info, false);
						break;
					case DelState:
						DataBaseHelper.execute("UPDATE BD_PD_GN SET DEL_FLAG = '1' WHERE PK_PDGN = ?", info.getPkPdgn());
						break;

					default:
						break;
					}
			}
		}
	}

}
