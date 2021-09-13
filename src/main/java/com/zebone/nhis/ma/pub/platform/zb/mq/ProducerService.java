package com.zebone.nhis.ma.pub.platform.zb.mq;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

/**
 * 消息生产者
 * @author chengjia
 *
 */
@Service
public class ProducerService {
	@Resource(name = "jmsTemplate")
	private JmsTemplate jmsTemplate;
	
	private static Logger loger = org.slf4j.LoggerFactory.getLogger(ProducerService.class.getName());

	/**
	 * 向指定队列发送消息
	 */
	public void sendJmsMessage(Destination destination, final String msg) {
		loger.info("向队列" + destination.toString() + "发送了消息------------"+ msg);
		// System.out.println(Thread.currentThread().getName()+" 向队列"+destination.toString()+"发送消息---------------------->"+msg);

		jmsTemplate.send(destination, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(msg);
			}
		});
	}

	/**
	 * 向默认队列发送消息
	 */
	public void sendJmsMessage(final String msg) {
		String destination = jmsTemplate.getDefaultDestination().toString();

		loger.info("向队列" + destination + "发送了消息:------------" + msg);
		// System.out.println(Thread.currentThread().getName()+" 向队列"+destination+"发送消息---------------------->"+msg);
		jmsTemplate.send(new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(msg);
			}
		});

	}
}
