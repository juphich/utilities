package net.jupic.mybatis;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.jupic.mybatis.exception.InvalidStatementExceptions;
import net.jupic.mybatis.exception.StatementInitializingFailedException;
import net.jupic.mybatis.result.ResultMapLoader;
import net.jupic.mybatis.sql.QueryType;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;


/**
 * @author chang jung pil
 *
 */
public class StatementLoader {

	private Configuration configuration;
	
	private PagingContext paingContext;
	
	/**
	 * @param sqlSession
	 */
	public StatementLoader(SqlSession sqlSession, PagingContext pagingContext) {
		this.configuration = sqlSession.getConfiguration();
		this.paingContext = pagingContext;
	}

	public PagingContext getPagingContext() {
		return paingContext;
	}
	
	/**
	 * @param statementId
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public void load(String statementId) {
		String originalStatementId = getOriginalStatementId(statementId);
		
		MappedStatement targetStatement = null;
		
		try {
			targetStatement = configuration.getMappedStatement(originalStatementId);
		} catch (Exception e) {
			throw new InvalidStatementExceptions("there is no statement ("
						+ originalStatementId +") : please check mappings!!", e);
		}
		
		if (!configuration.hasStatement(statementId)) {
			QueryType queryType = getSqlSourceType(statementId);
			
			if (queryType.equals(QueryType.UNKNOWN)) {
				return;
			}
			
			Class<?> sourceType = paingContext.getSqlSourceType(queryType);
			Class<?> resultMapLoaderType = paingContext.getResultMapLoaderType(queryType);
			
			Constructor<?> sqlSourceConstructor = null;
			SqlSource sqlSource = null;
			
			try {
				sqlSourceConstructor = sourceType.getConstructor(Configuration.class, MappedStatement.class);
				sqlSource = (SqlSource)sqlSourceConstructor.newInstance(configuration, targetStatement);
			} catch (Exception e) {
				throw new StatementInitializingFailedException("Could not create SqlSource Object.", e);
			}
			
			Constructor<?> rmLoaderConstructor = null;
			ResultMapLoader resultMapLoader = null;
			
			try {
				rmLoaderConstructor = resultMapLoaderType.getConstructor(Configuration.class, MappedStatement.class);
				resultMapLoader = (ResultMapLoader)rmLoaderConstructor.newInstance(configuration, targetStatement);
			} catch (Exception e) {
				throw new StatementInitializingFailedException("Could not create ResultMapLoader Object. ", e);
			}
			
			MappedStatement.Builder statementBuilder = 
					new MappedStatement.Builder(configuration, statementId, sqlSource, SqlCommandType.SELECT);
			statementBuilder.timeout(configuration.getDefaultStatementTimeout());
			statementBuilder.resultMaps(resultMapLoader.load());
			
			MappedStatement statement = statementBuilder.build();
			
			configuration.addMappedStatement(statement);
		}
	}
	
	private String getOriginalStatementId(String statementId) {
		int cntIndex = statementId.lastIndexOf(QueryType.COUNTING.getSuffixOfId());
		int pgIndex  = statementId.lastIndexOf(QueryType.PAGING.getSuffixOfId());
		
		if (cntIndex > 0) {
			return statementId.substring(1, cntIndex);
		} else if (pgIndex > 0) {
			return statementId.substring(1, pgIndex);
		} else {
			return statementId;
		}
	}
	
	private QueryType getSqlSourceType(String statementId) {
		if (statementId.indexOf(QueryType.COUNTING.getSuffixOfId()) > -1) {
			return QueryType.COUNTING;
		} else if (statementId.indexOf(QueryType.PAGING.getSuffixOfId()) > -1) {
			return QueryType.PAGING;
		} else {
			return QueryType.UNKNOWN;
		}
	}
}
