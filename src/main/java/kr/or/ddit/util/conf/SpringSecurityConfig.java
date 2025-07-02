package kr.or.ddit.util.conf;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.main.mapper.MemberMapper;
import kr.or.ddit.util.security.auth.CustomUserDetailsService;
import kr.or.ddit.util.security.jwt.JwtAuthenticationFilter;
import kr.or.ddit.util.security.jwt.JwtProvider;
import kr.or.ddit.util.security.oauth2.CustomOAuth2UserService;
import kr.or.ddit.util.security.oauth2.CustomOidcUserService;
import kr.or.ddit.util.security.oauth2.OAuth2AuthenticationFailureHandler;
import kr.or.ddit.util.security.oauth2.OAuth2AuthenticationSuccessHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "myapp")
@Data
@EnableWebSecurity
public class SpringSecurityConfig {
	private String loginUrl; 
	private String logoutUrl; 
	private String registerUrl;
	private String oauth2RegisterUrl;
	private String deleteValue;
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
	
	@Autowired // 주입 받는 방법 : 1.매개변수로 받기, 2.이렇게 받기/
	private MemberMapper mapper;
	
	@Bean 
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	/**
	 * 일반 form 로그인 사용자의 정보 조회
	 * @return
	 */
	@Bean
	public CustomUserDetailsService userDetailsService() {
		CustomUserDetailsService service = new CustomUserDetailsService(mapper,deleteValue);
		return service;
	}
	
	/**
	 * 소셜 로그인 사용자의 정보 조회
	 * @return
	 */
	@Bean
	public CustomOidcUserService oidcUserService() {
		return new CustomOidcUserService(mapper,deleteValue);
	}
	/**
	 * 소셜 로그인 사용자의 정보 조회
	 * @return
	 */
	@Bean
	public CustomOAuth2UserService oAuth2UserService() {
		return new CustomOAuth2UserService(mapper,deleteValue);
	}
	
	/**
	 * 쇼설 로그인 실패시,
	 * 미가입자로 인해 실패라면, 가입 페이지로 리다이렉트
	 * 그냥 인증 실패라면, 로그인 페이지로 리다이렉트
	 * @return
	 */
	@Bean
	public OAuth2AuthenticationFailureHandler failureHandler() {
		OAuth2AuthenticationFailureHandler handler =  new OAuth2AuthenticationFailureHandler(oauth2RegisterUrl);
		handler.setDefaultFailureUrl(loginUrl + "?error");
		return handler;
	}
	
	@Autowired
	private JwtProvider jwtProvider;
	@Bean
	public OAuth2AuthenticationSuccessHandler successHandler() {
		OAuth2AuthenticationSuccessHandler handler =  new OAuth2AuthenticationSuccessHandler(jwtProvider);
		return handler;
	}
	@Autowired
	private DataSource dataSource;

	// spring boot 로 자동 등록되어있는 객체 주입.
	@Autowired
	private ClientRegistrationRepository clientRegistrationRepository;
	

	/**
	 * access token 과 refresh token 을 발급받은 OAuth2AuthorizedClient 객체를 관리하는 객체
	 * memory 나 database 기반으로 토큰 정보를 관리할 수 있음.
	 * spring boot starter 를 사용하는 경우 memory 기반의 관리 객체가 자동 등록됨.
	 * DB 기반으로 관리할 경우, 스키마 필요( classpath:org/springframework/security/oauth2/client/oauth2-client-schema.sql )
	 * @param registrationRepository
	 * @return
	 */ // 이건 제 3자 앱. 인가된 앱. DB저장 구현체 선택후 구글관련 내용 저장..
	@Bean
	public OAuth2AuthorizedClientService authorizedClientService() {
	   // return new InMemoryOAuth2AuthorizedClientService(registrationRepository); // 인메모리 로그인할때엔 굳이 DB에 저장할 필요없음. 그때엔 인메모리서비스로 해도됨. 인메모리는 스프링이 가지고있음.
	   return new JdbcOAuth2AuthorizedClientService(new JdbcTemplate(dataSource), clientRegistrationRepository); // DB
	}

	private final String[] WHITE_LIST = new String[] {
			"/"
			,"/ws/meeting/**"
			,"/meeting/**"
			,"/js/**"
			,"/html/**"
			,"/dist/**"
			,"/error/**"
			,"/swagger-ui/**"
			,"/swagger-ui.html"
			,"/v3/api-docs/**"
			,"/v3/api-docs.yaml" // /를 빼고 전부다 묶임. 정적자원도 다. ==> 폐쇄형 사이트를 가지고 있다. 잘해얗마..
			,"/oauth2/**"
			 
	};
	
