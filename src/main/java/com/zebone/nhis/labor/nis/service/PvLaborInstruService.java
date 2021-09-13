package com.zebone.nhis.labor.nis.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.module.labor.nis.PvLaborInstru;
import com.zebone.nhis.common.module.labor.nis.PvLaborInstruDt;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.labor.nis.dao.PvLaborInstruMapper;
import com.zebone.nhis.labor.nis.vo.PvLaborInstruVo;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
/**
 * 器械清点单
 * @author yangxue
 *
 */
@Service
public class PvLaborInstruService {
	@Resource
	private PvLaborInstruMapper pvLaborInstruMapper;
    /**
     * 查询器械清点单 
     * @param param{pkPv,pkLaborrec}
     * @param user
     * @return
     */
	public PvLaborInstruVo queryInstru(String param,IUser user){
		Map<String,Object> paramMap = JsonUtil.readValue(param, Map.class);
		paramMap.put("pkOrg", ((User)user).getPkOrg());
		PvLaborInstruVo vo  = pvLaborInstruMapper.queryPvLaborInstru(paramMap);
		if(vo !=null && !CommonUtils.isEmptyString(vo.getPkInstru())){
			List<PvLaborInstruDt> dtlist = pvLaborInstruMapper.queryPvLaborInstruDt(vo.getPkInstru());
			vo.setDtlist(dtlist);
			vo.setFlagInit("0");
		}else{
			vo = new PvLaborInstruVo();
			List<PvLaborInstruDt> dtlist = DataBaseHelper.queryForList(" select doc.PK_DEFDOC ,doc.CODE as dtInstrument,doc.NAME  "
					+ "from bd_defdoc doc  where doc.CODE_DEFDOCLIST  = '140007' ", PvLaborInstruDt.class, new Object[]{});
			vo.setFlagInit("1");
			vo.setDtlist(dtlist);
		}
		return vo;
	}
	
	/**
	 * 保存器械清点单
	 * @param param
	 * @param user
	 */
	public String saveInstru(String param,IUser user){
		PvLaborInstruVo vo = JsonUtil.readValue(param, PvLaborInstruVo.class);
		if(vo == null){
			throw new BusException("未获取到要保存的内容！");
		}
		String pk_instr = "";
		PvLaborInstru ins = new PvLaborInstru();
		ApplicationUtils.copyProperties(ins, vo);
		if (CommonUtils.isEmptyString(ins.getPkInstru())) {//新增
			pk_instr = NHISUUID.getKeyId();
			ins.setPkInstru(pk_instr);
			DataBaseHelper.insertBean(ins);
		}else{//修改
			pk_instr = ins.getPkInstru();
//			DataBaseHelper.updateBeanByPk(ins, false);
			//手动写的更新语句，预防传入的手术开始或者结束时间为空的情况
			StringBuilder sql = new StringBuilder("update pv_labor_instru set pk_pv=:pkPv, pk_op=:pkOp, name_op=:nameOp, ");
			sql.append(" date_begin_op=:dateBeginOp, date_end_op=:dateEndOp, dt_ansi=:dtAnsi, create_time = :createTime, ");
			sql.append(" flag_check=:flagCheck, note=:note, pk_recive=:pkRecive, pk_nurse=:pkNurse ");
			sql.append(" where pk_instru=:pkInstru ");
			DataBaseHelper.update(sql.toString(),ins);
			DataBaseHelper.execute("delete from pv_labor_instru_dt where pk_instru = ?",new Object[]{pk_instr});
		}
		//插入
		if(vo.getDtlist()!=null&&vo.getDtlist().size()>0){
			for(PvLaborInstruDt dt:vo.getDtlist()){
				dt.setPkInstru(pk_instr);
				DataBaseHelper.insertBean(dt);
			}
		}
		return pk_instr;
	}
	/**
	 * 删除器械清点单
	 * @param param{pkInstru}
	 * @param user
	 */
	public void deleteInstru(String param,IUser user){
		String pkInstru = JsonUtil.readValue(param, String.class);
		if(CommonUtils.isEmptyString(pkInstru)){
			throw new BusException("未获取到要删除的器械清点单主键！");
		}
		//删除清点单
		DataBaseHelper.execute("delete from pv_labor_instru where pk_instru = ?",new Object[]{pkInstru});
		//删除明细
		DataBaseHelper.execute("delete from pv_labor_instru_dt where pk_instru = ?",new Object[]{pkInstru});
		
	}
	
}
