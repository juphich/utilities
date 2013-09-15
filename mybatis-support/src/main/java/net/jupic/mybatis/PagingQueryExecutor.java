package net.jupic.mybatis;

import java.util.List;

import net.jupic.commons.model.Page;
import net.jupic.commons.model.PageParameters;
import net.jupic.commons.utils.ReflectionUtils;
import net.jupic.mybatis.model.DefaultPage;
import net.jupic.mybatis.sql.QueryType;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author chang jung pil
 *
 */
public class PagingQueryExecutor {

	private static final Logger log = LoggerFactory.getLogger(PagingQueryExecutor.class);

	private SqlSession sqlSession;
	
	private StatementLoader statementLoader;
	
	private PagingMapperLoader pageMapperLoader;
	
	
	
	PagingQueryExecutor(final SqlSession sqlSession) {
		this(sqlSession, new PagingContext(sqlSession.getConfiguration()));
	}
	
	/**
	 * @param sqlSession
	 */
	PagingQueryExecutor(final SqlSession sqlSession, PagingContext pagingContext) {
		this.sqlSession = sqlSession;
		this.statementLoader = new StatementLoader(sqlSession, pagingContext);
		this.pageMapperLoader = new PagingMapperLoader(this);
	}

	/**
	 * @return the sqlSession
	 */
	public SqlSession getSqlSession() {
		return sqlSession;
	}
	
	public PagingContext getPagingContext() {
		return statementLoader.getPagingContext();
	}
	
	/**
	 * @param type
	 * @return mapper 
	 */
	public <T> T getMapper(Class<T> type) {
		return pageMapperLoader.getMapper(type);
	}

	public <T> Page<T> selectPaginatedList(String statementId, Object parameters) {
		String pagingStatement = makePagingStatementId(statementId);
		statementLoader.load(pagingStatement);
		
		String countingStatement = makeCountingStatementId(statementId);
		statementLoader.load(countingStatement);
		
		return selectPaginatedList(pagingStatement, countingStatement, parameters);
	}
	
	public <T> Page<T> selectPaginatedList(String pagingStatement, String countingStatement, Object parameters) {
		if (parameters == null) {
			throw new IllegalArgumentException("parameter object is null. It's not allowed null value as parameters.");
		} else if (!(parameters instanceof PageParameters)) {
			throw new IllegalArgumentException("It is illegalArgument. Type of parameters has to be PageParameters.");
		}
		
		if (log.isDebugEnabled()) {
			debug(sqlSession.getConfiguration().getMappedStatement(countingStatement), parameters);
		}
		int count = sqlSession.selectOne(countingStatement, parameters);
		
		if (log.isDebugEnabled()) {
			debug(sqlSession.getConfiguration().getMappedStatement(pagingStatement), parameters);
		}
		List<T> resultList = sqlSession.selectList(pagingStatement, parameters);
		
		return new DefaultPage<T>(count, resultList, (PageParameters)parameters);
	}

	private String makeCountingStatementId(String statementId) {
		return QueryType.COUNTING.getPrefixOfId() + statementId + QueryType.COUNTING.getSuffixOfId();
	}
	
	private String makePagingStatementId(String statementId) {
		return QueryType.PAGING.getPrefixOfId() + statementId + QueryType.PAGING.getSuffixOfId();
	}
	
	private void debug(MappedStatement statement, Object parameters) {
		BoundSql boundSql = statement.getBoundSql(parameters);
		String query = boundSql.getSql().replaceAll("[\\n\\r\\t]", " ").replaceAll("[\\s]{2,}", " ");

		log.debug(query);
		
		parameters = parameters == null ? new Object() : parameters;

		StringBuilder values = new StringBuilder("[");
		for (ParameterMapping mapping : boundSql.getParameterMappings() ) {
			Object value = ReflectionUtils.findPropertyValue(parameters, mapping.getProperty());
			if (value == null) {
				value = boundSql.getAdditionalParameter(mapping.getProperty());
			}
			
			values.append(value).append(", ");
		}
		values.append("]");
		
		log.debug("parameters - {}", values.toString().replaceFirst(", ]", "]"));
	}
}