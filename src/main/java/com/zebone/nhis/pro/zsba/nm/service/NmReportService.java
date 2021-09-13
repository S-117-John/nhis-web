package com.zebone.nhis.pro.zsba.nm.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.pro.zsba.common.support.FileUtils;
import com.zebone.nhis.pro.zsba.common.support.SystemPathUtils;
import com.zebone.nhis.pro.zsba.common.utils.ExcelUtil;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 汇总统计
 * @author lipz
 *
 */
@Service
public class NmReportService {
	
	private static Logger logger = LoggerFactory.getLogger(NmReportService.class);
	
	/**
	 * 已付款数据汇总查询
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> reportList(String param , IUser user){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		param = param.replaceAll("\r|\n", "");
		Map<String,String> params = JsonUtil.readValue(param, Map.class);
		String pkOrg = params.get("pkOrg");
		if(StringUtils.isEmpty(pkOrg)){
			pkOrg = UserContext.getUser().getPkOrg();
		}
		String pkDept = params.get("pkDept");
		String inputDept = params.get("inputDept");
		String codePv = params.get("codePv");
		String pvType = params.get("pvType");
		String dateBegin = params.get("dateBegin");
		String dateEnd = params.get("dateEnd");
		
		// 获取项目
		List<Map<String,Object>> itemAllList = getItems(pkOrg, "");
		if(itemAllList.isEmpty()){
			throw new BusException("未找到在用的收费项目集！");
		}

		/*
		 * 1.设置表格标题数据
		 */
		List<Map<String,Object>> titleList = new ArrayList<>();
		
		Map<String, Object> patMap1 = new HashMap<>();
		patMap1.put("pkCi", "patientName");
		patMap1.put("nameItem", "病人姓名");
		Map<String, Object> patMap2 = new HashMap<>();
		patMap2.put("pkCi", "codePv");
		patMap2.put("nameItem", "就诊号");
		Map<String, Object> patMap3 = new HashMap<>();
		patMap3.put("pkCi", "times");
		patMap3.put("nameItem", "就诊次数");
		titleList.add(patMap1);
		titleList.add(patMap2);
		titleList.add(patMap3);
		
