package com.zebone.nhis.ex.pub.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.support.CommonUtils;

/**
 * 排序设置标志工具类  
 * @author yangxue
 * 
 */
public abstract class SortByOrdUtil {
	
		public void ordGroup(List<?> list){
			if(list == null || list.size() == 0){
				return;
			}
			int size = list.size();
			if(size == 2 && this.isSameGroup(list.get(0), list.get(1))){
				setSign(list.get(0), getSign(1));
				setSign(list.get(1), getSign(3));
				return;
			}
			for(int i = 1 ; i < size-1 ; i++){
				Object vo_before = list.get(i-1);
				Object vo = list.get(i);
				Object vo_next = list.get(i+1);
				//设置当前元素的标志
				if(isSameGroup(vo_before, vo)){
					//设置第一个元素的标志
					if(i == 1){
						setSign(vo_before, getSign(1));
					}
					if(isSameGroup(vo_next, vo)){
						setSign(vo, getSign(2));
						//设置最后一个元素的标志
						if(i == size - 2){
							setSign(vo_next, getSign(3));
						}
					}else{
						setSign(vo, getSign(3));
					}
				}else if(isSameGroup(vo_next, vo)){
					setSign(vo, getSign(1));
					//设置最后一个元素的标志
					if(i == size - 2){
						setSign(vo_next, getSign(3));
					}
				}
			}
		}
		
		protected String getSign(int i){
			if(i == 1)
				return "┌";
			else if(i == 2)
				return "|";
			else
				return "└";
		}
		
		@SuppressWarnings("unchecked")
		protected boolean isEqualField(Object vo_before, Object vo,String fieldName) {
			Map<String,Object> before = (Map<String,Object>)vo_before;
			String sp_be = before.get(fieldName) == null ? null : before.get(fieldName).toString();
			if(CommonUtils.isEmptyString(sp_be)){
				return false;
			}
			Map<String,Object> now = (Map<String,Object>)vo;
			String sp_no = now.get(fieldName) == null ? null : now.get(fieldName).toString();
			if(CommonUtils.isEmptyString(sp_no)){
				return false;
			}
			return sp_be.equals(sp_no);
		}
		
		protected abstract void setSign(Object o , String sign);
		
		protected abstract boolean isSameGroup(Object vo_before, Object vo);
		
		/**
		 * 对门诊处方明细进行分组
		 * @param list	处方明细的数组
		 */
		public void presGroup(List<Map<String,Object>> list){
					
			if (null == list || list.size() < 1) return;
			
			Map<Object, List<Map<?, Object>>> map = new HashMap<>();
			//list的数据是需要进行分组的处方,其中存在同组的情况,下面的操作是将同组的处方放在同一个list中再放入map.
			for (Map<?, Object> pres : list) {
				
				Object object = pres.get("parent");
				
				if (map.containsKey(object)) {
					//已存在
					List<Map<?, Object>> groupList = map.get(object);
					groupList.add(pres);
					
					map.put(object, groupList);
				} else {
					//新建
					List<Map<?, Object>> groupList = new ArrayList<Map<?,Object>>();
					groupList.add(pres);
					
					map.put(object, groupList);
				}
			}
			
			for (Object keyString : map.keySet()) {
				
				List<Map<?, Object>> presGroup = map.get(keyString);
				
				//获取每个分组的条目数,大于1的才需要进行分组
				int max = presGroup.size();
				if(max > 1){
					for (int i = 0; i <= max - 1; i++) {
						
						if(i == 0){
							setSign(presGroup.get(i), getSign(1));
						} else if (i == max-1) {
							setSign(presGroup.get(i), getSign(3));
						} else {
							setSign(presGroup.get(i), getSign(2));
						}
							
					}
					
				}
			}	
		}
}
