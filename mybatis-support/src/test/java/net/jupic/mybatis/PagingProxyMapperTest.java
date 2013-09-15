package net.jupic.mybatis;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import net.jupic.commons.model.Page;
import net.jupic.commons.model.PageParameters;
import net.jupic.mock.domain.ServiceError;
import net.jupic.mock.domain.ServiceGroup;
import net.jupic.mock.domain.ServiceSearchParameters;
import net.jupic.mock.mapper.ServiceErrorMapper;
import net.jupic.mock.mapper.ServiceGroupMapper;
import net.jupic.mock.mapper.StatisticsMapper;
import net.jupic.mybatis.ExecutorRepository;
import net.jupic.mybatis.PagingMapperLoader;
import net.jupic.mybatis.PagingQueryExecutor;
import net.jupic.mybatis.spring.PagingMapperFactoryBean;

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
public class PagingProxyMapperTest {

	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	private SqlSession sqlSession;
	
	private PagingQueryExecutor executor;
	
	private PageParameters parameters;
	
	@Autowired
	private ServiceGroupMapper serviceGroupMapper;
	
	@Before
	public void init() {
		sqlSession = sqlSessionFactory.openSession();
		executor = ExecutorRepository.getPagingQueryExecutor(sqlSession);
		
		parameters = new ServiceSearchParameters();
		((ServiceSearchParameters) parameters).setSearchText("ru");
		((ServiceSearchParameters) parameters).setSearchType("message");
		((ServiceSearchParameters) parameters).setFromDate(new Date());
	}
	
	@Test
	public void testCreatingPagingProxyMapper() {
		PagingMapperLoader pageMapperLoader = new PagingMapperLoader(executor);
		ServiceErrorMapper mapper = pageMapperLoader.getMapper(ServiceErrorMapper.class);
		
		List<ServiceError> errors = mapper.findServiceErrors(parameters);
		Page<ServiceError> paginated = mapper.findPaginatedServiceErrors(parameters);
		
		assertEquals(errors.size(), paginated.getTotalRows());
	}
	
	@Test
	public void testCreatingDefaultProxyMapper() {
		PagingMapperLoader pageMapperLoader = new PagingMapperLoader(executor);
		StatisticsMapper mapper = pageMapperLoader.getMapper(StatisticsMapper.class);
		
		mapper.selectStatisticsByNation();
	}
	
	@Test
	public void testMapperFactoryBean() throws Exception {
		PagingMapperFactoryBean<ServiceErrorMapper> factoryBean = new PagingMapperFactoryBean<ServiceErrorMapper>();
		factoryBean.setMapperInterface(ServiceErrorMapper.class);
		factoryBean.setSqlSessionFactory(sqlSessionFactory);
		factoryBean.afterPropertiesSet();
		
		ServiceErrorMapper mapper = factoryBean.getObject();
		
		List<ServiceError> errors = mapper.findServiceErrors(parameters);
		Page<ServiceError> paginated = mapper.findPaginatedServiceErrors(parameters);
		
		assertEquals(errors.size(), paginated.getTotalRows());
	}
	
	@Test
	public void testMapperFactoryBeanAsDefaultMapper() throws Exception {
		PagingMapperFactoryBean<StatisticsMapper> factoryBean = new PagingMapperFactoryBean<StatisticsMapper>();
		factoryBean.setMapperInterface(StatisticsMapper.class);
		factoryBean.setSqlSessionFactory(sqlSessionFactory);
		factoryBean.afterPropertiesSet();
		
		StatisticsMapper mapper = factoryBean.getObject();
		mapper.selectStatisticsByNation();
	}
	
	@Test
	public void testPagingMapperInjection() {
		List<ServiceGroup> groups = serviceGroupMapper.findServiceGroupList();
		Page<ServiceGroup> paginated = serviceGroupMapper.findPaginatedServiceGroupList(new ServiceSearchParameters());
		
		assertEquals(groups.size(), paginated.getTotalRows());
	}
}
