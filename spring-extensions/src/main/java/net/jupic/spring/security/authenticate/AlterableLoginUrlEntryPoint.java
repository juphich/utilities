package net.jupic.spring.security.authenticate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.security.web.util.UrlUtils;

public class AlterableLoginUrlEntryPoint extends LoginUrlAuthenticationEntryPoint {

	private RequestCache requestCache = new HttpSessionRequestCache();	
	private String savedRequestUrlParameter;	
	private String overridingHost;	
	private int overridingPort = 80;	
	
	public AlterableLoginUrlEntryPoint(String loginFormUrl) {
		super(loginFormUrl);
	}
	
	@Override
	protected String buildRedirectUrlToLoginPage(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException) {
		SavedRequest savedRequest = requestCache.getRequest(request, response);		
		String targetUrl = savedRequest.getRedirectUrl();
		
		if ((overridingHost != null && overridingHost.length() > 0) 
				&& (savedRequest instanceof DefaultSavedRequest)) {
			String scheme = ((DefaultSavedRequest) savedRequest).getScheme();
			String requestURI = ((DefaultSavedRequest) savedRequest).getRequestURI();
			String queryString = ((DefaultSavedRequest) savedRequest).getQueryString();
			targetUrl = UrlUtils.buildFullRequestUrl(scheme, overridingHost, overridingPort, requestURI, queryString);
		}
		
		if (savedRequestUrlParameter != null && targetUrl != null && targetUrl.length() > 0) {
			
			StringBuilder redirectUrl = new StringBuilder(
					super.buildRedirectUrlToLoginPage(request, response, authException));
			
			String separator = "?";
			if (redirectUrl.indexOf("?") > -1) {
				separator = "&";
			}
			redirectUrl.append(separator).append(savedRequestUrlParameter).append("=").append(targetUrl);
			
			return redirectUrl.toString();
		} else {
			return super.buildRedirectUrlToLoginPage(request, response, authException);
		}
	}

	public void setRequestCache(RequestCache requestCache) {
		this.requestCache = requestCache;
	}

	public void setSavedRequestUrlParameter(String savedRequestUrlParameter) {
		this.savedRequestUrlParameter = savedRequestUrlParameter;
	}

	public void setOverridingHost(String overridingHost) {
		this.overridingHost = overridingHost;
	}

	public void setOverridingPort(int overridingPort) {
		this.overridingPort = overridingPort;
	}
}
