package com.zebone.nhis.pro.zsba.msg.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.pro.zsba.msg.vo.ConditionVo;
import com.zebone.nhis.pro.zsba.msg.vo.PacsVo;
import com.zebone.nhis.pro.zsba.msg.vo.PiPvVo;
import com.zebone.nhis.pro.zsba.msg.vo.Refuse;
import com.zebone.nhis.common.module.base.support.CvMsg;
import com.zebone.nhis.common.module.base.support.CvMsgCust;
import com.zebone.nhis.common.module.base.support.CvMsgSend;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface CriticalMsgMapper {
	public List<CvMsg> queryCriticalMsg(ConditionVo condition);
	public void deleteCriticalMsg(List<String> msgpk);
	public void deleteMsgCust(List<String> msgpk);
	public List<CvMsgCust> queryMsgCust(@Param("PkMsg")String PkMsg);
	public List<CvMsgCust> queryMsgCust_PvCode(Refuse refuse);
	public List<CvMsgSend> queryMsgSend(String PkMsg);
	public List<CvMsg> queryMsgReplyOne(ConditionVo cv);
	public PiPvVo queryPiPvMsgByPv(@Param("code")String code,@Param("eutype")String eupvtype);
	public PiPvVo queryPiPvMsgByPi(@Param("code")String code,@Param("eutype")String eupvtype);
	public List<String> queryCustByDept(@Param("deptcode")String code);
	public List<String> queryCustByGroup(@Param("groupcode")String code);
	public List<CvMsgCust> queryCustByEmp(@Param("list")List<String> code);
	public String queryRead(List<String> list);
	public void updateMsgSend(CvMsgSend cvMsgSend);
	
	public List<PacsVo> queryPacs(List<String> list);
	public List<PacsVo> queryPacsByNull();
	public Map<String,Object> queryDept(String patientId);
}
