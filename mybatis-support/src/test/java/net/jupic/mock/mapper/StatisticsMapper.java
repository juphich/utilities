package net.jupic.mock.mapper;

import java.util.List;
import java.util.Map;

import net.jupic.mybatis.annotation.Mapper;


/**
 * @author chang jung pil
 *
 */
@Mapper
public interface StatisticsMapper {

	List<Map<String, Object>> selectStatisticsByNation();
}
