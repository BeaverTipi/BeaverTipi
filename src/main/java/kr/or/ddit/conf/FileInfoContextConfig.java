package kr.or.ddit.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@PropertySource("classpath:kr/or/ddit/FileInfo.properties")
public class FileInfoContextConfig implements WebMvcConfigurer{
	@Value("${imagesFolder}")
	private Resource imagesLocation;
	@Value("${imagesUrl}")
	private String imagesUrl;
	
	// 각자 환경에 맞게 시스템상 경로를 표현해주고 있음.
	@Value("${user.dir}")
	private String userDir;
	
	// 기본설정 config를 변경할수 있도록..
	/**
	 * 파일 시스템 자원을 웹자원으로 매핑 할 때 많이 사용함. --> prodDetail.jsp에서 img태그 사용에 필요한 url 구조
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(imagesUrl + "/**")
				.addResourceLocations(imagesLocation);
	}
	
	@PostConstruct
	public void init(){
		log.info("=====================> user.dir : {}", userDir);
		log.info("=====================> imagesLocation : {}", imagesLocation);
	}
	
}
