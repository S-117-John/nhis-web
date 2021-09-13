package com.zebone.nhis.pro.zsba.compay.ins.qgyb.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.zebone.platform.modules.mybatis.Mapper;

/**
 * 全国医保
 * @author Administrator
 *
 */
@Mapper
public interface InsQgybFeedetailMapper {
	
	//删除费用明细
	@Delete("delete from ins_feedetail_qg where del_flag = '0' and pk_inspvqg = #{pkInspvqg}")
	public void delInsFeedetailQg (@Param(value="pkInspvqg")String pkInspvqg);
	
	//删除费用明细
	@Update("update ins_feedetail_qg set del_flag = '1' where del_flag = '0' and pk_inspvqg = #{pkInspvqg}")
	public void updateInsFeedetailQg (@Param(value="pkInspvqg")String pkInspvqg);
}
