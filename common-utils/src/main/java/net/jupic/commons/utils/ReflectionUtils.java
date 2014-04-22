package net.jupic.commons.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.jupic.commons.exception.NotAPojoException;

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
				Field field = findField(type, property);
				field.setAccessible(true);
				value = field.get(source);
				field.setAccessible(false);
			} catch (NoSuchFieldException e) {
				Method method = findGetterMethod(type, "get" + StringUtils.capitalize(property));
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
	
	private static Method findGetterMethod(Class<?> type, String methodName) throws NoSuchMethodException, SecurityException {
		try {
			return type.getDeclaredMethod(methodName);
		} catch (NoSuchMethodException ne) {
			Class<?> superType = type.getSuperclass();
			if (superType == null || superType == Object.class) {
				throw ne;
			} else {
				return findGetterMethod(superType, methodName);
			}
		}
	}
	
	public static void bindPropertyValue(Object source, String property, Object value) {
		
		try {
			try {
				Field field = findField(source, property);
				field.setAccessible(true);
				field.set(source, value);
				field.setAccessible(false);
			} catch (NoSuchFieldException e) {
				String methodName = "set" + StringUtils.capitalize(property);
				Method method = findMethod(source, methodName, value.getClass());
				method.invoke(source, value);
			}
		} catch (Exception e) {
			if (log.isDebugEnabled()) {
				log.debug("illegal property - ", e.getMessage());
			}
		}
		
	}
	
	public static Field findField(Object source, String fieldName) throws NoSuchFieldException {
		return findField(source.getClass(), fieldName);
	}
	
	public static Field findField(Class<?> type, String fieldName) throws NoSuchFieldException {
		try {
			Field field = type.getDeclaredField(fieldName);
			return field;
		} catch (NoSuchFieldException e) {
			Class<?> superType = type.getSuperclass();
			
			if (superType == Object.class) {
				throw e;
			}
			return findField(superType, fieldName);
		}
		
	}
	
	public static Method findMethod(Object source, String methodName, Class<?>... args) {
		return findMethod(source.getClass(), methodName, args);
	}
	
	public static Method findMethod(Class<?> type, String methodName, Class<?>... args) {
		Method[] methods = type.getMethods();
		
		for (Method method : methods) {
			if (method.getName().equals(methodName) 
					&& isMatchedTypes(method.getParameterTypes(), args)) {
				return method;
			}
		}
		
		return null;
		
	}
	
	public static boolean isMatchedTypes(Class<?>[] types, Class<?>[] args) {
		if (types.length != args.length) {
			return false;
		}
		
		for (int i=0; i<types.length; i++) {
			if (!types[i].isAssignableFrom(args[i])) {
				if (types[i].isPrimitive() && types[i] != convertToPrimitive(args[i])) {
				
					return false;
				}
			}
		}
		
		return true;
	}

	private static Class<?> convertToPrimitive(Class<?> type) {
		if (type.equals(Boolean.class)) {
			return Boolean.TYPE;
		} else if (type.equals(Byte.class)) {
			return Byte.TYPE;
		} else if (type.equals(Character.class)) {
			return Character.TYPE;
		} else if (type.equals(Short.class)) {
			return Short.TYPE;
		} else if (type.equals(Integer.class)) {
			return Integer.TYPE;
		} else if (type.equals(Long.class)) {
			return Long.TYPE;
		} else if (type.equals(Float.class)) {
			return Float.TYPE;
		} else if (type.equals(Double.class)) {
			return Double.TYPE;
		}
		
		return null;
	}

	public static Map<String, Object> asMap(Object source) throws NotAPojoException {
		if (!isPojo(source)) {
			throw new NotAPojoException(source);
		}
		
		Map<String, Object> valueMap = new HashMap<String, Object>();
		
		Class<?> type = source.getClass();
		Method[] methods = type.getMethods();
		for (Method method : methods) {
			if (!isObjectMethod(method) && (method.getName().startsWith("get") || method.getName().startsWith("is"))) {
				try {
					String name = StringUtils.uncapitalize(method.getName().replaceFirst("(get)|(is)", ""));
					Object value = method.invoke(source);
					valueMap.put(name, value);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				}
			}
		}
		
		return Collections.unmodifiableMap(valueMap);
	}
	
	public static boolean isPojo(Object source) {
		Class<?> type = source.getClass();
		
		if (type.isArray() || type.isEnum() || type.isAnnotation()) {
			return false;
		}
		
		if (source instanceof Iterable || source instanceof Map) {
			return false;
		}
		
		return true;
	}
	
	public static boolean isPrimitiveType(Object source) {
		if (source instanceof Number || source instanceof Character || source instanceof Boolean || source instanceof String) {
			return true;
		}
		
		return source.getClass().isPrimitive();
	}
	
	public static boolean isMember(Class<?> type, String memberName) {
		Method[] methods = type.getMethods();
		
		for (Method method : methods) {
			if (memberName.equals(method.getName())) {
				return true;
			}
		}
		
		return isMemberField(type, memberName);
	}
	
	private static boolean isMemberField(Class<?> type, String memberName) {		
		try {
			type.getDeclaredField(memberName);
			return true;
		} catch (NoSuchFieldException | SecurityException e) {
			Class<?> superType = type.getSuperclass();			
			if (superType == Object.class) {
				return false;
			} else {
				return isMemberField(superType, memberName);
			}
		}
	}
	
	private static boolean isObjectMethod(Method method) {
		try {
			Object.class.getDeclaredMethod(method.getName(), method.getParameterTypes());
			return true;
		} catch (SecurityException | NoSuchMethodException e) {
			return false;
		}
	} 
}
