package net.jupic.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chang jung pil
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Statement {
	String value() default "";

	String id() default "";
	
	Class<?>[] mapper() default {};
	
	String page() default "";
	
	String count() default "";
}
