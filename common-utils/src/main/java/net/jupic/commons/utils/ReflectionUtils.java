package net.jupic.commons.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chang jung pil
 *
 */
public class ReflectionUtils {
	
	private static Logger log = LoggerFactory.getLogger(ReflectionUtils.class);

	public static Object findPropertyValue(Object source, String property) {
		Object value = null;
		try {
			Class<?> type = source.getClass();
			try {
				Field field = type.getDeclaredField(property);
				field.setAccessible(true);
				value = field.get(source);
				field.setAccessible(false);
			} catch (NoSuchFieldException e) {
				Method method = findMethod(type, "get" + StringUtils.capitalize(property));
				method.setAccessible(true);
				value = method.invoke(source);
				method.setAccessible(false);
			}
			
		} catch (SecurityException 
				| IllegalArgumentException 
				| IllegalAccessException 
				| NoSuchMethodException 
				| InvocationTargetException e) {
			
			if (log.isDebugEnabled()) {
				log.debug("illegal property - ", e.getMessage());
			}
		}
		
		return value;
	}
	
	private static Method findMethod(Class<?> type, String methodName) throws NoSuchMethodException, SecurityException {
		try {
			return type.getDeclaredMethod(methodName);
		} catch (NoSuchMethodException ne) {
			Class<?> superType = type.getSuperclass();
			if (superType == null || superType == Object.class) {
				throw ne;
			} else {
				return findMethod(superType, methodName);
			}
		}
	}
	
	public static void bindPropertyValue(Object source, String property, Object value) {
		
		try {
			Class<?> type = source.getClass();
			try {
				Field field = type.getDeclaredField(property);
				field.setAccessible(true);
				field.set(source, value);
				field.setAccessible(false);
			} catch (NoSuchFieldException e) {
				String methodName = "set" + StringUtils.capitalize(property);
				Method method = type.getMethod(methodName, value.getClass());
				method.invoke(source, value);
			}
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("illegal property - ", e.getMessage());
			}
		}
		
	} 
}
