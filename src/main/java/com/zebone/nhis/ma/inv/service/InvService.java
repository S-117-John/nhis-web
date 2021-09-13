package com.zebone.nhis.ma.inv.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.bl.BlEmpInvoice;
import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlStInv;
import com.zebone.nhis.common.service.CommonService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.EnumerateParameter;
import com.zebone.nhis.ma.inv.dao.InvMapper;
import com.zebone.nhis.ma.inv.vo.NewNoReParam;
import com.zebone.nhis.ma.inv.vo.ReprintParam;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;

/**
 * 管理票据
 * @author Xulj
 * 
 */
@Service
public class InvService {

	@Autowired
	private InvMapper invMapper;

	@Resource
	private CommonService commonService;
	
	/**
	 * 查询票据领用
	 * 
	 * <pre>
	 * 1.根据系统参数，判断是全局还是跟人走的
	 * 2.如果是全局的 根据机器名称来查询
	 * 3.如果是根据人走的根据登入人员主键查询信息
	 * 
	 * 4.返回Bl_Emp_Invoice 表中信息，并返回票据类型里面的 euType和票据类型name属性。
	 * </pre>
	 * 
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getEmpInvoicesBySysParam(String param, IUser user) {

		/** 获取前台参数 */
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		String nameMachine = paramMap.get("nameMachine").toString();// 机器名称
		String activeState = paramMap.get("activeState").toString();// 1:当前使用，2所有
		/** 获取系统参数 */
		//1.住院发票使用方式
		String valZY = ApplicationUtils.getDeptSysparam("BL0006", UserContext.getUser().getPkDept());//使用科室级别系统参数
		
		//2.门诊发票使用方式
		String valMZ = ApplicationUtils.getDeptSysparam("BL0036", UserContext.getUser().getPkDept());//使用科室级别系统参数
		
		//3.预交金使用方式
		String valYJJ = ApplicationUtils.getDeptSysparam("BL0043", UserContext.getUser().getPkDept());//使用科室级别系统参数
				
		
		paramMap.put("pkEmp", ((User) user).getPkEmp());
		paramMap.put("pkOrg", ((User) user).getPkOrg());
		
		String sql = "select ei.*,i.eu_type,i.name as invcate_name from bl_emp_invoice ei "
				+ "left join bd_invcate i on i.pk_invcate = ei.pk_invcate "
				+ "where ei.del_flag = '0' and ei.pk_org = :pkOrg "; 
		List<Map<String, Object>> empInvoiceList = new ArrayList<Map<String, Object>>();
		//不配置，默认所有票据按私有
		if(CommonUtils.isEmptyString(valMZ)&&CommonUtils.isEmptyString(valZY)&&CommonUtils.isEmptyString(valYJJ)){
			sql += "and (ei.pk_emp_opera = :pkEmp  or ei.name_machine=:nameMachine)";
			if (EnumerateParameter.ONE.equals(activeState)) {
				empInvoiceList = DataBaseHelper.queryForList(sql + " and ei.flag_active = '1'", paramMap);
			} else if (EnumerateParameter.TWO.equals(activeState)) {
				empInvoiceList = DataBaseHelper.queryForList(sql, paramMap);
			}
			return empInvoiceList;
		}
		String wheresql = "";
		if ("0".equals(valZY)) {// 私用，根据登录人员查
			if(CommonUtils.isEmptyString(wheresql))
				wheresql = "and ( (ei.pk_emp_opera = :pkEmp and i.eu_type ='1' ) ";
			else
				wheresql = wheresql + " or (ei.pk_emp_opera = :pkEmp and i.eu_type ='1' )";
			
		} else if ("1".equals(valZY)) {// 公用，根据机器名称查
			if(CommonUtils.isEmptyString(wheresql))
				wheresql = "and ( (ei.name_machine = :nameMachine and i.eu_type ='1' ) ";
			else
				wheresql = wheresql + " or (ei.name_machine = :nameMachine and i.eu_type ='1' )";
		}
		if ("0".equals(valMZ)) {// 私用，根据登录人员查
			if(CommonUtils.isEmptyString(wheresql))
				wheresql = "and ( (ei.pk_emp_opera = :pkEmp and i.eu_type ='0' ) ";
			else
				wheresql = wheresql + " or (ei.pk_emp_opera = :pkEmp and i.eu_type ='0' )";
			
		} else if ("1".equals(valMZ)) {// 公用，根据机器名称查
			if(CommonUtils.isEmptyString(wheresql))
				wheresql = "and ( (ei.name_machine = :nameMachine and i.eu_type ='0' ) ";
			else
				wheresql = wheresql + " or (ei.name_machine = :nameMachine and i.eu_type ='0' )";
		}
		if ("0".equals(valYJJ)) {// 私用，根据登录人员查
			if(CommonUtils.isEmptyString(wheresql))
				wheresql = "and ( (ei.pk_emp_opera = :pkEmp and i.eu_type ='5' ) ";
			else
				wheresql = wheresql + " or (ei.pk_emp_opera = :pkEmp and i.eu_type ='5' )";
			
		} else if ("1".equals(valYJJ)) {// 公用，根据机器名称查
			if(CommonUtils.isEmptyString(wheresql))
				wheresql = "and ( (ei.name_machine = :nameMachine and i.eu_type ='5' ) ";
			else
				wheresql = wheresql + " or (ei.name_machine = :nameMachine and i.eu_type ='5' )";
		}
		wheresql = wheresql + " or ((ei.pk_emp_opera = :pkEmp or ei.name_machine=:nameMachine) and i.eu_type not in ('0','1','5') ) )"; //非门诊住院预交金发票，默认私有
		if (EnumerateParameter.ONE.equals(activeState)) {
			empInvoiceList = DataBaseHelper.queryForList(sql + wheresql + " and ei.flag_active = '1' order by ei.pk_invcate,date_opera asc ", paramMap);
		} else if (EnumerateParameter.TWO.equals(activeState)) {
			empInvoiceList = DataBaseHelper.queryForList(sql + wheresql+" order by ei.pk_invcate,date_opera asc ", paramMap);
		}
		
