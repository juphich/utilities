package net.jupic.mybatis.sql.oracle;

import net.jupic.mybatis.sql.AbstractSqlSource;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;


/**
 * @author chang jung pil
 *
 */
public class OraclePagingSqlSource extends AbstractSqlSource {

	/**
	 * @param configuration
	 * @param statement
	 */
	public OraclePagingSqlSource(Configuration configuration, MappedStatement statement) {
		super(configuration, statement);
	}

	/**
	 * @param originalSql
	 * @return
	 */
	@Override
	protected String replaceQueryString(String originalSql) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT * FROM ( SELECT INNER_VIEW.*, ROWNUM AS INPUT_QUERY_RNUM FROM (")
			.append(originalSql)
			.append(") INNER_VIEW WHERE ROWNUM <= #{pageIndex} * #{pageSize} ) WHERE INPUT_QUERY_RNUM > #{offset} ");
		
		return sql.toString();
	}
}