	// 세션 동시성을 제어하기 위한 리스너(분산서버에서 세션에 대해 이벤트가 일어나면 읽어내서 뿌려주는 리스너)
	// 내장서버에 하는거임. 서버에 뭐라도 하면 .. devTools..못써.. 이건 배포할때 필요할수도..
//	@Bean
//	public HttpSessionEventPublisher sessionEventPublisher() {
//		return new HttpSessionEventPublisher();
//	}
	
	@Autowired
	private CorsConfigurationSource authCorsConfigurationSource;
	
	@Bean
	public SecurityContextRepository securityContextRepository() {
		return new DelegatingSecurityContextRepository(
			new HttpSessionSecurityContextRepository()
			, new RequestAttributeSecurityContextRepository()
		);
	}
	
	@Bean
	public LogoutHandler logoutHandler() {
		SecurityContextLogoutHandler handler =  new SecurityContextLogoutHandler();
		handler.setClearAuthentication(true);
		handler.setInvalidateHttpSession(true);
		return handler;		
	}
	
	
	@Bean
	@Order(2)
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
			.csrf(csrf -> csrf.disable())
			.cors(cors->cors.configurationSource(authCorsConfigurationSource))
			.authorizeHttpRequests(authorize ->
				authorize
				.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll() // redirection으로 이동하는 것도 감지함. 그래서 막아버림. 그것도 풀어줘라 하는 게 필요.
					.requestMatchers(WHITE_LIST).permitAll()
					.requestMatchers(new AntPathRequestMatcher(registerUrl)).permitAll()
					.requestMatchers(new AntPathRequestMatcher("account/login/**")).permitAll()
					.requestMatchers(new AntPathRequestMatcher("account/logout/**")).permitAll()
					.requestMatchers(new AntPathRequestMatcher("/mypage")).authenticated()
					.requestMatchers(new AntPathRequestMatcher("/prod/*Insert*")).hasAnyRole("ADMIN")
					.requestMatchers(new AntPathRequestMatcher("/prod/*Update*")).hasAnyRole("ADMIN")
					.requestMatchers(new AntPathRequestMatcher("/prod/*Delete*")).hasAnyRole("ADMIN")
					.requestMatchers(new AntPathRequestMatcher("/buyer/**")).hasAnyRole("ADMIN")
//					.anyRequest().authenticated() // 폐쇄형 사이트 -> cors 필요없음
					.requestMatchers("/**").permitAll() // 우리는 공개형 사이트를 가지고 있다. -> cors필요함.
			)
			.sessionManagement(session->
			// 세션의 트래킹모드를 탈취해서 session을 변조 .. 변조의 1번째 단계. csrf를 꺼도 위조를 막아야함. 그러면 안전한 방법은 로그인전 로그인후 세션을 바꾸는 것임.
//				session.sessionFixation(fixation -> fixation.changeSessionId()) // 세션은 그대로 나두는데 아이디만 바꿔 서블릿 4이상인데에서 사용할 수 있음. 이하는 지원안됨.
				session.sessionFixation(fixation -> fixation.newSession()) //  fixation 에서 newSession -- 새로운 세션으로 만듬
						.maximumSessions(1) // 로그인 할수 있는 최대의 기기는 몇개인가
						.maxSessionsPreventsLogin(false) // 최대의 세션을 추가했는데 그걸 막을거야 말거야 true=> 새로 로그인 되는 걸 막음. false => 새로 로그인하는애가 우선권을 가지게 됨.
						.expiredUrl(loginUrl + "?expired") // false일경우 로그인 되어있던 녀석이 새로고침을 하면 어디로 보내겠냐. 에러페이지가 있는게 맞겠지? 그러면 그냥 로그인페이지로..
						// 분산되어있는 서버에서 한쪽서버에서 새로운세션이 만들어 지더라도 세션을 확인 못함. 이걸 쓸려면 넣어야하는게 있음.(분산서버에서)
			)
			.oauth2Login(oauth2->
			oauth2.loginPage("/")
					.successHandler(successHandler())
					.failureHandler(failureHandler())
					.userInfoEndpoint(user -> 
	        user
	            .userService(oAuth2UserService()) // 카카오 등
	            .oidcUserService(oidcUserService()) // 구글
	    )
					)
			.formLogin(login ->
				login
					.loginPage("/login")
					
				)
			
			.requestCache(requestCache->
				requestCache
					.requestCache(requestCache())
					);
		return http.build();
	}
	@Bean
	public AntPathMatcher antPathMatcher() {
		return new AntPathMatcher();
	}
	public RequestCache requestCache() {
		// 익명객체로 자식 만들거임.
		HttpSessionRequestCache cache = new HttpSessionRequestCache() {
			@Override
			public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
//				
				if(!antPathMatcher().match("/.well-known/**", request.getRequestURI())) {
					super.saveRequest(request, response);
				}
				
			}
		};
		
		return cache;
	}
}
