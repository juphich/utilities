package net.jupic.mybatis.spring;

import net.jupic.mybatis.ExecutorRepository;
import net.jupic.mybatis.PagingContext;
import net.jupic.mybatis.PagingQueryExecutor;

import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author chang jung pil
 *
 */
public class PagingMapperFactoryBean<T> extends MapperFactoryBean<T> {

	private PagingQueryExecutor pagingQueryExecutor;
	
	@Autowired(required=false)
	public void setPagingQueryExecutor(PagingQueryExecutor pagingQueryExecutor) {
		this.pagingQueryExecutor = pagingQueryExecutor;
	}
	
	/**
	 * 
	 */
	@Override
	protected void checkDaoConfig() {
		super.checkDaoConfig();
		
		if (pagingQueryExecutor == null) {
			PagingContext pagingContext = new PagingContext(getSqlSession().getConfiguration());
			pagingQueryExecutor = ExecutorRepository.getPagingQueryExecutor(getSqlSession(), pagingContext);
		}
	}

	/**
	 * @return
	 * @throws Exception
	 */
	@Override
	public T getObject() throws Exception {
		return pagingQueryExecutor.getMapper(getObjectType());
	}
}
