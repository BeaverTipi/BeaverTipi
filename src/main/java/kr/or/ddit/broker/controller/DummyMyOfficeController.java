package kr.or.ddit.broker.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/broker/myoffice")
public class DummyMyOfficeController {
	
	@GetMapping("/lstg/list")
	public Map<String, String> dummyMethod(
	) {
		return Map.of("김남혁", "보람로96 2010동",
		   "김찬영", "대전광역시 유성구 문화원로46번길 26",
		   "이학범", "대전광역시 중구 선화로35번길 18-13",
		   "김아린", "대전광역시 중구 계룡로 765번길 16",
		   "대덕인", "대전광역시 중구 계룡로 846");
	}
}
