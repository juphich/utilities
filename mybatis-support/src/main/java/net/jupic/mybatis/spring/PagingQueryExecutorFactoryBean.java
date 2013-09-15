package net.jupic.mybatis.spring;

import static org.springframework.util.Assert.notNull;

import net.jupic.mybatis.ExecutorRepository;
import net.jupic.mybatis.PagingContext;
import net.jupic.mybatis.PagingQueryExecutor;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author chang jung pil
 *
 */
public class PagingQueryExecutorFactoryBean implements FactoryBean<PagingQueryExecutor>, InitializingBean {

	private SqlSession sqlSession;
	
	private Class<?> countSqlSourceClass;
	private Class<?> pagingSqlSourceClass;
	
	private Class<?> countingResultMapLoaderClass;
	private Class<?> paginatedResultMapLoaderClass;
	
	@Autowired(required = false)
	public final void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSession = new SqlSessionTemplate(sqlSessionFactory);
	}
	
	public void setCountSqlSourceClass(Class<?> countSqlSourceClass) {
		this.countSqlSourceClass = countSqlSourceClass;
	}

	public void setPagingSqlSourceClass(Class<?> pagingSqlSourceClass) {
		this.pagingSqlSourceClass = pagingSqlSourceClass;
	}

	public void setCountingResultMapLoaderClass(
			Class<?> countingResultMapLoaderClass) {
		this.countingResultMapLoaderClass = countingResultMapLoaderClass;
	}

	public void setPaginatedResultMapLoaderClass(
			Class<?> paginatedResultMapLoaderClass) {
		this.paginatedResultMapLoaderClass = paginatedResultMapLoaderClass;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		notNull(this.sqlSession, "Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required");
	}

	@Override
	public PagingQueryExecutor getObject() throws Exception {
		PagingContext pagingContext = new PagingContext(sqlSession.getConfiguration());
		
		if (countSqlSourceClass != null) {
			pagingContext.setCountSqlSourceClass(countSqlSourceClass);
		}
		
		if (pagingSqlSourceClass != null) {
			pagingContext.setPaingSqlSourceClass(pagingSqlSourceClass);
		}
		
		if (countingResultMapLoaderClass != null) {
			pagingContext.setCountingResultMapLoader(countingResultMapLoaderClass);
		}
		
		if (countingResultMapLoaderClass != null) {
			pagingContext.setPaginatedResultMapLoader(paginatedResultMapLoaderClass);
		}
		
		return ExecutorRepository.getPagingQueryExecutor(sqlSession, pagingContext);
	}

	@Override
	public Class<PagingQueryExecutor> getObjectType() {
		return PagingQueryExecutor.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
