package com.zebone.nhis.bl.pub.service.impl;

import com.zebone.nhis.bl.pub.service.IWinnoDrugService;
import com.zebone.nhis.common.module.bl.opcg.BlOpDt;
import com.zebone.nhis.common.module.ex.nis.ns.ExPresOcc;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.ma.pub.support.ExtSystemProcessUtils;
import com.zebone.nhis.ma.pub.zsrm.vo.DrugPresUsecateVo;
import com.zebone.nhis.ma.pub.zsrm.vo.PressAttInfo;
import com.zebone.platform.modules.dao.DataBaseHelper;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("zsrmWinnoDrugService")
public class ZsrmWinnoDrugServiceImpl implements IWinnoDrugService {
    @Override
    public Object getDrugWinno(Object... obj) {
       return  ExtSystemProcessUtils.processExtMethod("DrugWinnoRule", "getDrugWinno",obj);
    }

}
