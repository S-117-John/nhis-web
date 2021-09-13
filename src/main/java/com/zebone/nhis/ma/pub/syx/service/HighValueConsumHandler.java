package com.zebone.nhis.ma.pub.syx.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.ma.pub.syx.vo.HighValueConsumVo;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.dao.jdbc.DataSourceRoute;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

@Service
public class HighValueConsumHandler {
	
	@Resource
	private HighValueConsumService highValueConsumService;
	
	public Object invokeMethod(String methodName,Object...args){
		Object obj=new Object();
    	switch(methodName){
    	case "saveConsumable":
    		this.saveConsumable(args);
    		break;
    	case "savaReturnConsumable":
    		this.savaReturnConsumable(args);
    		break;
    	case "qryHightItemBybarcode":
    	    obj= this.qryHightItemBybarcode(args);
    		break;
    	case "checkHighValueConsum":
    		this.checkHighValueConsum(args);
    		break;
    	case "qryHighIsDo":
    		obj= this.qryHighIsDo(args);
    		break;
    	} 
    	return obj;
	}
	
	/**
	 * 通过材料编码查询高值耗材中对应的收费项目
	 * @param param{"barcode":"材料编码"}
	 * @param user
	 * @return
	 */
	public String qryHightItemBybarcode(Object...args){
		try{
			String param=(String)args[0];
			String barcode=JsonUtil.getFieldValue(param, "barcode");
			DataSourceRoute.putAppId("HIS_Interface");
			Map<String,Object> itemMap=highValueConsumService.qryHightItemBybarcode(barcode);
			DataSourceRoute.putAppId("default");
			String sql="select pk_item  from bd_item  where old_id=?";
			Map<String,Object> item=DataBaseHelper.queryForMap(sql, new Object[]{itemMap.get("itemCode")});
			if(item==null||"".equals(item.get("pkItem")))
			{
				throw new BusException("本系统中未找到对应材料编码的收费项目！");
			}
			
//			DataSourceRoute.putAppId("default");
//			sql="select nvl(sum(amount),0) amt from bl_ip_dt  where barcode=?";
//			Map<String,Object> amtBarcodeMap  = DataBaseHelper.queryForMap(sql, new Object[]{barcode});
//			BigDecimal amtBarcode = (BigDecimal)amtBarcodeMap.get("amt");
//			BigDecimal amtZero = BigDecimal.ZERO;
//			if(amtBarcode.compareTo(amtZero) > 0)
//			{
//				throw new BusException("该条码已收费，不可重复收费！");
//			}
			
			return item.get("pkItem").toString();
		}catch(Exception ex){
			throw new BusException(ex.getMessage());
		}finally{
			DataSourceRoute.putAppId("default");
		}
	}
	
	/**
	 * @param param{paramList：{"oldId":"老系统项目编码";"pkDeptEx":"执行科室";"barcode":"材料编码";"quanCg":"需要数量"}}
	 * @param user
	 */
	public void checkHighValueConsum(Object...args){
		try {
			String param=(String)args[0];
			List<HighValueConsumVo> paramList=JsonUtil.readValue(param,new TypeReference<List<HighValueConsumVo>>(){});
			if(paramList==null)return ;
			for (HighValueConsumVo consumVo : paramList) {
				String sql="select old_id dept_code  from bd_ou_dept where pk_dept=?";
				Map<String,Object> deptMap=DataBaseHelper.queryForMap(sql, new Object[]{consumVo.getPkDeptEx()});
				if(deptMap==null ||null==deptMap.get("deptCode")||"".equals(deptMap.get("deptCode")))
				{
					throw new BusException("执行科室对应旧系统科室编码未维护");
				}
				consumVo.setDeptCode(deptMap.get("deptCode").toString());
				String oldSql="select old_id from bd_item where pk_item=?" ;
				Map<String,Object> oldId=DataBaseHelper.queryForMap(oldSql, consumVo.getPkItem());
				if(oldId==null || oldId.get("oldId")==null){
					throw new BusException("当前所选项目未和老系统做对照处理，请联系管理员！");
				}
				consumVo.setOldId(oldId.get("oldId").toString());
			}
			
			DataSourceRoute.putAppId("HIS_Interface");
			highValueConsumService.checkHighValueConsum(paramList);
		} catch (Exception e) {
			throw new BusException(e.getMessage());
		}
		finally{
			DataSourceRoute.putAppId("default");
		}
	}
	/**
	 * 高值耗材记费接口，修改库存（中间库）
	 * @param args
	 */
	public void saveConsumable(Object...args){
		try {
			String isHighValue = ApplicationUtils.getSysparam("EX0038", false);
			if(isHighValue==null){
				throw new BusException("系统参数【EX0038】未设置或者未设置取值，请前往基础数据维护或联系管理员！");
			}
			if("0".equals(isHighValue))return;
			String param=(String)args[0];
			List<HighValueConsumVo> consumList=JsonUtil.readValue(param, new TypeReference<List<HighValueConsumVo>>() {});
			if(consumList==null )return;
			for (int i=consumList.size()-1;i>=0;i--) {
				if(!"0701".equals(consumList.get(i).getDtItemtype())){
					consumList.remove(i);
					continue;
				}
				String sql="select old_id dept_code  from bd_ou_dept where pk_dept=?";
				Map<String,Object> deptMap=DataBaseHelper.queryForMap(sql, new Object[]{consumList.get(i).getPkDeptEx()});
				if(deptMap==null ||null==deptMap.get("deptCode")||"".equals(deptMap.get("deptCode")))
				{
					throw new BusException("执行科室对应旧系统科室编码未维护");
				}
				consumList.get(i).setDeptCode(deptMap.get("deptCode").toString());
				String oldSql="select old_id from bd_item where pk_item=?" ;
				Map<String,Object> oldId=DataBaseHelper.queryForMap(oldSql, consumList.get(i).getPkItem());
				if(oldId==null || oldId.get("oldId")==null){
					throw new BusException("当前所选项目未和老系统做对照处理，请联系管理员！");
				}
				consumList.get(i).setOldId(oldId.get("oldId").toString());
			}
			if(consumList.size()<=0)return;
			
			DataSourceRoute.putAppId("HIS_Interface");
			highValueConsumService.saveConsumable(consumList);
		} catch (Exception e) {
			throw new BusException(e.getMessage());
		}
		finally{
			DataSourceRoute.putAppId("default");
		}
	}
	
