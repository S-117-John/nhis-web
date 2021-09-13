/**
  * Copyright 2019 bejson.com 
  */
package com.zebone.nhis.webservice.syx.vo.send;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

/**
 * Auto-generated: 2019-04-22 15:49:53
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@XmlType(name = "CARD_LIST")
public class CardList {
	
	@XmlElementWrapper(name = "CARD_LIST")  
	@XmlElement(name = "CARD")  
	private List<Card> cards;

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}
	
}