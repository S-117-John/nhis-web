package com.zebone.nhis.cn.ipdw.factory;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.cn.ipdw.vo.CnOrderInputVO;

/**
 * 医嘱抽象工厂类
 * @author leiminjian 2020.03.24
 *
 */
public abstract class AbstractCnOrderFactory {
		
	public abstract List<CnOrderInputVO> findOrdBaseList(Map<String,Object> paramMap);
}
