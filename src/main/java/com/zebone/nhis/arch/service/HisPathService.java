package com.zebone.nhis.arch.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zebone.nhis.common.arch.vo.PatiInfo;
import com.zebone.nhis.common.support.CommonUtils;

@Service
public class HisPathService {

	static final String ORD = "0006";
	static final String ROOT = "HisPaths/Ords/";
	static final String suffix = ".pdf";
	
	public List<String> get_HISPathByDoctype(PatiInfo vo, String codeDoctype) {
		
		List<String> paths = new ArrayList<String>();
		if(vo!=null && !CommonUtils.isEmptyString(vo.getCodePv())){
			if(ORD.equals(codeDoctype)){
				String fileName = ROOT+"ORD"+vo.codePv+suffix;
				paths.add(fileName);
			}
		}
		return paths;
	}
	
	

}
