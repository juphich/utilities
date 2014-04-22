package net.jupic.commons.utils;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.jupic.commons.exception.NotAPojoException;

import org.junit.Before;
import org.junit.Test;

public class ReflectionUtilTest {
	
	private MockClass fixture;
	private ChiledMock childObj;
	
	@Before
	public void init() {
		fixture = new MockClass();
		fixture.setString("test string");
		fixture.setBooleanField(true);
		fixture.setInteger(111);
		
		childObj = new ChiledMock();
		childObj.setChild("child class");
		childObj.setString("child test string");
		childObj.setBooleanField(true);
		childObj.setInteger(222);
	}
	
	@Test
	public void findFieldTest() throws NoSuchFieldException {
		Field integer = ReflectionUtils.findField(childObj, "integer");
		Field string = ReflectionUtils.findField(childObj, "string");
		Field child = ReflectionUtils.findField(childObj, "child");
		Field bool = ReflectionUtils.findField(childObj, "booleanField");
		
		assertEquals(integer.getType(), int.class);
		assertEquals(string.getType(), String.class);
		assertEquals(child.getType(), String.class);
		assertEquals(bool.getType(), boolean.class);
	}
	
	@Test
	public void findMethodTest() {
		Method method = ReflectionUtils.findMethod(childObj, "setInteger", Integer.class);
		
		assertNotNull(method);
	}
	
	@Test
	public void findPropertyTest() {
		String value1 = (String)ReflectionUtils.findPropertyValue(fixture, "string");
		int value2 = (int)ReflectionUtils.findPropertyValue(fixture, "integer");
		boolean value3 = (boolean)ReflectionUtils.findPropertyValue(fixture, "booleanField");
		
		assertThat(value1, is("test string"));
		assertThat(value2, is(111));
		assertThat(value3, is(true));
	}
	
	@Test
	public void bindPropertyTest() {
		ReflectionUtils.bindPropertyValue(fixture, "string", "changed");
		ReflectionUtils.bindPropertyValue(fixture, "integer", 123);
		ReflectionUtils.bindPropertyValue(fixture, "booleanField", false);
		
		assertThat(fixture.getString(), is("changed"));
		assertThat(fixture.integer, is(123));
		assertThat(fixture.isBooleanField(), is(false));
	}
	
	@Test
	public void checkingTest() {
		assertTrue(ReflectionUtils.isPrimitiveType(1));
		assertTrue(ReflectionUtils.isPrimitiveType("test"));
		assertTrue(ReflectionUtils.isPrimitiveType(false));
		assertTrue(ReflectionUtils.isPrimitiveType(Boolean.valueOf(false)));
		assertFalse(ReflectionUtils.isPrimitiveType(fixture));
		
		assertTrue(ReflectionUtils.isMember(MockClass.class, "string"));
		assertFalse(ReflectionUtils.isMember(MockClass.class, "testField"));
		
		assertTrue(ReflectionUtils.isPojo(fixture));
		assertFalse(ReflectionUtils.isPojo(new Object[0]));
		assertFalse(ReflectionUtils.isPojo(new ArrayList<Object>()));
		assertFalse(ReflectionUtils.isPojo(new HashMap<Object, Object>()));
	}
	
	@Test
	public void propertyMapTest() throws NotAPojoException {
		Map<String, Object> actual = ReflectionUtils.asMap(fixture);

		assertThat(actual.size(), is(2));
		assertTrue(actual.containsKey("string"));
		assertThat((String)actual.get("string"), is("test string"));
		
		assertFalse(actual.containsKey("integer"));
	}
}

class MockClass {
	int integer;
	private String string;
	private boolean booleanField;
	
	public String getString() {
		return string;
	}
	public boolean isBooleanField() {
		return booleanField;
	}
	
	public void setString(String string) {
		this.string = string;
	}
	public void setInteger(int integer) {
		this.integer = integer;
	}
	public void setBooleanField(boolean bool) {
		this.booleanField = bool;
	}
}

class ChiledMock extends MockClass {
	private String child;

	public String getChild() {
		return child;
	}

	public void setChild(String child) {
		this.child = child;
	}
}
