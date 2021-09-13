package com.zebone.nhis.webservice.syx.vo.platForm;

public enum PaySource {
	JKZL(1,"健康之路"),
	EMEM(2,"EMEM"),
	TEL(3,"电话(114)"),
	NHWD(7,"农行网点"),
	YXWWX(8,"医享网微信"),
	YXWZFB(9,"医享网支付宝"),
	PAGH(11,"平安挂号"),
	YJK(13,"翼健康"),
	YCT(40,"医程通"),
	HY(58,"华医App"),
	PA(59,"平安APP"),
	YL(107,"银联（建自助）"),
	WX(108,"微信（建自助）"),
	ZFB(109,"支付宝（建自助）");
	
	private PaySource(int key,String value){
		this.key=key;
		this.value=value;
	}
	private int key;
	private String value;
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * 根据键获取值
	 * @param key
	 * @return
	 */
	public static String getName(int key) {
		for (PaySource pay : PaySource.values()) {
			if(pay.key==key){
				return pay.value;
			}
		}
		return "";
	}
	
	
}
