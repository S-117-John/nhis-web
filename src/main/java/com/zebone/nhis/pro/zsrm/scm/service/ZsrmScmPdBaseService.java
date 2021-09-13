package com.zebone.nhis.pro.zsrm.scm.service;

import com.zebone.nhis.common.module.scm.pub.BdPdGn;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.pro.zsrm.scm.dao.ZsrmScmPdBaseMapper;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.MultiDataSource;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 中山人医供应链基础字典服务类
 */
@Service
public class ZsrmScmPdBaseService {
	
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

    @Resource
    private ZsrmScmPdBaseMapper scmPdBaseMapper;

    /**
     * 022006003001
     * 获取药品字典表中code,dt_unitfy
     * @param param
     * @param user
     * @return
     */
    public String getBdPdCode(String param, IUser user){
        Map<String,Object> paramMap= JsonUtil.readValue(param,Map.class);
        String resStr="";
        if(paramMap==null)return resStr;
        String befStr= CommonUtils.getString(paramMap.get("befFiled"));
        if(CommonUtils.isNull(befStr)){
            return resStr;
        }
        Map<String,Object> resMap=scmPdBaseMapper.getBdPdCodeMax(paramMap);
        if(resMap==null){
            resStr=befStr+"001";
        }else {

            String maxStr = CommonUtils.getString(resMap.get("maxCode"), "");
            if (CommonUtils.isNull(maxStr)) {
                resStr = befStr + "001";
            } else {
                maxStr = maxStr.substring(befStr.length() , maxStr.length());
                Integer maxCode = Integer.parseInt(maxStr) + 1;
                maxStr = String.valueOf(maxCode);
                while (maxStr.length() < 3) {
                    maxStr = "0" + maxStr;
                }
                resStr = befStr + maxStr;
            }
        }
        return resStr;
    }
    
    /**
	 * 交易号：022006003003
	 * 保存的通用字典数据
	 * @param param 
	 * @param user
	 */
	public void operateSave(String param, IUser user){
		List<BdPdGn> infoList = JsonUtil.readValue(param, new TypeReference<List<BdPdGn>>() {});
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
						String sql = "SELECT MAX(pdgn.code) maxcode from BD_PD_GN pdgn";
						try
                        {
							String code = "";
							Map<String,Object> maxMap=DataBaseHelper.queryForMap(sql,new Object[]{});
							if(maxMap!=null && maxMap.get("maxcode")!=null){
								code= CommonUtils.getString(maxMap.get("maxcode"),"0");
							}else{
								code="0";
							}
                            String strCode = String.format("%0"+code.length()+"d",Integer.parseInt(code)+1);
						    info.setCode(strCode);
                        }catch(Exception e){
						    throw new BusException("药品类别为"+info.getEuDrugType()+"存在不符合规则的通用编码，请先删除！");
                        }

						info.setPkPdgn(NHISUUID.getKeyId());
						DataBaseHelper.insertBean(info);
						break;
					case UpdateState:
						int count = DataBaseHelper.queryForScalar("SELECT COUNT(1) FROM BD_PD WHERE DEL_FLAG = '0' "
								+ "AND PK_ORG = ? AND PK_PDGN = ?", Integer.class, info.getPkOrg(),info.getPkPdgn());
						/*BUG 33702 取消名称修改限制*/
						//if(count>0){throw new BusException("通用名称已经被药品字典引用，不能修改！");}
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
	/**
	 *
	 *删除附加属性
	 */

	public Map<String,String> deleteGcPd(String param, IUser user){
		Map<String,String> paramMap = JsonUtil.readValue(param, Map.class);
		String codeAtt = paramMap.get("codeAtt");
		if (StringUtils.isBlank(codeAtt)){
			throw new BusException("入参为空");
		}
		Map<String,String> map=new LinkedHashMap<>();
		int execute = DataBaseHelper.execute("delete from bd_pd_att_define where code_att=?", codeAtt);
		if (execute>0){
			map.put("state","0");
			map.put("msg", String.valueOf(execute));
		}else{
			map.put("state","1");
			map.put("msg","删除失败");
		}
		return map;
	}
}
