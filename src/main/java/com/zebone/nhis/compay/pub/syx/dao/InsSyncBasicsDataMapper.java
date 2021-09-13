package com.zebone.nhis.compay.pub.syx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.compay.pub.syx.vo.InsSyncBasicsDataVo;
import com.zebone.nhis.compay.pub.syx.vo.SyxHpDataVo;
import com.zebone.nhis.compay.pub.syx.vo.ViewItem;
import com.zebone.nhis.compay.pub.syx.vo.ViewItemOut;
import com.zebone.nhis.compay.pub.syx.vo.ViewMatch;
import com.zebone.nhis.compay.pub.syx.vo.ViewMatchOut;
import com.zebone.nhis.compay.pub.syx.vo.ViewMedi;
import com.zebone.nhis.compay.pub.syx.vo.ViewMediOut;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface InsSyncBasicsDataMapper {

	/**获取中间库诊疗项目及服务设施目录-市医保/花都公医 */
	public List<ViewItem> queryViewItemList(@Param(value = "codeHp") String codeHp);

	/**获取中间库药品目录 -市医保/花都公医*/
	public List<ViewMedi> queryViewMediList(@Param(value = "codeHp") String codeHp);

	/**获取中间库目录对应表-市医保/花都公医 */
	public List<ViewMatch> queryViewMatchList(@Param(value = "codeHp") String codeHp);
	
	/**获取中间库诊疗项目及服务设施目录-省内异地 */
	public List<ViewItemOut> queryViewItemListOut(@Param(value = "codeHp") String codeHp);

	/**获取中间库药品目录 -省内异地*/
	public List<ViewMediOut> queryViewMediListOut(@Param(value = "codeHp") String codeHp);

	/**获取中间库目录对应表-省内异地 */
	public List<ViewMatchOut> queryViewMatchListOut(@Param(value = "codeHp") String codeHp);

	public List<InsSyncBasicsDataVo> seachInsSyncBasicsData(
			@Param(value = "euhphicttype") String euhphicttype,
			@Param(value = "euitemtype") String euitemtype,
			@Param(value = "keyword") String keyword);
	
	public List<SyxHpDataVo> qryNeedCopyHpData(Map<String,Object> paramMap);
	
	public List<SyxHpDataVo> qryNeedHpDataForOther(Map<String,Object> paramMap);
}
