package com.andrels.anjsonparser.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.andrels.anjsonparser.demo.pojo.Phone;
import com.andrels.anjsonparser.demo.pojo.PhoneBook;
import com.andrels.anjsonparser.exception.JSONParserFailureException;
import com.andrels.anjsonparser.parser.JSONParser;

public class ParseDemo {

	public static void main(String[] args) {
		try{
			String json = readJson();
			
			JSONParser parser = new JSONParser();
			PhoneBook pb = parser.parse(json.getBytes(), PhoneBook.class);
			
			System.out.println(pb.getName());
			
			for(Phone phone : pb.getPhones()){
				System.out.println(String.format("\t %1$s [%2$s]",phone.getName(), phone.getPhoneNumber()));
			}
			
		} catch(IOException e){
			e.printStackTrace();
			
		} catch(JSONParserFailureException e){
			e.printStackTrace();
		}
	}
	
	private static String readJson() throws IOException{
		InputStream is = ParseDemo.class.getClassLoader().getResourceAsStream("com/andrels/anjsonparser/demo/json.txt");
		InputStreamReader isr = new InputStreamReader(is);
		
		char[] c = new char[100];
		int readed = 0;
		
		StringBuilder strBuild = new StringBuilder();
		while((readed = isr.read(c)) > -1){
			strBuild.append(c, 0, readed);
		}
		isr.close();
		
		return strBuild.toString();
	}

}
