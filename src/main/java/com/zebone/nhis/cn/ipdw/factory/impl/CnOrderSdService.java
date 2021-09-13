package com.zebone.nhis.cn.ipdw.factory.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.cn.ipdw.dao.CnOrderSdMapper;
import com.zebone.nhis.cn.ipdw.factory.AbstractCnOrderFactory;
import com.zebone.nhis.cn.ipdw.vo.CnOrderInputVO;
import com.zebone.nhis.cn.ipdw.vo.SzybVo;


/**
 * 深大项目实现医嘱工厂方法
 * @author Administrator
 *
 */
@Service("cnOrderSdService")
public class CnOrderSdService extends AbstractCnOrderFactory{

	@Resource
	private CnOrderSdMapper CnOrderDao ;
	
	public static final String ORDER_XZ = "(XZ)";
	public static final String ORDER_True = "1";
	
	public static final String ORDER_YB = "(YB)";
	public static final String ORDER_YB_JB = "01";
	public static final String ORDER_YB_DB = "02";
	public static final String ORDER_YB_ZJ = "03";
		
	public static final String ORDER_ZF = "(ZF)";
	
	@Override
	public List<CnOrderInputVO> findOrdBaseList(Map<String, Object> paramMap) {
		List<CnOrderInputVO> list = CnOrderDao.findOrdBaseList(paramMap);	
		
		
		
		if(list==null || list.size()==0){
			return list;
		}
		
		//从获得的50条数据中重新查询对应的数据
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("ord", list);		
		List<SzybVo> listYb = CnOrderDao.findOrdSzyb(param);
		boolean flag = false;
		//个性化数据处理
		for (CnOrderInputVO pd: list) {	
			//医保显示问题
			for(SzybVo yb: listYb ){
				if(yb.getCodeHosp().equals(pd.getCode())){
					flag = true;
					if(yb.getAka036()!=null && ORDER_True.equals(yb.getAka036())){
						//pd.setName(ORDER_XZ+pd.getName());
						pd.setViewname(ORDER_XZ+pd.getViewname());
						break;
					}else{
						if(yb.getBkm032() !=null && (ORDER_YB_JB.equals(yb.getBkm032()) 
								|| ORDER_YB_DB.equals(yb.getBkm032()) 
								|| ORDER_YB_ZJ.equals(yb.getBkm032()) )){
								//pd.setName(ORDER_YB+pd.getName());
								pd.setViewname(ORDER_YB+pd.getViewname());
							}else{
								//pd.setName(ORDER_ZF+pd.getName());
								pd.setViewname(ORDER_ZF+pd.getViewname());
							}
						break;
					}	
				}
			}
			
			if(!flag){
				//pd.setName(ORDER_ZF+pd.getName());
				pd.setViewname(ORDER_ZF+pd.getViewname());
			}
			
			
		}

		return list;
	}

	/**根据医嘱code查询医保信息
	 * 004004003056
	 * cnOrderSdService.findOrdSzybByCodes
	 * @param param
	 * @param user
	 * @return
	 */
	public String findOrdSzybByCodes(String param , IUser user){
		String name = "";
		String code = "";
		Map<String,Object> map = JsonUtil.readValue(param,Map.class);
		if(map.containsKey("code")&&map.containsKey("name")){
			if(map.get("code")!=null&&map.get("name")!=null){
				code = map.get("code").toString();
				name = map.get("name").toString();
			}
		}
		List<SzybVo> result = CnOrderDao.findOrdSzybByCodes(code);
		//医保显示问题
		for(SzybVo yb: result ){
			if(yb.getCodeHosp().equals(code)){
				if(yb.getAka036()!=null && ORDER_True.equals(yb.getAka036())){
					name = ORDER_XZ+name;
					break;
				}else{
					if(yb.getBkm032() !=null && (ORDER_YB_JB.equals(yb.getBkm032())
							|| ORDER_YB_DB.equals(yb.getBkm032())
							|| ORDER_YB_ZJ.equals(yb.getBkm032()) )){
						name = ORDER_YB+name;

					}else{
						name = ORDER_ZF+name;

					}
					break;
				}
			}
		}
		return name;
	}

}
