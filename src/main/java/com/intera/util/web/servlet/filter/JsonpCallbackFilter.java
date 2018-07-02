package com.intera.util.web.servlet.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonpCallbackFilter implements Filter {

	private static Logger log = LoggerFactory.getLogger(JsonpCallbackFilter.class);

	private static final String CONTENT_TYPE = "text/javascript;charset=UTF-8";
	private static final String CALLBACK = "callback";

	public void init(FilterConfig fConfig) throws ServletException {}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		@SuppressWarnings("unchecked")
		Map<String, String[]> params = httpRequest.getParameterMap();

		if (params.containsKey(CALLBACK)) {
			final String callbackName = params.get(CALLBACK)[0];

			if (log.isDebugEnabled())
				log.debug(String.format("Wrapping response with JSONP callback %s", callbackName));

			OutputStream out = httpResponse.getOutputStream();

			GenericResponseWrapper wrapper = new GenericResponseWrapper(httpResponse);

			chain.doFilter(request, wrapper);

			//handles the content-size truncation
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			outputStream.write(String.format("%s(", callbackName).getBytes());
			outputStream.write(wrapper.getData());
			outputStream.write(new String(");").getBytes());
			byte jsonpResponse[] = outputStream.toByteArray();

			wrapper.setContentType(CONTENT_TYPE);
			wrapper.setContentLength(jsonpResponse.length);

			out.write(jsonpResponse);

			out.close();

		} else {
			chain.doFilter(request, response);
		}
	}

	public void destroy() {}
}