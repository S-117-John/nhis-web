package com.zebone.nhis.ma.pub.lb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.lb.vo.HighValueConsumVo;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 灵璧高值耗材接口实现-调用产品级耗材应用，非外部接口
 * @author jd
 *
 */
@Service
public class LbHighValueConsumHandler {
	
	@Resource
	private LbHighValueConsumService lbHighValueConsumService;
	
	public Object invokeMethod(String methodName,Object...args){
		Object obj=new Object();
    	switch(methodName){
	    	case "saveConsumable"://记费
	    		this.saveConsumableHrp(args);
	    		break;
	    	case "savaReturnConsumable"://退费
	    		this.savaReturnConsumableHrp(args);
	    		break;
	    	case "qryHightItemBybarcode"://根据材料编码查询高值耗材中对应的收费项目
	    	    obj= this.qryHightItemBybarcodeHrp(args);break;
			case
	    		 "checkHighValueConsum"://效验库存
	    	    this.checkHighValueConsum();
	    		break;
	    	case "qryHighIsDo"://查询科室是否启用高值耗材
	    		obj=this.qryHighIsDo(args);
	    		break;
    	} 
    	return obj;
	}
	/**
	 * 通过材料编码查询高值耗材中对应的收费项目
	 * @param {"barcode":"材料编码"}
	 * @param
	 * @return
	 */
	public String qryHightItemBybarcode(Object...args){
			String param=(String)args[0];
			String barcode=JsonUtil.getFieldValue(param, "barcode");
			if(CommonUtils.isEmptyString(barcode)){
				throw new BusException("未获取到材料条码！");
			}
			StringBuffer sql=new StringBuffer();
			sql.append("select pd.pk_item from  bd_pd pd");
			sql.append(" inner join pd_single sign on sign.pk_pd=pd.pk_pd");
			sql.append(" where  sign.barcode=?");
			List<Map<String,Object>> resList=DataBaseHelper.queryForList(sql.toString(), new Object[]{barcode});
			if(resList==null || resList.size()==0 ||resList.get(0)==null || resList.get(0).get("pkItem")==null){
				throw new BusException("未获取到对应材料条码【"+barcode+"】的收费项目，请联系管理员进行核对！");
			}
			if(resList.size()!=1){
				throw new BusException("获取到对应材料条码【"+barcode+"】的收费项目存在多条，请联系管理员进行核对！");
			}
			return resList.get(0).get("pkItem").toString();
	}

	/**
	 * 通过材料编码查询高值耗材中对应的收费项目-HRP对接
	 * @param {"barcode":"材料编码"}
	 * @param
	 * @return
	 */
	public String qryHightItemBybarcodeHrp(Object...args){
		String param=(String)args[0];
		String barcode=JsonUtil.getFieldValue(param, "barcode");
		if(CommonUtils.isEmptyString(barcode)){
			throw new BusException("未获取到材料条码！");
		}
		return lbHighValueConsumService.qryHightItemBybarcodeHrp(barcode);
	}
	
