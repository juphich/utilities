package net.jupic.mock.mapper;

import java.util.List;

import net.jupic.commons.model.Page;
import net.jupic.commons.model.Parameters;
import net.jupic.mock.domain.ServiceGroup;
import net.jupic.mybatis.annotation.Mapper;
import net.jupic.mybatis.annotation.Statement;


/**
 * @author chang jung pil
 */
@Mapper
public interface ServiceGroupMapper {

	List<ServiceGroup> findServiceGroupList();
	
	ServiceGroup findServiceGroup(String groupId);
	
	void insertServiceGroup(ServiceGroup serviceGroup);

	void updateServiceGroup(ServiceGroup serviceGroup);

	void deleteServiceGroup(String groupId);

	@Statement(mapper=ServiceGroupMapper.class, id="findServiceGroupList")
	Page<ServiceGroup> findPaginatedServiceGroupList(Parameters parameters);
}