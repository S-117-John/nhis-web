package com.zebone.nhis.ex.pivas.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.ex.pivas.conf.BdPivasBatch;
import com.zebone.nhis.common.module.ex.pivas.conf.BdPivasCate;
import com.zebone.nhis.common.module.ex.pivas.conf.BdPivasPd;
import com.zebone.nhis.common.module.scm.pub.BdPd;
import com.zebone.nhis.ex.pivas.dao.BdPivasMapper;
import com.zebone.nhis.ex.pivas.vo.PdConditionParam;
import com.zebone.nhis.ex.pivas.vo.PivasCateAndPdsParam;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 静配设置
 * @author Xulj
 *
 */
@Service
public class PivasConfService {

	@Autowired
	private BdPivasMapper bdPivasMapper;
	
	/**
	 * 保存静配批次
	 * @param param
	 * @param user
	 */
	public void savePivasBatchList(String param, IUser user){
		List<BdPivasBatch> pivasBatchList = JsonUtil.readValue(param, new TypeReference<List<BdPivasBatch>>() {
		});
		
		if(pivasBatchList!=null && pivasBatchList.size()!=0){
			/**校验---1.校验前台所传的list的每一条编码，名称的唯一性*/
			Map<String, String> codemap = new HashMap<String, String>();
			Map<String, String> namemap = new HashMap<String, String>();
			int len = pivasBatchList.size();
			int countPack = 0;//打包次数
			for(int i=0;i<len;i++){
				String code = pivasBatchList.get(i).getCode();
				String name = pivasBatchList.get(i).getName();
				String euType = pivasBatchList.get(i).getEuType();//批次类型
				if(codemap.containsKey(code)){
					throw new BusException("静配批次编码重复！");
				}
				if(namemap.containsKey(name)){
					throw new BusException("静配批次名称重复！");
				}
				if("1".equals(euType)){
					countPack++;
				}
				codemap.put(code, pivasBatchList.get(i).getPkPivasbatch());
				namemap.put(name, pivasBatchList.get(i).getPkPivasbatch());
			}
			if(countPack > 1){
				throw new BusException("打包类型的记录不允许超过一条！");
			}
			
			/**查询数据库中所有*/
			String pkDept = ((User) user).getPkDept();
			List<BdPivasBatch> allist = this.bdPivasMapper.findAllPivasBatchs(pkDept);
			
			/**校验---2.pivasBatchList与数据库比较校验编码，名称的重复性*/
			for(BdPivasBatch databatch : allist){
				if(codemap.containsKey(databatch.getCode())){
					String pkPivasbatch = codemap.get(databatch.getCode());
					if(pkPivasbatch == null){
						throw new BusException("静配批次编码在数据库中已存在！");
					}else{
						if(!databatch.getPkPivasbatch().equals(pkPivasbatch)){
							throw new BusException("静配批次编码在数据库中已存在！");
						}
					}
				}
				
				if(namemap.containsKey(databatch.getName())){
					String pkPivasbatch = namemap.get(databatch.getName());
					if(pkPivasbatch == null){
						throw new BusException("静配批次名称在数据库中已存在！");
					}else{
						if(!databatch.getPkPivasbatch().equals(pkPivasbatch)){
							throw new BusException("静配批次名称在数据库中已存在！");
						}
					}
				}
			}
			
			/**新增或更新到数据库*/
			for(BdPivasBatch pibatch : pivasBatchList){
				if(pibatch.getPkPivasbatch() == null){
					pibatch.setPkDept(pkDept);
					DataBaseHelper.insertBean(pibatch);
				}else{
					DataBaseHelper.updateBeanByPk(pibatch,false);
				}
			}
		}
	}
	
	/**
	 * 删除静配批次
	 * @param param
	 * @param user
	 */
	public void delPivasBatch(String param, IUser user){
		String pkPivasbatch = JsonUtil.getFieldValue(param, "pkPivasbatch");
		DataBaseHelper.execute("delete from bd_pivas_batch where pk_pivasbatch = ?", pkPivasbatch);
	}
	
