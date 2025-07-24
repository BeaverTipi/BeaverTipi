package kr.or.ddit.main.member.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class RestCookieCheckController {
	@GetMapping("/rest/auth")
	public ResponseEntity<?> checkAuth(Authentication authentication, Principal principal) {
	    if (authentication == null || !authentication.isAuthenticated()) {
	    	log.debug("왜 서명페이지로 못 넘어가냐?????? principal>..{}", principal.toString());
	    	log.debug("왜 서명페이지로 못 넘어가냐?????? 2트 authentication>..{}", authentication.toString());
	    	//둘 다 null;
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	    }
	    return ResponseEntity.ok().build();
	}

}