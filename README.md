
spring-jsonp-support
====================

Adds support for jsonp requests in Spring MVC controllers.


Adding jsonp support for Spring MVC Controllers
------------------------------------------------

This project contains artifacts you can use for adding support for jsonp in your Spring MVC Controllers.

You can also use this in your Spring Roo projects to add jsonp support for controllers generated using `@RooJson` and `@RooWebJson` .

For more information on how this works, please read the following article.

http://jpgmr.wordpress.com/2010/07/28/tutorial-implementing-a-servlet-filter-for-jsonp-callback-with-springs-delegatingfilterproxy/

Special thanks to the original author of the article.

Getting Started
================
1. Checkout the project from github or download the tar/zip file and extract it.

2. Run `mvn clean install`

3. Add the following dependency to your project.

		<dependency>
			<groupId>com.intera.util</groupId>
			<artifactId>spring-jsonp-support</artifactId>
			<version>${version}</version>
		</dependency>

4. Modify and add the following lines to each of the following files.

`applicationContext.xml`


		<bean class="com.intera.util.web.servlet.filter.JsonpCallbackFilter" id="jsonpCallbackFilter" />


`web.xml`

		<filter>
		    <filter-name>jsonpCallbackFilter</filter-name>
		    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
		</filter>
		 
		<filter-mapping>
		    <filter-name>jsonpCallbackFilter</filter-name>
		    <url-pattern>*.json</url-pattern>
		</filter-mapping>

`OR` In Sprintboot

```
	@Bean
	public FilterRegistrationBean myFilterRegistration() {

		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new JsonpCallbackFilter());
		registration.addUrlPatterns("/*");
		registration.addInitParameter("paramName", "paramValue");
		registration.setName("JsonpCallbackFilter");
		registration.setOrder(1);
		return registration;
	}
```

5. When you are making the request, add .json at the end of the jsonp request and use a callback parameter.

Example : http://localhost:8080/mylistingsapp/listings.json?callback=myFunction

