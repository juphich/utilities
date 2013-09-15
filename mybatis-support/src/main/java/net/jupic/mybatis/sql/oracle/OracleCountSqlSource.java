package net.jupic.mybatis.sql.oracle;


import net.jupic.mybatis.sql.AbstractSqlSource;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;


/**
 * @author chang jung pil
 *
 */
public class OracleCountSqlSource extends AbstractSqlSource {

	/**
	 * @param configuration
	 */
	public OracleCountSqlSource(Configuration configuration, MappedStatement statement) {
		super(configuration, statement);
	}
	
	@Override
	protected String replaceQueryString(String originalSql) {
		return new StringBuilder("SELECT COUNT(*) FROM (")
				.append(originalSql).append(") CNT_VIEW").toString();		
	}
}