	/**
	 * 校验数据：库存，接口数据是否正确
	 * @param{paramList：{"oldId":"老系统项目编码";"pkDeptEx":"执行科室";"barcode":"材料编码";"quanCg":"需要数量"}}
	 * @param
	 */
	public void checkHighValueConsum(Object...args){
		String param=(String)args[0];
		List<HighValueConsumVo> paramList=JsonUtil.readValue(param,new TypeReference<List<HighValueConsumVo>>(){});
		if(paramList==null)return ;
		for (HighValueConsumVo highVal : paramList) {
			if(highVal.getQuanCg()>1){
				throw new BusException("开启高值耗材记费时，对应条材料条码要保持一物一码，收费项目【"+highVal.getName()+"】对应条码【"+highVal.getBarcode()+"】数量不能大于1");
			}
			int count=0;
			for (HighValueConsumVo high2 : paramList) {
				if(high2.getBarcode().equals(highVal.getBarcode())){
					count++;
					if(count>1){
						throw new BusException("对应材料条码【"+high2.getBarcode()+"】的收费项目【"+high2.getName()+"】存在多条，请核对数据！");
					}
				}
			}
			StringBuffer sql=new StringBuffer();
			sql.append("select count(1) from  bd_pd pd");
			sql.append(" inner join bd_item item on pd.pk_item=item.pk_item");
			sql.append(" inner join pd_single sign on sign.pk_pd=pd.pk_pd");
			sql.append(" where  item.dt_itemtype='0701' and pd.flag_single='1' and flag_precious='1' and sign.barcode=?");
			Integer cnt=DataBaseHelper.queryForScalar(sql.toString(), Integer.class, new Object[]{highVal.getBarcode()});
			if(cnt==null || cnt==0){
				throw new BusException("对应材料条码【"+highVal.getBarcode()+"】的收费项目【"+highVal.getName()+"】库存不足，请核对数据！");
			}
		}

	}
	/**
	 * 校验数据：库存，接口数据是否正确-灵璧HRP对接暂时无使用
	 * @param{paramList：{"oldId":"老系统项目编码";"pkDeptEx":"执行科室";"barcode":"材料编码";"quanCg":"需要数量"}}
	 * @param
	 */
	public void checkHighValueConsum(){

	}
	/**
	 * 高值耗材记费接口
	 * @param args
	 */
	public void saveConsumable(Object...args){
			String isHighValue = ApplicationUtils.getSysparam("EX0038", false);
			if(isHighValue==null){
				throw new BusException("系统参数【EX0038】未设置或者未设置取值，请前往基础数据维护或联系管理员！");
			}
			if("0".equals(isHighValue))return;
			if(args==null || args.length<2 ||args[1]==null)return ;
			BlPubReturnVo blretvo=new BlPubReturnVo();
			BeanUtils.copyProperties(args[1], blretvo);
			List<BlIpDt> ipdtList=blretvo.getBids();
			
			List<String> barcodes=new ArrayList<String>();
			List<BlIpDt> filterIpList=new ArrayList<BlIpDt>();
			//1.校验记费数据
			for (BlIpDt highVal : ipdtList) {
				if(CommonUtils.isEmptyString(highVal.getBarcode()))continue;
				if(highVal.getQuan()>1){
					throw new BusException("开启高值耗材记费时，对应条材料条码要保持一物一码，收费项目【"+highVal.getNameCg()+"】对应条码【"+highVal.getBarcode()+"】数量不能大于1");
				}
				int count=0;
				for (BlIpDt high2 : ipdtList) {
					if(high2.getBarcode().equals(highVal.getBarcode())){
						count++;
						if(count>1){
							throw new BusException("对应材料条码【"+high2.getBarcode()+"】的收费项目【"+high2.getNameCg()+"】存在多条，请核对数据！");
						}
					}
				}
				StringBuffer sql=new StringBuffer();
				sql.append("select count(1) from  bd_pd pd");
				sql.append(" inner join bd_item item on pd.pk_item=item.pk_item");
				sql.append(" inner join pd_single sign on sign.pk_pd=pd.pk_pd");
				sql.append(" where  item.dt_itemtype='0701' and pd.flag_single='1' and flag_precious='1' and sign.barcode=? and sign.eu_status in ('0','1')");
				Integer cnt=DataBaseHelper.queryForScalar(sql.toString(), Integer.class, new Object[]{highVal.getBarcode()});
				if(cnt==null || cnt==0){
					throw new BusException("对应材料条码【"+highVal.getBarcode()+"】的收费项目【"+highVal.getNameCg()+"】库存不足，请核对数据！");
				}
				barcodes.add(highVal.getBarcode());
				filterIpList.add(highVal);
			}
			
			if(barcodes.size()==0)return;
			//2.调用出库处理服务
		    lbHighValueConsumService.saveConsumable(filterIpList,barcodes);
	}

