package kr.or.ddit.member.controller;

import java.util.Map;

import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.member.service.MemberService;
import kr.or.ddit.validate.InsertGroup;
import kr.or.ddit.validate.exception.OidcUserNotRegisteredException;
import kr.or.ddit.validate.exception.PKDuplicatedException;
import kr.or.ddit.validate.exception.UserNotRegisteredException;
import kr.or.ddit.vo.MemberVO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberRegisterController {
	private final MemberService service;
	
	private static final String MODELNAME = "member";
	
	@ModelAttribute(MODELNAME)
	public MemberVO member(
	    @SessionAttribute(name = WebAttributes.AUTHENTICATION_EXCEPTION, required = false)
	    Exception lastException
	) {
	    MemberVO member = new MemberVO();

	    if (lastException instanceof OidcUserNotRegisteredException oidcEx) {
	        var user = oidcEx.getUnRegisteredUser();
	        member.setMbrId(user.getName());
	        member.setMbrEmlAddr(user.getEmail());

	    } else if (lastException instanceof UserNotRegisteredException oauth2Ex) {
	        var user = oauth2Ex.getUnRegisteredUser();
	        Map<String, Object> attributes = user.getAttributes();

	        // 카카오 로그인 정보 추출 (예시)
	        String id = String.valueOf(attributes.get("id"));
	        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
	        String email = (String) kakaoAccount.get("email");

	        member.setMbrId("kakao_" + id); // ID는 원하는 방식으로 가공 가능
	        member.setMbrEmlAddr(email);
	    }

	    return member;
	}

	@GetMapping("${myapp.register-url}")
	public String registerPage() {
		return "main/member/register";
	}
	@GetMapping("${myapp.oauth2-register-url}")
	public String oauthRegisterPage() {
		return "main/member/ouath2Register";
	}
	
	@PostMapping
	public String formProcess(@SessionAttribute(name = WebAttributes.AUTHENTICATION_EXCEPTION, required = false) 
																			OidcUserNotRegisteredException lastException
			, @Validated(InsertGroup.class) @ModelAttribute(MODELNAME) MemberVO member
			, BindingResult errors
			, RedirectAttributes redirectAttributes
			) {
		String lvn = null;
		if(errors.hasErrors()) {
			String errorName = BindingResult.MODEL_KEY_PREFIX+MODELNAME;
			redirectAttributes.addFlashAttribute(MODELNAME, member);
			redirectAttributes.addFlashAttribute(errorName, errors);
			lvn = "redirect:/member/register";
		}else {
			try {
				service.createMember(member);
				lvn = "redirect:/";
				if(lastException !=null) {
					// 등록 완료 후 다시 oauth 로그인 절차 진행
					String registrationId = lastException.getClientRegistration().getRegistrationId();
					lvn = "redirect:" + OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI +"/" + registrationId;
				}
			}catch (PKDuplicatedException e) {
				redirectAttributes.addFlashAttribute(MODELNAME, member);
				redirectAttributes.addFlashAttribute("message", "아이디가 중복되었습니다.");
				lvn = "redirect:/member/memberInsert.do";
			}
		
		}
		return lvn;
	}
	
}
