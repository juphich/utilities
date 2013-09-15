package net.jupic.mybatis.sql.hsql;

import net.jupic.mybatis.sql.AbstractSqlSource;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;


/**
 * @author chang jung pil
 *
 */
public class HsqlPagingSqlSource extends AbstractSqlSource {

	/**
	 * @param configuration
	 * @param statement
	 */
	public HsqlPagingSqlSource(Configuration configuration, MappedStatement statement) {
		super(configuration, statement);
	}

	/**
	 * @param originalSql
	 * @return
	 */
	@Override
	protected String replaceQueryString(String originalSql) {
		StringBuilder sql = new StringBuilder(originalSql);
		
		sql.append(" limit #{offset}, #{pageSize}");
		
		return sql.toString();
	}
}
