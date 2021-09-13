package com.zebone.nhis.ma.inv.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zebone.nhis.common.module.bl.BlInvoice;
import com.zebone.nhis.common.module.bl.BlInvoiceDt;
import com.zebone.nhis.common.module.bl.BlStInv;
import com.zebone.platform.modules.mybatis.Mapper;

@Mapper
public interface InvMapper {
	
	/**
	 * 根据票据号查询票据记录
	 * @param codeinv
	 * @return
	 */
	BlInvoice selectBlInvoiceByCodeInv(@Param("codeinv")String codeinv, @Param("pkOrg")String pkOrg);
	
	/**
	 * 根据一组票据号查询对应有效（flag_cancel = '0'）票据记录集
	 * @param codeinvlist
	 * @return
	 */
	List<BlInvoice> selectBlInvoicesByCodeInvList(@Param("codeinvlist")List<String> codeinvlist, @Param("pkOrg")String pkOrg);
	
	/**
	 * 根据发票记录在对照表（bl_st_inv）中查询对应的一或多个收费结算
	 * @param pkInvoice
	 * @return
	 */
	List<BlStInv> getBlStInvsByPkInvoice(@Param("pkInvoice")String pkInvoice, @Param("pkOrg")String pkOrg);
	
	/**
	 * 根据发票记录主键查询相应的明细
	 * @param pkInvoice
	 * @return
	 */
	List<BlInvoiceDt> getBlInvoiceDtsByPkInvoice(@Param("pkInvoice")String pkInvoice);
}
