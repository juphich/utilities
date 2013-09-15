package net.jupic.mybatis.support;

import java.util.concurrent.CopyOnWriteArrayList;

import net.jupic.commons.model.AbstractPageParameters;
import net.jupic.commons.model.Page;
import net.jupic.commons.model.PageParameters;
import net.jupic.mybatis.ExecutorRepository;
import net.jupic.mybatis.PagingQueryExecutor;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;


public class PagingItemReader<T> extends AbstractPagingItemReader<T> {

	private SqlSessionFactory sqlSessionFactory;
	private SqlSessionTemplate sqlSessionTemplate;
	private PagingQueryExecutor pagingQueryExecutor;
	
	private String queryId;
	private PageParameters parameters;
	
	public PagingItemReader() {
		setName(ClassUtils.getShortName(PagingItemReader.class));
	}
	
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}

	public void setPagingQueryExecutor(PagingQueryExecutor pagingQueryExecutor) {
		this.pagingQueryExecutor = pagingQueryExecutor;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public void setParameters(PageParameters parameters) {
		this.parameters = parameters;
	}

	@Override
	protected void doReadPage() {
		if (results == null) {
			results = new CopyOnWriteArrayList<T>();
		} else {
			results.clear();
		}
		
		if (parameters == null) {
			parameters = new InternalPageParameters();
		}
		parameters.setPageIndex(getPage() + 1);
		parameters.setPageSize(getPageSize());
		
		Page<T> page = pagingQueryExecutor.selectPaginatedList(queryId, parameters);
		//List<T> list = pagingQueryExecutor.getSqlSession().selectList(queryId, parameters);
		results.addAll(page.getList());
	}

	@Override
	protected void doJumpToPage(int itemIndex) {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		
		Assert.notNull(queryId, "MyBatis QueryId must be set.");
		if (pagingQueryExecutor == null && sqlSessionTemplate == null && sqlSessionFactory == null) {
			Assert.notNull(pagingQueryExecutor, "PagingQueryExecutor must be set. Set a PagingQueryExecutor or SqlSessionTemplate!!!");
		}
		
		if (pagingQueryExecutor == null) {
			if (this.sqlSessionTemplate == null) {
				this.sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
			}
			this.pagingQueryExecutor = ExecutorRepository.getPagingQueryExecutor(sqlSessionTemplate);
		}
	}
	
	private class InternalPageParameters extends AbstractPageParameters {
		private static final long serialVersionUID = -4743527944886524608L;
	}
}
