package com.zebone.nhis.emr.nis.service;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.nd.record.NdRecordDt;
import com.zebone.nhis.emr.nis.vo.NdRecordRowsVo;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<NdRecordRowsVo> list = new ArrayList<>();
		NdRecordRowsVo row=new NdRecordRowsVo();
		row.setPkRecordrow("1");
		list.add(row);
		
		row=new NdRecordRowsVo();
		row.setPkRecordrow("2");
		list.add(row);
		
		List<NdRecordDt> dtlist = new ArrayList<NdRecordDt>();
		List<NdRecordDt> dtListAll = new ArrayList<NdRecordDt>();
		List<NdRecordDt> dtlistNew = new ArrayList<NdRecordDt>();
		NdRecordDt dt= new NdRecordDt();
		dt.setPkRecordrow("1");
		dt.setPkRecorddt("1");
		dtListAll.add(dt);
		
		dt= new NdRecordDt();
		dt.setPkRecordrow("1");
		dt.setPkRecorddt("2");
		dtListAll.add(dt);
	
		dt= new NdRecordDt();
		dt.setPkRecordrow("1");
		dt.setPkRecorddt("3");
		dtListAll.add(dt);
		
		dt= new NdRecordDt();
		dt.setPkRecordrow("2");
		dt.setPkRecorddt("1");
		dtListAll.add(dt);
		
		
		dt= new NdRecordDt();
		dt.setPkRecordrow("2");
		dt.setPkRecorddt("2");
		dtListAll.add(dt);
		int rowSize=list.size();
		if(dtListAll!=null&&dtListAll.size()>0){
			int dtSize=dtListAll.size();
			int i=0;
			int j=0;
			int rowCnt=0;
			String pkRecordrow="";
			dtlist = new ArrayList<NdRecordDt>();
			for(i=0;i<dtSize;i++){
				dt=dtListAll.get(i);
				String pkRowLoop=dt.getPkRecordrow();
				if(i==0){
					pkRecordrow = pkRowLoop;
					dtlist.add(dt);
				}else{
					if(pkRecordrow.equals(pkRowLoop)){
						//dtlist.add(dt);
					}else{
						//遇到不同row
						for(j=rowCnt;j<rowSize;j++){
							row=list.get(j);
							String pk=row.getPkRecordrow();
							if(pkRecordrow.equals(pk)){
								dtlistNew=new ArrayList<NdRecordDt>();
								dtlistNew.addAll(dtlist);
								row.setDtlist(dtlistNew);
								rowCnt++;
								break;
							}
						}
						dtlist = new ArrayList<NdRecordDt>();
						pkRecordrow=dt.getPkRecordrow();
					}
					dtlist.add(dt);
				}
			}
			//遇到不同row
			for(j=rowCnt;j<rowSize;j++){
				row=list.get(j);
				String pk=row.getPkRecordrow();
				if(pkRecordrow.equals(pk)){
					row.setDtlist(dtlist);
					rowCnt++;
					break;
				}
			}
		}
		System.out.println(list);
	}

}
