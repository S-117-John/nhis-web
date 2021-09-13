package com.zebone.nhis.ma.pub.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("return")
public class Response {
	
		@XStreamAsAttribute
		private String ITSVersion="1.0";
		
		private String id;
		private String createTime;
		private String actionId;
		private String actionName;
		private Result result;
		public String getITSVersion() {
			return ITSVersion;
		}
		public void setITSVersion(String iTSVersion) {
			ITSVersion = iTSVersion;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getCreateTime() {
			return createTime;
		}
		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}
		public String getActionId() {
			return actionId;
		}
		public void setActionId(String actionId) {
			this.actionId = actionId;
		}
		public String getActionName() {
			return actionName;
		}
		public void setActionName(String actionName) {
			this.actionName = actionName;
		}
		public Result getResult() {
			if(result==null)result = new Result();
			return result;
		}
		public void setResult(Result result) {
			this.result = result;
		}
		
}
