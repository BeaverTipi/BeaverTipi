package kr.or.ddit.main.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;

@Controller
@Slf4j
public class VerificationController {
	
	@PostMapping("/ajax/member/verification")
	@ResponseBody
	public Map<String, Object> verification(String phone, HttpSession session) {
	    Map<String, Object> result = new HashMap<>();
	    int code = (int)((Math.random() * 900000) + 100000);

	    DefaultMessageService messageService = NurigoApp.INSTANCE.initialize(
	        "NCSHFBLSXWPERSLO", "BCON1RDIIP3ZLZ3J7LVMXMWI1Q909NFJ", "https://api.solapi.com"
	    );

	    Message message = new Message();
	    message.setFrom("01021959621");
	    message.setTo(phone);
	    String sendMessage = String.format("[beaverTipi] 인증번호는 [%s]입니다.\n- 유효시간 5분입니다.\n- 타인 노출 주의", code);
	    message.setText(sendMessage);

	    try {
	        messageService.send(message);
	        session.setAttribute("authCode", String.valueOf(code));
	        session.setMaxInactiveInterval(300); // 5분 유효
	        result.put("success", true);
	    } catch (NurigoMessageNotReceivedException ex) {
	        log.info(ex.getFailedMessageList().toString());
	        log.info(ex.getMessage());
	        result.put("success", false);
	        result.put("message", "메시지 발송 실패");
	    } catch (Exception ex) {
	        log.info(ex.getMessage());
	        result.put("success", false);
	        result.put("message", "알 수 없는 오류");
	    }

	    return result;
	}
	
	@PostMapping("/ajax/member/verification-check")
	@ResponseBody
	public String verifyCode(@RequestParam("code") String code, HttpSession session) {
	    String savedCode = (String) session.getAttribute("authCode");

	    if (savedCode == null) {
	        return "expired"; // 유효시간 초과 or 전송 안됨
	    }

	    if (savedCode.equals(code)) {
	        session.removeAttribute("authCode"); // 일치하면 인증코드 폐기
	        return "valid"; // 인증 성공
	    } else {
	        return "invalid"; // 인증번호 불일치
	    }
	}

	
}
