package kr.or.ddit.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class MemberLogoutController {
	@GetMapping("${myapp.logout-url}")
    public String logout(
    		HttpServletRequest request
    		, HttpServletResponse response
    		, HttpSession session
    		,RedirectAttributes redirectAttributes
    		) {
        // 세션 무효화
        session.invalidate();

        // 쿠키 삭제 (선택)
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0); // 즉시 만료
                cookie.setPath("/"); // 루트 경로 대상
                response.addCookie(cookie);
            }
        }
        redirectAttributes.addFlashAttribute("message", "로그아웃 되었습니다.");
        // 로그아웃 후 리다이렉트
        return "redirect:/";
    }
 
}
