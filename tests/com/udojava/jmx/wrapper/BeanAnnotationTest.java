package com.udojava.jmx.wrapper;

import static org.junit.Assert.assertEquals;

import javax.management.IntrospectionException;
import javax.management.MBeanOperationInfo;

import org.junit.Test;

import com.udojava.jmx.wrapper.JMXBeanOperation.IMPACT_TYPES;

public class BeanAnnotationTest {

	@JMXBean
	public class TestBeanDefaults {
		private String stringAttribute;
		private boolean booleanAttribute;

		@JMXBeanAttribute
		public boolean isBooleanAttribute() {
			return booleanAttribute;
		}

		@JMXBeanAttribute
		public void setBooleanAttribute(boolean booleanAttribute) {
			this.booleanAttribute = booleanAttribute;
		}

		@JMXBeanAttribute
		public String getStringAttribute() {
			return stringAttribute;
		}

		@JMXBeanAttribute
		public void setStringAttribute(String stringAttribute) {
			this.stringAttribute = stringAttribute;
		}

		@JMXBeanOperation
		public void voidMethod() {

		}

		@JMXBeanOperation
		public String complexMethod(String name, int value) {
			return "";
		}

	}

	@JMXBean(className = "The class name", description = "The bean description")
	public class TestBeanFullyDescribed {
		private String stringAttribute;
		private boolean booleanAttribute;

		@JMXBeanAttribute(name = "The stringAttribute name", description = "The stringAttribute description")
		public String getStAttribute() {
			return stringAttribute;
		}

		@JMXBeanAttribute(name = "The stringAttribute name")
		public void setStAttribute(String stringAttribute) {
			this.stringAttribute = stringAttribute;
		}

		@JMXBeanAttribute(name = "The booleanAttribute name", description = "The booleanAttribute description")
		public boolean isBool() {
			return booleanAttribute;
		}

		@JMXBeanAttribute(name = "The booleanAttribute name")
		public void setBool(boolean booleanAttribute) {
			this.booleanAttribute = booleanAttribute;
		}

		@JMXBeanOperation(name = "The void method", description = "The void method description", impactType=IMPACT_TYPES.INFO)
		public void voidMethod() {
		}

		@JMXBeanOperation(name = "The complex method", description = "The complex method description")
		public String complexMethod(
				@JMXBeanParameter(name = "The name", description = "The name description") String name,
				@JMXBeanParameter(name = "The value", description = "The value description") int value) {
			return "";
		}
	}
	
	@JMXBean
	public class ParseSpecial {
		@JMXBeanOperation
		public void methodGap(@JMXBeanParameter String p1, int p2, @JMXBeanParameter(name="P-3", description="P-3-desc") long p3) {
			
		}
	}
	
	@Test
	public void testParseSpecial() throws IntrospectionException, SecurityException {
		JMXBeanWrapper bean = new JMXBeanWrapper(new ParseSpecial());
		MBeanOperationInfo info = bean.getMBeanInfo().getOperations()[0];
		assertEquals("param1", info.getSignature()[0].getName());
		assertEquals("param2", info.getSignature()[1].getName());
		assertEquals("P-3", info.getSignature()[2].getName());
	}

	@Test
	public void testBeanAttributeCount() throws IntrospectionException,
			SecurityException {
		JMXBeanWrapper bean1 = new JMXBeanWrapper(new TestBeanFullyDescribed());
		assertEquals(2, bean1.getMBeanInfo().getAttributes().length);

		JMXBeanWrapper bean2 = new JMXBeanWrapper(new TestBeanDefaults());
		assertEquals(2, bean2.getMBeanInfo().getAttributes().length);
	}

	@Test
	public void testBeanAnnotationAll() throws IntrospectionException,
			SecurityException {
		JMXBeanWrapper bean = new JMXBeanWrapper(new TestBeanFullyDescribed());

		assertEquals("The class name", bean.getMBeanInfo().getClassName());
		assertEquals("The bean description", bean.getMBeanInfo()
				.getDescription());
	}

