package com.bjp.helloworld.mongodb.entity;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 
 * @author 小五老师-云析学院
 * @createTime 2019年1月5日 下午5:35:05
 * 
 */
@Document(collection="partner")
public class Partner {

	@Id
	private ObjectId id;
	private String name;
	@DBRef(db="five")
	private List<User> username;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<User> getUsername() {
		return username;
	}
	public void setUsername(List<User> username) {
		this.username = username;
	}
	
}
