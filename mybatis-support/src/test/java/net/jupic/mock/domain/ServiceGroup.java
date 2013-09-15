package net.jupic.mock.domain;

import java.io.Serializable;

public class ServiceGroup implements Serializable {

	private static final long serialVersionUID = -6362754167809385059L;
	
	private String groupId;
	private String serviceUrl;
	private String nation;
	private boolean enabledGroup;
		
	ServiceGroup() {}
	
	public ServiceGroup(String groupId, 
						String serviceUrl, 
						String nation, 
						boolean enabledGroup) {
		
		this.groupId = groupId;
		this.serviceUrl = serviceUrl;
		this.nation = nation;
		this.enabledGroup = enabledGroup;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}
	
	public boolean isEnabledGroup() {
		return enabledGroup;
	}
	
	public void setEnabledGroup(boolean enabledGroup) {
		this.enabledGroup = enabledGroup;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;		
		return prime * result + groupId.hashCode();
	}

	@Override
	public boolean equals(final Object otherGroup) {
		if (otherGroup == this) {
			return true; 
		} else if (otherGroup instanceof ServiceGroup) {
			return this.groupId.equals(((ServiceGroup) otherGroup).groupId);
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder info = new StringBuilder();
		info.append("[groupId : ").append(groupId).append(", ")
		    .append("serviceUrl : ").append(serviceUrl).append(", ")
		    .append("nation : ").append(nation).append(", ")
		    .append("enable : ").append(enabledGroup).append("]");
		
		return info.toString();
	}
}