		return empInvoiceList;
	}
	
	/**
	 * 交易号：010003001008
	 * 查询所有在用票据
	 * @param param
	 * @param user
	 * @return
	 */
	public List<Map<String, Object>> getEmpInvoices(String param, IUser user) {
		
		String sql = "select ei.*,i.eu_type,i.name as invcate_name from bl_emp_invoice ei "
				+ "left join bd_invcate i on i.pk_invcate = ei.pk_invcate "
				+ "where ei.del_flag = '0' and ei.pk_org = ? and ei.flag_active = '1' order by ei.pk_emp_opera,ei.NAME_EMP_OPERA,ei.pk_invcate asc"; 
		List<Map<String, Object>> empInvoiceList = DataBaseHelper.queryForList(sql, new Object[]{((User) user).getPkOrg()});
		return empInvoiceList;
	}
	
	/**
	 * 票据保存
	 * 
	 * @param param
	 * <pre>
	 *     [{Bl_Emp_Invoice}]
	 * </pre>
	 * @param user
	 * @return
	 * <pre>
	 *     [{Bl_Emp_Invoice ,//表里面的字段属性,
	 *     "euType":票据类型的类型,
	 *     "invcateName":票据类型名称,
	 *     }]
	 * </pre>
	 */
	public List<Map<String, Object>> saveInvoiceList(String param, IUser user) {

		List<BlEmpInvoice> empInvoiceList = JsonUtil.readValue(param, new TypeReference<List<BlEmpInvoice>>() {
		});
		/** 获取当前系统参数 */
		String pkEmp = ((User) user).getPkEmp();// 操作员
		String nameEmp = ((User) user).getNameEmp();// 操作员姓名
		Date dateOpera = new Date();// 发生日期
		if(empInvoiceList!=null && empInvoiceList.size()!=0){
			String pkEmpinvsStr = "";//搜集保存的所有票据领用主键
			for (BlEmpInvoice empinv : empInvoiceList) {
				Long beginNo = empinv.getBeginNo();
				Long endNo = empinv.getEndNo();
				Long curNo = empinv.getCurNo();
				if(CommonUtils.isEmptyString(empinv.getPkEmpinv())) {
					empinv.setCntUse(endNo - curNo + 1);// 可使用张数=发票结束号-当前发票号+1
					empinv.setInvCount(endNo - beginNo + 1);// 发票领用/退张数（总数）=发票结束号-发票起始号+1
				}
				
				
				Map<String, Object> invcateMap = DataBaseHelper.queryForMap("select * from bd_invcate bi where bi.del_flag = '0' and bi.pk_invcate = ?",
						empinv.getPkInvcate());
				/**判断获取到的票据分类是否为空,如果为空,提示票据分类已经不存在**/
				if(invcateMap!=null && invcateMap.size()>0){
					/**校验1：每一条票据领用号码段 位数 是否与 票据分类处设置的一致**/
					if(invcateMap.get("length")!=null){
						Long length = Long.parseLong(invcateMap.get("length").toString());
						//通过发票终止号来比较
						if(endNo.toString().length() > length){
							throw new BusException("票据号位数大于票据项目维护处设置的固定号位数!");
						}
					}
				} else {
					throw new BusException("票据分类 ["+empinv.getInvcateName()+"] 不存在或已经删除！");
				}
				
				/**校验2：前台所传的票据领用可使用张数与总数之间是否合理（可使用张数必须小于等于总数）**/
				if (empinv.getCntUse() <= empinv.getInvCount()) {
					if(!CommonUtils.isEmptyString(empinv.getPkEmpOpera())){
						Map<String,Object> empMap = DataBaseHelper.queryForMap(
								"select NAME_EMP from BD_OU_EMPLOYEE where pk_emp = ?",
								new Object[]{empinv.getPkEmpOpera()});
						empinv.setNameEmpOpera(CommonUtils.getString(empMap.get("nameEmp")));// 操作员姓名
					}else{
						empinv.setPkEmpOpera(pkEmp);// 操作员
						empinv.setNameEmpOpera(nameEmp);// 操作员姓名
					}
					/**新增：票据领用**/
					if (empinv.getPkEmpinv() == null) {
						//票据分类主键
						String pkInvcate = invcateMap.get("pkInvcate").toString();
						//票据前缀
						String invPrefix = empinv.getInvPrefix();
						/**校验3：分类前缀+发票前缀+发票号码段组合，全局唯一**/
						Boolean isValidInv = true;
						isValidInv = validEmpInvNos("add",null,beginNo, endNo, pkInvcate,invPrefix,isValidInv);
						if(isValidInv == false){
							throw new BusException("新增的发票领用号码段与已有的存在重叠！");
						}
						
						empinv.setEuOptype("0");// 操作类型：0-领用（默认值）
						empinv.setDateOpera(dateOpera);// 发生日期
						
						empinv.setFlagActive("1");// 可使用标志 true - 1 , false - 0
						//判断票据前缀是不是空字符串 如果是则转换为null
						System.out.println("empinv.getInvPrefix:"+empinv.getInvPrefix());
						if(empinv.getInvPrefix()==null || empinv.getInvPrefix().equals("")){
							empinv.setInvPrefix(null);
						}
						empinv.setModifier(pkEmp);
						DataBaseHelper.insertBean(empinv);
						pkEmpinvsStr += "'" + empinv.getPkEmpinv() + "',";
					} else {
					/**修改：票据领用**/
						//票据分类主键
						String pkInvcate = invcateMap.get("pkInvcate").toString();
						//票据前缀
						String invPrefix = empinv.getInvPrefix();
						/**校验3：分类前缀+发票前缀+发票号码段组合，全局唯一**/
						Boolean isValidInv = true;
						isValidInv = validEmpInvNos("update",empinv.getPkEmpinv(),beginNo, endNo, pkInvcate,invPrefix,isValidInv);
						if(isValidInv == false){
							throw new BusException("修改的发票领用号码段与已有的存在重叠！");
						}
						
						ApplicationUtils.setDefaultValue(empinv, false);
						//empinv.setModifier(pkEmp);
						DataBaseHelper.updateBeanByPk(empinv, false);
						//DataBaseHelper.update(DataBaseHelper.getUpdateSql(BlEmpInvoice.class), empinv);
						//DataBaseHelper.execute(DataBaseHelper.getUpdateSql(BlEmpInvoice.class), args);
						pkEmpinvsStr += "'" + empinv.getPkEmpinv() + "',";
					}
				} else {
					throw new BusException("可使用张数>发票总数，不允许保存！");
				}
			}
			
			/**
			 * 增加返回字段
			 * "euType":票据类型的类型,
			 * "invcateName":票据类型名称,
			 */
			if(!"".equals(pkEmpinvsStr)){
				pkEmpinvsStr = pkEmpinvsStr.substring(0,pkEmpinvsStr.length()-1);
				String sql = "select ei.*,i.eu_type,i.name as invcate_name from bl_emp_invoice ei left join bd_invcate i on i.pk_invcate = ei.pk_invcate " + 
                             "where ei.del_flag = '0' and ei.pk_empinv in ("+ pkEmpinvsStr +") order by ei.pk_emp_opera,ei.NAME_EMP_OPERA,ei.pk_invcate asc";
				List<Map<String, Object>> empInvMapList = DataBaseHelper.queryForList(sql);
				return empInvMapList;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}

	/**
	 * 校验：当前前缀的号码段是否全局唯一
	 * @param oper add 新增/update 修改
	 * @param pkEmpinvoice null 新增/票据领用主键 修改
	 * @param beginNo 开始号
	 * @param endNo 结束号
	 * @param pkInvcate null 票据分类主键
	 * @Param invPrefix 票据前缀
	 * @param isValidInv 是否合理
	 * @return isValidInv 是否合理
	 */
	private Boolean validEmpInvNos(String oper,String pkEmpinvoice,Long beginNo, Long endNo, String pkInvcate,String invPrefix, Boolean isValidInv) {
		List<Map<String, Object>> benolist = null;
		if("add".equals(oper)){
			if(pkInvcate != null){
				benolist = DataBaseHelper.queryForList("select begin_no,end_no,INV_PREFIX from bl_emp_invoice where del_flag = '0' and pk_invcate = ?", pkInvcate);
			}else{
				benolist = DataBaseHelper.queryForList("select begin_no,end_no,INV_PREFIX from bl_emp_invoice where del_flag = '0' and pk_invcate is null");
			}
		}else if("update".equals(oper)){
			if(pkInvcate != null){
				benolist = DataBaseHelper.queryForList("select begin_no,end_no,INV_PREFIX from bl_emp_invoice where del_flag = '0' and pk_empinv != ? and pk_invcate = ?",pkEmpinvoice,pkInvcate);
			}else{
				benolist = DataBaseHelper.queryForList("select begin_no,end_no,INV_PREFIX from bl_emp_invoice where del_flag = '0' and pk_empinv != ? and pk_invcate is null",pkEmpinvoice);
			}
		}
		if(benolist != null && benolist.size() != 0){
			for(Map<String, Object> beno : benolist){
				Long beginNotmp = Long.parseLong(beno.get("beginNo").toString());
				Long endNotmp = Long.parseLong(beno.get("endNo").toString());
				
				if((invPrefix!=null && !invPrefix.equals(""))&&(beno.get("invPrefix")!=null && !beno.get("invPrefix").equals(""))){
					//判断票据前缀是否相同
					if(invPrefix.equals(beno.get("invPrefix").toString())){
						if(!(endNo<beginNotmp||beginNo>endNotmp)){
							isValidInv=false;
						}
					}
				} else if((invPrefix==null || invPrefix.equals(""))&&(beno.get("invPrefix")==null || beno.get("invPrefix").equals(""))) {
					if(!(endNo<beginNotmp||beginNo>endNotmp)){
						isValidInv=false;
					}
				}
			}
		}else{
			//该前缀的票据领用是第一次存入
			//isValidInv = true;
		}
		return isValidInv;
	}

	/**
	 * 根据主键删除票据领用
	 * @param param
	 * @param user
	 */
	public void delEmpInvoiceByPk(String param, IUser user) {

		String pkEmpinv = JsonUtil.getFieldValue(param, "pkEmpinv");
		Map<String, Object> empinv = DataBaseHelper.queryForMap("select * from bl_emp_invoice where del_flag = '0' and pk_empinv = ?", pkEmpinv);
		Long invCount = Long.parseLong(empinv.get("invCount").toString());
		Long cntUse = Long.parseLong(empinv.get("cntUse").toString());
		String flagActive = empinv.get("flagActive").toString();
		int count = DataBaseHelper.queryForScalar("select count(1) from bl_invoice where del_flag = '0' and pk_empinvoice = ?", Integer.class, pkEmpinv);
		if (invCount.equals(cntUse) && "1".equals(flagActive) && count == 0) {
			DataBaseHelper.update("update bl_emp_invoice set del_flag = '1' where pk_empinv = ?", new Object[] { pkEmpinv });
		} else {
			if (!(invCount.equals(cntUse))) {
				throw new BusException("该票据领用已被使用过！");
			}
			if ("0".equals(flagActive)) {
				throw new BusException("该票据领用当前不可用！");
			}
			if (count != 0) {
				throw new BusException("该票据领用被发票记录引用！");
			}
		}

	}

	/**
	 * 查询当前可用票据服务
	 * @param param
	 *            { "nameMachine":"机器名", "euType":"票据类型: 0 门诊发票，1 住院发票" }
	 * 
	 * @return 
	 * {
     * "invPrefix":"号码前缀",
     * "curNo":"当前票据号码",
     * "length":"票据号长度"(可为null),
     * "curCodeInv":"当前发票号值",
     * "cntUse":"剩余张数",
     * "pkEmpinv":"领用主键",
     * "pkInvcate":"票据分类"
     * }
	 */
	public Map<String, Object> searchCanUsedEmpInvoices(String param, IUser user) {

		/** 获取前台所传参数 */
		Map paramMap = JsonUtil.readValue(param, Map.class);
		String nameMachine = "";
		if(paramMap.get("nameMachine")!=null)
			nameMachine = paramMap.get("nameMachine").toString();
		String euType = paramMap.get("euType").toString();

		/** 获取当前系统参数 */
		//String valBL = ApplicationUtils.getSysparam("BL0006", false);// 发票使用方式（0私用,1公用）
		String valBL = null;
		String pkOrg = ((User) user).getPkOrg();
		String pkEmp = ((User) user).getPkEmp();
		if("0".equals(euType)){//门诊发票
			valBL = ApplicationUtils.getDeptSysparam("BL0036", UserContext.getUser().getPkDept());//使用科室级别系统参数
			if(valBL==null||"".equals(valBL)){
				throw new BusException("请维护当前科室门诊发票使用方式参数BL0036");
			}
		}else if("1".equals(euType)){
			valBL = ApplicationUtils.getDeptSysparam("BL0006", UserContext.getUser().getPkDept());//使用科室级别系统参数
			if(valBL==null||"".equals(valBL)){
				throw new BusException("请维护当前科室住院发票使用方式参数BL0006");
			}
		}else if("5".equals(euType)){
			valBL = ApplicationUtils.getDeptSysparam("BL0043", UserContext.getUser().getPkDept());//使用科室级别系统参数
			if(valBL==null||"".equals(valBL)){
				throw new BusException("请维护当前科室住院预交金使用方式参数BL0043");
			}
		}
		
		/** 获取当前可用票据 */
		Map<String, Object> queryForMap = getCanUsedEmpinv(euType, nameMachine, pkEmp, valBL, pkOrg);
		return queryForMap;
	}

	/**
	 * 确认使用票据服务
	 * @param param
	 *            { "pkEmpinv":"领用主键", "cnt":"使用张数" }
	 * @param user
	 * @return 更新状态
	 */
	public void confirmUseEmpInvoice(String param, IUser user) {

		/** 获取前台所传参数 */
		Map paramMap = JsonUtil.readValue(param, Map.class);
		String pkEmpinv = paramMap.get("pkEmpinv").toString();
		Long cnt = Long.parseLong(paramMap.get("cnt").toString());

		this.commonService.confirmUseEmpInv( pkEmpinv, cnt);
	}

	/**
	 * 修改当前票据号服务
	 * @param param
	 *            { "nameMachine":"机器名", "pkEmp":"操作员", "curno":"当前号码",
	 *            "afterno":"更新后的号码" }
	 * 
	 * @return 更新状态
	 */
	public void updateCurEmpInvoice(String param, IUser user) {

		/** 获取前台所传参数 */
		Map paramMap = JsonUtil.readValue(param, Map.class);
		String nameMachine = paramMap.get("nameMachine").toString();
		String pkEmp = paramMap.get("pkEmp").toString();
		Long curno = Long.parseLong(paramMap.get("curno").toString());
		Long afterno = Long.parseLong(paramMap.get("afterno").toString());

		/** 获取当前系统参数 */
		//String valBL = ApplicationUtils.getSysparam("BL0006", false);// 发票使用方式（0私用,1公用）
		String valBL = ApplicationUtils.getDeptSysparam("BL0006", UserContext.getUser().getPkDept());//使用科室级别系统参数
		String pkOrg = ((User) user).getPkOrg();

		Map<String, Object> empinv = new HashMap<String, Object>();
		if ("0".equals(valBL)) {
			empinv = DataBaseHelper
					.queryForMap(
							"select pk_empinv,begin_no,end_no,cur_no from bl_emp_invoice where del_flag = '0' and pk_org = ? and flag_active = '1' and flag_use = '1' and pk_emp_opera = ?",
							pkOrg, pkEmp);
			String pkEmpinv = empinv.get("pkEmpinv").toString();
			Long endNo = Long.parseLong(empinv.get("endNo").toString());
			Long datano = Long.parseLong(empinv.get("curNo").toString());
			if (curno != datano) {
				throw new BusException("入参（当前号码）不等于票据记录中当前使用号码！");
			} else {
				if (afterno > endNo) {
					throw new BusException("入参（更新后的号码）不属于票据记录中当前使用的号码段范围！");
				} else {
					DataBaseHelper.update("update bl_emp_invoice inv set inv.cur_no = ? where inv.pk_empinv = ?", afterno, pkEmpinv);
				}
			}
		} else if ("1".equals(valBL)) {
			empinv = DataBaseHelper
					.queryForMap(
							"select pk_empinv,begin_no,end_no,cur_no from bl_emp_invoice where del_flag = '0' and pk_org = ? and flag_active = '1' and flag_use = '1' and name_machine = ?",
							pkOrg, nameMachine);
			String pkEmpinv = empinv.get("pkEmpinv").toString();
			Long endNo = Long.parseLong(empinv.get("endNo").toString());
			Long datano = Long.parseLong(empinv.get("curNo").toString());
			if (curno != datano) {
				throw new BusException("入参（当前号码）不等于票据记录中当前使用号码！");
			} else {
				if (afterno > endNo) {
					throw new BusException("入参（更新后的号码）不属于票据记录中当前使用的号码段范围！");
				} else {
					DataBaseHelper.update("update bl_emp_invoice inv set inv.cur_no = ? where inv.pk_empinv = ?", afterno, pkEmpinv);
				}
			}
		}

	}

	/**
	 * 票据重打
	 * 
	 * <pre>
	 * 由票据号的唯一性决定，可以根据票据号code_inv查询出唯一的票据记录bl_invoice
	 * </pre>
	 * 
	 * @param param
	 *         {
	 *		     "nameMachine":"机器名",
	 *		     "euType":"票据类型: 0 门诊发票，1 住院发票",
	 *		     "invalidNoList":["作废号码1","作废号码2",""...]//字符串数组
	 *		   }
	 * @param user
	 * 
	 * @return [{ "newNo":"新票据号码" }]
	 */
	public List<NewNoReParam> rePrintInvoice(String param, IUser user) {

		/** 1.获取前台参数 */
		ReprintParam reprintParam = JsonUtil.readValue(param, ReprintParam.class);
		List<String> invalidNoList = reprintParam.getInvalidNoList();
		String nameMachine = reprintParam.getNameMachine();
		String euType = reprintParam.getEuType();
		// 获取当前系统参数
		//String valBL = ApplicationUtils.getSysparam("BL0006", false);// 发票使用方式（0私用,1公用）
		String valBL = ApplicationUtils.getDeptSysparam("BL0006", UserContext.getUser().getPkDept());//使用科室级别系统参数
		String pkOrg = ((User) user).getPkOrg();
		String pkEmp = ((User) user).getPkEmp();
		String nameEmp = ((User) user).getNameEmp();

		/** 2.查询当前可用票据-票据号=票据分类前缀+票据前缀+号段组成 */
		Map<String, Object> canUsedEmpinvMap = getCanUsedEmpinv(euType, nameMachine, pkEmp, valBL, pkOrg);
		String pkEmpinv = canUsedEmpinvMap.get("pkEmpinv").toString();// 领用主键
		//票据号=票据分类前缀+票据前缀+号段组成
		String prefix = canUsedEmpinvMap.get("prefix") == null?"":canUsedEmpinvMap.get("prefix").toString(); //票据分类前缀
		String invPrefix = canUsedEmpinvMap.get("invPrefix") == null?"":canUsedEmpinvMap.get("invPrefix").toString();//票据前缀
		Long curNo = Long.parseLong(canUsedEmpinvMap.get("curNo").toString());// 当前票据号码
		String cntUse = canUsedEmpinvMap.get("cntUse").toString();// 剩余张数
		String pkInvcate = canUsedEmpinvMap.get("pkInvcate").toString();// 票据分类
		long length = -1;//初始默认没有约束位数，在下面重打票据号时，新票据号codeInv不需要按位数格式化
		if(canUsedEmpinvMap.get("length") != null){
			length = Long.parseLong(canUsedEmpinvMap.get("length").toString());
		}
		// List<BlInvoice> newInvoiceList = new ArrayList<BlInvoice>();
		List<NewNoReParam> newNoList = new ArrayList<NewNoReParam>();
		
		/** 3.查询票据记录表中相应需要作废的票据记录集 */
		if (invalidNoList != null && invalidNoList.size() > 0 && invalidNoList.get(0) != null) {
			List<BlInvoice> invalidInvoiceList = this.invMapper.selectBlInvoicesByCodeInvList(invalidNoList, pkOrg);
			if (invalidInvoiceList != null && invalidInvoiceList.size() == invalidNoList.size()) {
				/**
				 * 4.当前剩余票数必须满足重打张数需求，即 可使用张数 >= 重打张数 对每一张作废的票据一一处理
				 * */
				if (Integer.parseInt(cntUse) >= invalidInvoiceList.size()) {
					DataBaseHelper.update("update bl_emp_invoice set cnt_use = cnt_use - ?, cur_no = cur_no + ? where pk_empinv = ?", invalidInvoiceList.size(),
							invalidInvoiceList.size(), pkEmpinv);

					for (BlInvoice invalidInv : invalidInvoiceList) {
						/** 插入重打的发票记录 **/
						BlInvoice newInv = new BlInvoice();
						newInv.setPkOrg(pkOrg);
						newInv.setPkEmpinvoice(pkEmpinv);
						newInv.setPkInvcate(invalidInv.getPkInvcate());
						String curCodeInv = null;
						if(length == -1){  //不需要格式化
							curCodeInv = prefix + invPrefix + curNo++;
						}else{             //需要格式化
							curCodeInv = prefix + invPrefix + this.flushLeft("0", length, (curNo++).toString());
						}
						newInv.setCodeInv(curCodeInv);
						newInv.setDateInv(new Date());
						newInv.setAmountInv(invalidInv.getAmountInv());
						newInv.setAmountPi(invalidInv.getAmountPi());
						newInv.setNote(invalidInv.getNote());
						newInv.setPkEmpInv(pkEmp);
						newInv.setNameEmpInv(nameEmp);
						newInv.setPrintTimes(1);
						newInv.setFlagCancel("0");// 新发票未作废
						newInv.setDateCancel(null);
						newInv.setPkEmpCancel(null);
						newInv.setNameEmpCancel(null);
						newInv.setFlagCc("0");// 结账用字段,新发票暂取默认值
						newInv.setPkCc(null);// 结账用字段,新发票暂取默认值
						newInv.setFlagCcCancel("0");// 结账用字段,新发票暂取默认值
						newInv.setPkCcCancel(null);// 结账用字段,新发票暂取默认值
						DataBaseHelper.insertBean(newInv);
						
						List<BlInvoiceDt> invoiceDtList = this.invMapper.getBlInvoiceDtsByPkInvoice(invalidInv.getPkInvoice());
						/** 插入重打的发票明细 **/
						for (BlInvoiceDt invoiceDt : invoiceDtList) {
							BlInvoiceDt newDt = new BlInvoiceDt();
							newDt.setPkInvoice(newInv.getPkInvoice());
							newDt.setPkBill(invoiceDt.getPkBill());
							newDt.setCodeBill(invoiceDt.getCodeBill());
							newDt.setNameBill(invoiceDt.getNameBill());
							newDt.setAmount(invoiceDt.getAmount());
							DataBaseHelper.insertBean(newDt);
						}

						List<BlStInv> stInvs = this.invMapper.getBlStInvsByPkInvoice(invalidInv.getPkInvoice(), pkOrg);
						/** 插入新的结算与发票对照 **/
						for (BlStInv stinv : stInvs) {
							BlStInv newstinv = new BlStInv();
							newstinv.setPkOrg(pkOrg);
							newstinv.setPkInvoice(newInv.getPkInvoice());
							newstinv.setPkSettle(stinv.getPkSettle());
							DataBaseHelper.insertBean(newstinv);
						}
						invalidInv.setFlagCancel("1");// 作废
						invalidInv.setDateCancel(new Date());
						invalidInv.setPkEmpCancel(pkEmp);
						invalidInv.setNameEmpCancel(nameEmp);
						DataBaseHelper.updateBeanByPk(invalidInv, false);
						// newInvoiceList.add(newInv);
						NewNoReParam newNo = new NewNoReParam();
						newNo.setNewNo(newInv.getCodeInv());
						newNoList.add(newNo);
					}
				} else {
					throw new BusException("剩余票数不足以重打！");
				}
			} else {
				throw new BusException("根据需要查询的作废票据记录集与所传的作废号码组不一致，需要作废的票据中可能存在无效的票据！");
			}
		} else {
			throw new BusException("请传入需要作废的票据号组！");
		}

		return newNoList;
	}

	/**
	 * 获取当前可用票据
	 * @param euType
	 * @param nameMachine
	 * @param pkEmp
	 * @param valBL
	 * @param pkOrg
	 * @return
	 * {
     * "invPrefix":"号码前缀",
     * "curNo":"当前票据号码",
     * "length":"票据号长度"(可为null),
     * "curCodeInv":"当前发票号值",
     * "cntUse":"剩余张数",
     * "pkEmpinv":"领用主键",
     * "pkInvcate":"票据分类"
     * }
	 */
	private Map<String, Object> getCanUsedEmpinv(String euType, String nameMachine, String pkEmp, String valBL, String pkOrg) {

		String sql = "select inv.inv_prefix,inv.cur_no,cate.length,inv.cnt_use,inv.pk_empinv,inv.pk_invcate, cate.prefix from bl_emp_invoice inv "
				+ "inner join bd_invcate cate on inv.pk_invcate=cate.pk_invcate where inv.del_flag = '0' and inv.pk_org = ? "
				+ "and inv.flag_use ='1' and inv.flag_active ='1' and cate.eu_type = ? ";
		Map<String, Object> queryForMap = new HashMap<String, Object>();
		if ("0".equals(valBL)) {
			sql += "and inv.pk_emp_opera = ?";
			queryForMap = DataBaseHelper.queryForMap(sql, pkOrg, euType, pkEmp);
		} else if ("1".equals(valBL)) {
			sql += "and inv.name_machine = ?";
			//获取住院发票共享的计算机名称
			String newNameMachine = qryShareInvComName(nameMachine);
			//如果未获取到BL0037参数，以当前工作站名称为参数获取可用发票号
			nameMachine = !CommonUtils.isEmptyString(newNameMachine)?newNameMachine:nameMachine;
			
			queryForMap = DataBaseHelper.queryForMap(sql, pkOrg, euType, nameMachine);
		}else{//无参数控制的票据，共享私有均可
			sql += "and (inv.pk_emp_opera = ? or inv.name_machine = ?)";
			queryForMap = DataBaseHelper.queryForMap(sql, pkOrg, euType, pkEmp,nameMachine);
		}
		if(queryForMap!=null){
			String curNo = null;
			if(queryForMap.get("curNo") == null){
				throw new BusException("当前存在可用票据，但是当前号为空！");
			}else{
				curNo = queryForMap.get("curNo").toString();
			}
			//票据号=票据分类前缀+票据前缀+号段组成
			String prefix = queryForMap.get("prefix") == null?"":queryForMap.get("prefix").toString(); //票据分类前缀
			String invPrefix = queryForMap.get("invPrefix") == null?"":queryForMap.get("invPrefix").toString();
			if(queryForMap.get("length") != null){
				long length = Long.parseLong(queryForMap.get("length").toString());
				curNo = this.flushLeft("0", length, curNo);
				String curCodeInv = prefix + invPrefix + curNo;
				queryForMap.put("curCodeInv", curCodeInv);
			}else{
				String curCodeInv = prefix + invPrefix + curNo;
				queryForMap.put("curCodeInv", curCodeInv);
			}
		}else{
			throw new BusException("已无可用票据！");
		}
		return queryForMap;
	}
	
	/**
	 * 获取住院发票共享的计算机名称
	 * @param nameMachine
	 * @return
	 */
	private String qryShareInvComName(String nameMachine){
		String sql = "select argu.arguval from bd_res_pc pc"
				+" left join bd_res_pc_argu argu on pc.PK_PC = argu.pk_pc and argu.flag_stop = '0' and argu.DEL_FLAG = '0'"
				+" where pc.flag_active = '1' and pc.del_flag = '0' and pc.eu_addrtype = '0' and argu.code_argu = 'BL0037' and pc.addr = ?";
	
		Map<String,Object> qryMap = DataBaseHelper.queryForMap(sql, nameMachine);
		
		String rtnName = "";
		
		if(qryMap!=null && qryMap.size()>0)
			rtnName = CommonUtils.getString(qryMap.get("arguval"));
		
		return rtnName;
	}
	
	/**
	 * 左边补齐 
	 * c 要填充的字符    
     * length 填充后字符串的总长度    
     * content 要格式化的字符串   
     */  
	private String flushLeft(String c, long length, String content){             
		String str = "";     
		String cs = "";     
		if (content.length() > length){     
			throw new BusException("异常：当前号码长度与指定长度不一致！");     
		}else{    
			for (int i = 0; i < length - content.length(); i++){     
				cs = cs + c;     
			}  
		}  
		str = cs + content;      
		return str;      
	}    
}
