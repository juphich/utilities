package net.jupic.spring.security.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.jupic.spring.security.domain.LoginFailureInfo;


/**
 * @author chang jung pil
 *
 */
public class LoginFailureCheckTag extends TagSupport {

	private static final long serialVersionUID = -974402256029474356L;

	private String var = "fail";
	
	@Override
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
		LoginFailureInfo info = LoginFailureInfo.get(request);
		
		if (info != null) {
			this.pageContext.setAttribute(var, info.getInfoMap());
			
			return EVAL_BODY_INCLUDE;
		} else {
			return SKIP_BODY;
		}
	}
	
	@Override
	public int doEndTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
		LoginFailureInfo.clear(request);
		
		this.pageContext.removeAttribute(var);
		initialize();
		return EVAL_PAGE;
	}

	public void setVar(String var) {
		this.var = var;
	}
	
	private void initialize() {
		this.var = "fail";
	}
}
