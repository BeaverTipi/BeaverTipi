package kr.or.ddit.main.member.controller;

import java.util.Map;

import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import kr.or.ddit.main.member.service.MemberService;
import kr.or.ddit.util.validate.InsertGroup;
import kr.or.ddit.util.validate.exception.OidcUserNotRegisteredException;
import kr.or.ddit.util.validate.exception.PKDuplicatedException;
import kr.or.ddit.util.validate.exception.UserNotRegisteredException;
import kr.or.ddit.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberRegisterController {
	private final MemberService service;
	
	private static final String MODELNAME = "member";
	
	@ModelAttribute(MODELNAME)
	public MemberVO member(
	    @SessionAttribute(name = WebAttributes.AUTHENTICATION_EXCEPTION, required = false)
	    Exception lastException
	) {
	    // 예외가 있더라도, OAuth 회원가입 경로에서만 적용되게
	    if (!isOAuthRegisterRequest()) {
	        return new MemberVO(); // 일반 회원가입일 경우 무시
	    }
	    MemberVO member = new MemberVO();
	    if (lastException == null) return new MemberVO();
	    if (lastException instanceof OidcUserNotRegisteredException oidcEx) {
	        var user = oidcEx.getUnRegisteredUser();
	        member.setMbrId(user.getName());
	        member.setMbrEmlAddr(user.getEmail());
	    } else if (lastException instanceof OAuth2AuthenticationException oauth2Ex) {
	    	if ("register-required".equals(oauth2Ex.getError().getErrorCode())) {
	            Throwable cause = oauth2Ex.getCause();
	            if (cause instanceof UserNotRegisteredException ex) {
	                OAuth2User user = ex.getUnRegisteredUser();
	                Map<String, Object> attributes = user.getAttributes();

	                String id = String.valueOf(attributes.get("id"));
	                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
	                String email = (String) kakaoAccount.get("email");
	                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
	                String nickname = (String) profile.get("nickname");
	                String phone = (String) kakaoAccount.get("phone_number");

	                member.setMbrId(id);
	                member.setMbrEmlAddr(email);
	                member.setMbrNnm(nickname);
	                member.setMbrTelno(phone);
	            }
	        }
	    }

	    return member;
	}
	private boolean isOAuthRegisterRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String uri = request.getRequestURI();
	    return uri.contains("/member/oauth2/register");
	}
	@GetMapping("${myapp.register-url}")
	public String registerPage() {
		return "main/member/register";
	}
	@GetMapping("${myapp.oauth2-register-url}")
	public String oauthRegisterPage() {
		return "main/member/oauth2Register";
	}
	
	@PostMapping("${myapp.register-url}")
	public String formProcess(@SessionAttribute(name = WebAttributes.AUTHENTICATION_EXCEPTION, required = false) 
		Exception lastException
			, @Validated(InsertGroup.class) @ModelAttribute(MODELNAME) MemberVO member
			, BindingResult errors
			, RedirectAttributes redirectAttributes
			) {
		log.info("🔍 formProcess 진입: member={}", member);
		String lvn = null;
		if(errors.hasErrors()) {
			String errorName = BindingResult.MODEL_KEY_PREFIX+MODELNAME;
			redirectAttributes.addFlashAttribute(MODELNAME, member);
			redirectAttributes.addFlashAttribute(errorName, errors);
			if (lastException != null) {
		        lvn = "redirect:/member/oauth2/register";
		    } else {
		        lvn = "redirect:/member/register";
		    }
		}else {
			try {
				service.createMember(member);
				lvn = "redirect:/";
				  if (lastException instanceof OAuth2AuthenticationException oauth2Ex) {
		                Throwable cause = oauth2Ex.getCause();
		                if (cause instanceof UserNotRegisteredException ex) {
		                    String registrationId = ex.getClientRegistration().getRegistrationId();
		                    lvn = "redirect:" + OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI + "/" + registrationId;
		                }
		            } else if (lastException instanceof OidcUserNotRegisteredException oidcEx) {
		                String registrationId = oidcEx.getClientRegistration().getRegistrationId();
		                lvn = "redirect:" + OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI + "/" + registrationId;
		            }
			}catch (PKDuplicatedException e) {
				redirectAttributes.addFlashAttribute(MODELNAME, member);
				redirectAttributes.addFlashAttribute("message", "아이디가 중복되었습니다.");
				 lvn = (lastException != null) ? "redirect:/member/oauth2/register" : "redirect:/member/register";
			}
		
		}
		return lvn;
	}
	
}
