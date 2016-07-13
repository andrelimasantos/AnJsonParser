package com.andrels.anjsonparser.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface JSONAttribute {
	
	public String name();
	
	public String getGetterSetterName() default "";
	
	public Class<?> collectionArrayClass() default Object.class;
}
