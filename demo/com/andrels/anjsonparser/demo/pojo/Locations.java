package com.andrels.anjsonparser.demo.pojo;

import java.util.List;

import com.andrels.anjsonparser.annotation.JSONAttribute;
import com.andrels.anjsonparser.annotation.JSONType;

@JSONType
public class Locations {

	@JSONAttribute(name="name")
	private String locationName;
	
	@JSONAttribute(name="locations", collectionArrayClass=Address.class)
	private List<Address> locations;

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public List<Address> getLocations() {
		return locations;
	}

	public void setLocations(List<Address> locations) {
		this.locations = locations;
	}
}
