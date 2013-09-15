package net.jupic.mybatis;

import static org.junit.Assert.assertEquals;

import java.util.List;

import net.jupic.commons.model.Page;
import net.jupic.mock.dao.ServiceGroupDao;
import net.jupic.mock.domain.ServiceGroup;
import net.jupic.mock.domain.ServiceSearchParameters;
import net.jupic.mybatis.ExecutorRepository;
import net.jupic.mybatis.PagingContext;
import net.jupic.mybatis.PagingQueryExecutor;
import net.jupic.mybatis.sql.oracle.OraclePagingSqlSource;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/testContext-paging-spring.xml"})
@DirtiesContext
public class PagingSpringModuleTest {

	@Autowired
	@Qualifier("sqlSessionFactory")
	private SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	@Qualifier("pagingQueryExecutor")
	private PagingQueryExecutor pagingQueryExecutor;
	
	@Autowired
	private ServiceGroupDao serviceGroupDao;
	
	@Test
	public void testSqlSourceType() {
		Configuration configuration = sqlSessionFactory.getConfiguration();
		
		PagingContext pagingContext = new PagingContext(configuration);
		pagingContext.setPaingSqlSourceClass(OraclePagingSqlSource.class);
		
		assertEquals(OraclePagingSqlSource.class, pagingContext.getPaingSqlSourceClass());
	}
	
	@Test
	public void testPagingContextAsserting() {
		PagingContext context = pagingQueryExecutor.getPagingContext();
		
		assertEquals(OraclePagingSqlSource.class, context.getPaingSqlSourceClass());
	}
	
	@Test
	public void testPagingQueryExecutorSupportDao() {
		Page<ServiceGroup> paginated = serviceGroupDao.selectPaginatedServiceGroups(new ServiceSearchParameters());
		List<ServiceGroup> list = serviceGroupDao.selectServiceGrous(new ServiceSearchParameters());
		
		assertEquals(list.size(), paginated.getTotalRows());
		assertEquals(2, ExecutorRepository.getExecutorNumbers());
	}
}
