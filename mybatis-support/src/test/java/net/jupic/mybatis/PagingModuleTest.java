package net.jupic.mybatis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import net.jupic.commons.model.Page;
import net.jupic.commons.model.PageParameters;
import net.jupic.mock.domain.ServiceError;
import net.jupic.mock.domain.ServiceSearchParameters;
import net.jupic.mybatis.ExecutorRepository;
import net.jupic.mybatis.PagingContext;
import net.jupic.mybatis.PagingQueryExecutor;
import net.jupic.mybatis.StatementLoader;
import net.jupic.mybatis.result.CountingResultMapLoader;
import net.jupic.mybatis.result.PaginatedResultMapLoader;
import net.jupic.mybatis.result.ResultMapLoader;
import net.jupic.mybatis.sql.QueryType;
import net.jupic.mybatis.sql.mysql.MySqlCountSqlSource;
import net.jupic.mybatis.sql.mysql.MySqlPagingSqlSource;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * @author chang jung pil
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/testContext-paging-module.xml"})
@DirtiesContext
public class PagingModuleTest {

	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	private SqlSession sqlSession;
	
	private String statementId;

	private PageParameters parameters;
	
	@Before
	public void init() {
		sqlSession = sqlSessionFactory.openSession();
		statementId = "net.jupic.mock.mapper.ServiceErrorMapper.findServiceErrors";
		
		parameters = new ServiceSearchParameters();
		((ServiceSearchParameters) parameters).setSearchText("ru");
		((ServiceSearchParameters) parameters).setSearchType("message");
		((ServiceSearchParameters) parameters).setFromDate(new Date());
	}
	
	@Test
	public void testCountSqlSource() {
		Configuration configuration = sqlSession.getConfiguration();
		MappedStatement statement = configuration.getMappedStatement(statementId);
		
		SqlSource countSqlSource = new MySqlCountSqlSource(configuration, statement);
		BoundSql boundSql = countSqlSource.getBoundSql(parameters);
		
		List<ParameterMapping> srcMappings = statement.getBoundSql(parameters).getParameterMappings();
		List<ParameterMapping> mappings = boundSql.getParameterMappings();
		
		assertEquals(srcMappings.size(), mappings.size());
	}
	
	@Test
	public void testPagingSqlSource() {
		Configuration configuration = sqlSession.getConfiguration();
		MappedStatement statement = configuration.getMappedStatement(statementId);
		
		SqlSource pagingSqlSource = new MySqlPagingSqlSource(configuration, statement);
		
		BoundSql boundSql = pagingSqlSource.getBoundSql(parameters);
		
		List<ParameterMapping> srcMappings = statement.getBoundSql(parameters).getParameterMappings();
		List<ParameterMapping> mappings = boundSql.getParameterMappings();
		
		assertEquals(srcMappings.size(), mappings.size() - 2);
	}
	
	@Test
	public void testCountingResutMapLoader() {
		Configuration configuration = sqlSession.getConfiguration();
		MappedStatement statement = configuration.getMappedStatement(statementId);
		
		ResultMapLoader loader = new CountingResultMapLoader(configuration, statement);
		
		List<ResultMap> resultMaps = loader.load();
		
		assertEquals(resultMaps.get(0).getType(), Integer.class);
	}
	
	@Test
	public void testPaginatedResutMapLoader() {
		Configuration configuration = sqlSession.getConfiguration();
		MappedStatement statement = configuration.getMappedStatement(statementId);
		
		ResultMapLoader loader = new PaginatedResultMapLoader(configuration, statement);
		
		List<ResultMap> resultMaps = loader.load();
		
		assertEquals(resultMaps.get(0).getType(), ServiceError.class);
	}
	
	@Test
	public void testStatementLoader() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		PagingContext pagingContext = new PagingContext(sqlSession.getConfiguration());
		StatementLoader loader = new StatementLoader(sqlSession, pagingContext);
		
		String countStatement = QueryType.COUNTING.getPrefixOfId() + statementId + QueryType.COUNTING.getSuffixOfId();
		
		loader.load(countStatement);
		
		assertTrue(sqlSession.getConfiguration().hasStatement(countStatement));
		
		String pagingStatement = QueryType.PAGING.getPrefixOfId() + statementId + QueryType.PAGING.getSuffixOfId();
		
		loader.load(pagingStatement);
		
		assertTrue(sqlSession.getConfiguration().hasStatement(pagingStatement));
	}
	
	@Test
	public void testRetrievingPaginatedList() {
		PagingQueryExecutor queryExecutor = ExecutorRepository.getPagingQueryExecutor(sqlSession);
		
		Page<ServiceError> paginated = queryExecutor.selectPaginatedList(statementId, parameters);
		
		assertNotNull(paginated);
	}
}
