package com.andrels.anjsonparser.demo.pojo;

import java.util.List;

import com.andrels.anjsonparser.annotation.JSONAttribute;
import com.andrels.anjsonparser.annotation.JSONType;

@JSONType
public class PhoneBook {
	
	@JSONAttribute(name="name")
	private String name;
	
	@JSONAttribute(name="phones", collectionArrayClass=Phone.class)
	private List<Phone> phones;

	public List<Phone> getPhones() {
		return phones;
	}

	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