	/**
	 * 保存静配分类
	 * @param param
	 * @param user
	 */
	public void savePivasCateAndPds(String param, IUser user){
		/**接收参数*/
		PivasCateAndPdsParam cateAndPds = JsonUtil.readValue(param, PivasCateAndPdsParam.class);
		BdPivasCate bdPivasCate = cateAndPds.getBdPivasCate();
		List<BdPivasPd> bdPivasPdList = cateAndPds.getBdPivasPdList();
		String pkOrg = ((User)user).getPkOrg();
		/**保存静配分类**/
		if(bdPivasCate.getPkPivascate() == null){
			int count_code = DataBaseHelper.queryForScalar("select count(*) from bd_pivas_cate c where c.del_flag='0' and c.pk_org = ? and c.code = ?", Integer.class, pkOrg, bdPivasCate.getCode());
			int count_name = DataBaseHelper.queryForScalar("select count(*) from bd_pivas_cate c where c.del_flag='0' and c.pk_org = ? and c.name = ?", Integer.class, pkOrg, bdPivasCate.getName());
			if(count_code == 0 && count_name == 0){
				DataBaseHelper.insertBean(bdPivasCate);
			}else{
				if(count_code != 0){
					throw new BusException("静配分类编码重复！");
				}
				if(count_name != 0){
					throw new BusException("静配分类名称重复！");
				}
			}
		}else{
			int count_code = DataBaseHelper.queryForScalar("select count(*) from bd_pivas_cate c where c.del_flag='0' and c.pk_org = ? and c.code = ? and c.pk_pivascate != ?", Integer.class, pkOrg, bdPivasCate.getCode(), bdPivasCate.getPkPivascate());
			int count_name = DataBaseHelper.queryForScalar("select count(*) from bd_pivas_cate c where c.del_flag='0' and c.pk_org = ? and c.name = ? and c.pk_pivascate != ?", Integer.class, pkOrg, bdPivasCate.getName(), bdPivasCate.getPkPivascate());
			if(count_code == 0 && count_name == 0){
				DataBaseHelper.updateBeanByPk(bdPivasCate, false);
			}else{
				if(count_code != 0){
					throw new BusException("静配分类编码重复！");
				}
				if(count_name != 0){
					throw new BusException("静配分类名称重复！");
				}
			}
		}
		
		String pkPivascate = bdPivasCate.getPkPivascate();
		/**保存静配分类下的药品目录**/
		Map<String, String> pkpdmap = new HashMap<String, String>();
		if(bdPivasPdList == null || bdPivasPdList.size() <= 0)return ;
		/**校验---1.校验前台所传的list的每一条pk_pd的唯一性*/
//      更改为界面添加物品时校验--yangxue		
//		for(int i = 0; i<bdPivasPdList.size(); i++){
//			String pkpd = bdPivasPdList.get(i).getPkPd();
//			if(pkpdmap.containsKey(pkpd)){
//				throw new BusException("该静配分类下有药品重复，请核实！");
//			}
//			pkpdmap.put(pkpd, bdPivasPdList.get(i).getPkPivaspd());
//		}
			
		/**查询bd_pivas_pd表中所有**/
		List<BdPivasPd> beImportedPdList = this.bdPivasMapper.queryBdPivasPds(pkOrg);
		if(beImportedPdList!=null && beImportedPdList.size()!=0){
		/**校验---2.校验前台所传的list的每一条pk_pd是否在bd_pivas_pd表中重复*/
		 for(BdPivasPd pivaspd : beImportedPdList){
				String importedPkPd = pivaspd.getPkPd();//已经被引用的药品主键
				if(pkpdmap.containsKey(importedPkPd)){
				 String pkPivaspd = pkpdmap.get(importedPkPd);
				 if(pkPivaspd == null){//新增的
				   BdPd importedpd = DataBaseHelper.queryForBean("select * from bd_pd where del_flag= '0' and pk_pd=?", BdPd.class, importedPkPd);
				   throw new BusException(importedpd.getName() + "已经分配在其他静配分类中，请核实并选择其他药品！");
				   }else{                //修改的
						if(!pkPivaspd.equals(pivaspd.getPkPivaspd())){
							BdPd importedpd = DataBaseHelper.queryForBean("select * from bd_pd where del_flag= '0' and pk_pd=?", BdPd.class, importedPkPd);
						    throw new BusException(importedpd.getName() + "已经分配在其他静配分类中，请核实并选择其他药品！");
							}
						}
					}
			}
		}
		for(BdPivasPd pipd : bdPivasPdList){
			if(pipd.getPkPivaspd() == null){
					pipd.setPkPivascate(pkPivascate);
					DataBaseHelper.insertBean(pipd);
			}else{
					pipd.setPkPivascate(pkPivascate);
					DataBaseHelper.updateBeanByPk(pipd, false);
			}
		}
		//删除明细bd_pivas_pd
    	if(cateAndPds.getDelelteList()!=null&&cateAndPds.getDelelteList().size()>0){
    		DataBaseHelper.getJdbcTemplate().batchUpdate("delete from bd_pivas_pd where pk_pivaspd = ? ", cateAndPds.getDelelteList());
    	}
	}
	
