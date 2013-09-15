package net.jupic.mybatis.result;

import java.util.List;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.session.Configuration;

/**
 * @author chang jung pil
 *
 */
public abstract class AbstractResultMapLoader implements ResultMapLoader {

	protected Configuration configuration;
	protected MappedStatement statement;
	
	/**
	 * @param configuration
	 * @param statement
	 */
	public AbstractResultMapLoader(Configuration configuration, MappedStatement statement) {
		this.configuration = configuration;
		this.statement = statement;
	}

	/**
	 * @return
	 */
	public abstract List<ResultMap> load();
}
