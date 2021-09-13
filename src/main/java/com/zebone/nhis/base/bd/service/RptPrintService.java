package com.zebone.nhis.base.bd.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.base.pub.vo.RptPrintDataDto;
import com.zebone.nhis.base.pub.dao.BdRptSqlMapper;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 打印统一取数据服务
 * @author Administrator
 * 
 */
@Service
public class RptPrintService {

	@Autowired
	private BdRptSqlMapper bdRptSqlMapper;

	/**
	 * 通过获取前端的sql取数据库数据
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public RptPrintDataDto queryPrintData(String param, IUser user) {

		RptPrintDataDto rpdd = JsonUtil.readValue(param, RptPrintDataDto.class);
		RptPrintDataDto rtnResult = new RptPrintDataDto();
		if (rpdd.getQuerySql() != null) {// 查询的sql通过前台传递过来
			rtnResult.setGridData(DataBaseHelper.queryForList(rpdd.getQuerySql(), rpdd.getArgs()));
		} else if (rpdd.getOperateSqlFlag() != null) {// 查询的sql在XMl文件中
			Method mapperMethod;
			try {
				mapperMethod = bdRptSqlMapper.getClass().getMethod(rpdd.getOperateSqlFlag(), RptPrintDataDto.class);
				rtnResult.setGridData((List<Map<String, Object>>) mapperMethod.invoke(bdRptSqlMapper, rpdd));
			} catch (NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return rtnResult;
	}

	/**
	 * 设置打印标志
	 * @param param
	 * @param user
	 */
	public void setFlagPrt(String param, IUser user) {

		// RptPrintDataDto rpdd = JsonUtil.readValue(param,
		// RptPrintDataDto.class);
		// String tableName = rpdd.getTableName();
		// String tableColumn = rpdd.getTableColumn();
		// String pkName = rpdd.getPkName();
		// if (tableName == null || tableColumn == null || pkName == null) {
		// throw new BusException("传入志打印标志参数错误");
		// }
		// String sql = "update " + tableName + " set " + tableColumn +
		// "=1 where " + pkName + " in (" +
		// BlcgUtil.convertSetToSqlInPart(rpdd.getPks(), pkName)
		// + ")";
		// int num = DataBaseHelper.execute(sql, new Object[] {});
		// if (num != rpdd.getPks().size()) {
		// throw new BusException("设置打印标志时出错");
		// }
	}
}
