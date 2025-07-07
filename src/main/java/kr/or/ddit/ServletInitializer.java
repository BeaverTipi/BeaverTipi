package kr.or.ddit;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		// BootPart4Application.class 대신 당신의 실제 메인 클래스 이름을 사용합니다.
		return application.sources(BootPart81Application.class);
	}

}