	/**
	 * 高值耗材退费接口
	 * @param args
	 */
	public void savaReturnConsumable(Object...args){
		try {
			String isHighValue = ApplicationUtils.getSysparam("EX0038", false);
			if(isHighValue==null){
				throw new BusException("系统参数【EX0038】未设置或者未设置取值，请前往基础数据维护或联系管理员！");
			}
			if("0".equals(isHighValue))return;
			String param=(String)args[0];
			List<HighValueConsumVo> consumList=JsonUtil.readValue(param, new TypeReference<List<HighValueConsumVo>>() {});
			if(consumList==null)return;
			for (int i=consumList.size()-1;i>=0;i--) {
				if("".equals(consumList.get(i).getBarcode())||consumList.get(i).getBarcode()==null){
					consumList.remove(i);
					continue;
				}
				String sql="select old_id dept_code  from bd_ou_dept where pk_dept=?";
				Map<String,Object> deptMap=DataBaseHelper.queryForMap(sql, new Object[]{consumList.get(i).getPkDeptEx()});
				if(deptMap==null ||null==deptMap.get("deptCode")||"".equals(deptMap.get("deptCode")))
				{
					throw new BusException("执行科室对应旧系统科室编码未维护");
				}
				consumList.get(i).setDeptCode(deptMap.get("deptCode").toString());
			}
			if(consumList.size()<=0)return;
			DataSourceRoute.putAppId("HIS_Interface");
			highValueConsumService.savaReturnConsumable(consumList);
		} catch (Exception e) {
			throw new BusException(e.getMessage());
		}
		finally{
			DataSourceRoute.putAppId("default");
		}
	}
	
	/**
	 * 010005002013
	 * 校验执行科室是否启用高值耗材
	 * @param param
	 * @param user
	 * @return
	 */
	public int qryHighIsDo(Object... args){
		int count=0;
		try {
			String param=(String)args[0];
			String pkDept=JsonUtil.getFieldValue(param, "pkDept");
			String sql="select old_id dept_code  from bd_ou_dept where pk_dept=?";
			Map<String,Object> deptMap=DataBaseHelper.queryForMap(sql, new Object[]{pkDept});
			if(deptMap==null ||null==deptMap.get("deptCode")||"".equals(deptMap.get("deptCode")))
			{
				throw new BusException("执行科室对应旧系统科室编码未维护");
			}
			
			String deptCode=deptMap.get("deptCode").toString();
			DataSourceRoute.putAppId("HIS_Interface");
			String highSql="SELECT count(1) from t_Inv_BarCode_Entity bar where bar.Quantity>0 and bar.Dept_Code=?";
			count=DataBaseHelper.queryForScalar(highSql, Integer.class, new Object[]{deptCode});
			return count;
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			DataSourceRoute.putAppId("default");
		}
		return count;
	}
}
