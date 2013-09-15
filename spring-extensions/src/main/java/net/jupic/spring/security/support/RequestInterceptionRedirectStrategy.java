package net.jupic.spring.security.support;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.UrlUtils;

public class RequestInterceptionRedirectStrategy implements RedirectStrategy {

	public final static String DEFAULT_ARRIVAL_URL_PARAMETER = "arrival_url";
	
	protected final Logger log = LoggerFactory.getLogger(RequestInterceptionRedirectStrategy.class);

    private boolean contextRelative;
    private String relayUrl;
    private String arrivalUrlParameter = DEFAULT_ARRIVAL_URL_PARAMETER;
    
	@Override
	public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        String redirectUrl = null;
        if (relayUrl == null || relayUrl.isEmpty()) {
        	redirectUrl = calculateRedirectUrl(request.getContextPath(), url);
        } else {
        	redirectUrl = calculateRedirectUrl(request.getContextPath(), relayUrl);
        	String destUrl = calculateRedirectUrl(request.getContextPath(), url);
        	
        	//request.setAttribute(arrivalUrlParameter, response.encodeRedirectURL(destUrl));
        	request.getSession().setAttribute(arrivalUrlParameter, response.encodeRedirectURL(destUrl));
        }
        
        redirectUrl = response.encodeRedirectURL(redirectUrl);

        if (log.isDebugEnabled()) {
            log.debug("Redirecting to '" + redirectUrl + "'");
        }
        
        response.sendRedirect(redirectUrl);
    }

    private String calculateRedirectUrl(String contextPath, String url) {
        if (!UrlUtils.isAbsoluteUrl(url)) {
            if (contextRelative) {
                return url;
            } else {
                return contextPath + url;
            }
        }

        // Full URL, including http(s)://

        if (!contextRelative) {
            return url;
        }

        // Calculate the relative URL from the fully qualified URL, minus the scheme and base context.
        url = url.substring(url.indexOf("://") + 3); // strip off scheme
        url = url.substring(url.indexOf(contextPath) + contextPath.length());

        if (url.length() > 1 && url.charAt(0) == '/') {
            url = url.substring(1);
        }

        return url;
    }

    /**
     * If <tt>true</tt>, causes any redirection URLs to be calculated minus the protocol
     * and context path (defaults to <tt>false</tt>).
     */
    public void setContextRelative(boolean useRelativeContext) {
        this.contextRelative = useRelativeContext;
    }

	public void setRelayUrl(String relayUrl) {
		this.relayUrl = relayUrl;
	}

	public void setArrivalUrlParameter(String arrivalUrlParameter) {
		this.arrivalUrlParameter = arrivalUrlParameter;
	}
}