	/**
	 * 高值耗材记费接口
	 * @param args
	 */
	public void saveConsumableHrp(Object...args){
			String isHighValue = ApplicationUtils.getSysparam("EX0038", false);
			if(isHighValue==null){
				throw new BusException("系统参数【EX0038】未设置或者未设置取值，请前往基础数据维护或联系管理员！");
			}
			if("0".equals(isHighValue))return;
			if(args==null || args.length<2 ||args[1]==null)return ;
			BlPubReturnVo blretvo=new BlPubReturnVo();
			BeanUtils.copyProperties(args[1], blretvo);
			//判断blretvo.getBids是否有费用信息
			if(! lbHighValueConsumService.isNotNull(blretvo.getBids())){
				return;
			}
			List<BlIpDt> ipdtList=blretvo.getBids();
			
			List<String> barcodes=new ArrayList<String>();
			List<BlIpDt> filterIpList=new ArrayList<BlIpDt>();
			//1.校验记费数据
			for (BlIpDt highVal : ipdtList) {
				if(CommonUtils.isEmptyString(highVal.getBarcode()))continue;
				if(highVal.getQuan()>1){
					throw new BusException("开启高值耗材记费时，对应条材料条码要保持一物一码，收费项目【"+highVal.getNameCg()+"】对应条码【"+highVal.getBarcode()+"】数量不能大于1");
				}
				int count=0;
				for (BlIpDt high2 : ipdtList) {
					if(high2.getBarcode().equals(highVal.getBarcode())){
						count++;
						if(count>1){
							throw new BusException("对应材料条码【"+high2.getBarcode()+"】的收费项目【"+high2.getNameCg()+"】存在多条，请核对数据！");
						}
					}
				}
				barcodes.add(highVal.getBarcode());
				filterIpList.add(highVal);
			}
			
			if(barcodes.size()==0)return;
			//2.调用外部HRP系统出库处理服务
		    lbHighValueConsumService.saveConsumableHrp(filterIpList,barcodes);
	}
	/**
	 * 高值耗材退费接口
	 * @param args
	 */
	public void savaReturnConsumable(Object...args){
		String isHighValue = ApplicationUtils.getSysparam("EX0038", false);
		if(isHighValue==null){
			throw new BusException("系统参数【EX0038】未设置或者未设置取值，请前往基础数据维护或联系管理员！");
		}
		if("0".equals(isHighValue))return;
		if(args==null || args.length<2 ||args[1]==null)return ;
		BlPubReturnVo blretvo=new BlPubReturnVo();
		BeanUtils.copyProperties(args[1], blretvo);

	}
	
	/**
	 * 高值耗材退费接口-hrp
	 * @param args
	 */
	public void savaReturnConsumableHrp(Object...args){
		String isHighValue = ApplicationUtils.getSysparam("EX0038", false);
		if(isHighValue==null){
			throw new BusException("系统参数【EX0038】未设置或者未设置取值，请前往基础数据维护或联系管理员！");
		}
		if("0".equals(isHighValue))return;
		if(args==null || args.length<2 ||args[1]==null)return ;
		BlPubReturnVo blretvo=new BlPubReturnVo();
		BeanUtils.copyProperties(args[1], blretvo);
		lbHighValueConsumService.savaReturnConsumableHrp(blretvo);
	}
	
	/**
	 * 校验执行科室是否启用高值耗材
	 * @param
	 * @param
	 * @return
	 */
	public int qryHighIsDo(Object... args){
		
		return 1;
		//原始版本
		/*StringBuffer sql=new StringBuffer();
		sql.append("select count(1) from  bd_pd pd");
		sql.append(" inner join bd_item item on pd.pk_item=item.pk_item");
		sql.append(" inner join pd_single sign on sign.pk_pd=pd.pk_pd");
		sql.append(" where  item.dt_itemtype='0701' and pd.flag_single='1' and flag_precious='1' and sign.eu_status in ('0','1')");
		int count=DataBaseHelper.queryForScalar(sql.toString(), Integer.class, new Object[]{});
		return count;*/
	}
}
