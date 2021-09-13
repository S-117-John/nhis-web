package com.zebone.nhis.pv.arr.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.base.bd.srv.BdQcScreen;
import com.zebone.nhis.common.module.base.bd.srv.BdQcScreenDu;
import com.zebone.nhis.pv.arr.dao.QcBigScreenSetMapper;
import com.zebone.nhis.pv.arr.vo.BdQcScreenVO;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 分诊大屏幕设置
 * @author cuijiansheng 2019-08-27
 *
 */
@Service
public class QcBigScreenSetService {

	@Autowired
	private QcBigScreenSetMapper qcBigScreenSetMapper;

	/**
	 * 查询屏幕信息列表：003003002001
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> qryScreenList(String param,IUser user){
		
		User u = (User)user;
		
		return qcBigScreenSetMapper.qryScreenList(u.getPkOrg());
	}
	
	/**
	 * 查询诊室信息列表：003003002002
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> qryScreenDuList(String param,IUser user){
		
		String pkQcscreen = JsonUtil.getFieldValue(param, "pkQcscreen");
		if(StringUtils.isBlank(pkQcscreen)){
			throw new BusException("获取屏幕主键异常！");
		}
		
		return qcBigScreenSetMapper.qryScreenDuList(pkQcscreen);
	}
	
	/**
	 * 保存屏幕信息：003003002003
	 * @param param
	 * @param user
	 */
	public void saveScreen(String param,IUser user){
		BdQcScreenVO vo = JsonUtil.readValue(param, BdQcScreenVO.class);

		BdQcScreen bdQcScreen = vo.getBdQcScreen();

		if (StringUtils.isBlank(bdQcScreen.getPkQcscreen())) {
			DataBaseHelper.insertBean(bdQcScreen);
		} else {
			DataBaseHelper.updateBeanByPk(bdQcScreen, false);
		}

		List<BdQcScreenDu> list = vo.getList();
		List<Map<String, Object>> originList = qcBigScreenSetMapper.qryScreenDuList(bdQcScreen.getPkQcscreen());

		//检查诊室是否已在其他屏幕中存在
		List<String> listPKCheck = list.stream().map(BdQcScreenDu::getPkDeptunit).collect(Collectors.toList());
		List<String> listExist = qcBigScreenSetMapper.qryExists(listPKCheck, bdQcScreen.getPkQcscreen());

		if (listExist.size() > 0)
			throw new BusException("诊室" + String.join("、", listExist) + "已在其他屏幕存在！");

		if (list != null && list.size() > 0) {
			for (BdQcScreenDu bdQcScreenDu : list) {
				List<Map<String, Object>> sameList = originList.stream()
						.filter(m -> m.get("pkQcscreendu").toString().equals(bdQcScreenDu.getPkQcscreendu())).collect(Collectors.toList());
				if (sameList.size() > 0)
					originList.remove(sameList.get(0));

				bdQcScreenDu.setPkQcscreen(bdQcScreen.getPkQcscreen());
				if (StringUtils.isBlank(bdQcScreenDu.getPkQcscreendu())) {
					DataBaseHelper.insertBean(bdQcScreenDu);
				} else {
					DataBaseHelper.updateBeanByPk(bdQcScreenDu, false);
				}
			}

			//删除诊室
			List<String> listPk = originList.stream().map(m -> m.get("pkQcscreendu").toString()).collect(Collectors.toList());
			if (listPk.size() > 0)
				qcBigScreenSetMapper.delScreenDuByPks(listPk);
		}
	}
	
	/**
	 * 删除屏幕信息：003003002004
	 * @param param
	 * @param user
	 */
	public void delScreen(String param,IUser user){
		
		String pkQcscreen = JsonUtil.getFieldValue(param, "pkQcscreen");
		if(StringUtils.isBlank(pkQcscreen)){
			throw new BusException("获取屏幕主键异常！");
		}
		
		DataBaseHelper.execute("delete from bd_qc_screen_du where pk_qcscreen=?", pkQcscreen);
		DataBaseHelper.execute("delete from bd_qc_screen where pk_qcscreen=?", pkQcscreen);
	}
	
	/**
	 * 查询导入信息列表：003003002005
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> qryExportList(String param,IUser user){
		
		User u = (User)user;
		
		return qcBigScreenSetMapper.qryExportList(u.getPkOrg());
	}
}
