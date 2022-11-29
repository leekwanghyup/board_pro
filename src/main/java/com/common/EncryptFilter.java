package com.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;


@WebFilter(urlPatterns = {
		"/member/join",
		"/member/login"
})
public class EncryptFilter extends HttpFilter implements Filter {
       
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		System.out.println("EncryptFilter : 필터 동작");
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		EncryptWrapper ew = new EncryptWrapper(httpServletRequest);
		if(request.getParameter("userPwd")!=null) {
			request.setAttribute("userPwd", ew.getParameter("userPwd"));
		}
		chain.doFilter(request, response);
	}
}
