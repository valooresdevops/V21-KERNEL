package com.valoores.v21.apigateway.config;

import java.util.Enumeration;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class LoggingFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		System.out.println("\n------------------------ ZUUL REQUEST INTERCEPTED ------------------------\n");
		System.out.println("------ Server Name: " + ctx.getRequest().getServerName().toString());
		System.out.println("------ Method: " + ctx.getRequest().getMethod().toString());
		System.out.println("------ Request URI: " + ctx.getRequest().getRequestURI().toString());
		System.out.println("------ Route Host: " + ctx.getRouteHost());
		System.out.println("------ Keycloak User Id: " + ctx.getRequest().getRemoteUser() + "\n");
		
		Enumeration<String> headers = ctx.getRequest().getHeaderNames();
		String header;
		while (headers.hasMoreElements()) {
			header = headers.nextElement();
			System.out.println("Headers: " + header + " = " + ctx.getRequest().getHeader(header));
		};
		
		System.out.println("\n---------------------------------------------------------------------------\n");

		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return FilterConstants.SEND_RESPONSE_FILTER_ORDER;
	}

}
