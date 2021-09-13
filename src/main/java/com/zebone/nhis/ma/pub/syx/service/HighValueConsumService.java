package com.zebone.nhis.ma.pub.syx.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ma.pub.syx.dao.HighValueConsumMapper;
import com.zebone.nhis.ma.pub.syx.vo.HighValueConsumVo;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;

@Service
public class HighValueConsumService {
	
	@Resource
	private HighValueConsumMapper highValueConsumMapper;
	
	private Logger logger = LoggerFactory.getLogger("ma.syxInterface");
	
	/**
	 * 验证是否可以添加数据
	 * @param args args[1]list<HighValueConsumVo>：{Barcode:"材料编码"，quanCg:"数量"，oldId:"老系统项目编码"；pkDeptEx:"执行科室"}
	 */
	@SuppressWarnings("unchecked")
	public void checkHighValueConsum(Object ...args){
		
		List<HighValueConsumVo> consList=(List<HighValueConsumVo>) args[0];
		for (HighValueConsumVo consumVo : consList) {
			HighValueConsumVo result=highValueConsumMapper.qryHighValueConsum(consumVo);
			if(result!=null){
				if(result.getQuantity()==0){
					throw new BusException("当前执行科室录入的高值耗材项目【"+consumVo.getName()+"】，库存不足，请检查！");
				}
				if(!result.getItemCode().equals(consumVo.getOldId())){
					throw new BusException("高值耗材【"+consumVo.getName()+"】的项目编码【"+consumVo.getOldId()+"】与条码【"+consumVo.getBarcode()+"】对应的项目编码【"+result.getItemCode()+"】不匹配，请检查条码是否正确！");
				}
			    if(MathUtils.sub(consumVo.getQuanCg(), result.getQuantity())>0){
					throw new BusException("现有库存数量为【"+result.getQuantity()+"】，小于录入数量，请修改录入数量！");
				}
			}else{
				logger.info("高值耗材接口：在第三方系统中不存在，请检查！");
				throw new BusException("当前执行科室录入的高值耗材项目【"+consumVo.getName()+"】，在第三方系统中对应科室不存在，请检查！");
			}
			
		}
	}
	/**
	 * 高值耗材记费接口修改库存
	 * @param args[0]list<HighValueConsumVo>：{Barcode:"材料编码"，quanCg:"数量"，oldId:"老系统项目编码"，pkDeptEx:"执行科室"}
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void saveConsumable(Object ...args){
		checkHighValueConsum(args);
		List<HighValueConsumVo> consList=(List<HighValueConsumVo>)args[0];
		List<String> sqls=new ArrayList<String>();
		for (HighValueConsumVo consumVo : consList) {
			StringBuffer sql=new StringBuffer("UPDATE  t_Inv_Barcode_Entity  set Out_Quantity=Out_Quantity+");
			sql.append(consumVo.getQuanCg());
			sql.append(" where Bar_code='");
			sql.append(consumVo.getBarcode());
			sql.append("' and Dept_Code='");
			sql.append(consumVo.getDeptCode());
			sql.append("'");
			sqls.add(sql.toString());
		}
		DataBaseHelper.batchUpdate(sqls.toArray(new String[0]));
	}
	
	/**
	 * 高值耗材退费接口
	 * @param args args[0]:List<HighValueConsumVo>(主要包含Barcode:材料编码；quanCg:"退费数量");pkDeptEx:“执行科室”}
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void savaReturnConsumable(Object ...args){
		List<HighValueConsumVo> consList=(List<HighValueConsumVo>)args[0];
		List<String> sqls=new ArrayList<String>();
		for (HighValueConsumVo consumVo : consList) {
			StringBuffer sql=new StringBuffer("UPDATE  t_Inv_Barcode_Entity  set Out_Quantity=Out_Quantity-");
			sql.append(consumVo.getQuanCg());
			sql.append(" where Bar_code='");
			sql.append(consumVo.getBarcode());
			sql.append("' and Dept_Code='");
			sql.append(consumVo.getDeptCode());
			sql.append("'");
			sqls.add(sql.toString());
		}
		DataBaseHelper.batchUpdate(sqls.toArray(new String[0]));
	}

	/**
	 * 通过材料编码查询高值耗材中对应的收费项目
	 * @param args{args[0]:高值耗材材料编码}
	 * @return
	 */
	public Map<String,Object> qryHightItemBybarcode(Object ...args){
		String barcode=(String)args[0];
		String sql="select item_code from t_Inv_Barcode_Entity where Bar_code=?";
		Map<String,Object> itemMap=DataBaseHelper.queryForMap(sql, new Object[]{barcode});
		if(itemMap==null || "".equals(itemMap.get("itemCode"))){
			throw new BusException("未找到对应材料编码的收费项目，请检查是否输入正确！");
		}
		return itemMap;
	}
}