	/**
	 * 删除静配分类
	 * @param param
	 * @param user
	 */
	public void delPivasCateAndPds(String param, IUser user){
		
		String pkPivascate = JsonUtil.getFieldValue(param, "pkPivascate");
		DataBaseHelper.execute("delete from bd_pivas_pd  where pk_pivascate=?", pkPivascate);
		DataBaseHelper.execute("delete from bd_pivas_cate where pk_pivascate=?", pkPivascate);
	}
	
	/**
	 * 删除静配分类下的药品
	 * @param param
	 * @param user
	 */
	public void delPivasCatePd(String param, IUser user){
		
		List<String> pkPivaspdList = JsonUtil.readValue(param, new TypeReference<List<String>>() {
		});
		if(pkPivaspdList!=null && pkPivaspdList.size()!=0){
			for(String pkPivaspd : pkPivaspdList){
				DataBaseHelper.execute("delete from bd_pivas_pd where pk_pivaspd=?", pkPivaspd);
			}
		}
	}
	
	/**
	 * 根据条件查询药品信息(导入界面)
	 * <pre>
	 * 一个药品只属于一个分类，导入界面查询的药品必定是在bd_pivas_pd表中未被引用的
	 * </pre>
	 * {
	 *    "dtPharm":"药理分类" null,
	 *    "dtPois":"毒麻分类" null,
	 *    "dtAnti":"抗菌药" null,
	 *    "flagTpn":"TPN标志" null
	 * }
	 * @return
	 * [
	 *  {
	 *    "药品主键":"pkPd",
	 *    "药品编码":"code",
	 *    "药品名称":"name",
	 *    "药品规格":"spec",
	 *    "生产厂家名称":"factoryName"
	 *  },...
	 * ]
	 */
	public List<Map<String, Object>> queryImportingPds(String param, IUser user){
		/**药品查询条件**/
		PdConditionParam pdCondition = JsonUtil.readValue(param, PdConditionParam.class);
		/**1.查询bd_pivas_pd表中被引用了的药品主键**/
		List<BdPd> beImportedPdList = this.bdPivasMapper.queryBeImportedPds();
		if(beImportedPdList!=null && beImportedPdList.size()!=0){
			List<String> pkPdList = new ArrayList<String>();
			for(BdPd pd : beImportedPdList){
				pkPdList.add(pd.getPkPd());
			}
			pdCondition.setPkPdList(pkPdList);
		}
		/**2.查询未被bd_pivas_pd表引用的且符合条件的药品**/
		List<Map<String, Object>> conditionPdList = this.bdPivasMapper.queryPdsByCondition(pdCondition);
		return conditionPdList;
	}
}
