package net.jupic.mybatis;

import net.jupic.mybatis.exception.PagingContextInitializingFailedException;
import net.jupic.mybatis.exception.UnknownQueryTypeException;
import net.jupic.mybatis.result.CountingResultMapLoader;
import net.jupic.mybatis.result.PaginatedResultMapLoader;
import net.jupic.mybatis.sql.QueryType;
import net.jupic.mybatis.sql.hsql.HsqlCountSqlSource;
import net.jupic.mybatis.sql.hsql.HsqlPagingSqlSource;
import net.jupic.mybatis.sql.mysql.MySqlCountSqlSource;
import net.jupic.mybatis.sql.mysql.MySqlPagingSqlSource;
import net.jupic.mybatis.sql.oracle.OracleCountSqlSource;
import net.jupic.mybatis.sql.oracle.OraclePagingSqlSource;

import org.apache.ibatis.session.Configuration;


/**
 * @author chang jung pil
 *
 */
public class PagingContext {

	private final Configuration configuration;
	
	private Class<?> countSqlSourceClass;
	private Class<?> pagingSqlSourceClass;
	
	private Class<?> countingResultMapLoaderClass;
	private Class<?> paginatedResultMapLoaderClass;

	public PagingContext(final Configuration configuration) {
		this.configuration = configuration;
		
		init();
	}
	
	private void init() {
		String databaseId = this.configuration.getDatabaseId();
		
		if (databaseId == null) {
			throw new PagingContextInitializingFailedException(
					"could not found database type. check database connection or jdbc support.");
		}
		
		if(databaseId.toLowerCase().indexOf("oracle") > -1) {
			this.countSqlSourceClass = OracleCountSqlSource.class;
			this.pagingSqlSourceClass = OraclePagingSqlSource.class;
		} else if (databaseId.toLowerCase().indexOf("ms-sql") > -1) {
			
		} else if (databaseId.toLowerCase().indexOf("mysql") > -1) {
			this.countSqlSourceClass = MySqlCountSqlSource.class;
			this.pagingSqlSourceClass = MySqlPagingSqlSource.class;
		} else if (databaseId.toLowerCase().indexOf("db2") > -1) {
			
		} else if (databaseId.toLowerCase().indexOf("hsql") > -1) {
			this.countSqlSourceClass = HsqlCountSqlSource.class;
			this.pagingSqlSourceClass = HsqlPagingSqlSource.class;
		}
		
		this.countingResultMapLoaderClass = CountingResultMapLoader.class;
		this.paginatedResultMapLoaderClass = PaginatedResultMapLoader.class;
	}

	public Class<?> getCountSqlSourceClass() {
		return countSqlSourceClass;
	}

	public Class<?> getPaingSqlSourceClass() {
		return pagingSqlSourceClass;
	}

	public Class<?> getCountingResultMapLoader() {
		return countingResultMapLoaderClass;
	}

	public Class<?> getPaginatedResultMapLoader() {
		return paginatedResultMapLoaderClass;
	}

	public void setCountSqlSourceClass(Class<?> countSqlSourceClass) {
		this.countSqlSourceClass = countSqlSourceClass;
	}

	public void setPaingSqlSourceClass(Class<?> pagingSqlSourceClass) {
		this.pagingSqlSourceClass = pagingSqlSourceClass;
	}

	public void setCountingResultMapLoader(Class<?> countingResultMapLoader) {
		this.countingResultMapLoaderClass = countingResultMapLoader;
	}

	public void setPaginatedResultMapLoader(Class<?> paginatedResultMapLoader) {
		this.paginatedResultMapLoaderClass = paginatedResultMapLoader;
	}

	public Class<?> getSqlSourceType(QueryType queryType) {
		if (QueryType.COUNTING.equals(queryType)) {
			return countSqlSourceClass;
		} else if (QueryType.PAGING.equals(queryType)) {
			return pagingSqlSourceClass;
		} else {
			throw new UnknownQueryTypeException("It's unkown query type. can not set a SqlSource class..");
		}		
	}

	public Class<?> getResultMapLoaderType(QueryType queryType) {
		if (QueryType.COUNTING.equals(queryType)) {
			return countingResultMapLoaderClass;
		} else if (QueryType.PAGING.equals(queryType)) {
			return paginatedResultMapLoaderClass;
		} else {
			throw new UnknownQueryTypeException("It's unkown query type. can not set a ResultMapLoader class..");
		}
	}
}
