package com.zebone.nhis.pro.zsba.adt.vo;

import java.util.List;

public class SaveNhisDeptMapVo {

		public List<NhisDeptMap> addList;//新增
		public List<NhisDeptMap> upList;//更新
		public List<NhisDeptMap> delList;//删除
		
		public List<NhisDeptMap> getAddList() {
			return addList;
		}
		public void setAddList(List<NhisDeptMap> addList) {
			this.addList = addList;
		}
		public List<NhisDeptMap> getUpList() {
			return upList;
		}
		public void setUpList(List<NhisDeptMap> upList) {
			this.upList = upList;
		}
		public List<NhisDeptMap> getDelList() {
			return delList;
		}
		public void setDelList(List<NhisDeptMap> delList) {
			this.delList = delList;
		}
}
