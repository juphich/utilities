package net.jupic.mybatis.sql;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

/**
 * @author chang jung pil
 *
 */
public abstract class AbstractSqlSource implements SqlSource {

	protected Configuration configuration;
	protected MappedStatement statement;
	
	private final String bindVarPrefix = "#{";
	private final String bindVarSuffix = "}";

	/**
	 * @param configuration
	 * @param statement
	 */
	public AbstractSqlSource(Configuration configuration, MappedStatement statement) {
		this.configuration = configuration;
		this.statement = statement;
	}

	/**
	 * @param parameters
	 * @return
	 */
	@Override
	public BoundSql getBoundSql(Object parameters) {
		BoundSql srcBoundSql = statement.getBoundSql(parameters);
		
		String countSql = replaceQueryString(srcBoundSql.getSql());
		
		String sql = reverseMappingParameters(countSql, srcBoundSql.getParameterMappings());
		
		BoundSql boundSql = parseSource(sql, parameters);
		
		for (ParameterMapping mapping : srcBoundSql.getParameterMappings()) {
			String property = mapping.getProperty();
			Object additionalParameter = srcBoundSql.getAdditionalParameter(property);
			if (additionalParameter != null) {
				boundSql.setAdditionalParameter(property, additionalParameter);
			}
		}
		
		return boundSql;
	}

	protected abstract String replaceQueryString(String originalSql);
	
	protected BoundSql parseSource(String sql, Object parameters) {
		SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
		
		Class<?> parameterType = parameters == null ? Object.class : parameters.getClass();
		SqlSource sqlSource = sqlSourceParser.parse(sql, parameterType);
		
		return sqlSource.getBoundSql(parameters);
	}

	protected String reverseMappingParameters(String sourceSql, List<ParameterMapping> mappings) {
		StringBuffer mappedSql = new StringBuffer();
		
		Pattern pattern = Pattern.compile("[\\?]");
		Matcher matcher = pattern.matcher(sourceSql);
		
		int index = 0;
		while(matcher.find()) {
			matcher.appendReplacement(mappedSql, bindVarPrefix + mappings.get(index).getProperty() + bindVarSuffix);
			index++;
		}

		matcher.appendTail(mappedSql);
		
		return mappedSql.toString();
	}

}