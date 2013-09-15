package net.jupic.mybatis;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.session.SqlSession;

/**
 * @author chang jung pil
 *
 */
public class ExecutorRepository {

	private static ExecutorRepository repository;
	
	static {
		repository = new ExecutorRepository();
	}
	
	private final ConcurrentHashMap<SqlSession, PagingQueryExecutor> executorMap;
	
	private ExecutorRepository() {
		executorMap = new ConcurrentHashMap<SqlSession, PagingQueryExecutor>();
	}
	
	public static PagingQueryExecutor getPagingQueryExecutor(final SqlSession sqlSession) {
		return getPagingQueryExecutor(sqlSession, new PagingContext(sqlSession.getConfiguration()));
	}
	
	public static PagingQueryExecutor getPagingQueryExecutor(final SqlSession sqlSession, PagingContext pagingContext) {
		return repository.getExecutor(sqlSession, pagingContext);
	}
	
	/**
	 * @param sqlSession
	 * @param pagingContext
	 * @return
	 */
	private PagingQueryExecutor getExecutor(final SqlSession sqlSession, PagingContext pagingContext) {
		PagingQueryExecutor executor = null;
		
		if (this.executorMap.containsKey(sqlSession)) {
			executor = this.executorMap.get(sqlSession);
		} else {
			executor = new PagingQueryExecutor(sqlSession, pagingContext);
			this.executorMap.put(sqlSession, executor);
		}
		
		return executor;
	}

	/**
	 * @return
	 */
	public static int getExecutorNumbers() {
		return repository.executorMap.size();
	}
}
