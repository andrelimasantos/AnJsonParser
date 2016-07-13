package com.andrels.anjsonparser.demo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.andrels.anjsonparser.demo.pojo.Address;
import com.andrels.anjsonparser.demo.pojo.Locations;
import com.andrels.anjsonparser.parser.JSONParser;

public class SerializeDemo {

	public static void main(String[] args) {
		List<Address> locations = new ArrayList<Address>();
		
		Address address = new Address();
		address.setCity("New York");
		address.setCountry("EUA");
		address.setStreet("76 Ninth Avenue");
		address.setCounty("NY");
		
		locations.add(address);
		
		address = new Address();
		address.setCity("San Francisco");
		address.setCountry("EUA");
		address.setStreet("345 Spear Street");
		address.setCounty("CA");
		
		locations.add(address);
		
		Locations loc = new Locations();
		loc.setLocationName("Google Locations");
		loc.setLocations(locations);
		
		JSONParser parser = new JSONParser();
		JSONObject json = parser.serialize(loc);
		
		System.out.println(json);
	}

}
