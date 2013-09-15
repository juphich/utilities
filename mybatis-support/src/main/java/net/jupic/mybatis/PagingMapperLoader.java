package net.jupic.mybatis;

import net.jupic.mybatis.proxy.PagingMapperProxy;

import org.apache.ibatis.session.SqlSession;


/**
 * @author chang jung pil
 *
 */
public class PagingMapperLoader {

	private SqlSession sqlSession;
	private PagingQueryExecutor pagingQueryExecutor;
	
	/**
	 * @param sqlSessionFactory
	 */
	public PagingMapperLoader(PagingQueryExecutor pagingQueryExecutor) {
		this.pagingQueryExecutor = pagingQueryExecutor;
		this.sqlSession =  pagingQueryExecutor.getSqlSession();
	}

	/**
	 * @param class1
	 * @return
	 */
	public <T> T getMapper(Class<T> type) {
		Object mapper = sqlSession.getMapper(type);
		
		return PagingMapperProxy.newProxyInstance(type, mapper, pagingQueryExecutor);
	}

}
