package net.jupic.mybatis.result;

import java.util.Collections;
import java.util.List;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.session.Configuration;


/**
 * @author chang jung pil
 *
 */
public class PaginatedResultMapLoader extends AbstractResultMapLoader {

	/**
	 * @param configuration
	 * @param statement
	 */
	public PaginatedResultMapLoader(Configuration configuration,
									MappedStatement statement) {
		super(configuration, statement);
	}
	
	@Override
	public List<ResultMap> load() {
		if(statement.getResultMaps() != null 
				&& statement.getResultMaps().size() > 0) {
			return statement.getResultMaps();
		} else {
			return Collections.emptyList();
		}
	}
}
