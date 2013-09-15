package net.jupic.mock.mapper;

import java.util.List;

import net.jupic.commons.model.Page;
import net.jupic.commons.model.Parameters;
import net.jupic.mock.domain.ServiceError;
import net.jupic.mybatis.annotation.Mapper;
import net.jupic.mybatis.annotation.Statement;


/**
 * @author chang jung pil
 *
 */
@Mapper
public interface ServiceErrorMapper {

	/**
	 * @param groupId
	 * @return
	 */
	List<ServiceError> findErrorsByGroupId(String groupId);
	
	/**
	 * @param serviceError
	 */
	void insertServiceError(ServiceError serviceError);

	/**
	 * @param params
	 * @return
	 */
	List<ServiceError> findServiceErrors(Parameters params);

	/**
	 * @param errorId
	 * @return
	 */
	ServiceError findErrorByErrorId(int errorId);

	/**
	 * @param serviceSearchParameters
	 * @return 
	 */
	@Statement(mapper=ServiceErrorMapper.class, id="findServiceErrors")
	Page<ServiceError> findPaginatedServiceErrors(Parameters params);
}
