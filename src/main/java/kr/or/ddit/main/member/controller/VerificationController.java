package kr.or.ddit.main.member.controller;

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
	@PostMapping("/member/verification")
	@ResponseBody
	public String verification(String phone, HttpSession session) {
		int code = (int)((Math.random() * 900000) + 100000);
		DefaultMessageService messageService =  NurigoApp.INSTANCE.initialize("NCSHFBLSXWPERSLO", "BCON1RDIIP3ZLZ3J7LVMXMWI1Q909NFJ", "https://api.solapi.com");
		// Message 패키지가 중복될 경우 net.nurigo.sdk.message.model.Message로 치환하여 주세요
		Message message = new Message();
		message.setFrom("01089526419");
		message.setTo(phone);
		String sendMessage = String.format("[beaverTipi] 인증번호는 [%s]입니다.\n- 유효시간 5분입니다.\n- 타인 노출 주의", code);
		message.setText(sendMessage);
		String result = "success";
		try {
		  // send 메소드로 ArrayList<Message> 객체를 넣어도 동작합니다!
		  messageService.send(message);
		  session.setAttribute("authCode", String.valueOf(code));
          session.setMaxInactiveInterval(300); // 5분 유효
         
		} catch (NurigoMessageNotReceivedException exception) {
		  // 발송에 실패한 메시지 목록을 확인할 수 있습니다!
		  log.info(exception.getFailedMessageList().toString());
		  log.info(exception.getMessage());
		  result = "fail";
		} catch (Exception exception) {
			log.info(exception.getMessage());
			result = "error";
		}
		 return result;
	}
	
	@PostMapping("/member/verification-check")
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
