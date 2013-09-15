package net.jupic.mybatis.result;

import java.util.List;

import org.apache.ibatis.mapping.ResultMap;

/**
 * @author chang jung pil
 *
 */
public interface ResultMapLoader {
	public List<ResultMap> load();
}