	@Test
	public void testBeanAnnotationAttributeAll() throws IntrospectionException,
			SecurityException {
		JMXBeanWrapper bean = new JMXBeanWrapper(new TestBeanFullyDescribed());

		assertEquals("The stringAttribute name", bean.getMBeanInfo()
				.getAttributes()[1].getName());
		assertEquals("The stringAttribute description", bean.getMBeanInfo()
				.getAttributes()[1].getDescription());
		assertEquals("java.lang.String",
				bean.getMBeanInfo().getAttributes()[1].getType());
		assertEquals("The booleanAttribute name", bean.getMBeanInfo()
				.getAttributes()[0].getName());
		assertEquals("The booleanAttribute description", bean.getMBeanInfo()
				.getAttributes()[0].getDescription());
		assertEquals("boolean",
				bean.getMBeanInfo().getAttributes()[0].getType());
		assertEquals(true, bean.getMBeanInfo().getAttributes()[0].isIs());
	}

	@Test
	public void testBeanAnnotationDefaults() throws IntrospectionException,
			SecurityException {
		JMXBeanWrapper bean = new JMXBeanWrapper(new TestBeanDefaults());

		assertEquals(
				"com.udojava.jmx.wrapper.BeanAnnotationTest$TestBeanDefaults",
				bean.getMBeanInfo().getClassName());
		assertEquals("", bean.getMBeanInfo().getDescription());
	}

	@Test
	public void testBeanAnnotationOperationDefaults()
			throws IntrospectionException, SecurityException {
		JMXBeanWrapper bean = new JMXBeanWrapper(new TestBeanDefaults());

		assertEquals("voidMethod",
				bean.getMBeanInfo().getOperations()[0].getName());
		assertEquals("",
				bean.getMBeanInfo().getOperations()[0].getDescription());
		assertEquals("void",
				bean.getMBeanInfo().getOperations()[0].getReturnType());
		assertEquals(0,
				bean.getMBeanInfo().getOperations()[0].getSignature().length);
		assertEquals(MBeanOperationInfo.UNKNOWN,
				bean.getMBeanInfo().getOperations()[0].getImpact());
		

		MBeanOperationInfo info = bean.getMBeanInfo().getOperations()[1];
		assertEquals("complexMethod", info.getName());
		assertEquals("java.lang.String", info.getReturnType());
		assertEquals(2, info.getSignature().length);
		assertEquals("param1", info.getSignature()[0].getName());
		assertEquals("", info.getSignature()[0].getDescription());
		assertEquals("java.lang.String", info.getSignature()[0].getType());
		assertEquals("param2", info.getSignature()[1].getName());
		assertEquals("", info.getSignature()[1].getDescription());
		assertEquals("int", info.getSignature()[1].getType());

	}

	@Test
	public void testBeanAnnotationOperationAll() throws IntrospectionException,
			SecurityException {
		JMXBeanWrapper bean = new JMXBeanWrapper(new TestBeanFullyDescribed());

		assertEquals("The void method",
				bean.getMBeanInfo().getOperations()[0].getName());
		assertEquals("The void method description", bean.getMBeanInfo()
				.getOperations()[0].getDescription());
		assertEquals(MBeanOperationInfo.INFO,
				bean.getMBeanInfo().getOperations()[0].getImpact());

		MBeanOperationInfo info = bean.getMBeanInfo().getOperations()[1];
		assertEquals(2, info.getSignature().length);
		assertEquals("The name", info.getSignature()[0].getName());
		assertEquals("The name description", info.getSignature()[0].getDescription());
		assertEquals("java.lang.String", info.getSignature()[0].getType());
		assertEquals("The value", info.getSignature()[1].getName());
		assertEquals("The value description", info.getSignature()[1].getDescription());
		assertEquals("int", info.getSignature()[1].getType());
	}

	@Test
	public void testBeanAnnotationAttributeDefaults()
			throws IntrospectionException, SecurityException {
		JMXBeanWrapper bean = new JMXBeanWrapper(new TestBeanDefaults());

		assertEquals("stringAttribute",
				bean.getMBeanInfo().getAttributes()[0].getName());
		assertEquals("",
				bean.getMBeanInfo().getAttributes()[0].getDescription());
		assertEquals("java.lang.String",
				bean.getMBeanInfo().getAttributes()[0].getType());
		assertEquals(false, bean.getMBeanInfo().getAttributes()[0].isIs());
		assertEquals("booleanAttribute",
				bean.getMBeanInfo().getAttributes()[1].getName());
		assertEquals("boolean",
				bean.getMBeanInfo().getAttributes()[1].getType());
		assertEquals(true, bean.getMBeanInfo().getAttributes()[1].isIs());
	}

}
