package com.jstudyplanner.web;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * AuthenticationSuccessHandler implementation that is used in Spring Security
 * form authentication (form-login) as authentication-success-handler-ref. Refer
 * to spring-security.xml
 * It uses RedirectStrategy's sendRedirect method to redirect to one of the URLs.
 * The URL is defined based on users authority (granted role), e.g. ROLE_ADMIN
 * should redirect to /admin/admin.
 */
@Component
public class SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException {
		handle(request, response, authentication);
		clearAuthenticationAttributes(request);
	}

	protected void handle(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException {
		String url = getUrl(authentication);
		
		if (response.isCommitted()) {
			return;
		}

		redirectStrategy.sendRedirect(request, response, url);
	}

	/**
	 * Get the URL depending on user's role
	 */
	protected String getUrl(Authentication authentication) {
		boolean isStudent = false;
		boolean isStaff = false;
		boolean isAdmin = false;
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		for (GrantedAuthority grantedAuthority : authorities) {
			if (grantedAuthority.getAuthority().equals("ROLE_STUDENT")) {
				isStudent = true;
				break;
			} else if (grantedAuthority.getAuthority().equals("ROLE_STAFF")) {
				isStaff = true;
				break;
			} else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
				isAdmin = true;
				break;
			} else if (grantedAuthority.getAuthority().equals("ROLE_SUPERADMIN")) {
				isAdmin = true;
				break;
			}
		}

		if (isStudent) {
			return "/student/student";
		} else if (isStaff) {
			return "/staff/home";
		} else if (isAdmin) {
			return "/admin/home";
		} else {
			throw new IllegalStateException();
		}
	}

	protected void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return;
		}
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}

	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

	protected RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}

}