package kr.or.ddit.main.member.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.main.member.service.MemberService;
import kr.or.ddit.util.validate.OAuth2UpdateGroup;
import kr.or.ddit.util.validate.UpdateGroup;
import kr.or.ddit.vo.MemberVO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberUpdateController {
	private final MemberService service;
	private static final String MODELNAME ="member";	

	
	@GetMapping("/account/update")
	public String formUI(
			Authentication authentication
			, Model model
	) {
		String lvn = "main/member/updateForm";
		Object principal = authentication.getPrincipal();
		if(!model.containsAttribute(MODELNAME)) {
		    String username = authentication.getName();
		    MemberVO member = service.readMember(username);
			model.addAttribute(MODELNAME, member);
		}
		
		if (principal instanceof OAuth2User || principal instanceof OidcUser) {
			lvn = "main/member/updateOAuth2Form";
		}
		
		return lvn;
	}
	
	@PostMapping("/account/update")
	public String formProcess(
			@Validated(UpdateGroup.class) @ModelAttribute(MODELNAME) MemberVO member
			, BindingResult errors
			, RedirectAttributes redirectAttributes
			) {
		String lvn = "redirect:/account/update";
		// 검증 통과
		if(!errors.hasErrors()) {
			try {
				service.modifyMember(member);
				// 수정 성공 후? 새 mypage로 이동
				lvn = "redirect:/account/read";
			}catch (AuthenticationException e) {
				// 인증 실패? 수정양식으로 redirect, 비번오류 메시지, 기존 입력 데이터
				redirectAttributes.addFlashAttribute("message", e.getMessage());
				redirectAttributes.addFlashAttribute(MODELNAME, member);
			}
		}else {
			// 검증 실패? 수정양식으로 redirect, 검증 에러 메시지, 기존 입력데이터
			String errorName = BindingResult.MODEL_KEY_PREFIX + MODELNAME;
			redirectAttributes.addFlashAttribute(MODELNAME, member);
			redirectAttributes.addFlashAttribute(errorName, errors);
		}
		return lvn;
	}
	@PostMapping("/account/oauth2/update")
	public String formOAuth2Process(
			@Validated(OAuth2UpdateGroup.class) @ModelAttribute(MODELNAME) MemberVO member
			, BindingResult errors
			, RedirectAttributes redirectAttributes
			) {
		String lvn = "redirect:/account/update";
		// 검증 통과
		if(!errors.hasErrors()) {
			try {
				service.modifyMember(member);
				// 수정 성공 후? 새 mypage로 이동
				lvn = "redirect:/account/read";
			}catch (AuthenticationException e) {
				// 인증 실패? 수정양식으로 redirect, 비번오류 메시지, 기존 입력 데이터
				redirectAttributes.addFlashAttribute("message", e.getMessage());
				redirectAttributes.addFlashAttribute(MODELNAME, member);
			}
		}else {
			// 검증 실패? 수정양식으로 redirect, 검증 에러 메시지, 기존 입력데이터
			String errorName = BindingResult.MODEL_KEY_PREFIX + MODELNAME;
			redirectAttributes.addFlashAttribute(MODELNAME, member);
			redirectAttributes.addFlashAttribute(errorName, errors);
		}
		return lvn;
		
	}
}
