package com.andrels.anjsonparser.parser;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.andrels.anjsonparser.annotation.JSONAttribute;
import com.andrels.anjsonparser.annotation.JSONType;
import com.andrels.anjsonparser.exception.JSONParserFailureException;

public class JSONParser {
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	private static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());
	
	public JSONObject serialize(Object object){
		Class<?> clazz = object.getClass();
		JSONObject json = null;
		if(clazz.isAnnotationPresent(JSONType.class)){
			json = new JSONObject();
			
			try{
				Collection<Field> fields = getAnnotedFields(clazz);
				
				for(Field f : fields){
					JSONAttribute annotation = f.getAnnotation(JSONAttribute.class);
					Class<?> type = f.getType();
					String attributeName = annotation.getGetterSetterName().length() > 0 ? annotation.getGetterSetterName() : annotation.name();
					String getterName = getGetterName(annotation, f);
					
					Method m = clazz.getMethod(getterName, (Class<?>[])null);
					Object value = m.invoke(object, (Object[])null);
					
					if(value != null && Calendar.class.isAssignableFrom(value.getClass())){
						value = ((Calendar)value).getTime();
					}
					
					if(value == null){
						json.put(attributeName, JSONObject.NULL);
					}
					else if(String.class.equals(type)){
						json.put(attributeName, (String)value);
					}
					else if(Integer.class.equals(type) || int.class.equals(type)){
						json.put(attributeName, (Integer)value);
					}
					else if(Long.class.equals(type) || long.class.equals(type)){
						json.put(attributeName, (Long)value);
					}
					else if(Double.class.equals(type) || double.class.equals(type)){
						json.put(attributeName, (Double)value);
					}
					else if(Boolean.class.equals(type) || boolean.class.equals(type)){
						json.put(attributeName, (Boolean)value);
					}
					else if(value instanceof Collection){
						json.put(attributeName, serialize((Collection<?>)value));
					}
					else if(Date.class.isAssignableFrom(value.getClass())){
						json.put(attributeName, sdf.format((Date)value));
					}
					else if(type.isArray()){
						json.put(attributeName, serialize(Arrays.asList((Object[])value)));
					}
					else{
						json.put(attributeName,  serialize(value));
					}
				}
			}catch(NoSuchMethodException e){
				//TODO
			}catch(Exception e){
				//TODO
			}
		}
		return json;
	}
	
	public JSONArray serialize(Collection<?> collection){
		JSONArray jsonArray = new JSONArray();
		for(Object object : collection){
			if(object.getClass().isAnnotationPresent(JSONType.class)){
				jsonArray.put(serialize(object));
			}
		}
		return jsonArray;
	}
	
	public Object parse(byte[] json, Class<?> clazz) throws JSONParserFailureException{
		try {
			return parse(new JSONObject(new String(json)), clazz);
		} catch (JSONException e) {
			throw new JSONParserFailureException(e.getMessage(), e);
		}
	}
	
	public Object parse(JSONObject json, Class<?> clazz) throws JSONParserFailureException{
		if(clazz.isAnnotationPresent(JSONType.class) && json != null){
			try{
				Object object = clazz.newInstance();
				List<Field> fields = new LinkedList<Field>();
				
				Class<?> c = clazz;
				while(c != null){
					fields.addAll(Arrays.asList(c.getDeclaredFields()));
					c = c.getSuperclass();
				}
				
				for(Field f : fields){
					if(f.isAnnotationPresent(JSONAttribute.class)){
						JSONAttribute annotation = f.getAnnotation(JSONAttribute.class);
						Class<?> type = f.getType();
						f.getDeclaringClass();
						String attributeName = annotation.getGetterSetterName().length() > 0 ? annotation.getGetterSetterName() : annotation.name();
						String setterName = getSetterName(annotation, f);
						
						Method m = clazz.getMethod(setterName, f.getType());
						
						if(!json.isNull(attributeName)){
							if(String.class.equals(type)){
								m.invoke(object, json.optString(attributeName));
							}
							else if(Integer.class.equals(type) || int.class.equals(type)){
								m.invoke(object, json.optInt(attributeName));
							}
							else if(Long.class.equals(type) || long.class.equals(type)){
								m.invoke(object, json.optLong(attributeName));
							}
							else if(Double.class.equals(type) || double.class.equals(type)){
								m.invoke(object, json.optDouble(attributeName));
							}
							else if(Boolean.class.equals(type) || boolean.class.equals(type)){
								m.invoke(object, json.optBoolean(attributeName));
							}
							else if(type.isArray()){
								m.invoke(object, (Object)parse(json.optJSONArray(attributeName), annotation.collectionArrayClass()));
							}
							else if(type.isInstance((Collection<?>)new ArrayList<Object>())){
								Object[] objs = parse(json.optJSONArray(attributeName), annotation.collectionArrayClass());
								if(objs != null){
									m.invoke(object, Arrays.asList(objs));
								}
							}
							else{
								m.invoke(object, parse(json.getJSONObject(attributeName), m.getParameterTypes()[0]));
							}
						}
					}
				}
				return object;
			}catch(NoSuchMethodException e){
				throw new JSONParserFailureException(e.getMessage(), e);
			}catch(Exception e){
				throw new JSONParserFailureException(e.getMessage(), e);
			}
		}
		return null;
	}
	
	public Object[] parse(JSONArray jsonArray, Class<?> classIns){
		if(classIns.isAnnotationPresent(JSONType.class) && jsonArray != null){
			Object nArray = Array.newInstance(classIns, jsonArray.length());
			for(int i = 0; i < jsonArray.length(); i++){
				try{
					Array.set(nArray, i, parse(jsonArray.optJSONObject(i), classIns));
				}
				catch(Exception e){
					//TODO
				}
			}
			return (Object[])nArray;
		}
		
		return null;
	}
	
	private String getGetterName(JSONAttribute annotation, Field field){
		String getterName = (annotation.getGetterSetterName().length() > 0 ? annotation.getGetterSetterName() : field.getName());
		getterName = Character.toUpperCase(getterName.charAt(0)) + getterName.substring(1);
		return "get"+getterName;
	}
	
	private String getSetterName(JSONAttribute annotation, Field field){
		String getterName = (annotation.getGetterSetterName().length() > 0 ? annotation.getGetterSetterName() : field.getName());
		getterName = Character.toUpperCase(getterName.charAt(0)) + getterName.substring(1);
		return "set"+getterName;
	}
	
	private Collection<Field> getAnnotedFields(Class<?> clazz){
		ArrayList<Field> fields = new ArrayList<Field>();
		if(clazz.isAnnotationPresent(JSONType.class)){
			if(clazz.getSuperclass().isAnnotationPresent(JSONType.class)){
				fields.addAll(getAnnotedFields(clazz.getSuperclass()));
			}
			
			for(Field f : clazz.getDeclaredFields()){
				if(f.isAnnotationPresent(JSONAttribute.class)){
					fields.add(f);
				}
			}
		}
		return fields;
	}
}
