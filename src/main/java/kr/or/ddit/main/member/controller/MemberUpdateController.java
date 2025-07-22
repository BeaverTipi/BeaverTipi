package kr.or.ddit.main.member.controller;

import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import kr.or.ddit.main.member.service.MemberService;
import kr.or.ddit.util.validate.OAuth2UpdateGroup;
import kr.or.ddit.util.validate.UpdateGroup;
import kr.or.ddit.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberUpdateController {
	private final MemberService service;
	private static final String MODELNAME ="member";	
	@PostMapping("/ajax/member/check-password")
	@ResponseBody
	public Map<String, Object> checkPassword(
			@RequestBody Map<String, String> payload,HttpSession session, Authentication auth) {
	    String inputPassword = payload.get("password");

	    // 현재 로그인한 사용자 ID
	    String username = auth.getName();
	    boolean matched = service.checkedPassword(username, inputPassword);
	    // 저장된 해시된 비밀번호 조회
	    if (matched) {
	        session.setAttribute("verifiedPassword", inputPassword); // ✅ 세션에 비밀번호 저장
	    }
	    Map<String, Object> result = new HashMap<>();

	    result.put("success", matched);
	    return result;
	}
	
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
			, HttpSession session
			) {
		member.setMbrPw(session.getAttribute("verifiedPassword").toString());
		String lvn = "redirect:/account/update";
		// 검증 통과
		if(!errors.hasErrors()) {
				log.info("수정 하기 member 정보 : {}",member);
				service.modifyMember(member);
				 session.removeAttribute("verifiedPassword");
				// 수정 성공 후? 새 mypage로 이동
				lvn = "redirect:/account/read";
		}else {
			log.info(errors.toString());
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
				log.info(member.toString());
				service.modifyMember(member);
				// 수정 성공 후? 새 mypage로 이동
				lvn = "redirect:/account/read";
			}catch (AuthenticationException e) {
				log.info(e.toString());
				// 인증 실패? 수정양식으로 redirect, 비번오류 메시지, 기존 입력 데이터
				redirectAttributes.addFlashAttribute("message", e.getMessage());
				redirectAttributes.addFlashAttribute(MODELNAME, member);
			}
		}else {
			log.info(errors.toString());
			// 검증 실패? 수정양식으로 redirect, 검증 에러 메시지, 기존 입력데이터
			String errorName = BindingResult.MODEL_KEY_PREFIX + MODELNAME;
			redirectAttributes.addFlashAttribute(MODELNAME, member);
			redirectAttributes.addFlashAttribute(errorName, errors);
		}
		return lvn;
		
	}
}
