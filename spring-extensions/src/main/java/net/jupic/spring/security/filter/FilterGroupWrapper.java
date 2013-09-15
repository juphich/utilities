package net.jupic.spring.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.web.filter.GenericFilterBean;

/**
 * @author chang jung pil
 *
 */
public class FilterGroupWrapper extends GenericFilterBean implements ApplicationEventPublisherAware {

	private static Logger logger = LoggerFactory.getLogger(FilterGroupWrapper.class);
	
	private List<GenericFilterBean> filterGroups = new ArrayList<GenericFilterBean>();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			throws IOException, ServletException {
		
		InnerFilterChain innerFilterChain = new InnerFilterChain(filterGroups, chain);
		innerFilterChain.doFilter(request, response);
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		for (GenericFilterBean filterBean : filterGroups) {
			if (filterBean instanceof ApplicationEventPublisherAware) {
				((ApplicationEventPublisherAware) filterBean).setApplicationEventPublisher(applicationEventPublisher);
			}
		}
	}
	
	@Override
	public void afterPropertiesSet() throws ServletException {
	}
	
	public void setFilterList(List<GenericFilterBean> filters) {
		this.filterGroups.addAll(filters);
	}
	
	public void setFilter(GenericFilterBean filter) {
		this.filterGroups.add(filter);
	}
	

	private static class InnerFilterChain implements FilterChain {
		private List<GenericFilterBean> innerFilters;
		private FilterChain originalChain;
		
		private int currentFilterPosition = 0;
		
		InnerFilterChain(List<GenericFilterBean> innerFilters, FilterChain originalChain) {
			this.innerFilters = innerFilters;
			this.originalChain = originalChain;
		}
		
		@Override
		public void doFilter(ServletRequest request, ServletResponse response)
				throws IOException, ServletException {
			if (currentFilterPosition == innerFilters.size()) {
				originalChain.doFilter(request, response);				
			} else {
				GenericFilterBean filter = innerFilters.get(currentFilterPosition);
				
				if (logger.isDebugEnabled()) {
					logger.debug("do inner filter {} ", filter.getClass().getSimpleName());
				}
				
				filter.doFilter(request, response, this);
				
				currentFilterPosition++;
			}
		}
		
	}
}
