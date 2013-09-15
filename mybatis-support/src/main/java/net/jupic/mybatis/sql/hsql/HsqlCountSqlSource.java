package net.jupic.mybatis.sql.hsql;


import net.jupic.mybatis.sql.AbstractSqlSource;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;


/**
 * @author chang jung pil
 *
 */
public class HsqlCountSqlSource extends AbstractSqlSource {

	/**
	 * @param configuration
	 */
	public HsqlCountSqlSource(Configuration configuration, MappedStatement statement) {
		super(configuration, statement);
	}
	
	@Override
	protected String replaceQueryString(String originalSql) {
		return new StringBuilder("select count(*) from (")
				.append(originalSql).append(") in_view").toString();		
	}
}
