package com.zebone.nhis.common.module.compay.ins.shenzhen;

import java.util.List;

import com.zebone.nhis.common.module.scm.pub.BdPd;

public class BdPdAndCount {

		private int totalCount;

		//药品实体集合
		private List<BdPd> bdPdInfoList;

		public int getTotalCount() {
			return totalCount;
		}

		public void setTotalCount(int totalCount) {
			this.totalCount = totalCount;
		}

		public List<BdPd> getBdPdInfoList() {
			return bdPdInfoList;
		}

		public void setBdPdInfoList(List<BdPd> bdPdInfoList) {
			this.bdPdInfoList = bdPdInfoList;
		}




}
