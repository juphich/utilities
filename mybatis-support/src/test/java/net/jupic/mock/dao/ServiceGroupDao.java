package net.jupic.mock.dao;

import java.util.List;

import net.jupic.commons.model.Page;
import net.jupic.commons.model.PageParameters;
import net.jupic.commons.model.Parameters;
import net.jupic.mock.domain.ServiceGroup;
import net.jupic.mybatis.spring.PagingDaoSupport;


/**
 * @author chang jung pil
 *
 */
public class ServiceGroupDao extends PagingDaoSupport {

	public Page<ServiceGroup> selectPaginatedServiceGroups(PageParameters parameters) {
		return selectPagenatedList("net.jupic.mock.mapper.ServiceGroupMapper.findServiceGroupList", parameters);
	}
	
	public List<ServiceGroup> selectServiceGrous(Parameters parameters) {
		return getSqlSession().selectList("net.jupic.mock.mapper.ServiceGroupMapper.findServiceGroupList", parameters);
	}
}
