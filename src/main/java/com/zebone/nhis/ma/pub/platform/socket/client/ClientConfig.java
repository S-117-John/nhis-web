package com.zebone.nhis.ma.pub.platform.socket.client;

import javax.annotation.PostConstruct;

import org.apache.mina.core.filterchain.IoFilter;

import com.zebone.nhis.common.support.ApplicationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ClientConfig {

	private static Logger logger = LoggerFactory.getLogger("nhis.lbHl7Log");
	public static String BOUND_KEY_SUFFIX = "_bound";
	/** 主机*/
	private String host;
	/** 端口*/
	private int port;
	/**超时时间，单位秒 */
	private int timeOut;

	/** 空闲时间（两次消息发送中间的间隔时间超过IDLE_TIME秒，就认为此时连接空闲，然后执行sessionIdle方法） */
	private int idleTime = 60;
	/** 心跳频率，秒*/
	private int heartBeat = 10;
	/**是否开启心跳检测,默认开启*/
	private boolean openHeartBeat = false;
	/**处理器数量，我们是集成运行，不要设置超过CPU核心数一半以上*/
	private int processCount;

	/**过滤器*/
	private List<IoFilter> protocolCodecFilterList;
	/**客户化处理器*/
	private CustomerHandler clientHandler;

	@PostConstruct
	public void init(){
		//读取配置文件的方法，为了使用ApplicationUtils，放在了Spring容器启动之后处理
		this.host = ApplicationUtils.getPropertyValue("msg.send.host","127.0.0.1");
		this.port = Integer.parseInt(ApplicationUtils.getPropertyValue("msg.send.port", "5010"));
		this.timeOut = Integer.parseInt(ApplicationUtils.getPropertyValue("msg.send.timeout", "1000"));
		this.openHeartBeat = "1".equals(ApplicationUtils.getPropertyValue("msg.send.beat","0"));
		this.processCount = Integer.parseInt(ApplicationUtils.getPropertyValue("msg.send.processcount", "4"));
		this.protocolCodecFilterList = new ArrayList<>();
		setHandler();

		//TODO 本地测试
//		this.host="127.0.0.1";this.port=8007;this.timeOut=10000;
//		this.openHeartBeat=true;
	}

	private void setHandler(){
		try {
			String handlerClass = ApplicationUtils.getPropertyValue("msg.receive.class",null);
			this.clientHandler = (CustomerHandler) Class.forName(handlerClass).newInstance();
		} catch (Exception e) {
			this.clientHandler = new CustomerHandlerAdapter();
		}
	}

	public String getHost() {
		return host;
	}


	public void setHost(String host) {
		this.host = host;
	}


	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}

	public boolean isOpenHeartBeat() {
		return openHeartBeat;
	}

	public void setOpenHeartBeat(boolean openHeartBeat) {
		this.openHeartBeat = openHeartBeat;
	}

	public int getTimeOut() {
		return timeOut;
	}


	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public CustomerHandler getClientHandler(){
		return clientHandler;
	}

	public List<IoFilter> getProtocolCodecFilterList() {
		return protocolCodecFilterList;
	}

	public int getIdleTime() {
		return idleTime;
	}

	public void setIdleTime(int idleTime) {
		this.idleTime = idleTime;
	}

	public int getHeartBeat() {
		return heartBeat;
	}

	public void setHeartBeat(int heartBeat) {
		this.heartBeat = heartBeat;
	}

	public int getProcessCount() {
		return processCount;
	}

	public void setProcessCount(int processCount) {
		this.processCount = processCount;
	}
}
