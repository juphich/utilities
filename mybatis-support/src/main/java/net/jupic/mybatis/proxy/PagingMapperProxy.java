package net.jupic.mybatis.proxy;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import net.jupic.commons.model.Page;
import net.jupic.mybatis.PagingQueryExecutor;
import net.jupic.mybatis.annotation.Statement;
import net.jupic.mybatis.exception.InvalidStatementExceptions;

import org.apache.ibatis.session.Configuration;



/**
 * @author chang jung pil
 *
 */
public class PagingMapperProxy implements InvocationHandler, Serializable {

	private static final long serialVersionUID = 9146685874629349883L;

	private Object targetObject;
	private PagingQueryExecutor executor;
	
	private PagingMapperProxy(Object targetObject, PagingQueryExecutor executor) {
		this.targetObject = targetObject;		
		this.executor = executor;
	}
	
	/**
	 * @param proxy
	 * @param method
	 * @param args
	 * @return
	 * @throws Throwable
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (isAvailablePaging(method)) {
			InvocationMethod invokeMethod = new InvocationMethod(method, proxy.getClass().getInterfaces());
			return invokeMethod.execute(args);
		} else {
			return method.invoke(targetObject, args);
		}
	}
	
	private boolean isAvailablePaging(Method method) {
		if (method.getReturnType().isAssignableFrom(Page.class)
				&& method.getAnnotation(Statement.class) != null) {
			return true;
		} else {
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T newProxyInstance(Class<T> mapperInterface, Object targetObject, PagingQueryExecutor executor) {
		ClassLoader classLoader = mapperInterface.getClassLoader();
	    Class<?>[] interfaces = new Class[]{mapperInterface};
	    PagingMapperProxy proxy = new PagingMapperProxy(targetObject, executor);
	    return (T) Proxy.newProxyInstance(classLoader, interfaces, proxy);
	}
	
	enum MappingMode { MANUAL, AUTO; }
	
	private class InvocationMethod {
		Method method;
		Configuration configuration;
		Class<?>[] candidateMappers;
		
		InvocationMethod (Method method, Class<?>[] candidateMappers) {
			this.method = method;
			this.configuration = executor.getSqlSession().getConfiguration();
			this.candidateMappers = candidateMappers;
		}
		
		Object execute(Object[] args) {
			Statement statement = method.getAnnotation(Statement.class);
			
			if (MappingMode.MANUAL.equals(getMode(statement))) {
				String pagingStatement = getPaingStatementId(statement);
				String countingStatement = getCountingStatementId(statement);
				
				return executor.selectPaginatedList(pagingStatement, countingStatement, args[0]);
			} else {
				String statementId = getStatementId(statement);
				return executor.selectPaginatedList(statementId, args[0]);
			}
		}
		
		MappingMode getMode (Statement statement) {
			boolean page = !statement.page().equals("");
			boolean count = !statement.count().equals("");
			boolean value = !statement.value().equals("");
			boolean id = !statement.id().equals("");
			
			if ((count & page) && !(id | value)) {
				return MappingMode.MANUAL;
			} else if ((id ^ value) && !(page | count)) {
				return MappingMode.AUTO;
			} else if (!(id | value) && !(count & page)) {
				throw new IllegalArgumentException(
						"page and count parameters have to be setting at the same time on Statement Annotation.");
			} else {
				throw new IllegalArgumentException(
						"(page, count) and id and value parameters could not be setting together on Statement Annotation.");
			}
		}
		
		/**
		 * @param statement
		 * @return
		 */
		private String getCountingStatementId(Statement statement) {
			if (statement.mapper().length > 0) {
				return statement.mapper()[0].getName() + "." + statement.count();
			} else {
				for (Class<?> candidateMapper : candidateMappers ) {
					String id = candidateMapper.getName() + "." + statement.count();
					if (configuration.hasStatement(id)) {
						return id;
					}
				}
				
				throw new InvalidStatementExceptions("There is no matched counting statement id - " 
							+ statement.count() + " (check mapper configurations)");
			}
		}

		/**
		 * @param statement
		 * @return
		 */
		private String getPaingStatementId(Statement statement) {
			if (statement.mapper().length > 0) {
				return statement.mapper()[0].getName() + "." + statement.page();
			} else {
				for (Class<?> candidateMapper : candidateMappers ) {
					String id = candidateMapper.getName() + "." + statement.page();
					if (configuration.hasStatement(id)) {
						return id;
					}
				}
				
				throw new InvalidStatementExceptions("There is no matched paging statement id - " 
							+ statement.page() + " (check mapper configurations)");
			}
		}

		private String getStatementId(Statement statement) {
			String value = statement.value();
			String id = statement.id();
			
			if (!id.equals("")) {
				if (statement.mapper().length > 0) {
					return statement.mapper()[0].getName() + "." + statement.id();
				} else {
					for (Class<?> candidateMapper : candidateMappers ) {
						String cadidateId = candidateMapper.getName() + "." + statement.id();
						if (configuration.hasStatement(cadidateId)) {
							return cadidateId;
						}
					}
					
					throw new InvalidStatementExceptions("There is no matched statement id - " 
								+ statement.id() + " (check mapper configurations)");
				}
			} else {
				return value;
			}
		}
	}
}