		StringBuffer pkCiTxt = new StringBuffer("");
		List<Map<String,Object>> itemList = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map : itemAllList) {
			BigDecimal ciSettAmt = sumSettAmt(inputDept, map.get("pkCi").toString(), dateBegin, dateEnd, pvType);
			if(ciSettAmt.compareTo(BigDecimal.ZERO) == 1){
				titleList.add(map);
				itemList.add(map);
				pkCiTxt.append("'").append(map.get("pkCi").toString()).append("',");
			}
		}
		
		Map<String, Object> patMap4 = new HashMap<>();
		patMap4.put("pkCi", "settSum");
		patMap4.put("nameItem", "已结算总额");
		Map<String, Object> patMap5 = new HashMap<>();
		patMap5.put("pkCi", "refundSum");
		patMap5.put("nameItem", "已退款总额");
		Map<String, Object> patMap6 = new HashMap<>();
		patMap6.put("pkCi", "paySum");
		patMap6.put("nameItem", "已付款总额");
		titleList.add(patMap4);
		titleList.add(patMap5);
		titleList.add(patMap6);
		
		resultMap.put("itemList", titleList);
		
		/*
		 * 2. 设置获表格数据
		 */
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		
		String pkCis = pkCiTxt.toString();
		if(pkCis.length()>0){
			pkCis = pkCis.substring(0, pkCis.length()-1);
			
			List<Map<String, Object>> patList = getPatInfo(pkOrg, pkDept, inputDept, codePv, pvType, dateBegin, dateEnd);
			for (Map<String, Object> map : patList) {
				Map<String, Object> patMap = new HashMap<String, Object>();
				patMap.put("patientName", map.get("namePi"));
				patMap.put("codePv", map.get("codePv"));
				patMap.put("times", map.get("times"));
				//各个计费项目小计总额
				List<Map<String, Object>> ciAmtList = sumSettGroupPkCi(inputDept, pkCis, map.get("pkPv").toString(), Integer.parseInt(map.get("times").toString()), dateBegin, dateEnd);
				for (Map<String,Object> itemMap : itemList) {
					patMap.put(itemMap.get("pkCi").toString(), new BigDecimal("0.00"));
					for(Map<String,Object> ciAmt : ciAmtList){
						if(itemMap.get("pkCi").toString().equals(ciAmt.get("pkCi").toString())){
							patMap.put(itemMap.get("pkCi").toString(), new BigDecimal(ciAmt.get("total").toString()));
							break;
						}
					}
					//patMap.put(itemMap.get("pkCi").toString(), sumSett(inputDept, itemMap.get("pkCi").toString(), map.get("pkPv").toString(), Integer.parseInt(map.get("times").toString()), dateBegin, dateEnd));
				}
				//已结算总额
				BigDecimal settSum = sumSett(inputDept, null, map.get("pkPv").toString(), Integer.parseInt(map.get("times").toString()), dateBegin, dateEnd);
				patMap.put("settSum", settSum);
				//已退款总额
				BigDecimal refundSum = sumRefund(inputDept, map.get("pkPv").toString(), dateBegin, dateEnd);
				if(refundSum.compareTo(new BigDecimal(0))!=0){
					refundSum = refundSum.multiply(new BigDecimal(-1));
				}
				patMap.put("refundSum", refundSum);
				//已付款总额
				patMap.put("paySum", sumPay(inputDept, map.get("pkPv").toString(), dateBegin, dateEnd));
				
				resultList.add(patMap);
				
			}
		}
		resultMap.put("dataList", resultList);
		
		return resultMap;
	}
	
	/**
	 * 导出Excle文件数据
	 * @param param
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String exportExcle(String param , IUser user){
		param = param.replaceAll("\r|\n", "");
		Map<String,String> params = JsonUtil.readValue(param, Map.class);
		String pkOrg = params.get("pkOrg");
		if(StringUtils.isEmpty(pkOrg)){
			pkOrg = UserContext.getUser().getPkOrg();
		}
		String pkDept = params.get("pkDept");
		String inputDept = params.get("inputDept");
		String codePv = params.get("codePv");
		String pvType = params.get("pvType");
		String dateBegin = params.get("dateBegin");
		String dateEnd = params.get("dateEnd");
		
		// 获取项目
		List<Map<String,Object>> itemAllList = getItems(pkOrg, "");
		if(itemAllList.isEmpty()){
			throw new BusException("未找到在用的收费项目集！");
		}
		/*
		 * 1.设置表格标题数据
		 */
		StringBuffer pkCiTxt = new StringBuffer("");
		List<Map<String,Object>> itemList = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map : itemAllList) {
			//过滤没有结算状态计费数据的项目
			BigDecimal ciSettAmt = sumSettAmt(inputDept, map.get("pkCi").toString(), dateBegin, dateEnd, pvType);
			if(ciSettAmt.compareTo(BigDecimal.ZERO) == 1){
				itemList.add(map);
				pkCiTxt.append("'").append(map.get("pkCi").toString()).append("',");
			}
		}
		//表头
		int len = itemList.size() + 6;
        String[] titles = new String[len];
        titles[0] = "病人姓名";
        titles[1] = "就诊号";
        titles[2] = "就诊次数";
        for(int i=0; i<itemList.size(); i++){
        	Map<String, Object> item = itemList.get(i);
        	titles[i+3] = String.valueOf(item.get("nameItem")).replaceAll("-", "\n");
        }
        titles[len-3] = "已结算\n总额";
        titles[len-2] = "已退款\n总额";
        titles[len-1] = "已付款\n总额";
        
		/*
		 * 2. 设置获表格数据
		 */
		BigDecimal settSumTotal = new BigDecimal(0);
		BigDecimal refundSumTotal = new BigDecimal(0);
		BigDecimal paySumTotal = new BigDecimal(0);
		List<Object[]> dataList = new ArrayList<Object[]>();
		String pkCis = pkCiTxt.toString();
		if(pkCis.length()>0){
			pkCis = pkCis.substring(0, pkCis.length()-1);
			
			List<Map<String, Object>> patList = getPatInfo(pkOrg, pkDept, inputDept, codePv, pvType, dateBegin, dateEnd);
			for (Map<String, Object> map : patList) {
				Object[] patData = new Object[len];
				patData[0] = map.get("namePi").toString();
				patData[1] = map.get("codePv").toString();
				patData[2] = map.get("times").toString();
				
				//各个计费项目小计总额
				List<Map<String, Object>> ciAmtList = sumSettGroupPkCi(inputDept, pkCis, map.get("pkPv").toString(), Integer.parseInt(map.get("times").toString()), dateBegin, dateEnd);
				for(int k=0; k<itemList.size(); k++){
					patData[k+3] = new BigDecimal("0.00");
		        	Map<String, Object> item = itemList.get(k);
		        	for(Map<String, Object> ciAmt : ciAmtList){
		        		if(item.get("pkCi").toString().equals(ciAmt.get("pkCi").toString())){
		        			patData[k+3] = new BigDecimal(ciAmt.get("total").toString());
		        		}
		        	}
		        	//patData[k+3] = sumSett(inputDept, item.get("pkCi").toString(), map.get("pkPv").toString(), Integer.parseInt(map.get("times").toString()), dateBegin, dateEnd).doubleValue();
		        }
				//已结算总额
				BigDecimal sumSett = sumSett(inputDept, null, map.get("pkPv").toString(), Integer.parseInt(map.get("times").toString()), dateBegin, dateEnd);
				patData[len-3] = sumSett.doubleValue();
				//已退款总额
				BigDecimal sumRefund = sumRefund(inputDept, map.get("pkPv").toString(), dateBegin, dateEnd);
				if(sumRefund.compareTo(new BigDecimal(0))!=0){
					sumRefund = sumRefund.multiply(new BigDecimal(-1));
				}
				patData[len-2] = sumRefund.doubleValue();
				//已付款总额
				BigDecimal sumPay = sumPay(inputDept, map.get("pkPv").toString(), dateBegin, dateEnd);
				patData[len-1] = sumPay.doubleValue();
				
				dataList.add(patData);
				
				settSumTotal = settSumTotal.add(sumSett);
				refundSumTotal = refundSumTotal.add(sumRefund);
				paySumTotal = paySumTotal.add(sumPay);
			}
			
			/*
			 * 3.合计数据
			 */	
			Object[] totalData = new Object[len];
			totalData[0] = "合计";
			for(int i=0; i<itemList.size(); i++){
	        	Map<String, Object> item = itemList.get(i);
	        	totalData[i+3] = sumSett(inputDept, item.get("pkCi").toString(), null, null, dateBegin, dateEnd).doubleValue();
	        }
			totalData[len-3] = settSumTotal.doubleValue();
			totalData[len-2] = refundSumTotal.doubleValue();
			totalData[len-1] = paySumTotal.doubleValue();
			dataList.add(totalData);
						
			/*
			 * 4.差额
			 */
			String[] ceData = new String[len];
			ceData[0] = "差额："+ String.valueOf(settSumTotal.subtract(refundSumTotal).subtract(paySumTotal).setScale(2, BigDecimal.ROUND_HALF_UP));
			dataList.add(ceData);
		}
		
		/*
		 * 5.日期 - 时间
		 */
		String[] tiemData = new String[len];
		tiemData[0] = "数据日期：("+dateBegin+"~"+dateEnd+"), 导出时间："+ DateUtils.getDateTime();
		dataList.add(tiemData);
		
		//导出Excel开始
		String loadFileUrl = "/src/main/static/exportExcle/[3][4]";
		WritableWorkbook wwb = null;
		try {
			String projectPath = SystemPathUtils.filePath("");
			String filePath = projectPath + "src/main/static/exportExcle/";
			FileUtils.createDirectory(filePath);
			String fileName = "非医疗费用收费汇总统计("+DateUtils.getDate("yyyyMMddHHmmss")+")";
			String hz = ".xls";
			
	        wwb = ExcelUtil.createWorkBook(filePath +"/"+ fileName+hz);//写入本地文件
	        String[] names = {fileName};
	        List<WritableSheet> sheets = ExcelUtil.createSheet(names, wwb);
	        
	        WritableCellFormat titleFormat = ExcelUtil.getTitleFormat(15);

            //针对不同sheet填充数据
            for(WritableSheet sheet : sheets){
            	if(sheet.getName().equals(fileName)){
            		//标题
            		ExcelUtil.addCell(sheet, 0, 0, fileName, titleFormat);
                    //填充数据
                    ExcelUtil.setSheetData(sheet, dataList, titles);
                    //合并标题
                    sheet.mergeCells(0, 0, titles.length-1, 0);
                    
                    if(dataList.size()>0){
                    	//合并合计
                        sheet.mergeCells(0, dataList.size()-1, 2, dataList.size()-1);
                    	//合并差额
                        sheet.mergeCells(0, dataList.size(), len-1, dataList.size());
                        //合并时间日期
                        sheet.mergeCells(0, dataList.size()+1, len-1, dataList.size()+1);
                    }
            	}
            }
            wwb.write();
            
            loadFileUrl = loadFileUrl.replace("[3]", fileName).replace("[4]", hz);

			logger.info("导出文件下载路径：{}", loadFileUrl);
		} catch (Exception e) {
			throw new BusException("导出数据，系统异常：" + e.getMessage());
		}finally{
            if(wwb!=null){
                try {
                    wwb.close();
                    wwb = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
		return loadFileUrl;//返回下载地址
	}
	
	
	
	
	private List<Map<String,Object>> getItems(String pkOrg, String delFalg){
		StringBuffer sql = new StringBuffer("SELECT pk_ci, name_item FROM nm_charge_item WHERE 1=1");
		if (StringUtils.isNotEmpty(pkOrg)) {
			sql.append(" and pk_org='"+pkOrg+"' ");
		}
		if (StringUtils.isNotEmpty(delFalg)) {
			sql.append(" and del_falg='"+delFalg+"' ");
		}
		sql.append(" ORDER BY del_flag asc, code_item asc ");
		return DataBaseHelper.queryForList(sql.toString(), new Object[]{});
	}
	
	private BigDecimal sumSettAmt(String inputDept, String pkCi, String dateBegin, String dateEnd, String pvType){
		BigDecimal result = new BigDecimal("0.00");
		StringBuffer sql = new StringBuffer("select sum(total) total from nm_ci_st_details where is_sett='1'");
		sql.append(" and pk_ci='"+pkCi+"' and pv_type='"+pvType+"'");
		sql.append(" and charge_time>='"+dateBegin+" 00:00:00' and charge_time<='"+dateEnd+" 23:59:59'");
		if (StringUtils.isNotEmpty(inputDept)) {
			sql.append(" and input_dept='"+inputDept+"' ");
		}
		Map<String, Object> data = DataBaseHelper.queryForMap(sql.toString(), new Object[]{});
		if(data!=null && data.containsKey("total") && data.get("total")!=null){
			result = new BigDecimal(data.get("total").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		return result;
	}
		
	private List<Map<String, Object>> getPatInfo(String pkOrg, String pkDept, String inputDept, String codePv, String pvType, String dateBegin, String dateEnd){
		StringBuffer sql = new StringBuffer("");
		// pvType: 1门诊、2住院
		if("2".equals(pvType)){
			sql.append(" SELECT std.pk_pv, std.code_pv, std.times, pi.NAME_PI  ");
			sql.append(" FROM nm_ci_st_details std ");
			sql.append(" LEFT JOIN PI_MASTER pi ON pi.CODE_IP = std.code_pv ");
			sql.append(" WHERE std.pv_type='2' ");
			if (StringUtils.isNotEmpty(pkOrg)) {
				sql.append(" and std.pk_org='"+pkOrg+"' ");
			}
			if (StringUtils.isNotEmpty(pkDept)) {
				sql.append(" and std.pk_dept='"+pkDept+"' ");
			}
			if (StringUtils.isNotEmpty(inputDept)) {
				sql.append(" and std.input_dept='"+inputDept+"' ");
			}
			if (StringUtils.isNotEmpty(codePv)) {
				sql.append(" and std.code_pv='"+codePv+"' ");
			}
			if (StringUtils.isNotEmpty(dateBegin)) {
				sql.append(" and std.charge_time>='"+dateBegin+" 00:00:00' ");
			}
			if (StringUtils.isNotEmpty(dateEnd)) {
				sql.append(" and std.charge_time<='"+dateEnd+" 23:59:59' ");
			}
			sql.append(" GROUP BY std.pk_pv, std.code_pv, std.times, pi.NAME_PI ");
			
		}else{
			sql.append(" SELECT std.pk_pv, std.code_pv, std.times, std.name_pi  ");
			sql.append(" FROM nm_ci_st_details std ");
			sql.append(" WHERE std.pv_type='1' ");
			if (StringUtils.isNotEmpty(pkOrg)) {
				sql.append(" and std.pk_org='"+pkOrg+"' ");
			}
			if (StringUtils.isNotEmpty(pkDept)) {
				sql.append(" and std.pk_dept='"+pkDept+"' ");
			}
			if (StringUtils.isNotEmpty(inputDept)) {
				sql.append(" and std.input_dept='"+inputDept+"' ");
			}
			if (StringUtils.isNotEmpty(codePv)) {
				sql.append(" and std.code_pv='"+codePv+"' ");
			}
			if (StringUtils.isNotEmpty(dateBegin)) {
				sql.append(" and std.charge_time>='"+dateBegin+" 00:00:00' ");
			}
			if (StringUtils.isNotEmpty(dateEnd)) {
				sql.append(" and std.charge_time<='"+dateEnd+" 23:59:59' ");
			}
			sql.append(" GROUP BY std.pk_pv, std.code_pv, std.times, std.name_pi ");
		}
		return DataBaseHelper.queryForList(sql.toString(), new Object[]{});
	}
	
	private List<Map<String, Object>> sumSettGroupPkCi(String inputDept, String pkCi, String pkPv, Integer times, String dateBegin, String dateEnd){
		StringBuffer sql = new StringBuffer("select pk_ci, ISNULL(sum(total), 0) as total from nm_ci_st_details where charge_time is not null ");
		if (StringUtils.isNotEmpty(inputDept)) {
			sql.append(" and input_dept='"+inputDept+"' ");
		}
		if (StringUtils.isNotEmpty(pkCi)) {
			sql.append(" and pk_ci in ("+pkCi+") ");
		}
		if (StringUtils.isNotEmpty(pkPv)) {
			sql.append(" and pk_pv='"+pkPv+"' ");
		}
		if(times!=null){
			sql.append(" and times="+times+" ");
		}
		if (StringUtils.isNotEmpty(dateBegin)) {
			sql.append(" and charge_time>='"+dateBegin+" 00:00:00' ");
		}
		if (StringUtils.isNotEmpty(dateEnd)) {
			sql.append(" and charge_time<='"+dateEnd+" 23:59:59' ");
		}
		sql.append(" group by pk_ci");
		
		return DataBaseHelper.queryForList(sql.toString(), new Object[]{});
	}

	private BigDecimal sumSett(String inputDept, String pkCi, String pkPv, Integer times, String dateBegin, String dateEnd){
		BigDecimal result = new BigDecimal("0.00");
		StringBuffer sql = new StringBuffer("select sum(total) as total from nm_ci_st_details where charge_time is not null ");
		if (StringUtils.isNotEmpty(inputDept)) {
			sql.append(" and input_dept='"+inputDept+"' ");
		}
		if (StringUtils.isNotEmpty(pkCi)) {
			sql.append(" and pk_ci='"+pkCi+"' ");
		}
		if (StringUtils.isNotEmpty(pkPv)) {
			sql.append(" and pk_pv='"+pkPv+"' ");
		}
		if(times!=null){
			sql.append(" and times="+times+" ");
		}
		if (StringUtils.isNotEmpty(dateBegin)) {
			sql.append(" and charge_time>='"+dateBegin+" 00:00:00' ");
		}
		if (StringUtils.isNotEmpty(dateEnd)) {
			sql.append(" and charge_time<='"+dateEnd+" 23:59:59' ");
		}
		Map<String, Object> data = DataBaseHelper.queryForMap(sql.toString(), new Object[]{});
		if(data!=null && data.containsKey("total") && data.get("total")!=null){
			result = new BigDecimal(data.get("total").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		return result;
	}
	
	private BigDecimal sumPay(String inputDept, String pkPv, String dateBegin, String dateEnd){
		BigDecimal result = new BigDecimal("0.00");
		StringBuffer sql = new StringBuffer("select sum(amount) as total from nm_ci_st where is_pay in ('1', '3') ");
		if (StringUtils.isNotEmpty(inputDept)) {
			sql.append(" and input_dept='"+inputDept+"' ");
		}
		if (StringUtils.isNotEmpty(pkPv)) {
			sql.append(" and pk_pv='"+pkPv+"' ");
		}
		if (StringUtils.isNotEmpty(dateBegin)) {
			sql.append(" and charge_time>='"+dateBegin+" 00:00:00' ");
		}
		if (StringUtils.isNotEmpty(dateEnd)) {
			sql.append(" and charge_time<='"+dateEnd+" 23:59:59' ");
		}
		Map<String, Object> data = DataBaseHelper.queryForMap(sql.toString(), new Object[]{});
		if(data!=null && data.containsKey("total") && data.get("total")!=null){
			result = new BigDecimal(data.get("total").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		return result;
	}
		
	private BigDecimal sumRefund(String inputDept, String pkPv, String dateBegin, String dateEnd){
		BigDecimal result = new BigDecimal("0.00");
		StringBuffer sql = new StringBuffer("select sum(amount) as amount from nm_ci_st where is_pay in ('2','3') and amount<0 ");
		if (StringUtils.isNotEmpty(inputDept)) {
			sql.append(" and input_dept='"+inputDept+"' ");
		}
		if (StringUtils.isNotEmpty(pkPv)) {
			sql.append(" and pk_pv='"+pkPv+"' ");
		}
		if (StringUtils.isNotEmpty(dateBegin)) {
			sql.append(" and refund_time>='"+dateBegin+" 00:00:00' ");
		}
		if (StringUtils.isNotEmpty(dateEnd)) {
			sql.append(" and refund_time<='"+dateEnd+" 23:59:59' ");
		}
		Map<String, Object> data = DataBaseHelper.queryForMap(sql.toString(), new Object[]{});
		if(data!=null && data.containsKey("amount") && data.get("amount")!=null){
			result = new BigDecimal(data.get("amount").toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		return result;
	}

}
