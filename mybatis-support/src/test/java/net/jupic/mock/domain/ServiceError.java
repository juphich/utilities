package net.jupic.mock.domain;

import java.util.Date;

/**
 * @author chang jung pil
 *
 */
public final class ServiceError {

	private ServiceGroup serviceGroup = new ServiceGroup();
	private Integer errorId = 0;
	
	private String exceptionName;
	
	private String message;
	
	private String trace;
	
	private String remoteIp;
	
	private String currentUrl;
	
	private String urlRefferrer;
	
	private String customValue;
	
	private Date occuredTime;
	
	private Date reportedTime;

	/**
	 * Default Constructor for reflection
	 */
	ServiceError() {};
	
	/**
	 * @param serviceGroup
	 * @param errorId
	 * @param exceptionName
	 * @param message
	 * @param trace
	 * @param remoteIp
	 * @param currentUrl
	 * @param urlRefferrer
	 * @param customValue
	 * @param occuredTime
	 * @param reportedTime
	 */
	public ServiceError(ServiceGroup serviceGroup,
						Integer errorId,
						String exceptionName,
						String message,
						String trace,
						String remoteIp,
						String currentUrl,
						String urlRefferrer,
						String customValue,
						Date occuredTime) {
		this.serviceGroup = serviceGroup;
		this.errorId = errorId;
		this.exceptionName = exceptionName;
		this.message = message;
		this.trace = trace;
		this.remoteIp = remoteIp;
		this.currentUrl = currentUrl;
		this.urlRefferrer = urlRefferrer;
		this.customValue = customValue;
		this.occuredTime = occuredTime;
	}



	/**
	 * @param serviceGroup
	 */
	public ServiceError(ServiceGroup serviceGroup) {
		this.serviceGroup = serviceGroup;
	}

	/**
	 * @return the serviceGroup
	 */
	public ServiceGroup getServiceGroup() {
		return serviceGroup;
	}

	/**
	 * @return the errorId
	 */
	public int getErrorId() {
		return errorId;
	}

	/**
	 * @return the exceptionName
	 */
	public String getExceptionName() {
		return exceptionName;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return the trace
	 */
	public String getTrace() {
		return trace;
	}

	/**
	 * @return the remoteId
	 */
	public String getRemoteIp() {
		return remoteIp;
	}

	/**
	 * @return the currentUrl
	 */
	public String getCurrentUrl() {
		return currentUrl;
	}

	/**
	 * @return the uriRefferrer
	 */
	public String getUrlRefferrer() {
		return urlRefferrer;
	}

	/**
	 * @return the customValue
	 */
	public String getCustomValue() {
		return customValue;
	}

	/**
	 * @return the occuredTime
	 */
	public Date getOccuredTime() {
		return occuredTime;
	}

	/**
	 * @return the reportedTime
	 */
	public Date getReportedTime() {
		return reportedTime;
	}
	
	@Override
	public String toString() {
		StringBuilder info = new StringBuilder();
		info.append("[").append("group : ").append(this.serviceGroup.getGroupId()).append(", ")
			.append("name : ").append(this.exceptionName).append(", ")
		    .append("occuredTime : ").append(this.occuredTime).append(", ")
		    .append("message : ").append(this.message).append("]");
		
		return info.toString();
	}
}
