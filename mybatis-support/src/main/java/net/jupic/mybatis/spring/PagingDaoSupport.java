package net.jupic.mybatis.spring;

import net.jupic.commons.model.Page;
import net.jupic.commons.model.PageParameters;
import net.jupic.mybatis.ExecutorRepository;
import net.jupic.mybatis.PagingContext;
import net.jupic.mybatis.PagingQueryExecutor;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author chang jung pil
 *
 */
public class PagingDaoSupport extends SqlSessionDaoSupport {

	private PagingQueryExecutor pagingQueryExecutor;
	
	@Autowired(required = false)
	public void setPagingQueryExecutor(PagingQueryExecutor executor) {
		this.pagingQueryExecutor = executor;
	}
	
	public <D> Page<D> selectPagenatedList(String statementId) {
		return selectPagenatedList(statementId, null);
	}
	
	public <D> Page<D> selectPagenatedList(String statementId, PageParameters parameters) {
		return pagingQueryExecutor.selectPaginatedList(statementId, parameters);
	}
	
	public PagingQueryExecutor getPagingQueryExecutor() {
		return pagingQueryExecutor;
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
}
