package kr.or.ddit.conf;

import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class SiteMeshFilterConfig {
	
	@Bean
	FilterRegistrationBean<ConfigurableSiteMeshFilter> siteMeshFilter(){
		FilterRegistrationBean<ConfigurableSiteMeshFilter> filter = 
				new FilterRegistrationBean<>();
		
		filter.setFilter(
				ConfigurableSiteMeshFilter.create(builder->
					builder.setDecoratorPrefix("/WEB-INF/decorators/")
							.addExcludedPath("*.css")
							.addExcludedPath("*.js")
							.addExcludedPath("/ajax/**")
							.addExcludedPath("/rest/**")
							.addExcludedPath("*.html")
							.addExcludedPath("/")
							.addDecoratorPath("/admin/**", "adminDecorator.jsp")
							.addDecoratorPath("/building/**", "rentalOwnerDecorator.jsp")
							.addDecoratorPath("/resident/**", "rentalTenantDecorator.jsp")
							.addDecoratorPath("/**", "mainDecorator.jsp")
							
				)
			);
		filter.setOrder(Ordered.LOWEST_PRECEDENCE - 100);
		filter.addUrlPatterns("/*");
		
		return filter;
				
	}
	
}
