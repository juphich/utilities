package net.jupic.mybatis.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.jupic.mybatis.sql.QueryType;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;


/**
 * @author chang jung pil
 *
 */
public class CountingResultMapLoader extends AbstractResultMapLoader {

	/**
	 * @param configuration
	 * @param statement
	 */
	public CountingResultMapLoader(Configuration configuration, MappedStatement statement) {
		super(configuration, statement);
	}

	/**
	 * @return
	 */
	@Override
	public List<ResultMap> load()  {
		String resultMapId = new StringBuilder(QueryType.COUNTING.getPrefixOfId())
				.append(statement.getId())
				.append(QueryType.COUNTING.getSuffixOfId())
				.append("-Inline").toString();
		
		List<ResultMap> resultMaps = new ArrayList<ResultMap>(); 
		
		List<ResultMapping> mappings = Collections.emptyList();
		
		if(!configuration.hasResultMap(resultMapId)) {
			ResultMap.Builder resultMapBuilder = 
					new ResultMap.Builder(configuration,
							resultMapId,
							Integer.class,
							mappings);
			
			resultMaps.add(resultMapBuilder.build());
		} else {
			resultMaps.add(configuration.getResultMap(resultMapId));
		}
		
		return resultMaps;
	}
}
