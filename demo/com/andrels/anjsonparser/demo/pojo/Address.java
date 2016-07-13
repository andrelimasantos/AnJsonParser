package com.andrels.anjsonparser.demo.pojo;

import com.andrels.anjsonparser.annotation.JSONAttribute;
import com.andrels.anjsonparser.annotation.JSONType;

@JSONType
public class Address {

	@JSONAttribute(name="country")
	private String country;
	
	@JSONAttribute(name="county")
	private String county;
	
	@JSONAttribute(name="city")
	private String city;
	
	@JSONAttribute(name="street")
	private String street;
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
}
